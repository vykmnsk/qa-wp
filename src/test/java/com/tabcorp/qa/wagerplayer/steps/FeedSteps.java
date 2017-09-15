package com.tabcorp.qa.wagerplayer.steps;

import com.jayway.jsonpath.ReadContext;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.tabcorp.qa.common.FrameworkError;
import com.tabcorp.qa.common.Helpers;
import com.tabcorp.qa.common.StrictHashMap;
import com.tabcorp.qa.wagerplayer.Config;
import com.tabcorp.qa.wagerplayer.api.WAPI;
import cucumber.api.DataTable;
import cucumber.api.java8.En;
import org.apache.commons.lang3.RandomStringUtils;
import org.gearman.Gearman;
import org.gearman.GearmanClient;
import org.gearman.GearmanJobEvent;
import org.gearman.GearmanJobEventType;
import org.gearman.GearmanJobReturn;
import org.gearman.GearmanServer;
import org.gearman.impl.GearmanImpl;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

public class FeedSteps implements En {
    private final static String EXCHANGE_NAME = "wift_all";
    private final static String EXCHANGE_TYPE = "direct";
    private final static String ALTERNATE_EXCHANGE_NAME = "wift_primary";
    private final int FEED_TRAVEL_SECONDS = 2;
    private final int FEED_EVENT_STARTS_IN_MINS = 30;
    private final int SERVER_OFFSET_HOURS = 0;
    private WAPI wapi;
    private String apiSessionId;
    private String eventNameRequested;
    private Map eventReceived;
    private Integer scratchedPosition;
    public static Logger log = LoggerFactory.getLogger(FeedSteps.class);

    public FeedSteps() {
        When("^I feed \"(PA|WIFT)\" RabbitMQ with Event message based on \"([^\"]+)\"$", (String feedType, String templateFile) -> {
            final String baseName = "QAFEED";
            eventNameRequested = Helpers.createUniqueNameCompact(baseName);
            String eventId = String.format("%s_%s",
                    RandomStringUtils.randomNumeric(5),
                    RandomStringUtils.randomAlphanumeric(12));
            String payload = prepareRabbitMQPayload(templateFile, eventId, eventNameRequested, FEED_EVENT_STARTS_IN_MINS);
            ConnectionFactory factory = new ConnectionFactory();
            factory.setVirtualHost("/");
            switch(feedType) {
                case "PA":
                    factory.setHost(Config.feedMQPAHost());
                    factory.setPort(Config.feedMQPAPort());
                    factory.setUsername(Config.feedMQPAUsername());
                    factory.setPassword(Config.feedMQPAPassword());
                    break;
                case "WIFT":
                    factory.setHost(Config.feedMQWiftHost());
                    factory.setPort(Config.feedMQWiftPort());
                    factory.setUsername(Config.feedMQWiftUsername());
                    factory.setPassword(Config.feedMQWiftPassword());
                    break;
                 default:
                    throw new FrameworkError("Unknown RabbitMQ Feed: " + feedType);
            }

            try {
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();

                Map<String, Object> args = new HashMap<>();
                args.put("alternate-exchange", ALTERNATE_EXCHANGE_NAME);
                channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE, true, false, args);
                log.info("connected to rabbitMQ!");
                channel.basicPublish(EXCHANGE_NAME, "", null, payload.getBytes());
                log.info("Sent payload to RabbitMQ'" + payload + "'");

                channel.close();
                connection.close();
            } catch (Exception e) {
                throw new FrameworkError("Problem communicating with RabbitMQ: " + e.getMessage());
            }
        });

        When("^I feed Gearman with Event message based on \"([^\"]*)\"$", (String templateFile) -> {
             final String WORKER_NAME = "ss_snapshot";
            final String WORKLOAD_TYPE = "ss_snapshot";

            final String participantName1 = "QAFEED";
            final String participantName2 = Helpers.createUniqueNameCompact("");
            eventNameRequested = createSportEventName(participantName1, participantName2);

            String eventId = String.format("%s_%s",
                    RandomStringUtils.randomNumeric(5),
                    RandomStringUtils.randomAlphanumeric(12));
            int startInMinutes = 30;
            String workload = prepareGearmanWorkload(templateFile, eventId, participantName1, participantName2, startInMinutes, WORKER_NAME, WORKLOAD_TYPE);
            log.debug("Workload for Gearman: {}", workload);
            try {
                log.info("Submitting job for eventName={} to worker={} of type={}", eventNameRequested, WORKER_NAME, WORKLOAD_TYPE);
                Gearman gearman;
                try {
                    gearman = new GearmanImpl();
                } catch (IOException e) {
                    throw new FrameworkError(e);
                }
                GearmanServer server = gearman.createGearmanServer(Config.feedMQGearmanHost(), Config.feedMQGearmanPort());

                final GearmanClient client = gearman.createGearmanClient();
                client.addServer(server);

                GearmanJobReturn gearmanJobReturn;
                gearmanJobReturn = client.submitJob(WORKLOAD_TYPE, workload.getBytes("UTF-8"));
                GearmanJobEvent gearmanJobEvent = gearmanJobReturn.poll();
                while (gearmanJobEvent.getEventType() != GearmanJobEventType.GEARMAN_EOF) {
                    gearmanJobEvent = gearmanJobReturn.poll();
                    log.debug("polling");
                }
                log.info("Job Taken by Gearman: {}", gearmanJobEvent);
            } catch (Exception e) {
                throw new FrameworkError(e);
            }
        });

        Then("^WagerPlayer receives the Event in \"([^\"]+)\"-\"([^\"]+)\"$", (String catName, String subcatNme) -> {
            Map<String, Map<String, Integer>> categories = Helpers.loadYamlResource("categories.yml");

            String catNameNormed = Helpers.normalize(catName.toUpperCase());
            String subcatNameNormed = Helpers.normalize(subcatNme.toUpperCase());
            Map<String, Integer> subCats = categories.get(catNameNormed);
            assertThat(subCats).withFailMessage(String.format("No category found with name '%s'", catNameNormed)).isNotNull();

            Integer subcatId = subCats.get(subcatNameNormed);
            assertThat(subcatId).withFailMessage(String.format("No subcategory found with name '%s'", subcatNameNormed)).isNotNull();

            assertThat(eventNameRequested).as("Event Name sent to feed in previous step").isNotEmpty();
            Helpers.delayInMillis(FEED_TRAVEL_SECONDS * 1000);
            wapi = new WAPI();
            apiSessionId = wapi.login();

            final int EXTRA_WAIT_MINS = 10;
            LocalDateTime from = LocalDateTime.now().plusHours(SERVER_OFFSET_HOURS);
            LocalDateTime to = LocalDateTime.now().plusMinutes(FEED_EVENT_STARTS_IN_MINS + EXTRA_WAIT_MINS).plusHours(SERVER_OFFSET_HOURS);

            Helpers.retryOnFailure(() -> {
                List<Map> events = wapi.getEvents(apiSessionId, subcatId, from, to);
                eventReceived = events.stream()
                        .map(e -> (Map) e)
                        .filter(e -> matchByName(e, eventNameRequested))
                        .findFirst().orElse(null);
                assertThat(eventReceived).withFailMessage(String.format("No Events found matching name: '%s'", eventNameRequested)).isNotNull();
            }, 3, 5);
        });

        Then("^The received Event contains scratched selection for \"([^\"]+)\"$", (String selName) -> {
            assertThat(eventReceived).as("Event created by feed in previous step").isNotNull();
            Map selection = getSelection((String) eventReceived.get("id"), selName);
            scratchedPosition = Integer.valueOf((String)selection.get("position"));
            assertThat(selection.get("scratched")).as(String.format("Selection '%s' 'scratched' attribute", selName))
                    .isNotNull().isEqualTo(1);
        });

        Then("^The received Event contains normal selection for \"([^\"]+)\" with the same position as scratched$", (String selName) -> {
            assertThat(eventReceived).as("Event created by feed in previous step").isNotNull();
            assertThat(scratchedPosition).as("Scratched position from the previous step").isNotNull();
            Map selection = getSelection((String) eventReceived.get("id"), selName);
            Integer replacedPosition = Integer.valueOf((String)selection.get("position"));
            assertThat(replacedPosition).as("Replacement position match Scratched").isEqualTo(scratchedPosition);
        });

        Then("^the received Event contains markets$", () -> {
            assertThat(eventReceived).as("Event created by feed in previous step").isNotNull();
            String eventId = (String) eventReceived.get("id");
            assertThatCode(() ->
                    wapi.getEventMarkets(apiSessionId, eventId)
                ).as("Reading Event Markets").doesNotThrowAnyException();
        });


        Then("^the received Event market \"([^\"]+)\" data matches$", (String mktName, DataTable table) -> {
            StrictHashMap<String, String> expected = new StrictHashMap<>();
            expected.putAll(table.asMap(String.class, String.class));


            String eventId = "22464";
            WAPI wapi = new WAPI();
            ReadContext resp = wapi.getEventMarkets(apiSessionId, eventId);

            Map<String, String> actual = new HashMap<>();
            expected.forEach((expectedKey, expVal) -> {
                String actualVal = wapi.readMarketAttribute(resp, mktName, ui2api().get(expectedKey));
                actual.put(expectedKey, api2ui().get(actualVal));
            });
            assertThat(actual).as(String.format("Actual=%s, Expected=%s", actual, expected)).isEqualTo(expected);
        });

    }

    static Map<String, String> ui2api() {
        Map<String, String> ui2apiMap = new StrictHashMap<>();
        ui2apiMap.put("market status", "status");
        ui2apiMap.put("betting status", "betting_status");
        return ui2apiMap;
    }

    static Map<String, String> api2ui() {
        Map<String, String> ui2apiMap = new StrictHashMap<>();
        ui2apiMap.put("run", "Betting in the Run");
        ui2apiMap.put("enabled", "Enabled");
        return ui2apiMap;
    }

    private String createSportEventName(String participantName1, String participantName2) {
        return String.format("%s v %s", participantName1, participantName2);
    }

    private Map getSelection(String eventId, String selName) {
        assertThat(eventId).as("Received Event ID").isNotEmpty();
        ReadContext resp = wapi.getEventMarkets(apiSessionId, eventId);
        return wapi.findOneSelectionByName(resp, selName);
    }

    private String prepareRabbitMQPayload(String templateFile, String id, String name, int inMinutes) {
        JSONParser parser = new JSONParser();
        JSONObject json;
        try {
            json = (JSONObject) parser.parse(Helpers.readResourceFile(templateFile));
        } catch (ParseException pe) {
            throw new FrameworkError(pe);
        }
        LocalDateTime startTime = LocalDateTime.now().plusMinutes(inMinutes);
        String startTimeStamp = startTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        json.put("id", id);
        json.put("name", name);
        json.put("start_time", startTimeStamp);
        return json.toJSONString();
    }

    private String prepareGearmanWorkload(String templateFile, String eventId, String participantName1, String participantName2, int inMinutes, String worker, String workloadType) {
        JSONObject payload = Helpers.readJSON(templateFile);
        LocalDateTime startTime = LocalDateTime.now().plusMinutes(inMinutes);
        String startTimeStamp = startTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String nowTimeStamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String eventName = createSportEventName(participantName1, participantName2);
        payload.put("Id", eventId);
        payload.put("FixtureName", eventName);
        payload.put("StartTime", startTimeStamp);
        payload.put("TimeStamp", nowTimeStamp);

        JSONArray participants = (JSONArray) payload.get("Participants");
        JSONObject participant1 = (JSONObject) participants.get(0);
        JSONObject participant2 = (JSONObject) participants.get(1);
        participant1.put("Name", participantName1);
        participant2.put("Name", participantName2);

        log.debug("Payload for Gearman: {}", payload);

        JSONObject workload = new JSONObject();
        workload.put("snapshot", compressAndEncode(payload.toJSONString()));
        workload.put("resourceName", eventName);
        workload.put("marketWorkerName", worker);
        workload.put("type", workloadType);
        return workload.toJSONString();
    }

    public static String compressAndEncode(String input) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DeflaterOutputStream def = new DeflaterOutputStream(out, new Deflater(-1, true));
        try {
            def.write(input.getBytes());
            def.close();
        } catch (IOException e) {
            throw new FrameworkError(e);
        }
        return Base64.getEncoder().encodeToString(out.toByteArray());
    }

    private boolean matchByName(Map event, String initialName) {
        Map nameMap = (Map) event.get("name");
        String name = nameMap.get("-content").toString();
        return name.endsWith(initialName);

    }

}

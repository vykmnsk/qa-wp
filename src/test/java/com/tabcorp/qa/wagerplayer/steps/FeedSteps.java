package com.tabcorp.qa.wagerplayer.steps;

import com.jayway.jsonpath.ReadContext;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.tabcorp.qa.common.FrameworkError;
import com.tabcorp.qa.common.Helpers;
import com.tabcorp.qa.wagerplayer.Config;
import com.tabcorp.qa.wagerplayer.api.WAPI;
import cucumber.api.java8.En;
import net.minidev.json.JSONArray;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class FeedSteps implements En {
    private final static String EXCHANGE_NAME = "wift_all";
    private final static String EXCHANGE_TYPE = "direct";
    private final static String ALTERNATE_EXCHANGE_NAME = "wift_primary";
    public static Logger log = LoggerFactory.getLogger(FeedSteps.class);
    private WAPI wapi = new WAPI();
    private String apiSessionId;
    private String eventNameRequested;
    private Map eventReceived;
    private final int HOURSE_RACING_ID = 71;
    private final int GREYHOUND_RACING_ID = 405;
    private final int FEED_TRAVEL_SECONDS = 2;


    public FeedSteps() {
        When("^I login in \"(PA|WIFT)\" RabbitMQ and enqueue Racing Event message based on \"([^\"]+)\"$", (String feedType, String templateFile) -> {
            final boolean isPA = "PA".equals(feedType);
            final String baseName = "QAFEED";
            eventNameRequested = Helpers.createUniqueNameForFeed(baseName);
            String eventId = String.format("%s_%s",
                    RandomStringUtils.randomNumeric(5),
                    RandomStringUtils.randomAlphanumeric(12));
            String payload = preparePayload(templateFile, eventId, eventNameRequested, 30);

            ConnectionFactory factory = new ConnectionFactory();
            factory.setVirtualHost("/");

            if (isPA) {
                factory.setHost(Config.feedMQPAHost());
                factory.setPort(Config.feedMQPAPort());
                factory.setUsername(Config.feedMQPAUsername());
                factory.setPassword(Config.feedMQPAPassword());
            } else {
                factory.setHost(Config.feedMQWiftHost());
                factory.setPort(Config.feedMQWiftPort());
                factory.setUsername(Config.feedMQWiftUsername());
                factory.setPassword(Config.feedMQWiftPassword());
            }

            try {
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();

                Map<String, Object> args = new HashMap<String, Object>();
                args.put("alternate-exchange", ALTERNATE_EXCHANGE_NAME);
                channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE, true, false, args);
                log.info("connected to rabbitMQ!");
                channel.basicPublish(EXCHANGE_NAME, "", null, payload.getBytes());
                log.info("Sent '" + payload + "'");

                channel.close();
                connection.close();
            } catch (Exception e) {
                throw new FrameworkError(e);
            }
        });

        Then("^OLD WagerPlayer will receive the \"(Horse Racing|Greyhound Racing)\" Event$", (String catName) -> {
            assertThat(eventNameRequested).as("Event created in previous step has name").isNotEmpty();
            Helpers.delayInMillis(FEED_TRAVEL_SECONDS * 1000);
            int catId = ("Horse Racing".equals(catName) ? HOURSE_RACING_ID : GREYHOUND_RACING_ID);
            String sessionId = wapi.login();
            Helpers.retryOnFailure(() -> {
                List<String> foundEventNames = wapi.getExistingEventNames(sessionId, catId, 24);
                assertThat(foundEventNames).as("Looking for Event with name=" + eventNameRequested).anySatisfy(n -> assertThat(n).endsWith(eventNameRequested));
            }, 5, 3);
        });

        Then("^WagerPlayer receives the \"(Horse Racing|Greyhound Racing)\" Event$", (String catName) -> {
            // DBG
            //            eventNameRequested = "QAFEED170807140713232";
            assertThat(eventNameRequested).as("Event created in previous step has name").isNotEmpty();
            Helpers.delayInMillis(FEED_TRAVEL_SECONDS * 1000);
            int catId = ("Horse Racing".equals(catName) ? HOURSE_RACING_ID : GREYHOUND_RACING_ID);
            apiSessionId = wapi.login();
            Helpers.retryOnFailure(() -> {
                JSONArray allEvents = wapi.getExistingEvents(apiSessionId, catId, 24);
                eventReceived = allEvents.stream()
                        .map(e -> (Map) e)
                        .filter(e -> matchByName((e), eventNameRequested))
                        .findFirst().orElse(null);
                assertThat(eventReceived).withFailMessage(String.format("No Events found matching name: '%s'", eventNameRequested)).isNotNull();

            }, 5, 3);

        });

        Then("^The received event contains scratched selection for \"([^\"]+)\"$", (String selName) -> {
            ReadContext resp = wapi.getEventMarkets(apiSessionId, (String) eventReceived.get("id"));
            Map selection = wapi.findOneSelectionByName(resp, selName);
            assertThat(selection.get("scratched")).as(String.format("Selection '%s' 'scratched' attribute", selName))
                    .isNotNull().isEqualTo(1);
        });


    }

    private boolean matchByName(Map event, String initialName) {
        Map nameMap = (Map) event.get("name");
        String name = nameMap.get("-content").toString();
        return name.endsWith(initialName);

    }

    private String preparePayload(String templateFile, String id, String name, int inMinutes) {
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

}

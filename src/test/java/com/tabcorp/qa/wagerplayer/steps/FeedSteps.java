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
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
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
    private String eventName;
    private final int HOURSE_RACING_ID = 71;
    private final int GREYHOUND_RACING_ID = 405;


    public FeedSteps() {
        When("^I login in RabbitMQ and enqueue Racing Event message based on \"([^\"]+)\"$", (String templateFile) -> {
            final String baseName = "QAFEED";
            eventName = Helpers.createUniqueNameForFeed(baseName);
            String eventId = String.format("%s_%s",
                    RandomStringUtils.randomNumeric(5),
                    RandomStringUtils.randomAlphanumeric(12));
            String payload = preparePayload(templateFile, eventId, eventName, 30);

            ConnectionFactory factory = new ConnectionFactory();
            factory.setVirtualHost("/");
            factory.setHost(Config.feedMQHost());
            factory.setPort(Config.feedMQPort());
            factory.setUsername(Config.feedMQUsername());
            factory.setPassword(Config.feedMQPassword());

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

        Then("^WagerPlayer will receive the \"(Horse Racing|Greyhound Racing)\" Event$", (String catName) -> {
            int catId = ("Horse Racing".equals(catName) ? HOURSE_RACING_ID : GREYHOUND_RACING_ID);
            String sessionId = wapi.login();
            List<String> eventNames = wapi.getExistingEventNames(sessionId, catId, 24);
            assertThat(eventNames).anySatisfy(i -> assertThat(i).endsWith(eventName));
        });

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

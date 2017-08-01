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
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FeedSteps implements En {
    private final static String EXCHANGE_NAME = "wift_primary";
    private final static String EXCHANGE_TYPE = "fanout";
    public static Logger log = LoggerFactory.getLogger(FeedSteps.class);
    private WAPI wapi = new WAPI();
    private String eventId;

    public FeedSteps() {
        When("^I login in RabbitMQ and enqueue event message based on \"([^\"]+)\"$", (String templateFile) -> {
            final String baseName = "QAFEED-";
            String eventName = Helpers.createUniqueName(baseName);
            eventId = String.format("%s_%s",
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
                channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE, true);
                log.info("connected to rabbitMQ!");
                channel.basicPublish(EXCHANGE_NAME, "", null, payload.getBytes());
                log.info("Sent '" + payload + "'");

                channel.close();
                connection.close();
            } catch (Exception e) {
                throw new FrameworkError(e);
            }
        });

        Then("^Event details can be retrieved via WP API$", () -> {
            String sessionId = wapi.login();
            ReadContext resp = wapi.getEvents(sessionId, 1);
            log.info(resp.toString());
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

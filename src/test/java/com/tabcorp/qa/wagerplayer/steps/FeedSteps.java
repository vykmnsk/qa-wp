package com.tabcorp.qa.wagerplayer.steps;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.tabcorp.qa.common.FrameworkError;
import com.tabcorp.qa.common.Helpers;
import com.tabcorp.qa.wagerplayer.Config;
import cucumber.api.java8.En;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FeedSteps implements En {
    private final static String EXCHANGE_NAME = "wift_primary";
    public static Logger log = LoggerFactory.getLogger(FeedSteps.class);


    public FeedSteps() {
        When("^I login in RabbitMQ and enqueue event message based on \"([^\"]+)\"$", (String templateFile) -> {
            final String baseName = "QAFEED-";
            String eventName = Helpers.createUniqueName(baseName);
            String id = String.format("%s_%s",
                    RandomStringUtils.randomNumeric(5),
                    RandomStringUtils.randomAlphanumeric(12));
            String payload = preparePayload(templateFile, id, eventName, 30);

            ConnectionFactory factory = new ConnectionFactory();
            factory.setVirtualHost("/");
            factory.setHost(Config.feedMQHost());
            factory.setPort(Config.feedMQPort());
            factory.setUsername(Config.feedMQUsername());
            factory.setPassword(Config.feedMQPassword());

            try {
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
                channel.exchangeDeclare(EXCHANGE_NAME, "direct", true);
                log.info("connected to rabbitMQ!");
                channel.basicPublish(EXCHANGE_NAME, "", null, payload.getBytes());
                log.info("Sent '" + payload + "'");

                channel.close();
                connection.close();
            } catch (IOException e) {
                throw new FrameworkError(e);
            }
        });

        When("^I login in RabbitMQ and enqueue hardcoded event message", () -> {
            String payload = String.join("\n"
                    , "{"
                    , "  \"id\": \"88578_338995test02\","
                    , "  \"feed_id\": \"pa-bridge\","
                    , "  \"local_date\": \"20170731\","
                    , "  \"start_time\": \"2017-07-31T21:50:00.000+0000\","
                    , "  \"race_status\": \"NotRun\","
                    , "  \"event_status\": \"Normal\","
                    , "  \"category\": \"Thoroughbred\","
                    , "  \"sub_category\": \"Chester\","
                    , "  \"race_num\": 1,"
                    , "  \"name\": \"TEST02 32Red Casino Handicap\","
                    , "  \"distance\": 1609,"
                    , "  \"prize_money\": \"GBP 7870\","
                    , "  \"handicap\": true,"
                    , "  \"participants\": ["
                    , "  {"
                    , "  \"id\": \"3189446\","
                    , "  \"name\": \"Mighty Lady\","
                    , "  \"number\": 1,"
                    , "  \"barrier\": 11,"
                    , "  \"rider\": \"Josephine Gordon\","
                    , "  \"trainer\": \"R Brisland\","
                    , "  \"status\": \"Normal\","
                    , "  \"last_five_starts\": \"15232-6\","
                    , "  \"emergency\": false,"
                    , "  \"silks_image_file_name\": \"20170705kmp211001.png\","
                    , "  \"weight\": \"136 pounds\""
                    , "  },"
                    , "  {"
                    , "  \"id\": \"3236231\","
                    , "  \"name\": \"Capton\","
                    , "  \"number\": 2,"
                    , "  \"barrier\": 3,"
                    , "  \"rider\": \"Non Runner\","
                    , "  \"trainer\": \"H Candy\","
                    , "  \"status\": \"Scratched\","
                    , "  \"last_five_starts\": \"253-20\","
                    , "  \"emergency\": false,"
                    , "  \"silks_image_file_name\": \"20170705kmp211002.png\","
                    , "  \"weight\": \"135 pounds\""
                    , "  },"
                    , "  {"
                    , "  \"id\": \"2813426\","
                    , "  \"name\": \"Multitask\","
                    , "  \"number\": 3,"
                    , "  \"barrier\": 6,"
                    , "  \"rider\": \"T P Queally\","
                    , "  \"trainer\": \"G L Moore\","
                    , "  \"status\": \"Normal\","
                    , "  \"last_five_starts\": \"1174-00\","
                    , "  \"emergency\": false,"
                    , "  \"silks_image_file_name\": \"20170705kmp211003.png\","
                    , "  \"weight\": \"134 pounds\""
                    , "  },"
                    , "  {"
                    , "  \"id\": \"3253555\","
                    , "  \"name\": \"Daschas\","
                    , "  \"number\": 4,"
                    , "  \"barrier\": 8,"
                    , "  \"rider\": \"P J Dobbs\","
                    , "  \"trainer\": \"Mrs A J Perrett\","
                    , "  \"status\": \"Normal\","
                    , "  \"last_five_starts\": \"31-7\","
                    , "  \"emergency\": false,"
                    , "  \"silks_image_file_name\": \"20170705kmp211004.png\","
                    , "  \"weight\": \"133 pounds\""
                    , "  },"
                    , "  {"
                    , "  \"id\": \"3238503\","
                    , "  \"name\": \"Glorious Poet\","
                    , "  \"number\": 5,"
                    , "  \"barrier\": 7,"
                    , "  \"rider\": \"L Morris\","
                    , "  \"trainer\": \"J L Spearing\","
                    , "  \"status\": \"Normal\","
                    , "  \"last_five_starts\": \"81-7738\","
                    , "  \"emergency\": false,"
                    , "  \"silks_image_file_name\": \"20170705kmp211005.png\","
                    , "  \"weight\": \"133 pounds\""
                    , "  },"
                    , "  {"
                    , "  \"id\": \"3258759\","
                    , "  \"name\": \"Mr Minerals\","
                    , "  \"number\": 6,"
                    , "  \"barrier\": 9,"
                    , "  \"rider\": \"S W Kelly\","
                    , "  \"trainer\": \"R Hughes\","
                    , "  \"status\": \"Normal\","
                    , "  \"last_five_starts\": \"1-4\","
                    , "  \"emergency\": false,"
                    , "  \"silks_image_file_name\": \"20170705kmp211006.png\","
                    , "  \"weight\": \"131 pounds\""
                    , "  },"
                    , "  {"
                    , "  \"id\": \"3217910\","
                    , "  \"name\": \"Tailor's Row\","
                    , "  \"number\": 7,"
                    , "  \"barrier\": 12,"
                    , "  \"rider\": \"F Norton\","
                    , "  \"trainer\": \"M Johnston\","
                    , "  \"status\": \"Normal\","
                    , "  \"last_five_starts\": \"226246\","
                    , "  \"emergency\": false,"
                    , "  \"silks_image_file_name\": \"20170705kmp211007.png\","
                    , "  \"weight\": \"131 pounds\""
                    , "  },"
                    , "  {"
                    , "  \"id\": \"3161848\","
                    , "  \"name\": \"Ebbisham\","
                    , "  \"number\": 8,"
                    , "  \"barrier\": 10,"
                    , "  \"rider\": \"Charlie Bennett\","
                    , "  \"trainer\": \"J R Boyle\","
                    , "  \"status\": \"Normal\","
                    , "  \"last_five_starts\": \"45-9160\","
                    , "  \"emergency\": false,"
                    , "  \"silks_image_file_name\": \"20170705kmp211008.png\","
                    , "  \"weight\": \"130 pounds\""
                    , "  },"
                    , "  {"
                    , "  \"id\": \"3230698\","
                    , "  \"name\": \"Hersigh\","
                    , "  \"number\": 9,"
                    , "  \"barrier\": 4,"
                    , "  \"rider\": \"Oisin Murphy\","
                    , "  \"trainer\": \"S bin Suroor\","
                    , "  \"status\": \"Normal\","
                    , "  \"last_five_starts\": \"2141-35\","
                    , "  \"emergency\": false,"
                    , "  \"silks_image_file_name\": \"20170705kmp211009.png\","
                    , "  \"weight\": \"130 pounds\""
                    , "  },"
                    , "  {"
                    , "  \"id\": \"3196595\","
                    , "  \"name\": \"Braztime\","
                    , "  \"number\": 10,"
                    , "  \"barrier\": 1,"
                    , "  \"rider\": \"Tom Marquand\","
                    , "  \"trainer\": \"R Hannon\","
                    , "  \"status\": \"Normal\","
                    , "  \"last_five_starts\": \"536-618\","
                    , "  \"emergency\": false,"
                    , "  \"silks_image_file_name\": \"20170705kmp211010.png\","
                    , "  \"weight\": \"129 pounds\""
                    , "  },"
                    , "  {"
                    , "  \"id\": \"3196464\","
                    , "  \"name\": \"Glorious Power\","
                    , "  \"number\": 11,"
                    , "  \"barrier\": 2,"
                    , "  \"rider\": \"J P Spencer\","
                    , "  \"trainer\": \"C Hills\","
                    , "  \"status\": \"Normal\","
                    , "  \"last_five_starts\": \"8-1096\","
                    , "  \"silks_image_file_name\": \"20170705kmp211011.png\","
                    , "  \"weight\": \"126 pounds\""
                    , "  },"
                    , "  {"
                    , "  \"id\": \"3219641\","
                    , "  \"name\": \"Haulani\","
                    , "  \"number\": 12,"
                    , "  \"barrier\": 5,"
                    , "  \"rider\": \"Hector Crouch\","
                    , "  \"trainer\": \"P Hide\","
                    , "  \"status\": \"Normal\","
                    , "  \"last_five_starts\": \"143-887\","
                    , "  \"emergency\": false,"
                    , "  \"silks_image_file_name\": \"20170705kmp211012.png\","
                    , "  \"weight\": \"124 pounds\""
                    , "  }"
                    , "  ]"
                    , "}"
            );
            ConnectionFactory factory = new ConnectionFactory();
            factory.setVirtualHost("/");
            factory.setHost(Config.feedMQHost());
            factory.setPort(Config.feedMQPort());
            factory.setUsername(Config.feedMQUsername());
            factory.setPassword(Config.feedMQPassword());

            try {
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
                channel.exchangeDeclare(EXCHANGE_NAME, "direct", true);
                log.info("connected to rabbitMQ!");
                channel.basicPublish(EXCHANGE_NAME, "", null, payload.getBytes());
                log.info("Sent '" + payload + "'");

                channel.close();
                connection.close();
            } catch (IOException e) {
                throw new FrameworkError(e);
            }
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

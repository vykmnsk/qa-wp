package com.tabcorp.qa.wagerplayer.steps;

import com.tabcorp.qa.common.FrameworkError;
import com.tabcorp.qa.common.Helpers;
import cucumber.api.java8.En;
import org.apache.commons.lang3.RandomStringUtils;
import org.gearman.Gearman;
import org.gearman.GearmanClient;
import org.gearman.GearmanJobEvent;
import org.gearman.GearmanJobEventType;
import org.gearman.GearmanJobReturn;
import org.gearman.GearmanServer;
import org.gearman.impl.GearmanImpl;
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
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;


/**
 * Created by klindy on 11/8/17.
 */
public class FeedGearmanSteps implements En {
    final String WORKER_NAME = "ss_market_create"; //"ss_burrito_market_update"
    final String WORKLOAD_TYPE = "ss_snapshot";


    public static Logger log = LoggerFactory.getLogger(FeedSteps.class);

    private String eventNameRequested;

    public FeedGearmanSteps() {
        When("^I login in Gearman and schedule a job with data based on \"([^\"]*)\"$", (String templateFile) -> {

            final String baseName = "QAFEED";
            eventNameRequested = Helpers.createUniqueNameForFeed(baseName);
            String eventId = String.format("%s_%s",
                    RandomStringUtils.randomNumeric(5),
                    RandomStringUtils.randomAlphanumeric(12));
            int startInMinutes = 30;
            String workload = prepareWorkload(templateFile, eventId, eventNameRequested, startInMinutes, WORKER_NAME, WORKLOAD_TYPE);

            String jobType = "ss_snapshot";
            try {
                log.info("Submitting the job to the worker: {} ", jobType);
                Gearman gearman;
                try {
                    gearman = new GearmanImpl();
                } catch (IOException e) {
                    throw new FrameworkError(e);
                }
                GearmanServer server = gearman.createGearmanServer("php7-rds0-gearman.sunppw.in.cld", 4730);
                final GearmanClient client = gearman.createGearmanClient();
                client.addServer(server);

                GearmanJobReturn gearmanJobReturn;
                gearmanJobReturn = client.submitJob(jobType, workload.getBytes("UTF-8"));
                GearmanJobEvent gearmanJobEvent = gearmanJobReturn.poll();
                while (gearmanJobEvent.getEventType() != GearmanJobEventType.GEARMAN_EOF) {
                    gearmanJobEvent = gearmanJobReturn.poll();
                    log.debug(".");
                }
                log.info("Job Taken by Gearman: {}", gearmanJobEvent);
            } catch (Exception e) {
                throw new FrameworkError(e);
            }
        });

    }

    private String prepareWorkload(String templateFile, String eventId, String eventName, int inMinutes, String worker, String workloadType) {
        JSONObject payloadTempl = Helpers.readJSON(templateFile);
        JSONObject payload = updateJSON(payloadTempl, eventId, eventName, inMinutes);
        JSONObject workload = new JSONObject();
        workload.put("snapshot", compressAndEncode(payload.toJSONString()));
        workload.put("resourceName", eventName);
        workload.put("marketWorkerName", worker);
        workload.put("type", workloadType);
        return workload.toJSONString();
    }

    private JSONObject updateJSON(JSONObject json, String id, String name, int inMinutes) {
        LocalDateTime startTime = LocalDateTime.now().plusMinutes(inMinutes);
        String startTimeStamp = startTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        json.put("Id", id);
        json.put("FixtureName", name);
        json.put("StartTime", startTimeStamp);
        return json;
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
}

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


    public static Logger log = LoggerFactory.getLogger(FeedSteps.class);

    private String eventNameRequested;

    public FeedGearmanSteps() {


    }



}

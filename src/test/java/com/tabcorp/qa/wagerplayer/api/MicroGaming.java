package com.tabcorp.qa.wagerplayer.api;

import com.tabcorp.qa.common.Helpers;
import com.tabcorp.qa.common.REST;
import com.tabcorp.qa.common.Storage;
import com.tabcorp.qa.wagerplayer.Config;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class MicroGaming {

    private static final String API_URL = Config.env_URL() + "/mgsapi.php";
    private static final Logger log = LoggerFactory.getLogger(MicroGaming.class);

    public void placeBet(String customerID, String gameID, BigDecimal betAmount) {
        String sequenceID = RandomStringUtils.randomAlphanumeric(32);
        String actionID = RandomStringUtils.randomNumeric(15);
        // Making the call to the micro gaming token here because the micro gaming token can only be used once per call.
        String mToken = new MOBI_V2().getMicrogamingToken((String) Storage.get(Storage.KEY.API_ACCESS_TOKEN));
        // the stake placed is always is in cents.Hence the multiplication by 100;
        String betAmountInCents = (betAmount.multiply(BigDecimal.valueOf(100.00))).toString();
        Map<String, Object> templateData = new HashMap<>();

        String timeStamp = Helpers.timestamp("yyyy/MM/dd HH:mm:ss.SSS");
        templateData.put("sequenceID", sequenceID);
        templateData.put("customerID", customerID);
        templateData.put("microGamingToken", mToken);
        templateData.put("gameID", gameID);
        templateData.put("betAmount", betAmountInCents);
        templateData.put("timeStamp", timeStamp);
        templateData.put("actionID", actionID);

        String requestPayload = Helpers.getRequestPayLoad(templateData, "microgaming-bet.ftl");
        REST.postXML(API_URL, requestPayload);
    }

    public void placeEndBet(String customerID, String gameID) {
        String sequenceID = RandomStringUtils.randomAlphanumeric(32);
        // Making the call to the micro gaming token here because the micro gaming token can only be used once per call.
        String mToken = new MOBI_V2().getMicrogamingToken((String) Storage.get(Storage.KEY.API_ACCESS_TOKEN));
        Map<String, Object> templateData = new HashMap<>();

        String timeStamp = Helpers.timestamp("yyyy/MM/dd HH:mm:ss.SSS");
        templateData.put("sequenceID", sequenceID);
        templateData.put("customerID", customerID);
        templateData.put("microGamingToken", mToken);
        templateData.put("gameID", gameID);
        templateData.put("timeStamp", timeStamp);

        String requestPayload = Helpers.getRequestPayLoad(templateData, "microgaming-end.ftl");
        REST.postXML(API_URL, requestPayload);
    }

}

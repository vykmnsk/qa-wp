package com.tabcorp.qa.wagerplayer.api;

import com.tabcorp.qa.common.Helpers;
import com.tabcorp.qa.common.REST;
import com.tabcorp.qa.common.Storage;
import com.tabcorp.qa.wagerplayer.Config;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class MicroGaming {

    private static final String API_URL = Config.env_URL() + "/mgsapi.php";

    public void placeWinBet(final String customerID, final String gameID, final BigDecimal betAmount) {
        final String sequenceID = RandomStringUtils.randomAlphanumeric(32);
        final String actionID = RandomStringUtils.randomNumeric(15);
        // Making the call to the micro gaming token here because the micro gaming token can only be used once per call.
        final String mToken = new MOBI_V2().getMicrogamingToken((String) Storage.get(Storage.KEY.API_ACCESS_TOKEN));
        // the stake placed is always is in cents.Hence the multiplication by 100;
        final String betAmountInCents = betAmount.multiply(BigDecimal.valueOf(100)).toString();
        HashMap<String, Object> templateData = new HashMap<>();

        final String timeStamp = Helpers.timestamp("yyyy/MM/dd HH:mm:ss.SSS");
        templateData.put("sequenceID", sequenceID);
        templateData.put("customerID", customerID);
        templateData.put("microGamingToken", mToken);
        templateData.put("gameID", gameID);
        templateData.put("betAmount", betAmountInCents);
        templateData.put("timeStamp", timeStamp);
        templateData.put("actionID", actionID);
        String requestPayload = Helpers.getRequestPayLoad(templateData, "microgaming-win.ftl");
        REST.postXML(API_URL, requestPayload);
    }

    public String placeBet(final String customerID, final String gameID, final BigDecimal betAmount) {
        String sequenceID = RandomStringUtils.randomAlphanumeric(32);
        String actionID = RandomStringUtils.randomNumeric(15);
        // Making the call to the micro gaming token here because the micro gaming token can only be used once per call.
        String mToken = new MOBI_V2().getMicrogamingToken((String) Storage.get(Storage.KEY.API_ACCESS_TOKEN));
        // the stake placed is always is in cents.Hence the multiplication by 100;
        String betAmountInCents = betAmount.multiply(BigDecimal.valueOf(100)).toString();
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
        return REST.postXML(API_URL, requestPayload);
    }

    public String placeEndBet(final String customerID, final String gameID) {
        String sequenceID = RandomStringUtils.randomAlphanumeric(32);
        // Making the call to the micro gaming token here because the micro gaming token can only be used once per call.
        String mToken = new MOBI_V2().getMicrogamingToken((String) Storage.get(Storage.KEY.API_ACCESS_TOKEN));
        String actionID = RandomStringUtils.randomNumeric(15);
        Map<String, Object> templateData = new HashMap<>();
        String timeStamp = Helpers.timestamp("yyyy/MM/dd HH:mm:ss.SSS");
        templateData.put("sequenceID", sequenceID);
        templateData.put("customerID", customerID);
        templateData.put("microGamingToken", mToken);
        templateData.put("gameID", gameID);
        templateData.put("timeStamp", timeStamp);
        templateData.put("actionID", actionID);
        String requestPayload = Helpers.getRequestPayLoad(templateData, "microgaming-end.ftl");
        //returns updated balance.
        return readBalance(REST.postXML(API_URL, requestPayload));
    }

    //Method to ensure the right amount is won. Required in specific scenarios.
    public void placeEndBet(final String customerID, final String gameID, final BigDecimal betAmount, final BigDecimal winningAmount) {
        BigDecimal actualBalanceInCents = new BigDecimal(placeEndBet(customerID, gameID));
        BigDecimal expectedBalance = winningAmount.subtract(betAmount).add((BigDecimal) Storage.get(Storage.KEY.BALANCE_BEFORE));
        BigDecimal expectedBalanceInCents = expectedBalance.multiply(new BigDecimal("100"));
        assertThat(Helpers.roundOff(actualBalanceInCents)).isEqualTo(Helpers.roundOff(expectedBalanceInCents));
    }

    private String readBalance(final String betResponse) {
        String myXpath = "/pkt/methodresponse/result/@balance";
        String balance = Helpers.extractByXpath(betResponse, myXpath).toString();
        assertThat(balance).as("Microgaming::Balance-Text").isNotNull();
        return balance;
    }

    public static String readErrorMessage(final String betResponse) {
        String myXpath = "/pkt/methodresponse/result/@errordescription";
        String errorText = Helpers.extractByXpath(betResponse, myXpath).toString();
        assertThat(errorText).as("MicroGaming::Error-Description").isNotNull();
        return errorText;
    }

    public static String readErrorCode(final String betResponse) {
        String myXpath = "/pkt/methodresponse/result/@errorcode";
        String errorCode = Helpers.extractByXpath(betResponse, myXpath).toString();
        assertThat(errorCode).as("MicroGaming::Error-Code").isNotNull();
        return errorCode;
    }

}

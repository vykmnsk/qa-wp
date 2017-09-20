package com.tabcorp.qa.wagerplayer.api;

import com.tabcorp.qa.common.Helpers;
import com.tabcorp.qa.common.REST;
import com.tabcorp.qa.wagerplayer.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayTech {

    private static final String API_URL = Config.env_URL() + "/playtech-gameplay-api/";

    private static final Logger log = LoggerFactory.getLogger(PlayTech.class);

    public String login(String username, String password) {
        String accessToken;
        Map<String, Object> templateData = new HashMap<>();
        String myXpath = "/*[local-name()='loginResponse']/*[local-name()='externalSessionToken']";
        templateData.put("username", username);
        templateData.put("password", password);
        String requestPayload = Helpers.getRequestPayLoad(templateData, "playtech-login.ftl");
        String response = REST.postXML(API_URL, requestPayload);
        accessToken = Helpers.extractByXpath(response, myXpath).toString();
        log.info("PlayTech Access Token :" + accessToken);
        return accessToken;
    }

    public String placeWinBet(String accessToken, Map<String, String> custData, BigDecimal stake, String gameProvider, String gameType) {
        return placeBet(accessToken, custData, stake, gameProvider, gameType, "playtech-place-and-win-bet.ftl");
    }

    public String placeBet(String accessToken, Map<String, String> custData, BigDecimal stake, String gameProvider, String gameType) {
        return placeBet(accessToken, custData, stake, gameProvider, gameType, "playtech-bet.ftl");
    }

    private String placeBet(String accessToken, Map<String, String> custData, BigDecimal betValue, String gameProvider, String gameType, String templateFile) {
        // Add a different prefix to the message id ike "auto-xxx" easier to debug
        int MAX_MESSAGE_ID_RANGE = 999999999;
        String messageIDPrefix = "auto-";
        String messageID = messageIDPrefix.concat(Integer.toString(Helpers.randomBetweenInclusive(0, MAX_MESSAGE_ID_RANGE)));
        String transactionCode = Integer.toString(Helpers.randomBetweenInclusive(0, MAX_MESSAGE_ID_RANGE));
        Map<String, Object> templateData = new HashMap<>();

        gameProvider = (gameProvider == null ? "rhino" : gameProvider);
        String timeStamp = Helpers.timestamp("yyyy/MM/dd HH:mm:ss.SSS");
        gameType = (gameType == null ? "jdean" : gameType);
        String betAmount = betValue.toString();
        String currency = "GBP";

        templateData.put("playTechAccessToken", accessToken);
        templateData.put("username", custData.get("username"));
        templateData.put("password", custData.get("password"));
        templateData.put("messageID", messageID);
        templateData.put("gameProvider", gameProvider);
        templateData.put("currency", currency);
        templateData.put("betAmount", betAmount);
        templateData.put("gameType", gameType);
        templateData.put("timeStamp", timeStamp);
        templateData.put("transactionCode", transactionCode);

        String requestPayload = Helpers.getRequestPayLoad(templateData, templateFile);

        return REST.postXML(API_URL,requestPayload);
    }

    public static String getErrorMessage(String betResponse) {
        String myXpath = "/*[local-name()='walletBatchResponse']/*[local-name()='gameMultiBalanceTransactionResponse']/*[local-name()='errorText']";
        String errorText = Helpers.extractByXpath(betResponse, myXpath).toString();
        assertThat(errorText).as("Playtech::Error-Text").isNotNull();
        return errorText;
    }

    public static String getErrorCode(String betResponse) {
        String myXpath = "/*[local-name()='walletBatchResponse']/*[local-name()='gameMultiBalanceTransactionResponse']/*[local-name()='errorCode']";
        String errorCode = Helpers.extractByXpath(betResponse, myXpath).toString();
        assertThat(errorCode).as("Playtech::Error-Code").isNotNull();
        return errorCode;
    }

}

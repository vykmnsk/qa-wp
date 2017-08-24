package com.tabcorp.qa.wagerplayer.api;

import com.mashape.unirest.http.HttpResponse;
import com.tabcorp.qa.common.FrameworkError;
import com.tabcorp.qa.common.Helpers;
import com.tabcorp.qa.common.REST;
import com.tabcorp.qa.wagerplayer.Config;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;

import java.io.File;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayTech {

    private static String URL_ROOT = Config.env_URL();
    private final String PLAYTECH_RSC_PATH = "src/test/resources/playtech/";
    private final int MAX_MESSAGE_ID_RANGE = 999999999;
    private final String messageIDPrefix = "auto-";
    private final String FREE_MARKER_VERSION = "2.3.23";


    private String postXML(String url, Map<String, String> headers, String requestBody) {
        HttpResponse<String> verifiedResponse = REST.postXML(url, requestBody, headers);
        return REST.verifyXMLResponse(verifiedResponse);
    }

    private Configuration getTemplateConfig() {
        Configuration cfg = new Configuration(new Version(FREE_MARKER_VERSION));

        try {
            cfg.setDirectoryForTemplateLoading(new File(PLAYTECH_RSC_PATH));
            cfg.setDefaultEncoding("UTF-8");
        } catch (Exception e) {
            System.out.println("Something went wrong in PlayTech::getTemplateConfiguration method");

        }
        return cfg;
    }

    private String getRequestPayLoad(Map<String, Object> templateData, Template template) {
        String requestPayload;
        try {
            try (StringWriter out = new StringWriter()) {

                template.process(templateData, out);
                requestPayload = out.getBuffer().toString();
                out.flush();
            }
        } catch (Exception e) {
            throw new FrameworkError(String.format("Something went wrong in PlayTech::getRequestPayload method : %s", e));
        }
        return requestPayload;
    }

    public String login(String username, String password) {

        Configuration cfg = getTemplateConfig();
        String accessToken;
        Map<String, Object> templateData = new HashMap<>();

        templateData.put("username", username);
        templateData.put("password", password);

        try {
            Template template = cfg.getTemplate("login.ftl");
            String requestPayload = getRequestPayLoad(templateData, template);

            Map<String, String> headers = new HashMap<>();
            headers.put("content-type", "text/xml");
            String response = postXML(URL_ROOT + "/playtech-gameplay-api/", headers, requestPayload);
            String myXpath = "/*[local-name()='loginResponse']/*[local-name()='externalSessionToken']";

            accessToken = Helpers.extractByXpath(response, myXpath).toString();
            assertThat(accessToken).isNotEmpty();

        } catch (Exception e) {
            throw new FrameworkError(String.format("Something went wrong in PlayTech::Login method : %s", e));
        }
        return accessToken;
    }

    public String placeBet(String accessToken, Map<String, String> custData, BigDecimal betValue, String gameProvider, String gameType) {

        // Add a different prefix to the message id ike "auto-xxx" easier to debug
        String messageID = messageIDPrefix.concat(Integer.toString(Helpers.randomBetweenInclusive(0, MAX_MESSAGE_ID_RANGE)));
        String transactionCode = Integer.toString(Helpers.randomBetweenInclusive(0, MAX_MESSAGE_ID_RANGE));
        gameProvider = (gameProvider == null ? "rhino" : gameProvider);
        String timeStamp = Helpers.timestamp("yyyy/MM/dd HH:mm:ss.SSS");
        gameType = (gameType == null ? "jdean" : gameType);
        String betAmount = betValue.negate().toString();
        String currency = "GBP";

        Map<String, Object> templateData = new HashMap<>();

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

        try {
            Template template = getTemplateConfig().getTemplate("bet.ftl");
            String requestPayload = getRequestPayLoad(templateData, template);

            Map<String, String> headers = new HashMap<>();
            headers.put("content-type", "text/xml");
            return postXML(URL_ROOT + "/playtech-gameplay-api/", headers, requestPayload);

        } catch (Exception e) {
            throw new FrameworkError(String.format("Something went wrong in PlayTech::placeBet method : %s", e));
        }

    }


}

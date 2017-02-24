package com.tabcorp.qa.wagerplayer.api;

import com.jayway.jsonpath.JsonPath;
import com.tabcorp.qa.common.REST;
import com.tabcorp.qa.wagerplayer.Config;
import net.minidev.json.JSONArray;
import org.assertj.core.api.Assertions;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


public class WAPI {

    static String URL = Config.wapiURL();

    static Object post(Map<String, Object> fields) {
        fields.put("output_type", "json");
        Object resp = REST.post(URL, fields);
        JSONArray errors = JsonPath.read(resp, "$..error..error_text");
        Assertions.assertThat(errors).as("Errors in response").isEmpty();
        return resp;
    }
    public static String login(){
        Map<String, Object> fields = new HashMap<>();
        fields.put("action", "bet_customer_login");
        fields.put("wapi_client_user", Config.wapiUsername());
        fields.put("wapi_client_pass", Config.wapiPassword());
        fields.put("username", Config.customerUsername());
        fields.put("password", Config.customerPassword());
        Object resp = post(fields);
        return JsonPath.read(resp, "$.RSP.login[0].session");
    }

    public static BigDecimal getBalance(String sessionId){
        Map<String, Object> fields = new HashMap<>();
        fields.put("action", "bet_get_balance");
        fields.put("wapi_client_user", Config.wapiUsername());
        fields.put("wapi_client_pass", Config.wapiPassword());
        fields.put("session_id", sessionId);
        Object resp = post(fields);
        String balanceValue = JsonPath.read(resp, "$.RSP.account[0].balance");
        return new BigDecimal(balanceValue);
    }

    public static void verifyBalanceGreaterThan(String sessionId, BigDecimal minBalance) {
        BigDecimal currentBalance = getBalance(sessionId);
        Assertions.assertThat(currentBalance).as("Current balance").isGreaterThan(minBalance);
    }
}

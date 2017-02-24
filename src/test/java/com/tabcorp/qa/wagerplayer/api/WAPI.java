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

    public static String login() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("action", "bet_customer_login");
        fields.put("wapi_client_user", Config.wapiUsername());
        fields.put("wapi_client_pass", Config.wapiPassword());
        fields.put("username", Config.customerUsername());
        fields.put("password", Config.customerPassword());
        Object resp = post(fields);
        return JsonPath.read(resp, "$.RSP.login[0].session");
    }

    public static BigDecimal getBalance(String sessionId) {
        Map<String, Object> fields = new HashMap<>();
        fields.put("action", "bet_get_balance");
        fields.put("wapi_client_user", Config.wapiUsername());
        fields.put("wapi_client_pass", Config.wapiPassword());
        fields.put("session_id", sessionId);
        Object resp = post(fields);
        String balance = JsonPath.read(resp, "$.RSP.account[0].balance");
        return new BigDecimal(balance);
    }

    public static Object placeBetSingleWin(String sessionId, String productId, String mpid, String winPrice, BigDecimal stake) {
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "bet_place_bet");
        fields.put("product_id", productId);
        fields.put("mpid", mpid);
        fields.put("win_price", winPrice);
        fields.put("amount", stake);
        return post(fields);
    }

    public static Object placeBetSingleEW(String sessionId, String productId, String mpid, String winPrice, String placePrice, BigDecimal stake) {
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "bet_place_bet");
        fields.put("product_id", productId);
        fields.put("mpid", mpid);
        fields.put("win_price", winPrice);
        fields.put("place_price", placePrice);
        fields.put("amount", stake);
        return post(fields);
    }

    static Map<String, Object> wapiAuthFields(){
        Map<String, Object> fields = new HashMap<>();
        fields.put("wapi_client_user", Config.wapiUsername());
        fields.put("wapi_client_pass", Config.wapiPassword());
        return fields;
    }

    static Map<String, Object> wapiAuthFields(String sessionId){
        Map<String, Object> fields = wapiAuthFields();
        fields.put("session_id", sessionId);
        return fields;
    }

    public static BigDecimal readNewBalance(Object resp){
        String newBalance = JsonPath.read(resp, "$.RSP.bet[0].new_balance");
        return new BigDecimal(newBalance);
    }
}

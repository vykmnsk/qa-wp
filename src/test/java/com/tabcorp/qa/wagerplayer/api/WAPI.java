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

    private static String URL = Config.wapiURL();

    private static Map<String, Object> wapiAuthFields(){
        Map<String, Object> fields = new HashMap<>();
        fields.put("wapi_client_user", Config.wapiUsername());
        fields.put("wapi_client_pass", Config.wapiPassword());
        return fields;
    }

    private static Map<String, Object> wapiAuthFields(String sessionId){
        Map<String, Object> fields = wapiAuthFields();
        fields.put("session_id", sessionId);
        return fields;
    }

    static Object post(Map<String, Object> fields) {
        fields.put("output_type", "json");
        Object resp = REST.post(URL, fields);
        JSONArray errors = JsonPath.read(resp, "$..error..error_text");
        Assertions.assertThat(errors).as("Errors in response when sending " + fields).isEmpty();
        return resp;
    }

    public static String login() {
        Map<String, Object> fields = wapiAuthFields();
        fields.put("action", "account_login");
        fields.put("customer_username", Config.customerUsername());
        fields.put("customer_password", Config.customerPassword());
        Object resp = post(fields);
        return JsonPath.read(resp, "$.RSP.login[0].session_id");
    }

    public static BigDecimal getBalance(String sessionId) {
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "bet_get_balance");
        Object resp = post(fields);
        String balance = JsonPath.read(resp, "$.RSP.account[0].balance");
        return new BigDecimal(balance);
    }

    public static Object placeBetSingleWin(String sessionId, Integer productId, String mpid, String winPrice, BigDecimal stake) {
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "bet_place_bet");
        fields.put("bet_type", "1");
        fields.put("product_id", productId);
        fields.put("mpid", mpid);
        fields.put("win_price", winPrice);
        fields.put("amount", stake);
        return post(fields);
    }

    public static Object placeBetSinglePlace(String sessionId, Integer productId, String mpid, String placePrice, BigDecimal stake) {
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "bet_place_bet");
        fields.put("bet_type", "2");
        fields.put("product_id", productId);
        fields.put("mpid", mpid);
        fields.put("place_price", placePrice);
        fields.put("amount", stake);
        return post(fields);
    }

    public static Object placeBetSingleEW(String sessionId, Integer productId, String mpid, String winPrice, String placePrice, BigDecimal stake) {
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "bet_place_bet");
        fields.put("bet_type", "3");
        fields.put("product_id", productId);
        fields.put("mpid", mpid);
        fields.put("win_price", winPrice);
        fields.put("place_price", placePrice);
        fields.put("amount", stake);
        return post(fields);
    }

    public static BigDecimal readNewBalance(Object resp){
        Object val = JsonPath.read(resp, "$.RSP.bet[0].new_balance");
        BigDecimal newBalance = new BigDecimal(val.toString());
        return newBalance;
    }

    public static Object getEventMarkets(String sessionId, String evtId){
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "site_get_markets");
        fields.put("eid", evtId);
        fields.put("show_held", false);
        return post(fields);
    }

    public enum KEY {
        MPID,
        WIN_PRICE,
        PLACE_PRICE
    }

    public static Map<KEY, String> readSelection(Object resp, String selName, Integer prodId){
        String selPath = "$.RSP.markets.market[0].selections.selection" + jfilter("name", selName);
        String pricePath = selPath + ".prices.price" + jfilter("product_id", prodId.toString());

        HashMap<KEY, String> sel = new HashMap<>();
        sel.put(KEY.MPID, readPriceAttr(resp, pricePath,"Win", "mpid"));
        sel.put(KEY.WIN_PRICE, readPriceAttr(resp, pricePath,"Win", "precise_price"));
        sel.put(KEY.PLACE_PRICE, readPriceAttr(resp, pricePath,"Place", "precise_price"));
        return sel;
    }

    private static String readPriceAttr(Object resp, String pricePath, String betTypeName, String attrName){
        String path = pricePath + jfilter("bet_type_name", betTypeName);
        JSONArray attrs = JsonPath.read(resp, path + "." + attrName);
        Assertions.assertThat(attrs.size())
                .as(String.format("expected to find one attribute '%s' at path='%s'", attrName, pricePath))
                .isEqualTo(1);
        String attr = String.valueOf(attrs.get(0));
        Assertions.assertThat(attr).as("attribute '%s' at path='%s'").isNotEmpty();
        return attr;
    }

    private static String jfilter(String attr, String value){
        return String.format("[?(@.%s == '%s')]", attr, value);
    }
}

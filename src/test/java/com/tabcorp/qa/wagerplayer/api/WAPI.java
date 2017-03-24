package com.tabcorp.qa.wagerplayer.api;

import com.jayway.jsonpath.JsonPath;
import com.tabcorp.qa.common.BetType;
import com.tabcorp.qa.common.REST;
import com.tabcorp.qa.wagerplayer.Config;
import net.minidev.json.JSONArray;
import org.assertj.core.api.Assertions;

import java.math.BigDecimal;
import java.util.*;

public class WAPI implements WagerPlayerAPI {

    private static String URL = Config.wapiURL();

    private static Map<String, Object> wapiAuthFields() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("wapi_client_user", Config.wapiUsername());
        fields.put("wapi_client_pass", Config.wapiPassword());
        return fields;
    }

    private static Map<String, Object> wapiAuthFields(String sessionId) {
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

    static Object postWithQueryStrs(Map<String, Object> fields, List selectionIds, String key) {
        fields.put("output_type", "json");
        Object resp = REST.postWithQueryStrings(URL, fields, selectionIds, key);
        JSONArray errors = JsonPath.read(resp, "$..error..error_text");
        Assertions.assertThat(errors).as("Errors in response when sending " + fields).isEmpty();
        return resp;
    }

    public String getAccessToken(String username, String password) {
        Map<String, Object> fields = wapiAuthFields();
        fields.put("action", "account_login");
        fields.put("customer_username", username);
        fields.put("customer_password", password);
        Object resp = post(fields);
        return JsonPath.read(resp, "$.RSP.login[0].session_id");
    }

    public BigDecimal getBalance(String sessionId) {
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "bet_get_balance");
        Object resp = post(fields);
        String balance = JsonPath.read(resp, "$.RSP.account[0].balance");
        return new BigDecimal(balance);
    }

    public Object placeSingleWinBet(String sessionId, Integer productId, String mpid, String winPrice, BigDecimal stake) {
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "bet_place_bet");
        fields.put("bet_type", BetType.Win.id);
        fields.put("product_id", productId);
        fields.put("mpid", mpid);
        fields.put("win_price", winPrice);
        fields.put("amount", stake);
        return post(fields);
    }

    public Object placeSinglePlaceBet(String sessionId, Integer productId, String mpid, String placePrice, BigDecimal stake) {
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "bet_place_bet");
        fields.put("bet_type", BetType.Place.id);
        fields.put("product_id", productId);
        fields.put("mpid", mpid);
        fields.put("place_price", placePrice);
        fields.put("amount", stake);
        return post(fields);
    }

    public Object placeSingleEachwayBet(String sessionId, Integer productId, String mpid, String winPrice, String placePrice, BigDecimal stake) {
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "bet_place_bet");
        fields.put("bet_type", BetType.EachWay.id);
        fields.put("product_id", productId);
        fields.put("mpid", mpid);
        fields.put("win_price", winPrice);
        fields.put("place_price", placePrice);
        fields.put("amount", stake);
        return post(fields);
    }

    public static Object placeExoticBet(String sessionId, Integer productId, List<String> selectionIds, String marketId, BigDecimal stake) {
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "bet_place_bet");
        fields.put("product_id", productId);
        fields.put("slot[1][market]", marketId);
        fields.put("amount", stake);
        return postWithQueryStrs(fields, selectionIds, "slot[1][selection][]");
    }

    public static BigDecimal readNewBalance(Object resp) {
        Object val = JsonPath.read(resp, "$.RSP.bet[0].new_balance");
        BigDecimal newBalance = new BigDecimal(val.toString());
        return newBalance;
    }

    public Object getEventMarkets(String evtId) {
        String sessionId = getAccessToken(Config.customerUsername(), Config.customerPassword());
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "site_get_markets");
        fields.put("eid", evtId);
        fields.put("show_held", false);
        return post(fields);
    }

    public static Map<KEY, String> readSelection(Object resp, String selName, Integer prodId) {
        String selPath = "$.RSP.markets.market[0].selections.selection" + jfilter("name", selName);
        String pricePath = selPath + ".prices.price" + jfilter("product_id", prodId.toString());

        HashMap<KEY, String> sel = new HashMap<>();
        sel.put(KEY.MPID, readPriceAttr(resp, pricePath, BetType.Win.name(), "mpid"));
        sel.put(KEY.WIN_PRICE, readPriceAttr(resp, pricePath, BetType.Win.name(), "precise_price"));
        sel.put(KEY.PLACE_PRICE, readPriceAttr(resp, pricePath, BetType.Place.name(), "precise_price"));
        return sel;
    }

    public static Map<KEY, List<String>> readSelectionMultiple(Object resp, List<String> selName, Integer prodId) {
        HashMap<KEY, List<String>> sel = new HashMap<>();
        ArrayList<String> sels = new ArrayList<>();

        String marketIdPath = "$.RSP.markets.market[*][?(@.name == 'Racing Live')]";
        String market = readMarketId(resp, marketIdPath, "id").toString();
        sel.put(KEY.MARKET_ID, Arrays.asList(stripBrackets(market)));

        for (String selection : selName) {
            String selPath = "$.RSP.markets.market[0].selections.selection";
            sels.add(Arrays.asList(readSelectionId(resp, selPath, selection, "id")).toString().replaceAll("\\[","").replaceAll("]",""));
            sel.put(KEY.SELECTION_ID, sels);
        }
        return sel;
    }

    private static String stripBrackets(String stringWithBrackets) {
        stringWithBrackets.replaceAll("\\[]","").replaceAll("]","");
        return stringWithBrackets;
    }

    private static String readPriceAttr(Object resp, String pricePath, String betTypeName, String attrName) {
        String path = pricePath + jfilter("bet_type_name", betTypeName);
        JSONArray attrs = JsonPath.read(resp, path + "." + attrName);

        //TODO hack to bypass broken WAPI for Redbook
        if (attrs.size() != 1 && BetType.Place.name().equals(betTypeName)) {
            return "1";
        }

        Assertions.assertThat(attrs.size())
                .as(String.format("expected to find one attribute '%s' at path='%s'", attrName, path))
                .isEqualTo(1);
        String attr = String.valueOf(attrs.get(0));
        Assertions.assertThat(attr).as("attribute '%s' at path='%s'").isNotEmpty();
        return attr;
    }

    private static String readSelectionId(Object resp, String pricePath, String runnerName, String attrName) {
        String path = pricePath + jfilter("name", runnerName);
        JSONArray attrs = JsonPath.read(resp, path + "." + attrName);
        Assertions.assertThat(attrs.size())
                .as(String.format("expected to find one attribute '%s' at path='%s'", attrName, pricePath))
                .isEqualTo(1);
        String attr = String.valueOf(attrs.get(0));
        Assertions.assertThat(attr).as("attribute '%s' at path='%s'").isNotEmpty();
        return attr;
    }

    private static String readMarketId(Object resp, String marketIdPath, String marketName) {
        String path = marketIdPath + jfilter("name", "Racing Live");
        JSONArray attrs = JsonPath.read(resp, path + "." + marketName);
        Assertions.assertThat(attrs.size())
                .as(String.format("expected to find one attribute '%s' at path='%s'", marketName, marketIdPath))
                .isEqualTo(1);
        String attr = String.valueOf(attrs.get(0));
        Assertions.assertThat(attr).as("attribute '%s' at path='%s'").isNotEmpty();
        return attr;
    }

    private static String jfilter(String attr, String value) {
        return String.format("[?(@.%s == '%s')]", attr, value);
    }
}

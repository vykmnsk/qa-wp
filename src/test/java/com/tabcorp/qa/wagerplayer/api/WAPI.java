package com.tabcorp.qa.wagerplayer.api;

import com.jayway.jsonpath.JsonPath;
import com.tabcorp.qa.common.BetType;
import com.tabcorp.qa.common.REST;
import com.tabcorp.qa.wagerplayer.Config;
import com.tabcorp.qa.wagerplayer.dto.Customer;
import net.minidev.json.JSONArray;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class WAPI implements WagerPlayerAPI {

    private static Logger log = LoggerFactory.getLogger(WAPI.class);

    private static final String URL = Config.wapiURL();
    private static final String RESP_ROOT_PATH = "$.RSP";

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

    private static Object post(Map<String, Object> fields) {
        fields.put("output_type", "json");
        Object resp = REST.post(URL, fields);
        verifyNoErrors(resp, fields);
        return resp;
    }

    private static void verifyNoErrors(Object resp, Object req) {
        JSONArray errors = JsonPath.read(resp, "$..error");
        assertThat(errors).as("Errors in response when sending " + req).isEmpty();
    }

    static Object postWithQueryStrings(Map<String, Object> fields, Pair<String, List<String>> pair) {
        fields.put("output_type", "json");
        Object resp = REST.postWithQueryStrings(URL, fields, pair);
        String reqText = String.format("%s %s", fields, pair);
        verifyNoErrors(resp, reqText);
        return resp;
    }

    public String getAccessToken(String username, String password) {
        Map<String, Object> fields = wapiAuthFields();
        fields.put("action", "account_login");
        fields.put("customer_username", username);
        fields.put("customer_password", password);
        Object resp = post(fields);
        return JsonPath.read(resp, RESP_ROOT_PATH + ".login[0].session_id");
    }

    public String createNewCustomer(Customer cust) {
        Map<String, Object> fields = wapiAuthFields();
        fields.put("action", "account_insert_customer");
        fields.put("client_ip", cust.clientIP);
        fields.put("username", cust.username);
        fields.put("telephone_password", cust.telephonePassword);
        fields.put("internet_password", cust.internetPassword);
        fields.put("password", cust.password);
        fields.put("secret_question", cust.securityQuestion);
        fields.put("secret_answer", cust.securityAnswer);
        fields.put("salutation", cust.title);
        fields.put("firstname", cust.firstName);
        fields.put("lastname", cust.lastName);
        fields.put("dob", DateTimeFormatter.ISO_LOCAL_DATE.format(cust.dob));
        fields.put("email_address", cust.email);
        fields.put("deposit_limit", cust.weeklyDepositLimit);
        fields.put("street", cust.street);
        fields.put("postcode", cust.postCode);
        fields.put("country", cust.country);
        fields.put("telephone", cust.telephoneNo);
        fields.put("state", cust.state);
        fields.put("suburb", cust.suburb);
        fields.put("currency", cust.currency);
        fields.put("timezone", cust.timezone);
        Object resp = post(fields);
        Integer custId = JsonPath.read(resp, RESP_ROOT_PATH + ".success.customer_id");
        String msg = JsonPath.read(resp, RESP_ROOT_PATH + ".success.message");
        log.info("Customer ID=" + custId);
        return msg;
    }

    public BigDecimal getBalance(String sessionId) {
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "bet_get_balance");
        Object resp = post(fields);
        String balance = JsonPath.read(resp, RESP_ROOT_PATH + ".account[0].balance");
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

    public Object placeExoticBet(String sessionId, Integer productId, List<String> selectionIds, String marketId, BigDecimal stake, boolean flexi) {
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "bet_place_bet");
        fields.put("product_id", productId);
        fields.put("slot[1][market]", marketId);
        fields.put("amount", stake);
        if (flexi) fields.put("flexi", "y");
        return postWithQueryStrings(fields, Pair.of("slot[1][selection][]", selectionIds));
    }

    public Object placeExoticBetMultiMarkets(String sessionId, Integer productId, List<String> selectionIds, List<String> marketIds, BigDecimal stake, boolean flexi) {
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "bet_place_bet");
        fields.put("product_id", productId);
        AtomicInteger atomicIntSel = new AtomicInteger(1);
        selectionIds.forEach(id -> {
            fields.put("slot[" + atomicIntSel.getAndIncrement() + "][selection][]", id);
        });
        AtomicInteger atomicIntMarket = new AtomicInteger(1);
        marketIds.forEach(id -> {
            fields.put("slot[" + atomicIntMarket.getAndIncrement() + "][market]", id);
        });
        fields.put("amount", stake);
        if (flexi) fields.put("flexi", "y");
        fields.put("output_type", "json");
        return post(fields);
    }

    public BigDecimal readNewBalance(Object resp) {
        Object val = JsonPath.read(resp, RESP_ROOT_PATH + ".bet[0].new_balance");
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

    public String readAmlStatus(String customerUsername, String customerPassword) {
        String sessionId = getAccessToken(customerUsername, customerPassword);
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "account_verify_aml");
        Object resp = post(fields);
        return (String) JsonPath.read(resp, RESP_ROOT_PATH + ".account[0].aml_status");
    }

    public static Map<KEY, String> readSelection(Object resp, String selName, Integer prodId) {
        String mktPath = ".markets.market[0]";
        String selPath = ".selections.selection" + jfilter("name", selName);
        String pricePath = ".prices.price" + jfilter("product_id", prodId.toString());
        String path = mktPath + selPath + pricePath;

        HashMap<KEY, String> sel = new HashMap<>();
        sel.put(KEY.MPID, readPriceAttr(resp, path, BetType.Win.name(), "mpid"));
        sel.put(KEY.WIN_PRICE, readPriceAttr(resp, path, BetType.Win.name(), "precise_price"));
        sel.put(KEY.PLACE_PRICE, readPriceAttr(resp, path, BetType.Place.name(), "precise_price"));
        return sel;
    }

    private static String readPriceAttr(Object resp, String pricePath, String betTypeName, String attrName) {
        String path = pricePath + jfilter("bet_type_name", betTypeName);

        //qTODO hack to bypass broken WAPI for Redbook
//        JSONArray attrs = JsonPath.read(resp, RESP_ROOT_PATH + path + "." + attrName);
//        if (attrs.size() != 1 && BetType.Place.name().equals(betTypeName)) {
//            return "1";
//        }
        return readOneAttr(resp, path, attrName);
    }

    public String readMarketId(Object resp, String mktName) {
        String mktPath = ".markets.market" + jfilter("name", mktName);
        return readOneAttr(resp, mktPath, "id");
    }

    public List<String> readSelectionIds(Object resp, String marketId, List<String> selectionNames) {
        return selectionNames.stream().map(sn -> readSelectionId(resp, marketId, sn)).collect(Collectors.toList());
    }

    public String readSelectionId(Object resp, String marketId, String selectionName) {
        String mktPath = ".markets.market" + jfilter("id", marketId);
        String selPath = ".selections.selection" + jfilter("name", selectionName);
        return readOneAttr(resp, mktPath + selPath, "id");
    }

    private static String readOneAttr(Object resp, String basePath, String attrName) {
        String path = RESP_ROOT_PATH + basePath + "." + attrName;
        JSONArray attrs = JsonPath.read(resp, path);
        assertThat(attrs.size())
                .as(String.format("expected to find one attribute '%s' at path='%s'", attrName, path))
                .isEqualTo(1);
        String attr = String.valueOf(attrs.get(0));
        assertThat(attr).as("attribute '%s' at path='%s'").isNotEmpty();
        return attr;
    }

    private static String jfilter(String attr, String value) {
        return String.format("[?(@.%s == '%s')]", attr, value);
    }

}

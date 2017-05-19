package com.tabcorp.qa.wagerplayer.api;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.tabcorp.qa.common.BetType;
import com.tabcorp.qa.common.REST;
import com.tabcorp.qa.wagerplayer.Config;
import net.minidev.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

public class WAPI implements WagerPlayerAPI {

    private static Logger log = LoggerFactory.getLogger(WAPI.class);

    private static final String URL = Config.wapiURL();
    private static final String RESP_ROOT = "$.RSP";

    private static Map<String, Object> wapiAuthFields() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("wapi_client_user", Config.wapiUsername());
        fields.put("wapi_client_pass", Config.wapiPassword());
        if (!StringUtils.isEmpty(Config.clientIp())) {
            fields.put("client_ip", Config.clientIp());
        }
        return fields;
    }

    private static Map<String, Object> wapiAuthFields(String sessionId) {
        Map<String, Object> fields = wapiAuthFields();
        fields.put("session_id", sessionId);
        return fields;
    }

    private static Object post(Map<String, Object> fields) {
        return post(fields, true);
    }

    private static Object post(Map<String, Object> fields, boolean checkErros) {
        fields.put("output_type", "json");
        Object resp = REST.post(URL, fields);
        verifyJSONRoot(resp);
        if (checkErros) verifyNoErrors(resp, fields);
        return resp;
    }

    private static void verifyJSONRoot(Object resp) {
        try {
            JsonPath.read(resp, RESP_ROOT);
        } catch (PathNotFoundException e) {
            fail(String.format("Response JSON seems invalid: %s, %s", resp.toString(), e.getMessage()));
        }
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

    public String login(){
        return login(Config.customerUsername(), Config.customerPassword(), Config.clientIp());
    }

    public String login(String username, String password, String clientIp) {
        Map<String, Object> fields = wapiAuthFields();
        fields.put("action", "account_login");
        fields.put("customer_username", username);
        fields.put("customer_password", password);
        fields.put("client_ip", clientIp);
        Object resp = post(fields);
        return JsonPath.read(resp, RESP_ROOT + ".login[0].session_id");
    }

    public String createNewCustomer(Map custData) {
        Map<String, Object> fields = wapiAuthFields();
        fields.put("action", "account_insert_customer");
        fields.putAll(custData);
        Object resp = post(fields);
        Integer custId = JsonPath.read(resp, RESP_ROOT + ".success.customer_id");
        String msg = JsonPath.read(resp, RESP_ROOT + ".success.message");
        log.info("Customer ID=" + custId);
        return msg;
    }

    public String depositCash(String sessionId, BigDecimal cashAmount) {
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "account_deposit");
        fields.put("amount", cashAmount);
        Object resp = post(fields);
        String msg = JsonPath.read(resp, RESP_ROOT + ".account[0].message");
        int transId = JsonPath.read(resp, RESP_ROOT + ".account[0].transaction_id");
        log.info("Deposit Status message=" + msg);
        log.info("Deposit Transaction ID=" + transId);
        return msg;
    }

    public BigDecimal getBalance(String sessionId) {
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "bet_get_balance");
        Object resp = post(fields);
        String balance = JsonPath.read(resp, RESP_ROOT + ".account[0].balance");
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
        fields.put("bet_type", BetType.Eachway.id);
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
        return post(fields);
    }

    public Object addSelectionToMulti(String sessionId, Integer prodId, String mpid) {
        return addSelectionToMulti(sessionId, prodId, mpid, null);
    }

    public Object addSelectionToMulti(String sessionId, Integer prodId, String mpid, BetType bt) {
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "bet_add_sel_to_multi");
        fields.put("mpid", mpid);
        fields.put("product_id", prodId);
        if (null != bt) {
            fields.put("bt", bt.id);
        }
        return post(fields, false);
    }

    public String prepareSelectionsForDoubleMultiBet(String sessionId, List<Map<WAPI.KEY, String>> selections, List<Integer> prodIds, List<BetType> betTypes) {
        List<Integer> sizes = Arrays.asList(selections.size(), prodIds.size(), betTypes.size());
        assertThat(sizes).as("Number of Selections, Products and BetTypes").allMatch(size -> size.equals(2));
        return prepareSelectionsForMultiBet(sessionId, selections, prodIds,"Double", betTypes);
    }

    public String prepareSelectionsForMultiBet(String sessionId, List<Map<WAPI.KEY, String>> selections, List<Integer> prodIds, String multiType) {
        List<Integer> sizes = Arrays.asList(selections.size(), prodIds.size());
        Integer count = sizes.get(0);
        assertThat(count).as("Multi Selections count").isGreaterThan(1);
        assertThat(sizes).as("Number of Selections and Products").allMatch(count::equals);
        List<BetType> dummyBetTypes = Collections.nCopies(count,null);
        return prepareSelectionsForMultiBet(sessionId, selections, prodIds, multiType, dummyBetTypes);
    }

    private String prepareSelectionsForMultiBet(String sessionId, List<Map<WAPI.KEY, String>> selections, List<Integer> prodIds, String multiType, List<BetType> betTypes) {
        assertThat(selections.size()).isEqualTo(prodIds.size()).isEqualTo(betTypes.size());
        Object response = null;
        for (int i = 0; i < selections.size(); i++) {
            String selectionId = selections.get(i).get(KEY.MPID);
            response = addSelectionToMulti(sessionId, prodIds.get(i), selectionId, betTypes.get(i));
            if (i == 0) {  // error shows only for 1 selection
                String msg = JsonPath.read(response, RESP_ROOT + ".error[0].error_text");
                assertThat(msg).isEqualTo("Add more selections for multiple bet types");
            }
        }
        JSONArray uuidsFound = JsonPath.read(response, RESP_ROOT + ".multiple" + jfilter("name", multiType) + ".uuid");
        assertThat(uuidsFound).as("Multi UUIDs after adding selections").hasSize(1);
        return uuidsFound.get(0).toString();
    }

    public Object placeMultiBet(String sessionId, String uuid, BigDecimal stake, boolean flexi) {
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "bet_place_bet");
        fields.put("placing_multiple", 1);
        fields.put("multi[uuid][" + uuid + "][stake]", stake);
        if (flexi) fields.put("flexi", "y");
        return post(fields);
    }

    public BigDecimal readNewBalance(Object resp) {
        Object val = JsonPath.read(resp, RESP_ROOT + ".bet[0].new_balance");
        BigDecimal newBalance = new BigDecimal(val.toString());
        return newBalance;
    }

    public List readBetIds(Object resp) {
        JSONArray betIds = JsonPath.read(resp, RESP_ROOT + ".bet[*].bet_id");
        return betIds;
    }

    public Object getEventMarkets(String sessionId, String evtId) {
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "site_get_markets");
        fields.put("eid", evtId);
        fields.put("show_held", false);
        return post(fields);
    }

    public String readAmlStatus(String sessionId, String clientIp) {
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "account_verify_aml");
        fields.put("client_ip", clientIp);
        Object resp = post(fields);
        return (String) JsonPath.read(resp, RESP_ROOT + ".account[0].aml_status");
    }


    public String generateAffiliateLoginToken(String sessionId) {
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "account_generate_login_token");
        Object resp = post(fields);
        return (String) JsonPath.read(resp, RESP_ROOT + ".token");
    }

    private static String getPricePath(String selName, Integer prodId) {
        String mktPath = ".markets.market[0]";
        String selPath = ".selections.selection" + jfilter("name", selName);
        String pricePath = ".prices.price" + jfilter("product_id", prodId.toString());
        return mktPath + selPath + pricePath;
    }

    public List<Map<WAPI.KEY, String>> getMultiEventSelections(String sessionId, List<String> eventIds, List<String> runners, List<Integer> prodIds) {
        List<Map<WAPI.KEY, String>> selections = new ArrayList<>();
        for (int i = 0; i < eventIds.size(); i++) {
            Object resp = getEventMarkets(sessionId, eventIds.get(i));
            Map<WAPI.KEY, String> sel = readSelection(resp, runners.get(i), prodIds.get(i));
            selections.add(sel);
        }
        return selections;
    }

    public static Map<KEY, String> readSelection(Object resp, String selName, Integer prodId) {
        return readSelection(resp, selName, prodId, true);
    }

    public static Map<KEY, String> readSelection(Object resp, String selName, Integer prodId, boolean withPrices) {
        String path = getPricePath(selName, prodId);
        HashMap<KEY, String> sel = new HashMap<>();
        sel.put(KEY.MPID, readPriceAttr(resp, path, BetType.Win.name(), "mpid"));
        if (withPrices) {
            sel.put(KEY.WIN_PRICE, readPriceAttr(resp, path, BetType.Win.name(), "precise_price"));
            sel.put(KEY.PLACE_PRICE, readPriceAttr(resp, path, BetType.Place.name(), "precise_price"));
        } else {
            sel.put(KEY.WIN_PRICE, "1.00");
            sel.put(KEY.PLACE_PRICE, "1.00");
        }
        return sel;
    }

    public List<String> readSelectionIds(Object resp, String marketId, List<String> selectionNames) {
        return selectionNames.stream().map(sn -> readSelectionId(resp, marketId, sn)).collect(Collectors.toList());
    }

    public String readSelectionId(Object resp, String marketId, String selectionName) {
        String mktPath = ".markets.market" + jfilter("id", marketId);
        String selPath = ".selections.selection" + jfilter("name", selectionName);
        return readOneAttr(resp, mktPath + selPath, "id");
    }

    public String readMarketId(Object resp, String mktName) {
        String mktPath = ".markets.market" + jfilter("name", mktName);
        return readOneAttr(resp, mktPath, "id");
    }

    private static String readPriceAttr(Object resp, String pricePath, String betTypeName, String attrName) {
        String path = pricePath + jfilter("bet_type_name", betTypeName);
        //qTODO hack to bypass broken WAPI for Redbook
//        JSONArray attrs = JsonPath.read(resp, RESP_ROOT + path + "." + attrName);
//        if (attrs.size() != 1 && BetType.Place.name().equals(betTypeName)) {
//            return "1";
//        }
        return readOneAttr(resp, path, attrName);
    }

    private static String readOneAttr(Object resp, String basePath, String attrName) {
        String path = RESP_ROOT + basePath + "." + attrName;
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

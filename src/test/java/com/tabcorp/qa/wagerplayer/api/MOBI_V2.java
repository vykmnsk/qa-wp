package com.tabcorp.qa.wagerplayer.api;

import com.jayway.jsonpath.JsonPath;
import com.tabcorp.qa.common.BetType;
import com.tabcorp.qa.common.REST;
import com.tabcorp.qa.wagerplayer.Config;
import com.tabcorp.qa.wagerplayer.dto.Customer;
import net.minidev.json.JSONArray;
import org.assertj.core.api.Assertions;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class MOBI_V2 implements WagerPlayerAPI {

    private static String URL_ROOT = Config.moby_V2_URL();
    private static Logger log = LoggerFactory.getLogger(MOBI_V2.class);

    private static Object get(String url, Map<String, Object> queryParams) {
        Object response = REST.get(URL_ROOT + url, queryParams);
        verifyNoErrors(response, queryParams);
        return response;
    }

    private static Object post(String url, Map<String, Object> fields) {
        fields.put("output_type", "json");
        Object response = REST.post(URL_ROOT + url, fields);
        verifyNoErrors(response, fields);
        return response;
    }

    private static Object put(String url, String reqJSON) {
        Object response = REST.put(URL_ROOT + url, reqJSON);
        return response;
    }

    private static void verifyNoErrors(Object resp, Object req) {
        verifyNoErrors(resp, req, null);
    }

    private static void verifyNoErrors(Object resp, Object req, String errPath) {
        List<String> errPaths = new ArrayList<>();
        errPaths.add("$..errors");
        errPaths.add("$..error");
        //default
        if (null != errPath) errPaths.add(errPath);
        JSONArray errors = new JSONArray();
        for (String path : errPaths) {
            errors.addAll(JsonPath.read(resp, path));
        }
        assertThat(errors).as("Errors in response when sending " + req).isEmpty();
    }

    private static Object checkoutBet(String reqJSON) {
        Object response = put("/betslip/checkout", reqJSON);
        verifyNoErrors(response, reqJSON, "$..selections..error_message");
        JSONArray betId = JsonPath.read(response, "$..selections[0].bet_id");
        Assertions.assertThat(betId).as("Bet ID in response").isNotEmpty();
        return response;
    }

    private static Object checkoutMultiBet(String reqJSON, int selectionCount) {
        Object response = put("/betslip/checkout", reqJSON);
        verifyNoErrors(response, reqJSON);
        List<String> selectionsErrors = JsonPath.read(response, "$..selections..error_message");
        Assertions.assertThat(selectionsErrors.size())
                .as("Number of Selection Errors in response: %s", selectionsErrors).isLessThanOrEqualTo(selectionCount);
        JSONArray betId = JsonPath.read(response, "$..selections..combins..bet_id");
        Assertions.assertThat(betId).as("Bet ID in response").isNotEmpty();
        return response;
    }

    public BigDecimal getBalance(String accessToken) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("access_token", accessToken);
        Object response = get("/customer/balance", queryParams);
        Double balance = Double.parseDouble(JsonPath.read(response, "$.balance"));
        return new BigDecimal(balance);
    }

    public String getAccessToken(String username, String password) {
        Map<String, Object> fields = new HashMap<>();
        fields.put("username", username);
        fields.put("password", password);
        Object response = post("/login", fields);

        JSONArray accessTokens = JsonPath.read(response, "$..login_data..access_token");
        Assertions.assertThat(accessTokens).as("Got one token").hasSize(1);
        String accessToken = accessTokens.get(0).toString();
        Assertions.assertThat(accessToken).as("Access Token ").isNotEmpty();
        return accessToken;
    }

    public Object placeSingleWinBet(String accessToken, Integer productId, String mpid, String winPrice, BigDecimal stake) {
        //Ignore productID
        //Added to ensure function signature remains same as to WagerPlayerAPI interface.

        //prices
        JSONObject price = new JSONObject();
        price.put("win_price", winPrice);
        String betPayload = createBetPayload(accessToken, mpid, stake, BetType.Win.id, price);
        return checkoutBet(betPayload);
    }

    public Object placeSinglePlaceBet(String accessToken, Integer productId, String mpid, String placePrice, BigDecimal stake) {
        //Ignore productID
        //Added to ensure function signature remains same as to WagerPlayerAPI interface.

        //prices
        JSONObject price = new JSONObject();
        price.put("place_price", placePrice);

        String betPayload = createBetPayload(accessToken, mpid, stake, BetType.Place.id, price);
        return checkoutBet(betPayload);
    }

    public Object placeSingleEachwayBet(String accessToken, Integer productId, String mpid, String winPrice, String placePrice, BigDecimal stake) {
        //Ignore productID
        //Added to ensure function signature remains same as to WagerPlayerAPI interface.

        //prices
        JSONObject price = new JSONObject();
        price.put("place_price", placePrice);
        price.put("win_price", winPrice);

        String betPayload = createBetPayload(accessToken, mpid, stake, BetType.Eachway.id, price);
        return checkoutBet(betPayload);
    }

    public Object placeExoticBet(String accessToken, Integer productId, List<String> selectionIds, String marketId, BigDecimal stake, boolean isFlexi) {
        JSONObject obj = new JSONObject();
        JSONObject selectionsData = new JSONObject();
        JSONArray selections = new JSONArray();
        JSONObject slots = new JSONObject();
        JSONObject options = new JSONObject();
        JSONArray selection;
        JSONObject slotData;

        options.put("flexi", "0");

        for (int index = 0; index < selectionIds.size(); index++) {
            selection = new JSONArray();
            slotData = new JSONObject();
            selection.add(0, selectionIds.get(index));
            slotData.put("market", marketId);
            slotData.put("selection", selection);
            slots.put(index + 1, slotData);
        }


        obj.put("access_token", accessToken);
        selectionsData.put("type", "exotic");
        selectionsData.put("stake", stake);
        selectionsData.put("options", options);
        selectionsData.put("product_id", productId);
        selectionsData.put("slots", slots);
        selections.add(selectionsData);

        obj.put("selections", selections);

        return checkoutBet(obj.toJSONString());
    }

    public Object placeMultiBet(String accessToken, Integer productId, List<Map<WAPI.KEY, String>> selections, Integer betType, String multiType, BigDecimal stake) {
        JSONObject betPayload = new JSONObject();
        JSONArray selectionsObj = new JSONArray();
        JSONArray selection = new JSONArray();


        JSONObject selectionsData;
        JSONObject selPrices;
        JSONObject options;

        for (Map<WAPI.KEY, String> sel : selections) {
            selectionsData = new JSONObject();
            selPrices = new JSONObject();
            options = new JSONObject();

            options.put("bet_type", betType);
            options.put("include_in_multi", 1);

            selPrices.put("win_price", sel.get(KEY.WIN_PRICE));
            selPrices.put("win_price", sel.get(KEY.PLACE_PRICE));


            selectionsData.put("type", "single");
            selectionsData.put("stake", "0.00");
            selectionsData.put("mpid", sel.get(KEY.MPID));
            selectionsData.put("options", options);
            selectionsData.put("prices", selPrices);

            selectionsObj.add(selectionsData);

        }

        selectionsData = new JSONObject();
        options = new JSONObject();

        options.put("bet_type", betType);

        selectionsData.put("type", "multi");
        selectionsData.put("stake", stake);
        selectionsData.put("multi_type", multiType);
        selectionsData.put("options", options);

        selectionsObj.add(selectionsData);

        selection.add(0, selectionsObj);


        betPayload.put("access_token", accessToken);
        betPayload.put("selections", selectionsObj);

        return checkoutMultiBet(betPayload.toJSONString(), selections.size());
    }

    private String createBetPayload(String accessToken, String mpid, BigDecimal stake, Integer betId, JSONObject priceObject) {
        JSONObject obj = new JSONObject();
        JSONArray selections = new JSONArray();
        JSONObject betType = new JSONObject(); //options
        JSONObject bet = new JSONObject();

        obj.put("access_token", accessToken);

        betType.put("bet_type", betId);

        bet.put("type", "single");
        bet.put("stake", stake.toString());
        bet.put("mpid", mpid);
        bet.put("options", betType);
        bet.put("prices", priceObject);
        selections.add(bet);

        obj.put("selections", selections);

        return obj.toJSONString();
    }

    public BigDecimal readNewBalance(Object resp) {
        JSONArray val = JsonPath.read(resp, "$..new_balance");
        BigDecimal newBalance = new BigDecimal((val.get(val.size() - 1)).toString());
        return newBalance;
    }

    public String createNewCustomer(Customer customer) {
        Map<String, Object> fields = new HashMap<>();

        fields.put("username", customer.username);
        fields.put("firstname", customer.firstName);
        fields.put("lastname", customer.lastName);
        fields.put("salutation", customer.title);
        fields.put("dob", DateTimeFormatter.ISO_LOCAL_DATE.format(customer.dob));
        fields.put("email_address", customer.email);
        fields.put("password", customer.password);
        fields.put("postcode", customer.postCode);
        fields.put("country", customer.country);
        fields.put("telephone", customer.telephoneNo);
        fields.put("secret_question", customer.securityQuestion);
        fields.put("secret_answer", customer.securityAnswer);
        fields.put("city", customer.city);

        fields.put("currency_code", customer.currency);
        fields.put("building", customer.building);
        fields.put("postal_country", customer.country);
        fields.put("postal_postcode", customer.postCode);
        fields.put("suburb", customer.suburb);
        fields.put("postal_county", customer.suburb);
        fields.put("street", customer.street);
        fields.put("manual_verification", customer.manualVerification);

        Object resp = post("/customer", fields);
        log.info("Response for create customer mobi_v2 : " + resp);
        Integer custId = JsonPath.read(resp, "$.success.customer_id");
        log.info("Customer ID=" + custId);
        String msg = JsonPath.read(resp, "$.success.message");
        return msg;
    }

    public String readAmlStatus(String customerUsername, String customerPassword) {
        String accessToken = getAccessToken(customerUsername, customerPassword);
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("access_token", accessToken);
        Object response = get("/customer", queryParams);
        String amlStatus = JsonPath.read(response, "$.customer_balance.aml_status");
        log.info("AML Status : " + amlStatus);
        return amlStatus;
    }

}
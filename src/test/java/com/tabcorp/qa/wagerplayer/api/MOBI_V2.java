package com.tabcorp.qa.wagerplayer.api;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.tabcorp.qa.common.BetType;
import com.tabcorp.qa.common.REST;
import com.tabcorp.qa.wagerplayer.Config;
import net.minidev.json.JSONArray;
import org.assertj.core.api.Assertions;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class MOBI_V2 implements WagerPlayerAPI {

    private static String URL_ROOT = Config.moby_V2_URL();
    private static final String RESP_ROOT = "$";

    private static Logger log = LoggerFactory.getLogger(MOBI_V2.class);

    private ReadContext get(String url, Map<String, Object> queryParams) {
        Object response = REST.get(URL_ROOT + url, queryParams);
        ReadContext ctx = parseVerifyJSON(response, RESP_ROOT);
        verifyNoErrors(ctx, queryParams);
        return ctx;
    }

    private ReadContext post(String url, Map<String, Object> fields) {
        fields.put("output_type", "json");
        Object response = REST.post(URL_ROOT + url, fields);
        ReadContext ctx = parseVerifyJSON(response, RESP_ROOT);
        verifyNoErrors(ctx, fields);
        return ctx;
    }

    private ReadContext put(String url, String reqJSON) {
        Object response = REST.put(URL_ROOT + url, reqJSON);
        ReadContext ctx = parseVerifyJSON(response, RESP_ROOT);
        verifyNoErrors(ctx, reqJSON);
        return ctx;
    }

    private static void verifyNoErrors(ReadContext resp, Object req) {
        verifyNoErrors(resp, req, null);
    }

    private static void verifyNoErrors(ReadContext resp, Object req, String errPath) {
        List<String> errPaths = new ArrayList<>();
        errPaths.add("$..errors");
        errPaths.add("$..error");
        //default
        if (null != errPath) errPaths.add(errPath);
        JSONArray errors = new JSONArray();
        for (String path : errPaths) {
            errors.addAll(resp.read(path));
        }
        assertThat(errors).as("Errors in response when sending " + req).isEmpty();
    }

    private ReadContext checkoutBet(String reqJSON) {
        ReadContext response = put("/betslip/checkout", reqJSON);
        verifyNoErrors(response, reqJSON, "$..selections..error_message");
        JSONArray betId = response.read("$..selections[0].bet_id");
        Assertions.assertThat(betId).as("Bet ID in response").isNotEmpty();
        return response;
    }

    private ReadContext checkoutMultiBet(String reqJSON, int selectionCount) {
        ReadContext response = put("/betslip/checkout", reqJSON);
        verifyNoErrors(response, reqJSON);
        List<String> selectionsErrors = response.read("$..selections..error_message");
        Assertions.assertThat(selectionsErrors.size())
                .as("Number of Selection Errors in response: %s", selectionsErrors).isLessThanOrEqualTo(selectionCount);
        JSONArray betId = response.read("$..selections..combins..bet_id");
        Assertions.assertThat(betId).as("Bet ID in response").isNotEmpty();
        return response;
    }

    public BigDecimal getBalance(String accessToken) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("access_token", accessToken);
        ReadContext response = get("/customer/balance", queryParams);
        Double balance = Double.parseDouble(response.read("$.balance"));
        return new BigDecimal(balance);
    }

    public String login(String username, String password, String unused) {
        Map<String, Object> fields = new HashMap<>();
        fields.put("username", username);
        fields.put("password", password);
        ReadContext response = post("/login", fields);

        JSONArray accessTokens = response.read("$..login_data..access_token");
        Assertions.assertThat(accessTokens).as("Got one token").hasSize(1);
        String accessToken = accessTokens.get(0).toString();
        Assertions.assertThat(accessToken).as("Access Token ").isNotEmpty();
        return accessToken;
    }

    public ReadContext placeSingleWinBet(String accessToken, Integer productId, String mpid, String winPrice, BigDecimal stake) {
        //Ignore productID
        //Added to ensure function signature remains same as to WagerPlayerAPI interface.

        //prices
        JSONObject price = new JSONObject();
        price.put("win_price", winPrice);
        String betPayload = createBetPayload(accessToken, mpid, stake, BetType.Win.id, price);
        return checkoutBet(betPayload);
    }

    public ReadContext placeSinglePlaceBet(String accessToken, Integer productId, String mpid, String placePrice, BigDecimal stake) {
        //Ignore productID
        //Added to ensure function signature remains same as to WagerPlayerAPI interface.

        //prices
        JSONObject price = new JSONObject();
        price.put("place_price", placePrice);

        String betPayload = createBetPayload(accessToken, mpid, stake, BetType.Place.id, price);
        return checkoutBet(betPayload);
    }

    public ReadContext placeSingleEachwayBet(String accessToken, Integer productId, String mpid, String winPrice, String placePrice, BigDecimal stake) {
        //Ignore productID
        //Added to ensure function signature remains same as to WagerPlayerAPI interface.

        //prices
        JSONObject price = new JSONObject();
        price.put("place_price", placePrice);
        price.put("win_price", winPrice);

        String betPayload = createBetPayload(accessToken, mpid, stake, BetType.Eachway.id, price);
        return checkoutBet(betPayload);
    }

    public ReadContext placeExoticBet(String accessToken, Integer productId, List<String> selectionIds, String marketId, BigDecimal stake, boolean isFlexi) {
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

    public ReadContext placeMultiBet(String accessToken, List<Map<WAPI.KEY, String>> selections, String multiType, BetType betType, BigDecimal stake) {
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

            options.put("bet_type", betType.id);
            options.put("include_in_multi", 1);

            selPrices.put("win_price", sel.get(KEY.WIN_PRICE));
            selPrices.put("place_price", sel.get(KEY.PLACE_PRICE));


            selectionsData.put("type", "single");
            selectionsData.put("stake", "0.00");
            selectionsData.put("mpid", sel.get(KEY.MPID));
            selectionsData.put("options", options);
            selectionsData.put("prices", selPrices);

            selectionsObj.add(selectionsData);

        }

        selectionsData = new JSONObject();
        options = new JSONObject();

        options.put("bet_type", betType.id);

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

    public BigDecimal readNewBalance(ReadContext resp) {
        JSONArray val = resp.read("$..new_balance");
        BigDecimal newBalance = new BigDecimal((val.get(val.size() - 1)).toString());
        return newBalance;
    }

    public String createNewCustomer(Map custData) {
        ReadContext response = post("/customer", custData);
        Integer custId = response.read("$.success.customer_id");
        log.info("Customer ID=" + custId);
        String msg = response.read("$.success.message");
        return msg;
    }

    public String readAmlStatus(String accessToken, String unused) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("access_token", accessToken);
        ReadContext response = get("/customer", queryParams);
        String amlStatus = response.read("$.customer_balance.aml_status");
        log.info("AML Status : " + amlStatus);
        return amlStatus;
    }

    public String getPaymentRefence(String accessToken) {
        Map fields = new HashMap<String, Object>();
        fields.put("access_token", accessToken);
        ReadContext response = get("/payment/reference", fields);
        String paymentRef = response.read("$.results.reference");
        Assertions.assertThat(paymentRef).as("Payment reference in response").isNotEmpty();
        return paymentRef;
    }

    public List readBetIds(ReadContext response) {
        JSONArray betIds = response.read("$..selections[0].bet_id");
        return betIds;
    }

    public String getEncryptionKey(String accessToken) {
        Map<String, Object> fields = new HashMap<>();
        fields.put("access_token", accessToken);
        ReadContext response = get("/payment/encryption_key", fields);
        String key = response.read("$.results.encryption_key");
        Assertions.assertThat(key).as("Encryption key in response").isNotEmpty();
        return key;
    }

    public String getStoredCardReference(String accessToken) {
        Map fields = new HashMap<String, Object>();
        fields.put("access_token", accessToken);
        ReadContext response = get("/payment/info/stored", fields);
        String selectedCardReference = response.read("$.results.stored[0].selected_card_reference");
        Assertions.assertThat(selectedCardReference).as("Selected card reference is ").isNotEmpty();
        return selectedCardReference;
    }

    public void withdrawWithCreditCard(String accessToken, String cardType, BigDecimal withdrawAmount, String selectedCardReference, String withdrawReference) {
        Map<String, Object> fields = new HashMap<>();
        fields.put("access_token", accessToken);
        fields.put("payment_method", cardType);
        fields.put("amount", withdrawAmount);
        fields.put("selected_card_reference", selectedCardReference);
        fields.put("withdraw_reference", withdrawReference);
        ReadContext response = post("/payment/withdraw/stored", fields);
        String resultCode = response.read("$.results.result_code");
        Assertions.assertThat(resultCode).as("Result code is not matching with ").isNotEqualToIgnoringCase("payout-submit-received");
    }

    public void addCardAndDeposit(String accessToken, String paymentReference, String cardEncryption, String cardType, BigDecimal deposit) {
        Map fields = new HashMap<String, Object>();
        fields.put("access_token", accessToken);
        fields.put("card_encrypted", cardEncryption);
        fields.put("payment_method", cardType);
        fields.put("amount", String.valueOf(deposit));
        fields.put("deposit_reference", paymentReference);
        fields.put("output_type", "json");
        ReadContext response = post("/payment/deposit/card", fields);
        String resultCode = response.read("$.results.result_code");
        Assertions.assertThat(resultCode).as("Found result code in response is " + resultCode).isEqualToIgnoringCase("Authorised");
    }

}
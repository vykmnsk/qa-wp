package com.tabcorp.qa.wagerplayer.api;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class MOBI_V2 implements WagerPlayerAPI {

    private static String URL_ROOT = Config.moby_V2_URL();
    private static final String RESP_ROOT = "$";

    public enum MultiType {
        Double("Double"),
        Doubles("Doubles"),
        WPDoubles("W/P Doubles"),
        Patent("Patent"),
        Trixie("Trixie"),
        Treble("Treble"),
        Trebles("Trebles"),
        WPTrebles("W/P Trebles"),
        FourFold("4-Fold"),
        Lucky15("Lucky 15"),
        Yankee("Yankee"),
        Lucky31("Lucky 31"),
        Canadian("Canadian"),
        Lucky63("Lucky 63"),
        Heinz("Heinz"),
        Lucky127("Lucky 127"),
        SuperHeinz("Super Heinz"),
        Lucky255("Lucky 255"),
        Goliath("Goliath"),
        Lucky511("Lucky 511"),
        MassiveLux("Massive Lux");

        public final String exactName;

        MultiType(String name) {
            exactName = name;
        }

        public static MultiType fromName(String name) {
            MultiType found = Arrays.stream(MultiType.values())
                    .filter(mt -> mt.exactName.equalsIgnoreCase(name))
                    .findFirst().orElse(null);
            assertThat(found)
                    .withFailMessage(String.format(
                            "Could not find MOBI_V2 MultiType with name='%s'. Available MultiTypes: %s",
                            name, MultiType.allNames()))
                    .isNotNull();
            return found;
        }

        public static List<String> allNames() {
            return Arrays.stream(MultiType.values()).map(mt -> mt.exactName).collect(Collectors.toList());
        }
    }

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

    @SuppressWarnings("unchecked")
    public ReadContext placeSingleWinBet(String accessToken, Integer productId, String mpid, String winPrice, BigDecimal stake, Integer unused) {
        //Ignore productID
        //Added to ensure function signature remains same as to WagerPlayerAPI interface.
        JSONObject prices = new JSONObject();
        prices.put("win_price", winPrice);
        String betPayload = createBetPayload(accessToken, mpid, stake, BetType.Win.id, prices);
        return checkoutBet(betPayload);
    }

    @SuppressWarnings("unchecked")
    public ReadContext placeSinglePlaceBet(String accessToken, Integer productId, String mpid, String placePrice, BigDecimal stake, Integer unused) {
        //Ignore productID
        //Added to ensure function signature remains same as to WagerPlayerAPI interface.
        JSONObject prices = new JSONObject();
        prices.put("place_price", placePrice);
        String betPayload = createBetPayload(accessToken, mpid, stake, BetType.Place.id, prices);
        return checkoutBet(betPayload);
    }

    @SuppressWarnings("unchecked")
    public ReadContext placeSingleEachwayBet(String accessToken, Integer productId, String mpid, String winPrice, String placePrice, BigDecimal stake, Integer unused) {
        //Ignore productID
        //Added to ensure function signature remains same as to WagerPlayerAPI interface.
        JSONObject prices = new JSONObject();
        prices.put("place_price", placePrice);
        prices.put("win_price", winPrice);
        String betPayload = createBetPayload(accessToken, mpid, stake, BetType.Eachway.id, prices);
        return checkoutBet(betPayload);
    }

    @SuppressWarnings("unchecked")
    public ReadContext placeExoticBet(String accessToken, Integer productId, List<String> selectionIds, String marketId, BigDecimal stake, boolean isFlexi, boolean isBoxed) {
        JSONObject slots = new JSONObject();
        JSONObject options = new JSONObject();
        options.put("flexi", isFlexi ? "1" : "0");
        if (isBoxed) {
            options.put("boxed", "1");
            JSONObject slot = new JSONObject();
            JSONArray selection = new JSONArray();
            selection.addAll(selectionIds);
            slot.put("selection", selection);
            slot.put("market", marketId);
            slots.put(1, slot);
        } else {
            options.put("boxed", "0");
            for (int index = 0; index < selectionIds.size(); index++) {
                JSONObject slot = new JSONObject();
                JSONArray selection = new JSONArray();
                selection.add(selectionIds.get(index));
                slot.put("selection", selection);
                slot.put("market", marketId);
                slots.put(index + 1, slot);
            }
        }
        JSONObject selection = new JSONObject();
        selection.put("slots", slots);
        selection.put("type", "exotic");
        selection.put("stake", stake);
        selection.put("options", options);
        selection.put("product_id", productId);

        JSONArray selections = new JSONArray();
        selections.add(selection);

        JSONObject payload = new JSONObject();
        payload.put("access_token", accessToken);
        payload.put("selections", selections);
        return checkoutBet(payload.toJSONString());
    }

    @SuppressWarnings("unchecked")
    public ReadContext placeMultiBet(String accessToken, List<Map<WAPI.KEY, String>> selectionInfos, MultiType multiType, BetType betType, BigDecimal stake) {
        JSONArray selections = new JSONArray();
        for (Map<WAPI.KEY, String> selInfo : selectionInfos) {
            JSONObject selSingle = new JSONObject();

            selSingle.put("type", "single");
            selSingle.put("stake", "0.00");
            selSingle.put("mpid", selInfo.get(KEY.MPID));

            JSONObject options = new JSONObject();
            options.put("bet_type", betType.id);
            options.put("include_in_multi", 1);
            selSingle.put("options", options);

            JSONObject selPrices = new JSONObject();
            if (BetType.Win.equals(betType) || BetType.Eachway.equals(betType)) {
                selPrices.put("win_price", selInfo.get(KEY.WIN_PRICE));
            }
            if (BetType.Place.equals(betType) || BetType.Eachway.equals(betType)) {
                selPrices.put("place_price", selInfo.get(KEY.PLACE_PRICE));
            }
            selSingle.put("prices", selPrices);

            selections.add(selSingle);
        }

        JSONObject selMulti = new JSONObject();
        selMulti.put("type", "multi");
        selMulti.put("stake", stake);
        selMulti.put("multi_type", multiType.exactName);

        JSONObject options = new JSONObject();
        options.put("bet_type", betType.id);
        selMulti.put("options", options);
        selections.add(selMulti);

        JSONObject payload = new JSONObject();
        payload.put("access_token", accessToken);
        payload.put("selections", selections);
        return checkoutMultiBet(payload.toJSONString(), selectionInfos.size());
    }

    @SuppressWarnings("unchecked")
    private String createBetPayload(String accessToken, String mpid, BigDecimal stake, Integer betId, JSONObject prices) {
        JSONObject bet = new JSONObject();
        bet.put("type", "single");
        bet.put("stake", stake.toString());
        bet.put("mpid", mpid);
        bet.put("prices", prices);
        JSONObject options = new JSONObject();
        options.put("bet_type", betId);
        bet.put("options", options);

        JSONArray selections = new JSONArray();
        selections.add(bet);

        JSONObject payload = new JSONObject();
        payload.put("access_token", accessToken);
        payload.put("selections", selections);
        return payload.toJSONString();
    }

    public BigDecimal readNewBalance(ReadContext resp) {
        JSONArray allBalances = resp.read("$..new_balance");
        assertThat(allBalances).as("new_balance in response").isNotEmpty();
        String lastBalance = allBalances.get(allBalances.size() - 1).toString();
        return new BigDecimal(lastBalance);
    }

    @SuppressWarnings("unchecked")
    public String createNewCustomer(Map custData) {
        ReadContext response = post("/customer", custData);
        Integer custId = response.read("$.success.customer_id");
        log.info("Customer ID=" + custId);
        return response.read("$.success.message");
    }

    public String readAmlStatus(String accessToken) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("access_token", accessToken);
        ReadContext response = get("/customer", queryParams);
        String amlStatus = response.read("$.customer_balance.aml_status");
        log.info("AML Status : " + amlStatus);
        return amlStatus;
    }

    public String getPaymentRefence(String accessToken) {
        Map<String, Object> fields = new HashMap<>();
        fields.put("access_token", accessToken);
        ReadContext response = get("/payment/reference", fields);
        String paymentRef = response.read("$.results.reference");
        Assertions.assertThat(paymentRef).as("Payment reference in response").isNotEmpty();
        return paymentRef;
    }

    public List<Integer> readBetIds(ReadContext response) {
        return response.read("$..selections[*].bet_id");
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
        Map<String, Object> fields = new HashMap<>();
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
        Map<String, Object> fields = new HashMap<>();
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
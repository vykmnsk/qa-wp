package com.tabcorp.qa.wagerplayer.api;

import com.jayway.jsonpath.ReadContext;
import com.tabcorp.qa.common.BetType;
import com.tabcorp.qa.common.REST;
import com.tabcorp.qa.common.Storage;
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

import static com.tabcorp.qa.common.Storage.KEY.API_ACCESS_TOKEN;
import static com.tabcorp.qa.common.Storage.KEY.EVENT_IDS;
import static com.tabcorp.qa.common.Storage.KEY.PRODUCT_IDS;
import static org.assertj.core.api.Assertions.assertThat;

public class WAPI implements WagerPlayerAPI {
    private static final String URL = Config.wapiURL();
    private static final String RESP_ROOT = "$.RSP";


    public enum MultiType {
        Double("Double"),
        Treble("Treble"),
        Doubles("Doubles"),
        Trixie("Trixie"),
        Patent("Patent"),
        FourFold("4-Fold"),
        Trebles("Trebles"),
        Yankee("Yankee"),
        Lucky15("Lucky 15"),
        FiveFold("5-Fold"),
        FourFolds("4-Folds"),
        Canadian("Canadian"),
        Lucky31("Lucky 31");

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
                            "Could not find WAPI MultiType with name='%s'. Available MultiTypes: %s",
                            name, MultiType.allNames()))
                    .isNotNull();
            return found;
        }

        public static List<String> allNames() {
            return Arrays.stream(MultiType.values()).map(mt -> mt.exactName).collect(Collectors.toList());
        }
    }

    public enum InterceptOption {
        Accept("Accept"),
        Reject("Reject"),
        Partial("Partial");

        public final String exactName;

        InterceptOption(String name) {
            exactName = name;
        }
    }

    private static final Logger log = LoggerFactory.getLogger(WAPI.class);

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

    private ReadContext post(Map<String, Object> fields) {
        return post(fields, true);
    }

    private ReadContext post(Map<String, Object> fields, boolean checkErrors) {
        fields.put("output_type", "json");
        Object resp = REST.post(URL, fields);
        ReadContext ctx = parseVerifyJSON(resp, RESP_ROOT);
        if (checkErrors) {
            verifyNoErrors(ctx, fields);
        }
        return ctx;
    }

    private static void verifyNoErrors(ReadContext ctx, Object req) {
        JSONArray errors = ctx.read("$..error");
        assertThat(errors).as("Errors in response when sending " + req).isEmpty();
    }

    private ReadContext postWithQueryStrings(Map<String, Object> fields, Pair<String, List<String>> pair) {
        fields.put("output_type", "json");
        Object resp = REST.postWithQueryStrings(URL, fields, pair);
        String reqText = String.format("%s %s", fields, pair);
        ReadContext ctx = parseVerifyJSON(resp, RESP_ROOT);
        verifyNoErrors(ctx, reqText);
        return ctx;
    }

    public String login() {
        return login(Config.customerUsername(), Config.customerPassword(), Config.clientIp());
    }

    public String login(String username, String password, String clientIp) {
        Map<String, Object> fields = wapiAuthFields();
        fields.put("action", "account_login");
        fields.put("customer_username", username);
        fields.put("customer_password", password);
        if (!StringUtils.isEmpty(Config.clientIp())) {
            fields.put("client_ip", Config.clientIp());
        }
        return post(fields).read(RESP_ROOT + ".login[0].session_id");
    }

    @SuppressWarnings("unchecked")
    public String createNewCustomer(Map custData) {
        Map<String, Object> fields = wapiAuthFields();
        fields.put("action", "account_insert_customer");
        fields.putAll(custData);
        ReadContext resp = post(fields);
        Integer custId = resp.read(RESP_ROOT + ".success.customer_id");
        String msg = resp.read(RESP_ROOT + ".success.message");
        log.info("Customer ID=" + custId);
        return msg;
    }

    public String depositCash(String sessionId, BigDecimal cashAmount) {
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "account_deposit");
        fields.put("amount", cashAmount);
        ReadContext resp = post(fields);
        String msg = resp.read(RESP_ROOT + ".account[0].message");
        int transId = resp.read(RESP_ROOT + ".account[0].transaction_id");
        log.info("Deposit Status message=" + msg);
        log.info("Deposit Transaction ID=" + transId);
        return msg;
    }

    public BigDecimal getBalance(String sessionId) {
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "bet_get_balance");
        String balance = post(fields).read(RESP_ROOT + ".account[0].balance");
        return new BigDecimal(balance);
    }

    public BigDecimal getBonusBalance(String sessionId) {
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "bet_get_balance");
        String balance = post(fields).read(RESP_ROOT + ".account[0].bonus_balance");
        return new BigDecimal(balance);
    }


    public Map<KEY, String> getBetDetails(String sessionId, int betId) {
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "bet_get_bet");
        fields.put("bet_id", betId);
        ReadContext resp = post(fields);
        HashMap<KEY, String> bet = new HashMap<>();
        bet.put(KEY.RUNNER_NAME, resp.read(RESP_ROOT + ".bet.selections.betdetail[0].side"));
        bet.put(KEY.BET_STATUS, resp.read(RESP_ROOT + ".bet.status"));
        bet.put(KEY.BET_PAYOUT, resp.read(RESP_ROOT + ".bet.bet_win"));
        return bet;
    }

    public List<BetType> getBetTypes(String sessionId, int betId) {
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "bet_get_bet");
        fields.put("bet_id", betId);
        ReadContext resp = post(fields);
        List<String> betTypeNames = resp.read(RESP_ROOT + ".bet.selections.betdetail[*].bet_type_name");
        return betTypeNames.stream().map(BetType::fromName).collect(Collectors.toList());
    }


    private Map<String, Object> bonusBetFields(String sessionId, Integer bbFlag, String mpid, BetType betType) {
        Map<String, Object> fields = new HashMap<>();
        if (bbFlag.equals(1)) {
            fields.put("free_bet_code", getBonusBetCode(sessionId, mpid, betType.id));
        } else if (bbFlag.equals(2)) {
            fields.put("free_bet_code", "::_::");
        }
        fields.put("stake_source", bbFlag);
        return fields;
    }

    public ReadContext placeSingleWinBet(String sessionId, Integer productId, String mpid, String winPrice, BigDecimal stake, Integer bonusBetFlag) {
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "bet_place_bet");
        fields.put("bet_type", BetType.Win.id);
        fields.put("product_id", productId);
        fields.put("mpid", mpid);
        fields.put("win_price", winPrice);
        fields.put("amount", stake);
        if (bonusBetFlag > 0) {
            fields.putAll(bonusBetFields(sessionId, bonusBetFlag, mpid, BetType.Win));
        }
        return post(fields);
    }

    public ReadContext placeSinglePlaceBet(String sessionId, Integer productId, String mpid, String placePrice, BigDecimal stake, Integer bonusBetFlag) {
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "bet_place_bet");
        fields.put("bet_type", BetType.Place.id);
        fields.put("product_id", productId);
        fields.put("mpid", mpid);
        fields.put("place_price", placePrice);
        fields.put("amount", stake);
        if (bonusBetFlag > 0) {
            fields.putAll(bonusBetFields(sessionId, bonusBetFlag, mpid, BetType.Place));
        }
        return post(fields);
    }

    public ReadContext placeSingleEachwayBet(String sessionId, Integer productId, String mpid, String winPrice, String placePrice, BigDecimal stake, Integer bonusBetFlag) {
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "bet_place_bet");
        fields.put("bet_type", BetType.Eachway.id);
        fields.put("product_id", productId);
        fields.put("mpid", mpid);
        fields.put("win_price", winPrice);
        fields.put("place_price", placePrice);
        fields.put("amount", stake);
        if (bonusBetFlag > 0) {
            fields.putAll(bonusBetFields(sessionId, bonusBetFlag, mpid, BetType.Eachway));
        }
        return post(fields);
    }

    private String getBonusBetCode(String sessionId, String mpid, Integer betType) {
        Map<String, Object> bbFields = wapiAuthFields(sessionId);
        bbFields.put("action", "bet_bonus_bet_allowed");
        bbFields.put("mpid", mpid);
        bbFields.put("bet_type", betType);
        ReadContext resp = post(bbFields);
        return resp.read(RESP_ROOT + ".bonus_bets[0].bonus_bet[0].free_bet_code");
    }

    public ReadContext placeExoticBet(String sessionId, Integer productId, List<String> selectionIds, String marketId, BigDecimal stake, boolean flexi, boolean unused) {
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "bet_place_bet");
        fields.put("product_id", productId);
        fields.put("slot[1][market]", marketId);
        fields.put("amount", stake);
        if (flexi) {
            fields.put("flexi", "y");
        }
        return postWithQueryStrings(fields, Pair.of("slot[1][selection][]", selectionIds));
    }

    public ReadContext placeExoticBetMultiMarkets(String sessionId, Integer productId, List<String> selectionIds, List<String> marketIds, BigDecimal stake, boolean flexi) {
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "bet_place_bet");
        fields.put("product_id", productId);
        AtomicInteger atomicIntSel = new AtomicInteger(1);
        selectionIds.forEach(id -> fields.put("slot[" + atomicIntSel.getAndIncrement() + "][selection][]", id));
        AtomicInteger atomicIntMarket = new AtomicInteger(1);
        marketIds.forEach(id -> fields.put("slot[" + atomicIntMarket.getAndIncrement() + "][market]", id));
        fields.put("amount", stake);
        if (flexi) {
            fields.put("flexi", "y");
        }
        return post(fields);
    }

    private ReadContext addSelectionToMulti(String sessionId, Integer prodId, String mpid, BetType bt) {
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "bet_add_sel_to_multi");
        fields.put("mpid", mpid);
        fields.put("product_id", prodId);
        if (null != bt) {
            fields.put("bt", bt.id);
        }
        return post(fields, false);
    }

    public String prepareSelectionsForDoubleBet(String sessionId, List<Map<WAPI.KEY, String>> selections, List<Integer> prodIds, List<BetType> betTypes) {
        List<Integer> sizes = Arrays.asList(selections.size(), prodIds.size(), betTypes.size());
        assertThat(sizes).as("Number of Selections, Products and BetTypes").allMatch(size -> size.equals(2));
        return prepareSelectionsForMultiBet(sessionId, selections, prodIds, MultiType.Double, betTypes);
    }

    public String prepareSelectionsForTrebleBet(String sessionId, List<Map<WAPI.KEY, String>> selections, List<Integer> prodIds, List<BetType> betTypes) {
        List<Integer> sizes = Arrays.asList(selections.size(), prodIds.size(), betTypes.size());
        assertThat(sizes).as("Number of Selections, Products and BetTypes").allMatch(size -> size.equals(3));
        return prepareSelectionsForMultiBet(sessionId, selections, prodIds, MultiType.Treble, betTypes);
    }

    public String prepareSelectionsForMultiBet(String sessionId, List<Map<WAPI.KEY, String>> selections, List<Integer> prodIds, MultiType multiType, List<BetType> betTypesInput) {
        List<BetType> betTypes = (null != betTypesInput) ? betTypesInput : Collections.nCopies(selections.size(), null);
        ReadContext response = addAllSelectionsToMulti(sessionId, selections, prodIds, betTypes);
        JSONArray uuidsFound = response.read(RESP_ROOT + ".multiple" + jfilter("name", multiType.exactName) + ".uuid");
        assertThat(uuidsFound).as("Multi UUIDs after adding selections").hasSize(1);
        return uuidsFound.get(0).toString();
    }

    public List<String> prepareSelectionsForMultiMultiBet(String sessionId, List<Map<WAPI.KEY, String>> selections, List<Integer> prodIds, List<String> multiType, List<BetType> betTypes) {
        ReadContext response = addAllSelectionsToMulti(sessionId, selections, prodIds, betTypes);
        List<String> uuidsFound = new ArrayList<>();
        for (String newMType : multiType) {
            WAPI.MultiType finalMType = WAPI.MultiType.fromName(newMType);
            JSONArray uuid = response.read(RESP_ROOT + ".multiple" + jfilter("name", finalMType.exactName) + ".uuid");
            uuidsFound.add(uuid.get(0).toString());
        }
        assertThat(uuidsFound).as("Multi UUIDs after adding selections").hasSize(multiType.size());
        return uuidsFound;
    }

    private ReadContext addAllSelectionsToMulti(String sessionId, List<Map<KEY, String>> selections, List<Integer> prodIds, List<BetType> betTypes) {
        assertThat(selections.size()).as("Number of selections for Multi bet").isGreaterThan(1);
        assertThat(selections.size()).isEqualTo(prodIds.size()).isEqualTo(betTypes.size());
        ReadContext response = null;
        for (int i = 0; i < selections.size(); i++) {
            String selectionId = selections.get(i).get(KEY.MPID);
            response = addSelectionToMulti(sessionId, prodIds.get(i), selectionId, betTypes.get(i));
            if (i == 0) {  // error shows only for 1 selection
                String msg = response.read(RESP_ROOT + ".error[0].error_text");
                assertThat(msg).isEqualTo("Add more selections for multiple bet types");
            }
        }
        assertThat(response).isNotNull();
        return response;
    }

    public ReadContext placeMultiBet(String sessionId, String uuid, BigDecimal stake, boolean flexi) {
        return placeMultiMultiBet(sessionId, Arrays.asList(uuid), Arrays.asList(stake), Arrays.asList(flexi));
    }

    public ReadContext placeMultiMultiBet(String sessionId, List<String> uuid, List<BigDecimal> stakes, List<Boolean> flexis) {
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "bet_place_bet");
        fields.put("placing_multiple", 1);
        for (int i = 0; i < uuid.size(); i++) {
            fields.put("multi[uuid][" + uuid.get(i) + "][stake]", stakes.get(i));
            if (flexis.get(i)) {
                fields.put("multi[uuid][" + uuid.get(i) + "][flexi]", "y");
            }
        }
        return post(fields);
    }

    public BigDecimal readNewBalance(ReadContext resp) {
        JSONArray allBalances = resp.read(RESP_ROOT + ".bet[*].new_balance");
        assertThat(allBalances).as("new_balance array in response").isNotEmpty();
        String lastBalance = allBalances.get(allBalances.size() - 1).toString();
        return new BigDecimal(lastBalance);
    }

    public List<Integer> readBetIds(ReadContext resp) {
        return resp.read(RESP_ROOT + ".bet[*].bet_id");
    }

    public List<Integer> readBetSlipIds(ReadContext resp) {
        return resp.read(RESP_ROOT + ".bet[*].betslip_id");
    }

    public ReadContext getEventMarkets(String sessionId, String evtId) {
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "site_get_markets");
        fields.put("eid", evtId);
        fields.put("show_held", false);
        return post(fields);
    }

    public JSONArray getExistingEvents(String sessionId, int catId, int latestHours) {
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "site_get_events");
        fields.put("cid", catId);
        fields.put("latest", latestHours);
        ReadContext resp = post(fields);
        log.debug(resp.jsonString());
        JSONArray events = resp.read(RESP_ROOT + ".events.event.*");
        assertThat(events).withFailMessage("No Events found").isNotEmpty();
        return events;
    }

    public List<String> getExistingEventNames(String sessionId, int catId, int latestHours) {
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "site_get_events");
        fields.put("cid", catId);
        fields.put("latest", latestHours);
        ReadContext resp = post(fields);
        log.debug(resp.jsonString());
        JSONArray events = resp.read(RESP_ROOT + ".events.*");
        assertThat(events).withFailMessage("No Events found").isNotEmpty();
        return resp.read(RESP_ROOT + ".events.event[*].name.-content");
    }

    public String readAmlStatus(String sessionId) {
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "account_verify_aml");
        return (String) post(fields).read(RESP_ROOT + ".account[0].aml_status");
    }

    public String generateAffiliateLoginToken(String sessionId) {
        Map<String, Object> fields = wapiAuthFields(sessionId);
        fields.put("action", "account_generate_login_token");
        return (String) post(fields).read(RESP_ROOT + ".token");
    }

    public List<Map<WAPI.KEY, String>> getMultiEventSelections(String sessionId, List<String> eventIds, List<String> runners, List<Integer> prodIds) {
        List<Map<WAPI.KEY, String>> selections = new ArrayList<>();
        for (int i = 0; i < eventIds.size(); i++) {
            ReadContext resp = getEventMarkets(sessionId, eventIds.get(i));
            Map<WAPI.KEY, String> sel = readSelection(resp, runners.get(i), prodIds.get(i));
            selections.add(sel);
        }
        return selections;
    }

    public Map findOneSelectionByName(ReadContext resp, String selName) {
        String selPath = RESP_ROOT + ".markets.market[*].selections.selection" + jfilter("name", selName);
        JSONArray selections = resp.read(selPath);
        assertThat(selections).as("Expected to find 1 selection at path=" + selPath).hasSize(1);
        return (Map) selections.get(0);
    }

    public Map<KEY, String> readSelection(ReadContext resp, String selName, Integer prodId) {
        return readSelection(resp, selName, prodId, true);
    }

    public Map<KEY, String> readSelection(ReadContext resp, String selName, Integer prodId, boolean withPrices) {
        String mktPath = RESP_ROOT + ".markets.market[0]";
        String selPath = mktPath + ".selections.selection" + jfilter("name", selName);
        String pricePath = selPath + ".prices.price";
        String prodPricePath = pricePath + jfilter("product_id", prodId.toString());
        Map market = resp.read(mktPath);
        JSONArray selections = resp.read(selPath);
        JSONArray prices = resp.read(pricePath);
        JSONArray productPrices = resp.read(prodPricePath);
        assertThat(market).as("No markets found at path=" + mktPath).isNotEmpty();
        assertThat(selections).as("No selections for market=" + market).isNotEmpty();
        assertThat(prices).as("No prices in selection=" + selections).isNotEmpty();
        assertThat(productPrices).as(String.format("No price for product=%s: other prices: %s", prodId, prices)).isNotEmpty();

        HashMap<KEY, String> sel = new HashMap<>();
        sel.put(KEY.MPID, readPriceAttr(resp, prodPricePath, BetType.Win.name(), "mpid"));
        if (withPrices) {
            sel.put(KEY.WIN_PRICE, readPriceAttr(resp, prodPricePath, BetType.Win.name(), "precise_price"));
            sel.put(KEY.PLACE_PRICE, readPriceAttr(resp, prodPricePath, BetType.Place.name(), "precise_price"));
        } else {
            sel.put(KEY.WIN_PRICE, "1.00");
            sel.put(KEY.PLACE_PRICE, "1.00");
        }
        return sel;
    }

    public List<String> readSelectionIds(ReadContext resp, String marketId, List<String> selectionNames) {
        return selectionNames.stream().map(sn -> readSelectionId(resp, marketId, sn)).collect(Collectors.toList());
    }

    public String readSelectionId(ReadContext resp, String marketId, String selectionName) {
        String mktPath = ".markets.market" + jfilter("id", marketId);
        String selPath = ".selections.selection" + jfilter("name", selectionName);
        return readAttrOneOnly(resp, mktPath + selPath, "id");
    }

    public String readMarketId(ReadContext resp, String mktName) {
        String mktPath = ".markets.market" + jfilter("name", mktName);
        return readAttrOneOnly(resp, mktPath, "id");
    }

    private static String readPriceAttr(ReadContext resp, String pricePath, String betTypeName, String attrName) {
        String path = pricePath + jfilter("bet_type_name", betTypeName);
        // TODO shall we consider returning 1.0?
        // return readAttrOrElse(resp, path, attrName, "1");
        return readAttrOneOnly(resp, path, attrName);
    }

    public String readInterceptRejectedCombin(ReadContext response) {
        String path = "$.RSP.bet_intercept.rejected_bets.multiple[0].rejected_combin";
        return response.read(path).toString();
    }

    public String readInterceptStatus(ReadContext resp) {
        return resp.read("$.RSP.bet_intercept.status");
    }

    public String readInterceptNewStake(ReadContext resp) {
        return resp.read("$.RSP.bet[0].new_stake");
    }

    private static String readAttrOrElse(ReadContext resp, String basePath, String attrName, String orElse) {
        String attr = orElse;
        JSONArray attrs = resp.read(basePath + "." + attrName);
        if (attrs.size() > 0) {
            attr = String.valueOf(attrs.get(0));
            assertThat(attr).as("attribute '%s' at path='%s'").isNotEmpty();
        }
        return attr;
    }

    private static String readAttrOneOnly(ReadContext resp, String basePath, String attrName) {
        String attr = readAttrOrElse(resp, basePath, attrName, null);
        assertThat(attr).as(String.format("expected to find one attribute '%s' at path='%s'", attrName, basePath));
        return attr;
    }

    public ReadContext placeSingleWinBetForIntercept(String accessToken, Integer prodId, String mpid, String winPrice, BigDecimal stake, Integer bonusBetFlag, WAPI.InterceptOption interceptOption, String partialAmount) {
        ReadContext response = placeSingleWinBet(accessToken, prodId, mpid, winPrice, stake, bonusBetFlag);
        return response;
    }

    public ReadContext placeMultiBetInIntercept(WAPI.MultiType multiType, List<BetType> betTypes, List<String> runners, BigDecimal stake, boolean isFlexi, WAPI.InterceptOption interceptOption, String partialAmount) {
        String accessToken = (String) Storage.get(API_ACCESS_TOKEN);
        String uuid = configureMultiBet(accessToken, multiType, betTypes, runners, stake, isFlexi);
        ReadContext response = placeMultiBet(accessToken, uuid, stake, isFlexi);
        return response;
    }

    public ReadContext placeMultipleBetsInIntercept(WAPI.MultiType multiType, List<BetType> betTypes, List<String> runners, BigDecimal stake, boolean isFlexi, List<String> interceptOptions, String partialAmount) {
        String accessToken = (String) Storage.get(API_ACCESS_TOKEN);
        String uuid = configureMultiBet(accessToken, multiType, betTypes, runners, stake, isFlexi);
        ReadContext response = placeMultiBet(accessToken, uuid, stake, isFlexi);
        return response;
    }

    public String configureMultiBet(String accessToken, WAPI.MultiType multiType, List<BetType> betTypes, List<String> runners, BigDecimal stake, boolean isFlexi) {
        List<String> eventIds = (List<String>) Storage.get(EVENT_IDS);
        List<Integer> prodIds = (List<Integer>) Storage.get(PRODUCT_IDS);
        assertThat(eventIds.size()).isEqualTo(runners.size()).isEqualTo(prodIds.size());
        List<Map<WAPI.KEY, String>> selections = getMultiEventSelections(accessToken, eventIds, runners, prodIds);
        String uuid;
        if (WAPI.MultiType.Double.equals(multiType)) {
            uuid = prepareSelectionsForDoubleBet(accessToken, selections, prodIds, betTypes);
        } else if (WAPI.MultiType.Treble.equals(multiType)) {
            uuid = prepareSelectionsForTrebleBet(accessToken, selections, prodIds, betTypes);
        } else {
            uuid = prepareSelectionsForMultiBet(accessToken, selections, prodIds, multiType, betTypes);
        }
        return uuid;
    }

    private static String jfilter(String attr, String value) {
        return String.format("[?(@.%s == '%s')]", attr, value);
    }

}

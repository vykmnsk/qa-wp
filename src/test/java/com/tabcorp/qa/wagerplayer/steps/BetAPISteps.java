package com.tabcorp.qa.wagerplayer.steps;

import com.jayway.jsonpath.ReadContext;
import com.tabcorp.qa.common.BetType;
import com.tabcorp.qa.common.Helpers;
import com.tabcorp.qa.common.Storage;
import com.tabcorp.qa.wagerplayer.Config;
import com.tabcorp.qa.wagerplayer.api.MOBI_V2;
import com.tabcorp.qa.wagerplayer.api.WAPI;
import com.tabcorp.qa.wagerplayer.api.WagerPlayerAPI;
import cucumber.api.DataTable;
import cucumber.api.java8.En;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.tabcorp.qa.common.Storage.KEY.*;
import static com.tabcorp.qa.wagerplayer.api.WagerPlayerAPI.KEY.*;
import static org.assertj.core.api.Assertions.assertThat;


public class BetAPISteps implements En {
    private static Logger log = LoggerFactory.getLogger(BetAPISteps.class);

    private BigDecimal balanceAfterBet;
    private WagerPlayerAPI api = Config.getAPI();
    private WAPI wapi = new WAPI();

    public BetAPISteps() {
        When("^I place a single \"([a-zA-Z]+)\" bet on the runner \"([^\"]*)\" for \\$(\\d+.\\d\\d)$",
                (String betTypeName, String runner, BigDecimal stake) -> {
                    String evId = (String) Storage.getLast(EVENT_IDS);
                    Integer prodId = (Integer) Storage.getLast(PRODUCT_IDS);
                    Integer bonusBetFlag = 0;
                    balanceAfterBet = placeSingleBet(betTypeName, evId, prodId, runner, stake, bonusBetFlag, false);
                });

        When("^I place a bonus single \"([a-zA-Z]+)\" bet on the runner \"([^\"]*)\" for \\$(\\d+.\\d\\d) with bonus wallet as \"([Y|N])\"$",
                (String betTypeName, String runner, BigDecimal stake, String bonusWalletOption) -> {
                    boolean useBonusWallet = "Y".equalsIgnoreCase(bonusWalletOption);
                    Integer bonusBetFlag = useBonusWallet ? 2 : 1;
                    String evId = (String) Storage.getLast(EVENT_IDS);
                    Integer prodId = (Integer) Storage.getLast(PRODUCT_IDS);
                    balanceAfterBet = placeSingleBet(betTypeName, evId, prodId, runner, stake, bonusBetFlag, false);
                });

        When("^I place a unfixed single \"([a-zA-Z]+)\" bet on the runner \"([^\"]*)\" for \\$(\\d+.\\d\\d)$",
                (String betTypeName, String runner, BigDecimal stake) -> {
                    String evId = (String) Storage.getLast(EVENT_IDS);
                    Integer prodId = (Integer) Storage.getLast(PRODUCT_IDS);
                    Integer bonusBetFlag = 0;
                    balanceAfterBet = placeSingleBet(betTypeName, evId, prodId, runner, stake, bonusBetFlag, true);
                });


        When("^I place an exotic \"([^\"]*)\" bet on the runners \"([^\"]*)\" for \\$(\\d+.\\d\\d)$",
                (String betTypeName, String runnersCSV, BigDecimal stake) -> {
                    balanceAfterBet = oneEventExoticBetStep(betTypeName, runnersCSV, stake, false, false);
                });

        When("^I place an exotic \"([^\"]*)\" bet on the runners \"([^\"]*)\" for \\$(\\d+.\\d\\d) with boxed as \"([Y|N])\"$",
                (String betTypeName, String runnersCSV, BigDecimal stake, String boxed) -> {
                    boolean isBoxed = "Y".equalsIgnoreCase(boxed);
                    balanceAfterBet = oneEventExoticBetStep(betTypeName, runnersCSV, stake, false, isBoxed);
                });

        When("^I place an exotic \"([^\"]*)\" bet on the runners \"([^\"]*)\" for \\$(\\d+.\\d\\d) with flexi as \"([Y|N])\"$",
                (String betTypeName, String runnersCSV, BigDecimal stake, String flexi) -> {
                    boolean isFlexi = "Y".equalsIgnoreCase(flexi);
                    balanceAfterBet = oneEventExoticBetStep(betTypeName, runnersCSV, stake, isFlexi, false);
                });

        When("^I place an exotic \"([^\"]*)\" bet on the runners \"([^\"]*)\" across multiple events for \\$(\\d+.\\d\\d) with flexi as \"([^\"]*)\"$",
                (String betTypeName, String runnersCSV, BigDecimal stake, String flexi) -> {
                    assertThat(betTypeName.toUpperCase()).as("Exotic BetTypeName input")
                            .isIn("DAILY DOUBLE", "RUNNING DOUBLE", "QUADDIE");
                    List<String> runners = Helpers.extractCSV(runnersCSV);
                    boolean isFlexi = "Y".equalsIgnoreCase(flexi);
                    String accessToken = (String) Storage.get(API_ACCESS_TOKEN);
                    Integer prodId = (Integer) Storage.getLast(PRODUCT_IDS);
                    List<String> eventIds = (List<String>) Storage.get(EVENT_IDS);
                    ReadContext response = placeExoticBetMultEvents(accessToken, eventIds, prodId, runners, stake, isFlexi);
                    balanceAfterBet = api.readNewBalance(response);
                });

        When("^I place a Luxbet Double Bet \"([A-Za-z]+)\"-\"([A-Za-z]+)\" on the runners \"([^\"]*)\" for \\$(\\d+.\\d\\d) with flexi as \"([^\"]*)\"$",
                (String betTypeName1, String betTypeName2, String runnersCSV, BigDecimal stake, String flexi) -> {
                    assertThat(Config.isLuxbet()).as("LUXBET only step").isTrue();
                    BetType betType1 = BetType.fromName(betTypeName1);
                    BetType betType2 = BetType.fromName(betTypeName2);
                    List<String> runners = Helpers.extractCSV(runnersCSV);
                    boolean isFlexi = "Y".equalsIgnoreCase(flexi);
                    ReadContext response = placeMultiBet(WAPI.MultiType.Double, Arrays.asList(betType1, betType2), runners, stake, isFlexi);
                    balanceAfterBet = api.readNewBalance(response);
                });

        When("^I place a Luxbet Treble Bet \"([A-Za-z]+)\"-\"([A-Za-z]+)\"-\"([A-Za-z]+)\" on the runners \"([^\"]*)\" for \\$(\\d+.\\d\\d) with flexi as \"([^\"]*)\"$",
                (String betTypeName1, String betTypeName2, String betTypeName3, String runnersCSV, BigDecimal stake, String flexi) -> {
                    assertThat(Config.isLuxbet()).as("LUXBET only step").isTrue();
                    BetType betType1 = BetType.fromName(betTypeName1);
                    BetType betType2 = BetType.fromName(betTypeName2);
                    BetType betType3 = BetType.fromName(betTypeName3);
                    List<String> runners = Helpers.extractCSV(runnersCSV);
                    boolean isFlexi = "Y".equalsIgnoreCase(flexi);
                    ReadContext response = placeMultiBet(WAPI.MultiType.Treble, Arrays.asList(betType1, betType2, betType3), runners, stake, isFlexi);
                    balanceAfterBet = api.readNewBalance(response);
                });

        When("^I place a Luxbet Multi Bet \"([^\"]*)\" on the runners \"([^\"]*)\" for \\$(\\d+.\\d\\d) with flexi as \"([^\"]*)\"$",
                (String multiTypeName, String runnersCSV, BigDecimal stake, String flexi) -> {
                    assertThat(Config.isLuxbet()).as("LUXBET only step").isTrue();
                    WAPI.MultiType multiType = WAPI.MultiType.fromName(multiTypeName);
                    List<String> runners = Helpers.extractCSV(runnersCSV);
                    boolean isFlexi = "Y".equalsIgnoreCase(flexi);
                    ReadContext response = placeMultiBet(multiType, null, runners, stake, isFlexi);
                    balanceAfterBet = api.readNewBalance(response);
                });

        And("^I place a Luxbet Multi-Treble \"([^\"]*)\" bet on types \"([A-Za-z]+)\"-\"([A-Za-z]+)\"-\"([A-Za-z]+)\" on the runners \"([^\"]*)\" for \\$\"([^\"]*)\" with flexi as \"([^\"]*)\"$",
                (String multiTypeName, String betTypeName1, String betTypeName2, String betTypeName3, String runnersCSV, String stake, String flexi) -> {
                    assertThat(Config.isLuxbet()).as("LUXBET only step").isTrue();
                    List<String> multiTypes = Helpers.extractCSV(multiTypeName);
                    List<String> runners = Helpers.extractCSV(runnersCSV);
                    List<String> splitStake = Helpers.extractCSV(stake);
                    List<BigDecimal> stakes = splitStake.stream().map(BigDecimal::new).collect(Collectors.toList());
                    List<String> splitFlexi = Helpers.extractCSV(flexi);
                    List<Boolean> flexis = splitFlexi.stream().map("Y"::equalsIgnoreCase).collect(Collectors.toList());

                    BetType betType1 = BetType.fromName(betTypeName1);
                    BetType betType2 = BetType.fromName(betTypeName2);
                    BetType betType3 = BetType.fromName(betTypeName3);

                    ReadContext response = placeMultiTrebleBet(multiTypes, Arrays.asList(betType1, betType2, betType3), runners, stakes, flexis);
                    balanceAfterBet = wapi.readNewBalance(response);
                });

        And("^I place a Luxbet Multi-Multi bet on \"([^\"]*)\" on the runners \"([^\"]*)\" for \\$\"([^\"]*)\" with flexi as \"([^\"]*)\"$",
                (String multiTypeName, String runnersCSV, String stake, String flexi) -> {
                    assertThat(Config.isLuxbet()).as("LUXBET only step").isTrue();
                    List<String> multiTypes = Helpers.extractCSV(multiTypeName);
                    List<String> runners = Helpers.extractCSV(runnersCSV);
                    List<String> splitStake = Helpers.extractCSV(stake);
                    List<BigDecimal> stakes = splitStake.stream().map(BigDecimal::new).collect(Collectors.toList());
                    List<String> splitFlexi = Helpers.extractCSV(flexi);
                    List<Boolean> flexis = splitFlexi.stream().map("Y"::equalsIgnoreCase).collect(Collectors.toList());
                    List<BetType> dummyBetTypes = runners.stream().map(r -> (BetType) null).collect(Collectors.toList());
                    ReadContext response = placeMultiMultiBet(multiTypes, dummyBetTypes, runners, stakes, flexis);
                    balanceAfterBet = wapi.readNewBalance(response);
                });

        When("^I place a Redbook Multi Bet \"([^\"]+)\" \"([A-Za-z]+)\" on the runners \"([^\"]+)\" for \\$(\\d+.\\d\\d)$",
                (String multiTypeName, String betTypeName, String runnersCVS, BigDecimal stake) -> {
                    assertThat(Config.isRedbook()).as("REDBOOK only step").isTrue();
                    MOBI_V2.MultiType multiType = MOBI_V2.MultiType.fromName(multiTypeName);
                    BetType betType = BetType.fromName(betTypeName);
                    List<String> runners = Helpers.extractCSV(runnersCVS);
                    String accessToken = (String) Storage.get(API_ACCESS_TOKEN);
                    List<String> eventIds = (List<String>) Storage.get(EVENT_IDS);
                    List<Integer> prodIds = (List<Integer>) Storage.get(PRODUCT_IDS);
                    assertThat(eventIds.size()).isEqualTo(runners.size()).isEqualTo(prodIds.size());
                    List<Map<WAPI.KEY, String>> selections = wapi.getMultiEventSelections(accessToken, eventIds, runners, prodIds);
                    ReadContext resp = ((MOBI_V2) api).placeMultiBet(accessToken, selections, multiType, betType, stake);
                    balanceAfterBet = api.readNewBalance(resp);
                });

        Then("^customer balance after bet is decreased by \\$(\\d+\\.\\d\\d)$", (BigDecimal diff) -> {
            BigDecimal balanceBefore = (BigDecimal) Storage.get(BALANCE_BEFORE);
            assertThat(Helpers.roundOff(balanceBefore.subtract(balanceAfterBet))).isEqualTo(Helpers.roundOff(diff));
        });

        Then("^customer balance since last bet is increased by \\$(\\d+.\\d\\d)$", (BigDecimal diffInput) -> {
            final BigDecimal difference = Helpers.roundOff(diffInput);
            final BigDecimal balBefore = Helpers.roundOff(balanceAfterBet);
            String accessToken = (String) Storage.get(API_ACCESS_TOKEN);

            //array here because lambda expr has to be able to update it from inside
            final BigDecimal[] balNow = new BigDecimal[1];
            Helpers.retryOnFailure(() -> {
                balNow[0] = Helpers.roundOff(api.getBalance(accessToken));
                if (!difference.equals(new BigDecimal("0.00"))) {
                    assertThat(balNow[0]).as("Customer balance hasn't changed").isNotEqualTo(balBefore);
                }
            }, 10, 4);
            assertThat(balNow[0].subtract(balBefore)).as("Customer Balance increased by").isEqualTo(difference);
        });

        Then("^I verify status and payout of bets placed on runners$", (DataTable table) -> {
            List<List<String>> expectedBetData = table.raw();
            String accessToken = (String) Storage.get(API_ACCESS_TOKEN);
            List<Integer> placedBetIds = (List<Integer>) Storage.get(BET_IDS);
            assertThat(expectedBetData.size()).as("Bet Data Input table must match placed bets").isEqualTo(placedBetIds.size());
            for (int i = 0; i < expectedBetData.size(); i++) {
                List<String> expectedBet = expectedBetData.get(i);
                assertThat(expectedBet.size()).as("expecting for example: [ROCKING HORSE, Refunded, 0.00]").isEqualTo(3);
                Map<WagerPlayerAPI.KEY, String> actualBetData = api.getBetDetails(accessToken, placedBetIds.get(i));
                List<String> actualBet = Arrays.asList(
                        actualBetData.get(RUNNER_NAME),
                        actualBetData.get(BET_STATUS),
                        actualBetData.get(BET_PAYOUT));
                assertThat(actualBet).isEqualTo(expectedBet);
            }
        });

    }

    private ReadContext placeMultiBet(WAPI.MultiType multiType, List<BetType> betTypes, List<String> runners, BigDecimal stake, boolean isFlexi) {
        String accessToken = (String) Storage.get(API_ACCESS_TOKEN);
        List<String> eventIds = (List<String>) Storage.get(EVENT_IDS);
        List<Integer> prodIds = (List<Integer>) Storage.get(PRODUCT_IDS);
        assertThat(eventIds.size()).isEqualTo(runners.size()).isEqualTo(prodIds.size());
        List<Map<WAPI.KEY, String>> selections = wapi.getMultiEventSelections(accessToken, eventIds, runners, prodIds);
        String uuid;
        if (WAPI.MultiType.Double.equals(multiType)) {
            uuid = wapi.prepareSelectionsForDoubleBet(accessToken, selections, prodIds, betTypes);
        } else if (WAPI.MultiType.Treble.equals(multiType)) {
            uuid = wapi.prepareSelectionsForTrebleBet(accessToken, selections, prodIds, betTypes);
        } else {
            uuid = wapi.prepareSelectionsForMultiBet(accessToken, selections, prodIds, multiType, betTypes);
        }
        ReadContext response = wapi.placeMultiBet(accessToken, uuid, stake, isFlexi);

        List<Integer> betIds = api.readBetIds(response);
        log.info("Bet IDs=" + betIds);
        if (null != betTypes) {
            for (int betId : betIds){
                List<BetType> placedBetTypes = wapi.getBetTypes(accessToken, betId);
                assertThat(placedBetTypes)
                        .as(String.format("Expected Bet Types for %s bet, betId=%d", multiType.exactName, betId))
                        .isEqualTo(betTypes);
            }
        }
        return response;
    }

    private ReadContext placeMultiMultiBet(List<String> multiTypes, List<BetType> betTypes, List<String> runners, List<BigDecimal> stakes, List<Boolean> flexis) {
        String accessToken = (String) Storage.get(API_ACCESS_TOKEN);
        List<String> eventIds = (List<String>) Storage.get(EVENT_IDS);
        List<Integer> prodIds = (List<Integer>) Storage.get(PRODUCT_IDS);
        assertThat(eventIds.size()).isEqualTo(runners.size()).isEqualTo(prodIds.size());
        List<Map<WAPI.KEY, String>> selections = wapi.getMultiEventSelections(accessToken, eventIds, runners, prodIds);
        
        List<String> uuids = wapi.prepareSelectionsForMultiMultiBet(accessToken, selections, prodIds, multiTypes, betTypes);
        
        ReadContext response = wapi.placeMultiMultiBet(accessToken, uuids, stakes, flexis);
        List<Integer> betIds = api.readBetIds(response);
        log.info("Bet IDs=" + betIds);
        List<Integer> betSlipIds = wapi.readBetSlipIds(response);
        log.info("Bet Slip IDs=" + betSlipIds);
        return response;
    }

    private ReadContext placeMultiTrebleBet(List<String> multiTypes, List<BetType> betTypes, List<String> runners, List<BigDecimal> stakes, List<Boolean> flexis) {
        assertThat(betTypes.size()).as("Treble should have 3 bet types").isEqualTo(3);
        return placeMultiMultiBet(multiTypes, betTypes, runners, stakes, flexis);
    }

    private BigDecimal placeSingleBet(String betTypeName, String eventId, Integer prodId, String runner, BigDecimal stake, Integer bonusBetflag, boolean useDefaultPrices) {
        String accessToken = (String) Storage.get(API_ACCESS_TOKEN);
        ReadContext resp = wapi.getEventMarkets(accessToken, eventId);
        Map<WagerPlayerAPI.KEY, String> selection = wapi.readSelection(resp, runner, prodId, useDefaultPrices);

        ReadContext response;
        switch (betTypeName.toUpperCase()) {
            case "WIN":
                response = api.placeSingleWinBet(accessToken, prodId,
                        selection.get(MPID), selection.get(WIN_PRICE), stake, bonusBetflag);
                break;
            case "PLACE":
                response = api.placeSinglePlaceBet(accessToken, prodId,
                        selection.get(MPID), selection.get(PLACE_PRICE), stake, bonusBetflag);
                break;
            case "EACHWAY":
                response = api.placeSingleEachwayBet(accessToken, prodId,
                        selection.get(MPID), selection.get(WIN_PRICE), selection.get(PLACE_PRICE), stake, bonusBetflag);
                break;
            default:
                throw new RuntimeException("Unknown BetTypeName=" + betTypeName);
        }
        List<Integer> betIds = api.readBetIds(response);
        assertThat(betIds.size()).as("Single Bet IDs").isEqualTo(1);
        int betId = betIds.get(0);
        Storage.add(BET_IDS, betId);
        log.info("Bet ID=" + betId);

        List<BetType> expectedBetTypes = Arrays.asList(BetType.fromName(betTypeName));
        List<BetType> placedBetTypes = api.getBetTypes(accessToken, betId);
        assertThat(placedBetTypes)
                .as(String.format("Expected Bet Types for Single bet, betId=%d", betId))
                .isEqualTo(expectedBetTypes);
        return api.readNewBalance(response);
    }

    private BigDecimal oneEventExoticBetStep(String betTypeName, String runnersCSV, BigDecimal stake, boolean isFlexi, boolean isBoxed) {
        assertThat(betTypeName.toUpperCase()).as("Exotic BetTypeName input")
                .isIn("FIRST FOUR", "TRIFECTA", "EXACTA", "QUINELLA", "EXOTIC");
        List<String> runners = Helpers.extractCSV(runnersCSV);
        String accessToken = (String) Storage.get(API_ACCESS_TOKEN);
        Integer prodId = (Integer) Storage.getLast(PRODUCT_IDS);
        String eventId = (String) Storage.getLast(EVENT_IDS);
        ReadContext response = placeExoticBetOneEvent(accessToken, eventId, prodId, runners, stake, isFlexi, isBoxed);
        List<Integer> betIds = api.readBetIds(response);
        log.info("Bet IDs=" + betIds.toString());
        return api.readNewBalance(response);
    }

    private ReadContext placeExoticBetOneEvent(String accessToken, String eventId, Integer prodId, List<String> runners, BigDecimal stake, boolean isFlexi, boolean isBoxed) {
        ReadContext resp = wapi.getEventMarkets(accessToken, eventId);
        String marketId = wapi.readMarketId(resp, "Racing Live");
        List<String> selectionIds = wapi.readSelectionIds(resp, marketId, runners);
        return api.placeExoticBet(accessToken, prodId, selectionIds, marketId, stake, isFlexi, isBoxed);
    }

    private ReadContext placeExoticBetMultEvents(String accessToken, List<String> eventIds, Integer prodId, List<String> runners, BigDecimal stake, boolean isFlexi) {
        assertThat(eventIds.size()).as("Events count must match Runners count").isEqualTo(runners.size());
        List<String> marketIds = new ArrayList<>();
        List<String> selectionIds = new ArrayList<>();
        for (int i = 0; i < runners.size(); i++) {
            ReadContext mktResp = wapi.getEventMarkets(accessToken, eventIds.get(i));
            String marketId = wapi.readMarketId(mktResp, "Racing Live");
            marketIds.add(marketId);
            String selId = wapi.readSelectionId(mktResp, marketId, runners.get(i));
            selectionIds.add(selId);
        }
        return wapi.placeExoticBetMultiMarkets(accessToken, prodId, selectionIds, marketIds, stake, isFlexi);
    }

}

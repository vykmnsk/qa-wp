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
                    balanceAfterBet = oneEventExoticBetStep(betTypeName, runnersCSV, stake, false);
                });

        When("^I place an exotic \"([^\"]*)\" bet on the runners \"([^\"]*)\" for \\$(\\d+.\\d\\d) with flexi as \"([Y|N])\"$",
                (String betTypeName, String runnersCSV, BigDecimal stake, String flexi) -> {
                    boolean isFlexi = "Y".equalsIgnoreCase(flexi);
                    balanceAfterBet = oneEventExoticBetStep(betTypeName, runnersCSV, stake, isFlexi);
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

        When("^I place a multi bet \"([^\"]*)\" on the runners \"([^\"]*)\" for \\$(\\d+.\\d\\d) with flexi as \"([^\"]*)\"$",
                (String multiType, String runnersCSV, BigDecimal stake, String flexi) -> {
                    assertThat(multiType.toUpperCase()).as("Multi TypeName input")
                            .isIn("DOUBLE,WIN-WIN", "DOUBLE,WIN-PLACE", "DOUBLE,PLACE-WIN", "DOUBLE,PLACE-PLACE",
                                    "TREBLE", "DOUBLES", "DOUBLES", "TRIXIE", "PATENT", "4-FOLD", "TREBLES",
                                    "YANKEE", "LUCKY 15", "5-FOLD", "4-FOLDS", "CANADIAN", "LUCKY 31");
                    boolean isFlexi = "Y".equalsIgnoreCase(flexi);
                    List<String> runners = Helpers.extractCSV(runnersCSV);
                    String accessToken = (String) Storage.get(API_ACCESS_TOKEN);
                    List<String> eventIds = (List<String>) Storage.get(EVENT_IDS);
                    List<Integer> prodIds = (List<Integer>) Storage.get(PRODUCT_IDS);
                    assertThat(eventIds.size()).isEqualTo(runners.size()).isEqualTo(prodIds.size());

                    List<Map<WAPI.KEY, String>> selections = wapi.getMultiEventSelections(accessToken, eventIds, runners, prodIds);

                    String uuid;
                    if (multiType.contains(",")) {
                        List<String> doubleMultiTypeInfo = Helpers.extractCSV(multiType);
                        assertThat(doubleMultiTypeInfo).hasSize(2);
                        assertThat(doubleMultiTypeInfo.get(0)).isEqualToIgnoringCase("DOUBLE");
                        List<String> doubleBetTypesStr = Helpers.extractCSV(doubleMultiTypeInfo.get(1), '-');
                        assertThat(doubleBetTypesStr).hasSize(2);
                        List<BetType> doubleBetTypes = doubleBetTypesStr.stream().map(str -> BetType.fromName(str)).collect(Collectors.toList());
                        uuid = wapi.prepareSelectionsForDoubleMultiBet(accessToken, selections, prodIds, doubleBetTypes);
                    } else {
                        uuid = wapi.prepareSelectionsForMultiBet(accessToken, selections, prodIds, multiType);
                    }

                    ReadContext response = wapi.placeMultiBet(accessToken, uuid, stake, isFlexi);
                    List betIds = api.readBetIds(response);
                    log.info("Bet IDs=" + betIds.toString());
                    balanceAfterBet = api.readNewBalance(response);
                });

        When("^I place multi bet \"Double (Win|Eachway)+\" on the runners \"([^\"]+)\" for \\$(\\d+.\\d\\d)$",
                (String betTypeName, String runnersCVS, BigDecimal stake) -> {
                    assertThat(Config.isRedbook()).as("step runs only REDBOOK").isTrue();
                    final String multiType = "Double";
                    final BetType betType = BetType.fromName(betTypeName);

                    List<String> runners = Helpers.extractCSV(runnersCVS);
                    String accessToken = (String) Storage.get(API_ACCESS_TOKEN);
                    List<String> eventIds = (List<String>) Storage.get(EVENT_IDS);
                    List<Integer> prodIds = (List<Integer>) Storage.get(PRODUCT_IDS);
                    assertThat(eventIds.size()).isEqualTo(runners.size()).isEqualTo(prodIds.size());

                    List<Map<WAPI.KEY, String>> selections = wapi.getMultiEventSelections(accessToken, eventIds, runners, prodIds);

                    ReadContext response = ((MOBI_V2) api).placeMultiBet(accessToken, selections, multiType, betType, stake);
                    balanceAfterBet = api.readNewBalance(response);
                });

        Then("^customer balance after bet is decreased by \\$(\\d+\\.\\d\\d)$", (BigDecimal diff) -> {
            BigDecimal balanceBefore = (BigDecimal) Storage.get(BALANCE_BEFORE);
            assertThat(Helpers.roundOff(balanceBefore.subtract(balanceAfterBet))).isEqualTo(Helpers.roundOff(diff));
        });

        Then("^customer balance since last bet is increased by \\$(\\d+.\\d\\d)$", (BigDecimal diff) -> {
            String accessToken = (String) Storage.get(API_ACCESS_TOKEN);
            Helpers.retryOnAssertionFailure(() -> {
                BigDecimal balanceNow = api.getBalance(accessToken);
                assertThat(Helpers.roundOff(balanceNow.subtract(balanceAfterBet))).isEqualTo(Helpers.roundOff(diff));
            }, 10, 4);
        });

        Then("^I verify status and payout of bets placed on runners$", (DataTable table) -> {
            List<List<String>> expectedBetData = table.raw();
            String accessToken = (String) Storage.get(API_ACCESS_TOKEN);
            List<Integer> placedBetIds = (List<Integer>) Storage.get(BET_IDS);
            assertThat(expectedBetData.size()).as("Bet Data Input table must match placed bets").isEqualTo(placedBetIds.size());
            for (int i = 0; i < expectedBetData.size(); i++) {
                List<String> expectedBet = expectedBetData.get(i);
                assertThat(expectedBet.size()).as("expecting for example: [ROCKING HORSE, Refunded, 0.00]").isEqualTo(3);
                Map<WagerPlayerAPI.KEY, String> actualBetData = wapi.getBetDetails(placedBetIds.get(i), accessToken);
                List<String> actualBet = Arrays.asList(
                        actualBetData.get(RUNNER_NAME),
                        actualBetData.get(BET_STATUS),
                        actualBetData.get(BET_PAYOUT));
                assertThat(actualBet).isEqualTo(expectedBet);
            }
        });

    }

    private BigDecimal placeSingleBet(String betTypeName, String eventId, Integer prodId, String runner, BigDecimal stake, Integer bonusBetflag, boolean useDefaultPrices) {
        String accessToken = (String) Storage.get(API_ACCESS_TOKEN);
        String wapiSessionId;
        if (Config.isRedbook()) {
            wapiSessionId = wapi.login();
        } else {
            wapiSessionId = accessToken;
        }
        ReadContext resp = wapi.getEventMarkets(wapiSessionId, eventId);
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
        betIds.forEach(betId -> Storage.add(BET_IDS, betId));
        log.info("Bet IDs=" + betIds.toString());
        return api.readNewBalance(response);
    }

    private BigDecimal oneEventExoticBetStep(String betTypeName, String runnersCSV, BigDecimal stake, boolean isFlexi) {
        assertThat(betTypeName.toUpperCase()).as("Exotic BetTypeName input")
                .isIn("FIRST FOUR", "TRIFECTA", "EXACTA", "QUINELLA", "EXOTIC");
        List<String> runners = Helpers.extractCSV(runnersCSV);
        String accessToken = (String) Storage.get(API_ACCESS_TOKEN);
        Integer prodId = (Integer) Storage.getLast(PRODUCT_IDS);
        String eventId = (String) Storage.getLast(EVENT_IDS);
        ReadContext response = placeExoticBetOneEvent(accessToken, eventId, prodId, runners, stake, isFlexi);
        List betIds = api.readBetIds(response);
        log.info("Bet IDs=" + betIds.toString());
        return api.readNewBalance(response);
    }

    private ReadContext placeExoticBetOneEvent(String accessToken, String eventId, Integer prodId, List<String> runners, BigDecimal stake, boolean isFlexi) {
        ReadContext resp = wapi.getEventMarkets(accessToken, eventId);
        String marketId = wapi.readMarketId(resp, "Racing Live");
        List<String> selectionIds = wapi.readSelectionIds(resp, marketId, runners);
        return api.placeExoticBet(accessToken, prodId, selectionIds, marketId, stake, isFlexi);
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

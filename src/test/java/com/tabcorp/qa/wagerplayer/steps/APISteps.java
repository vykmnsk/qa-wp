package com.tabcorp.qa.wagerplayer.steps;

import com.tabcorp.qa.common.BetType;
import com.tabcorp.qa.common.Helpers;
import com.tabcorp.qa.common.Storage;
import com.tabcorp.qa.wagerplayer.Config;
import com.tabcorp.qa.wagerplayer.api.MOBI_V2;
import com.tabcorp.qa.wagerplayer.api.WAPI;
import com.tabcorp.qa.wagerplayer.api.WagerPlayerAPI;
import cucumber.api.java8.En;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.tabcorp.qa.common.Storage.KEY.*;
import static org.assertj.core.api.Assertions.assertThat;


public class APISteps implements En {
    private static Logger log = LoggerFactory.getLogger(APISteps.class);

    private String accessToken = null;
    private BigDecimal balanceBefore = null;
    private BigDecimal balanceAfterBet = null;
    private WagerPlayerAPI api = Config.getAPI();
    private WAPI wapi = new WAPI();

    public APISteps() {
        Given("^I am logged into WP API$", () -> {
            accessToken = api.getAccessToken(Config.customerUsername(), Config.customerPassword());
            assertThat(accessToken).as("session ID / accessToken").isNotEmpty();
        });

        Given("^customer balance is at least \\$(\\d+.\\d\\d)$", (BigDecimal minBalance) -> {
            balanceBefore = api.getBalance(accessToken);
            assertThat(balanceBefore).as("balance before bet").isGreaterThanOrEqualTo(minBalance);
        });

        When("^I place a single \"([a-zA-Z]+)\" bet on the runner \"([^\"]*)\" for \\$(\\d+.\\d\\d)$",
                (String betTypeName, String runner, BigDecimal stake) -> {
                    Integer prodId = (Integer) Storage.getLast(PRODUCT_IDS);
                    Object resp = wapi.getEventMarkets((String) Storage.getLast(EVENT_IDS));  // this is always WAPI.
                    Map<WAPI.KEY, String> sel = wapi.readSelection(resp, runner, prodId);

                    Object response;
                    switch (betTypeName.toUpperCase()) {
                        case "WIN":
                            response = api.placeSingleWinBet(accessToken, prodId,
                                    sel.get(WagerPlayerAPI.KEY.MPID), sel.get(WagerPlayerAPI.KEY.WIN_PRICE), stake);
                            break;
                        case "PLACE":
                            response = api.placeSinglePlaceBet(accessToken, prodId,
                                    sel.get(WagerPlayerAPI.KEY.MPID), sel.get(WagerPlayerAPI.KEY.PLACE_PRICE), stake);
                            break;
                        case "EACHWAY":
                            response = api.placeSingleEachwayBet(accessToken, prodId,
                                    sel.get(WagerPlayerAPI.KEY.MPID), sel.get(WagerPlayerAPI.KEY.WIN_PRICE), sel.get(WagerPlayerAPI.KEY.PLACE_PRICE), stake);
                            break;
                        default:
                            throw new RuntimeException("Unknown BetTypeName=" + betTypeName);
                    }
                    List betIds = wapi.readBetId(response);
                    log.info("Bet IDs=" + betIds.toString());
                    balanceAfterBet = api.readNewBalance(response);
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
                    Integer prodId = (Integer) Storage.getLast(Storage.KEY.PRODUCT_IDS);
                    List<String> eventIds = (List<String>) Storage.get(EVENT_IDS);
                    Object response = placeExoticBetMultEvents(eventIds, prodId, runners, stake, isFlexi);
                    balanceAfterBet = api.readNewBalance(response);
                });

        When("^I place a multi bet \"([^\"]*)\" on the runners \"([^\"]*)\" for \\$(\\d+.\\d\\d) with flexi as \"([^\"]*)\"$",
                (String multiType, String runnersCSV, BigDecimal stake, String flexi) -> {
                    assertThat(multiType.toUpperCase()).as("Multi TypeName input")
                            .isIn("DOUBLE", "TREBLE", "DOUBLES", "TRIXIE", "PATENT", "4-FOLD", "TREBLES", "YANKEE",
                                    "LUCKY 15", "5-FOLD", "4-FOLDS", "CANADIAN", "LUCKY 31");
                    boolean isFlexi = "Y".equalsIgnoreCase(flexi);
                    List<String> runners = Helpers.extractCSV(runnersCSV);
                    List<Integer> prodIds = (List<Integer>) Storage.get(PRODUCT_IDS);
                    List<String> eventIds = (List<String>) Storage.get(EVENT_IDS);

                    Object response = placeMultiBets(multiType, eventIds, prodIds, runners, stake, isFlexi);
                    List betIds = wapi.readBetId(response);
                    log.info("Bet IDs=" + betIds.toString());
                    balanceAfterBet = api.readNewBalance(response);
                });

        When("^I place \"([^\"]*)\" multi bet \"([^\"]*)\" on the runners \"([^\"]*)\" for \\$(\\d+.\\d\\d)$",
                (String betTypeName, String multiType, String runnersCVS, BigDecimal stake) -> {
                    List<String> runners = Helpers.extractCSV(runnersCVS);
                    Integer prodId = (Integer) Storage.getLast(Storage.KEY.PRODUCT_IDS);
                    List<String> eventIds = (List<String>) Storage.get(EVENT_IDS);
                    assertThat(eventIds.size()).isEqualTo(runners.size());

                    List<Map<WAPI.KEY, String>> selections = new ArrayList();
                    for (int i = 0; i < runners.size(); i++) {
                        Object marketsResponse = wapi.getEventMarkets(eventIds.get(i));
                        Map<WAPI.KEY, String> sel = wapi.readSelection(marketsResponse, runners.get(i), prodId);
                        selections.add(sel);
                    }

                    betTypeName = Helpers.toTitleCase(betTypeName);
                    Integer betTypeId = BetType.valueOf(betTypeName).id;

                    Object betResponse;
                    MOBI_V2 mobi_v2 = new MOBI_V2();
                    betResponse = mobi_v2.placeMultiBet(accessToken, prodId,
                            selections, betTypeId, multiType, stake);
                    balanceAfterBet = api.readNewBalance(betResponse);

                });

        Then("^customer balance is decreased by \\$(\\d+\\.\\d\\d)$", (String diffText) -> {
            BigDecimal diff = new BigDecimal(diffText);
            assertThat(Helpers.roundOff(balanceBefore.subtract(balanceAfterBet))).isEqualTo(Helpers.roundOff(diff));
        });

        Then("^customer balance is increased by \\$(\\d+.\\d\\d)$", (String payoutText) -> {
            BigDecimal payout = new BigDecimal(payoutText);
            BigDecimal balanceAfterSettle = api.getBalance(accessToken);
            assertThat(Helpers.roundOff(balanceAfterSettle.subtract(balanceAfterBet))).isEqualTo(Helpers.roundOff(payout));
        });

        Then("^customer balance is not changed$", () -> {
            BigDecimal balanceAfterSettle = api.getBalance(accessToken);
            assertThat(Helpers.roundOff(balanceAfterSettle)).isEqualTo(Helpers.roundOff(balanceAfterBet));
        });

    }

    private BigDecimal oneEventExoticBetStep(String betTypeName, String runnersCSV, BigDecimal stake, boolean isFlexi) {
        assertThat(betTypeName.toUpperCase()).as("Exotic BetTypeName input")
                .isIn("FIRST FOUR", "TRIFECTA", "EXACTA", "QUINELLA", "EXOTIC");
        List<String> runners = Helpers.extractCSV(runnersCSV);
        Integer prodId = (Integer) Storage.getLast(Storage.KEY.PRODUCT_IDS);
        String eventId = (String) Storage.getLast(Storage.KEY.EVENT_IDS);
        Object response = placeExoticBetOneEvent(eventId, prodId, runners, stake, isFlexi);
        return api.readNewBalance(response);
    }

    private Object placeExoticBetOneEvent(String eventId, Integer prodId, List<String> runners, BigDecimal stake, boolean isFlexi) {
        Object resp = wapi.getEventMarkets(eventId);
        String marketId = wapi.readMarketId(resp, "Racing Live");
        List<String> selectionIds = wapi.readSelectionIds(resp, marketId, runners);
        return api.placeExoticBet(accessToken, prodId, selectionIds, marketId, stake, isFlexi);
    }

    private Object placeExoticBetMultEvents(List<String> eventIds, Integer prodId, List<String> runners, BigDecimal stake, boolean isFlexi) {
        List<String> marketIds = new ArrayList<>();
        List<String> selectionIds = new ArrayList<>();

        assertThat(eventIds.size()).as("Events count must match Runners count").isEqualTo(runners.size());
        for (int i = 0; i < runners.size(); i++) {
            Object marketsResponse = wapi.getEventMarkets(eventIds.get(i));
            String marketId = wapi.readMarketId(marketsResponse, "Racing Live");
            marketIds.add(marketId);
            String selId = wapi.readSelectionId(marketsResponse, marketId, runners.get(i));
            selectionIds.add(selId);
        }
        return wapi.placeExoticBetMultiMarkets(accessToken, prodId, selectionIds, marketIds, stake, isFlexi);
    }

    private Object placeMultiBets(String multiType, List<String> eventIds, List<Integer> prodIds, List<String> runners, BigDecimal stake, boolean isFlexi) {
        String uuid = wapi.addSelectionsToMulti(accessToken, eventIds, prodIds, runners, multiType);
        return wapi.placeAMultiBet(accessToken, uuid, stake, isFlexi);
    }

}

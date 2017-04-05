package com.tabcorp.qa.wagerplayer.steps;

import com.tabcorp.qa.common.Helpers;
import com.tabcorp.qa.common.Storage;
import com.tabcorp.qa.wagerplayer.Config;
import com.tabcorp.qa.wagerplayer.api.WAPI;
import com.tabcorp.qa.wagerplayer.api.WagerPlayerAPI;
import cucumber.api.java8.En;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.tabcorp.qa.common.Storage.KEY.EVENT_IDS;
import static com.tabcorp.qa.common.Storage.KEY.PRODUCT_ID;
import static org.assertj.core.api.Assertions.assertThat;


public class APISteps implements En {

    private String accessToken = null;
    private BigDecimal balanceBefore = null;
    private BigDecimal balanceAfterBet = null;
    private WagerPlayerAPI api = Config.getAPI();
    private WAPI wapi = new WAPI();

    public APISteps() {
        Given("^I am logged into WP API$", () -> {
            accessToken = Config.getAPI().getAccessToken(Config.customerUsername(), Config.customerPassword());
            assertThat(accessToken).as("session ID / accessToken").isNotEmpty();
        });

        Given("^customer balance is at least \\$(\\d+.\\d\\d)$", (BigDecimal minBalance) -> {
            balanceBefore = Config.getAPI().getBalance(accessToken);
            assertThat(balanceBefore).as("balance before bet").isGreaterThanOrEqualTo(minBalance);
        });

        When("^I place a single \"([a-zA-Z]+)\" bet on the runner \"([^\"]*)\" for \\$(\\d+.\\d\\d)$",
                (String betTypeName, String runner, BigDecimal stake) -> {
                    Integer prodId = (Integer) Storage.get(PRODUCT_ID);
                    if (null == wapi) wapi = new WAPI();
                    Object resp = wapi.getEventMarkets((String) Storage.getLast(EVENT_IDS));  // this is always WAPI.
                    Map<WAPI.KEY, String> sel = WAPI.readSelection(resp, runner, prodId);

                    switch (betTypeName.toUpperCase()) {
                        case "WIN":
                            Config.getAPI().placeSingleWinBet(accessToken, prodId,
                                    sel.get(WagerPlayerAPI.KEY.MPID), sel.get(WagerPlayerAPI.KEY.WIN_PRICE), stake);
                            break;
                        case "PLACE":
                            Config.getAPI().placeSinglePlaceBet(accessToken, prodId,
                                    sel.get(WagerPlayerAPI.KEY.MPID), sel.get(WagerPlayerAPI.KEY.PLACE_PRICE), stake);
                            break;
                        case "EACHWAY":
                            Config.getAPI().placeSingleEachwayBet(accessToken, prodId,
                                    sel.get(WagerPlayerAPI.KEY.MPID), sel.get(WagerPlayerAPI.KEY.WIN_PRICE), sel.get(WagerPlayerAPI.KEY.PLACE_PRICE), stake);
                            break;
                        default:
                            throw new RuntimeException("Unknown BetTypeName=" + betTypeName);
                    }
                    balanceAfterBet = Config.getAPI().getBalance(accessToken);
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
                    Integer prodId = (Integer) Storage.get(Storage.KEY.PRODUCT_ID);
                    List<String> eventIds = (List<String>) Storage.get(EVENT_IDS);
                    Object response = placeExoticBetMultEvents(eventIds, prodId, runners, stake, isFlexi);
                    balanceAfterBet = api.readNewBalance(response);
                });

        Then("^customer balance is decreased by \\$(\\d+\\.\\d\\d)$", (String diffText) -> {
            BigDecimal diff = new BigDecimal(diffText);
            assertThat(Helpers.roundOff(balanceBefore.subtract(balanceAfterBet))).isEqualTo(Helpers.roundOff(diff));
        });

        Then("^customer balance is increased by \\$(\\d+.\\d\\d)$", (String payoutText) -> {
            BigDecimal payout = new BigDecimal(payoutText);
            BigDecimal balanceAfterSettle = Config.getAPI().getBalance(accessToken);
            assertThat(Helpers.roundOff(balanceAfterSettle.subtract(balanceAfterBet))).isEqualTo(Helpers.roundOff(payout));
        });

        Then("^customer balance is not changed$", () -> {
            BigDecimal balanceAfterSettle = Config.getAPI().getBalance(accessToken);
            assertThat(Helpers.roundOff(balanceAfterSettle)).isEqualTo(Helpers.roundOff(balanceAfterBet));
        });

    }

    private BigDecimal oneEventExoticBetStep(String betTypeName, String runnersCSV, BigDecimal stake, boolean isFlexi) {
        assertThat(betTypeName.toUpperCase()).as("Exotic BetTypeName input")
                .isIn("FIRST FOUR", "TRIFECTA", "EXACTA", "QUINELLA", "EXOTIC");
        List<String> runners = Helpers.extractCSV(runnersCSV);
        Integer prodId = (Integer) Storage.get(Storage.KEY.PRODUCT_ID);
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

}

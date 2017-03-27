package com.tabcorp.qa.wagerplayer.steps;

import com.tabcorp.qa.common.Helpers;
import com.tabcorp.qa.common.Storage;
import com.tabcorp.qa.wagerplayer.Config;
import com.tabcorp.qa.wagerplayer.api.WAPI;
import com.tabcorp.qa.wagerplayer.api.WagerPlayerAPI;
import cucumber.api.java8.En;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.tabcorp.qa.common.Storage.KEY.*;
import static org.assertj.core.api.Assertions.assertThat;


public class APISteps implements En {

    private String accessToken = null;
    private BigDecimal balanceBefore = null;
    private BigDecimal balanceAfterBet = null;
    private WAPI wapi = null;

    public APISteps() {
        Given("^I am logged into WP API$", () -> {
            accessToken = Config.getAPI().getAccessToken(Config.customerUsername(),Config.customerPassword());
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
                    Object resp = wapi.getEventMarkets((String) Storage.get(EVENT_ID));  // this is always WAPI.
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
                (String betTypeName, String runner, BigDecimal stake) -> {
                    Integer prodId = (Integer) Storage.get(Storage.KEY.PRODUCT_ID);
                    List<String> runners = new ArrayList<>(Arrays.asList(runner.split(",")));

                    if (null == wapi) wapi = new WAPI();
                    Object resp = wapi.getEventMarkets((String) Storage.get(Storage.KEY.EVENT_ID));

                    String marketId = WAPI.readMarketId(resp, "Racing Live");
                    List<String> selectionIds = WAPI.readSelectionIds(resp, marketId, runners);

                    Object response;
                    switch (betTypeName.toUpperCase()) {
                        case "TRIFECTA":
                        case "EXACTA":
                        case "QUINELLA":
                            response = WAPI.placeExoticBet(accessToken, prodId,
                                    selectionIds , marketId, stake);
                            break;
                        default:
                            throw new RuntimeException("Unknown BetTypeName=" + betTypeName);
                    }
                    balanceAfterBet = WAPI.readNewBalance(response);
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

}

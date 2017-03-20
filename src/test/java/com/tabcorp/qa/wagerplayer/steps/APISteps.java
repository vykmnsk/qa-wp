package com.tabcorp.qa.wagerplayer.steps;

import com.tabcorp.qa.common.Storage;
import com.tabcorp.qa.common.Storage.KEY;
import com.tabcorp.qa.wagerplayer.Config;
import com.tabcorp.qa.wagerplayer.api.WAPI;
import cucumber.api.java8.En;
import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


public class APISteps implements En {

    private String accessToken = null;
    private BigDecimal balanceBefore = null;
    private BigDecimal balanceAfterBet = null;
    private WAPI wapi = new WAPI();

    public APISteps() {
        Given("^I am logged into WP API$", () -> {
            accessToken = Config.getAPI().getAccessToken(Config.customerUsername(),Config.customerPassword());
            assertThat(accessToken).as("session ID / accessToken").isNotEmpty();
        });

        Given("^customer balance is at least \\$(\\d+.\\d\\d)$", (BigDecimal minBalance) -> {
            balanceBefore = Config.getAPI().getBalance(accessToken);
            assertThat(balanceBefore).as("balance before bet").isGreaterThanOrEqualTo(minBalance);
        });

        When("^I place a single \"([^\"]*)\" bet on the runner \"([^\"]*)\" for \\$(\\d+.\\d\\d)$",
                (String betTypeName, String runner, BigDecimal stake) -> {
                    Integer prodId = (Integer) Storage.get(KEY.PRODUCT_ID);
                    Object resp = wapi.getEventMarkets((String) Storage.get(KEY.EVENT_ID));  // this is always WAPI.
                    Map<WAPI.KEY, String> sel = WAPI.readSelection(resp, runner, prodId);

                    switch (betTypeName.toUpperCase()) {
                        case "WIN":
                            Config.getAPI().placeSingleWinBet(accessToken, prodId,
                                    sel.get(WAPI.KEY.MPID), sel.get(WAPI.KEY.WIN_PRICE), stake);
                            break;
                        case "PLACE":
                            WAPI.placeBetSinglePlace(accessToken, prodId,
                                    sel.get(WAPI.KEY.MPID), sel.get(WAPI.KEY.PLACE_PRICE), stake);
                            break;
                        case "EACHWAY":
                            WAPI.placeBetSingleEW(accessToken, prodId,
                                    sel.get(WAPI.KEY.MPID), sel.get(WAPI.KEY.WIN_PRICE), sel.get(WAPI.KEY.PLACE_PRICE), stake);
                            break;
                        default:
                            throw new RuntimeException("Unknown BetTypeName=" + betTypeName);
                    }
                    balanceAfterBet = Config.getAPI().getBalance(accessToken);
                });


        Then("^customer balance is decreased by \\$(\\d+\\.\\d\\d)$", (String diffText) -> {
            BigDecimal diff = new BigDecimal(diffText);
            assertThat(balanceBefore.subtract(balanceAfterBet).stripTrailingZeros()).isEqualTo(diff.stripTrailingZeros());
        });

        Then("^customer balance is increased by \\$(\\d+.\\d\\d)$", (String payoutText) -> {
            BigDecimal payout = new BigDecimal(payoutText);
            BigDecimal balanceAfterSettle = Config.getAPI().getBalance(accessToken);
            assertThat(balanceAfterSettle.subtract(balanceAfterBet).stripTrailingZeros()).isEqualTo(payout.stripTrailingZeros());
        });

        Then("^customer balance is not changed$", () -> {
            BigDecimal balanceAfterSettle = Config.getAPI().getBalance(accessToken);
            assertThat(balanceAfterSettle.stripTrailingZeros()).isEqualTo(balanceAfterBet.stripTrailingZeros());
        });
    }

}

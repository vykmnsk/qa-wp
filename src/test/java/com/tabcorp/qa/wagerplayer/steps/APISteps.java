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

    public APISteps() {
        Given("^I am logged into WP API$", () -> {
            accessToken = Config.getAPI().getAccessToken(Config.customerUsername(),Config.customerPassword());
            assertThat(accessToken).as("session ID / accessToken").isNotEmpty();
        });

        Given("^customer balance is at least \\$(\\d+.\\d\\d)$", (BigDecimal minBalance) -> {
            balanceBefore = WAPI.getBalance(accessToken);
            assertThat(balanceBefore).as("balance before bet").isGreaterThanOrEqualTo(minBalance);
        });

        When("^I place a single \"(WIN|PLACE|EACHWAY)\" bet on the runner \"([^\"]*)\" for \\$(\\d+.\\d\\d)$",
                (String betTypeName, String runner, BigDecimal stake) -> {
                    Integer prodId = (Integer) Storage.get(KEY.PRODUCT_ID);
                    Object resp = WAPI.getEventMarkets(accessToken, (String) Storage.get(KEY.EVENT_ID));
                    Map<WAPI.KEY, String> sel = WAPI.readSelection(resp, runner, prodId);

                    Object response;
                    switch (betTypeName) {
                        case "WIN":
                            response = WAPI.placeBetSingleWin(accessToken, prodId,
                                    sel.get(WAPI.KEY.MPID), sel.get(WAPI.KEY.WIN_PRICE), stake);
                            break;
                        case "PLACE":
                            response = WAPI.placeBetSinglePlace(accessToken, prodId,
                                    sel.get(WAPI.KEY.MPID), sel.get(WAPI.KEY.PLACE_PRICE), stake);
                            break;
                        case "EACHWAY":
                            response = WAPI.placeBetSingleEW(accessToken, prodId,
                                    sel.get(WAPI.KEY.MPID), sel.get(WAPI.KEY.WIN_PRICE), sel.get(WAPI.KEY.PLACE_PRICE), stake);
                            break;
                        default:
                            throw new RuntimeException("Unknown BetTypeName=" + betTypeName);
                    }
                    balanceAfterBet = WAPI.readNewBalance(response);
                });


        Then("^customer balance is decreased by \\$(\\d+\\.\\d\\d)$", (String diffText) -> {
            BigDecimal diff = new BigDecimal(diffText);
            assertThat(balanceBefore.subtract(balanceAfterBet)).isEqualTo(diff);
        });

        Then("^customer balance is increased by \\$(\\d+.\\d\\d)$", (String payoutText) -> {
            BigDecimal payout = new BigDecimal(payoutText);
            BigDecimal balanceAfterSettle = WAPI.getBalance(accessToken);
            assertThat(balanceAfterSettle.subtract(balanceAfterBet)).isEqualTo(payout);
        });

        Then("^customer balance is not changed$", () -> {
            BigDecimal balanceAfterSettle = WAPI.getBalance(accessToken);
            assertThat(balanceAfterSettle.stripTrailingZeros()).isEqualTo(balanceAfterBet.stripTrailingZeros());
        });
    }

}

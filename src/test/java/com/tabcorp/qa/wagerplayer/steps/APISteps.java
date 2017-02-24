package com.tabcorp.qa.wagerplayer.steps;

import com.tabcorp.qa.wagerplayer.api.WAPI;
import cucumber.api.java8.En;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;


public class APISteps implements En {

    private String wapiSessionId = null;
    private BigDecimal balanceBefore = null;
    private BigDecimal balanceAfter = null;

    public APISteps() {
        Given("^I am logged in WAPI$", () -> {
            wapiSessionId = WAPI.login();
            assertThat(wapiSessionId).as("session ID").isNotEmpty();
        });

        Given("^customer balance is at least \\$(\\d+.\\d\\d)$", (BigDecimal minBalance) -> {
            balanceBefore = WAPI.getBalance(wapiSessionId);
            assertThat(balanceBefore).as("balance before bet").isGreaterThanOrEqualTo(minBalance);
        });

        When("^I place a single Win bet on the first runner for \\$(\\d+\\.\\d+)$", (BigDecimal stake) -> {
            //TODO fetch these from UI or Feed
            String prodId = "280";
            String mpid = "150342497";
            String winPrice = "62.680";
            Object response = WAPI.placeBetSingleWin(wapiSessionId, prodId, mpid, winPrice, stake);
            balanceAfter = WAPI.readNewBalance(response);
        });

        Then("^customer balance changes$", () -> {
            assertThat(balanceAfter).as("balance after bet").isNotNull().isNotEqualTo(balanceBefore);
        });

    }
}

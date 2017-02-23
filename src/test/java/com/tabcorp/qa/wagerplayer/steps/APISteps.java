package com.tabcorp.qa.wagerplayer.steps;

import com.tabcorp.qa.wagerplayer.api.WAPI;
import cucumber.api.java8.En;
import org.assertj.core.api.Assertions;

import java.math.BigDecimal;


public class APISteps implements En {

    String wapiSessionId = null;

    public APISteps() {
        Given("^I login to WAPI$", () -> {
            wapiSessionId = WAPI.login();
        });

        Then("^I get WAPI session ID$", () -> {
            Assertions.assertThat(wapiSessionId).as("session ID").isNotEmpty();
        });

        Given("^Customer balance is greater than \\$(\\d+.\\d\\d)$", (BigDecimal minBal) -> {
            WAPI.verifyBalanceGreaterThan(wapiSessionId, minBal);
        });

    }
}

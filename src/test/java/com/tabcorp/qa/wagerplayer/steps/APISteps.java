package com.tabcorp.qa.wagerplayer.steps;

import com.tabcorp.qa.wagerplayer.api.WAPI;
import cucumber.api.java8.En;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;


public class APISteps implements En {

    String wapiSessionId = null;

    public APISteps() {
        Given("^I am logged in WAPI$", () -> {
            wapiSessionId = WAPI.login();
            assertThat(wapiSessionId).as("session ID").isNotEmpty();
        });

        Given("^Customer balance is greater than \\$(\\d+.\\d\\d)$", (BigDecimal minBal) -> {
            WAPI.verifyBalanceGreaterThan(wapiSessionId, minBal);
        });

    }
}

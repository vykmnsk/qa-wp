package com.tabcorp.qa.wagerplayer.steps;

import com.tabcorp.qa.wagerplayer.api.WAPI;
import cucumber.api.java8.En;
import org.assertj.core.api.Assertions;


public class APISteps implements En {

    String wapiSessionId = null;

    public APISteps() {
        Given("^I login to WAPI$", () -> {
            wapiSessionId = WAPI.login();
        });

        Then("^I get WAPI session ID$", () -> {
            Assertions.assertThat(wapiSessionId).as("session ID").isNotEmpty();
        });
    }

}

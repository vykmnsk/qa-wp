package com.tabcorp.qa.wagerplayer.steps;

import com.jayway.jsonpath.JsonPath;
import com.tabcorp.qa.wagerplayer.Config;
import com.tabcorp.qa.wagerplayer.api.MOBI_V2;
import cucumber.api.java8.En;
import org.assertj.core.api.Assertions;

import java.math.BigDecimal;
import java.util.List;

public class PlaceBetsSteps implements En {

    private String userAccessToken;
    private Object document;

    public PlaceBetsSteps() {
        Given("^I am a Sunbets customer$", () -> {
            userAccessToken = MOBI_V2.getAccessToken(Config.moby_V2_USERNAME(),Config.moby_V2_Password());
        });

        When("^I place a single Win bet for \\$(\\d+.\\d\\d)$", (BigDecimal betPrice) -> {
            document = MOBI_V2.placeSingleWinBet(userAccessToken,"single",1, "150342497","2.8", betPrice );
        });

        Then("^I should receive a success message$",() -> {
            List<String> betSlipID = JsonPath.read(document, "$..betslip");
            Assertions.assertThat(betSlipID.size()).isGreaterThan(0);
        });
    }

}
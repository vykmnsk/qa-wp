package com.tabcorp.qa.wagerplayer.steps;

import com.tabcorp.qa.wagerplayer.api.WAPI;
import com.tabcorp.qa.wagerplayer.pages.HeaderPage;
import com.tabcorp.qa.wagerplayer.pages.LiabilityPage;
import cucumber.api.java8.En;
import org.assertj.core.api.Assertions;

import java.math.BigDecimal;
import java.util.List;

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
            //TODO fix hardcoding
            String prodId = "280";
            List<String> selection = readSelectionDataFromUI(prodId).get(0);
            String mpid = selection.get(0);
            String winPrice = selection.get(1);
            Object response = WAPI.placeBetSingleWin(wapiSessionId, prodId, mpid, winPrice, stake);
            balanceAfter = WAPI.readNewBalance(response);
        });

        Then("^customer balance is decreased by \\$(\\d+\\.\\d\\d)$", (String diffText) -> {
            BigDecimal diff = new BigDecimal(diffText);
            assertThat(balanceBefore.subtract(balanceAfter)).isEqualTo(diff);
        });
    }

    private static List<List<String>> readSelectionDataFromUI(String prodId) {
        HeaderPage header = new HeaderPage();
        LiabilityPage liabilityPage = header.navigateToF5();
        return liabilityPage.getSelections(prodId);
    }
}

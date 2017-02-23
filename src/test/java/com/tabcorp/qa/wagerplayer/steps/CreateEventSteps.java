package com.tabcorp.qa.wagerplayer.steps;

import com.tabcorp.qa.wagerplayer.pages.*;
import cucumber.api.DataTable;
import cucumber.api.java8.En;
import org.assertj.core.api.Assertions;

import java.util.List;
import java.util.Map;

public class CreateEventSteps implements En {

    NewEventPage newEvtPage;
    MarketsPage marketsPage;
    LiabilityPage liabilityPage;
    HeaderPage header;


    public CreateEventSteps() {

        Given("^I am logged in and on Home Page$", () -> {
            LoginPage lp = new LoginPage();
            lp.load();
            HomePage hp = lp.enterValidCredentials();
            hp.verifyLoaded();
        });
        When("^I enter specifics category \"([^\"]*)\" and subcategory \"([^\"]*)\"$", (String category, String subcategory) -> {
            header = new HeaderPage();
            header.pickCategories(category, subcategory);
        });


        When("^I enter event details with (\\d+) horses, current 'show time' and 'event date/time' in (\\d+) minutes with data$",
                (Integer numberOfRunners, Integer inMinutes, DataTable table) -> {
                    Map<String, String> evt = table.asMap(String.class, String.class);
                    Assertions.assertThat(evt.keySet()).as("event details").isNotEmpty();

                    marketsPage = newEvtPage.enterEventDetails(
                            inMinutes,
                            evt.get("event name"),
                            evt.get("bet in run type"),
                            evt.get("create market"),
                            numberOfRunners
                    );
                });
        Then("^I see Create Market page$", () -> {
            marketsPage.verifyLoaded();
        });

        When("^I enter random prices matching (\\d+)$", (Integer count) -> {
            marketsPage.enterPrices(count);
        });

        When("^I update race number to \"([^\"]*)\"$", (String num) -> {
            marketsPage.showMarketManagement();
            marketsPage.updateRaceNumber(num);
        });

        When("^I enable \"([^\"]*)\" product settings$", (String name, DataTable table) -> {
            List<List<String>> settings = table.raw();
            marketsPage.enableProductSettings(name, settings);
        });

        When("^I enter market details$", (DataTable table) -> {
            Map<String, String> mkt = table.asMap(String.class, String.class);
            marketsPage.showMarketDetails();
            boolean isLive = mkt.get("Market Status").equals("Live");
            boolean isEW = mkt.get("E/W").equals("yes");
            marketsPage.enterMarketDetail(
                    isLive,
                    mkt.get("Bets Allowed"),
                    mkt.get("Bets Allowed Place"),
                    mkt.get("Place Fraction"),
                    mkt.get("No of Places"),
                    isEW);
        });

        Then("^I can see success status with message \"([^\"]*)\"$", (String msg) -> {
            marketsPage.verifySuccessStatus(msg);
        });

        Then("^I see New Event page is loaded$", () -> {
            newEvtPage = new NewEventPage();
            newEvtPage.load();
        });
        Then("^event status is \"([^\"]*)\"$", (String expectedStatus) -> {
            marketsPage.verifyMarketStatus(expectedStatus);

        });
        When("^i collect mpid of selections$", () -> {
            liabilityPage = header.navigateToF5();
            List<String> mpids = liabilityPage.getMarketPriceIds();
        });

    }

}

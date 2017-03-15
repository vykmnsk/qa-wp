package com.tabcorp.qa.wagerplayer.steps;

import com.tabcorp.qa.common.Helpers;
import com.tabcorp.qa.common.Storage;
import com.tabcorp.qa.wagerplayer.pages.*;
import cucumber.api.DataTable;
import cucumber.api.java8.En;
import org.assertj.core.api.Assertions;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CreateEventSteps implements En {

    private NewEventPage newEvtPage;
    private MarketsPage marketsPage;
    private HeaderPage header;
    private SettlementPage settlementPage;
    private String category = null;
    private String subcategory = null;
    private String eventName = null;
    private int raceNumber = 1;


    public CreateEventSteps() {

        Given("^I am logged into WP UI and on Home Page$", () -> {
            LoginPage lp = new LoginPage();
            lp.load();
            HomePage hp = lp.enterValidCredentials();
            hp.verifyLoaded();
        });
        When("^I enter specifics category \"([^\"]*)\" and subcategory \"([^\"]*)\"$", (String category, String subcategory) -> {
            this.subcategory = subcategory;
            this.category = category;
            header = new HeaderPage();
            header.navigateToF3(category, subcategory);
        });


        When("^I enter event details with (\\d+) runners, current 'show time' and 'event date/time' in (\\d+) minutes with data$",
                (Integer numberOfRunners, Integer inMinutes, DataTable table) -> {
                    Map<String, String> evt = table.asMap(String.class, String.class);
                    Assertions.assertThat(evt.keySet()).as("event details").isNotEmpty();
                    List<String> runners = Helpers.generateRunners("Runner_", numberOfRunners);
                    String evtBaseName = (String) Helpers.noNullGet(evt, "base name");
                    eventName = Helpers.createUniqueName(evtBaseName);
                    marketsPage = newEvtPage.enterEventDetails(
                            inMinutes,
                            eventName,
                            (String) Helpers.noNullGet(evt, "bet in run type"),
                            (String) Helpers.noNullGet(evt, "create market"),
                            runners
                    );
                });
        Then("^I see Create Market page$", () -> {
            marketsPage.verifyLoaded();
        });

        When("^I enter random prices matching (\\d+)$", (Integer count) -> {
            List<BigDecimal> prices = Helpers.generateRandomPrices(2, 100, count);
            marketsPage.enterPrices(prices);
        });

        When("^I update race number to \"(\\d+)\"$", (Integer num) -> {
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
            boolean isLive = Helpers.noNullGet(mkt, "Market Status").equals("Live");
            boolean isEW = mkt.get("E/W").equals("yes");
            marketsPage.enterMarketDetail(
                    isLive,
                    (String) Helpers.noNullGet(mkt, "Bets Allowed"),
                    (String) Helpers.noNullGet(mkt, "Bets Allowed Place"),
                    (String) Helpers.noNullGet(mkt, "Place Fraction"),
                    (String) Helpers.noNullGet(mkt, "No of Places"),
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

        When("^I create a default event with details$", (DataTable table) -> {
            Map<String, String> evt = table.asMap(String.class, String.class);
            int inMinutes = 30;
            String betInRunType = "Both Allowed";
            String createMarket = "Racing Live";

            String evtBaseName = (String) Helpers.noNullGet(evt, "base name");
            eventName = Helpers.createUniqueName(evtBaseName);
            String runnersText = (String) Helpers.noNullGet(evt, "runners");
            List<String> runners = Arrays.asList(runnersText.split(",\\s+"));

            String pricesText = (String) Helpers.noNullGet(evt, "prices");
            List<String> pricesTokens = Arrays.asList(pricesText.split(",\\s+"));
            List<BigDecimal> prices = pricesTokens.stream().map(BigDecimal::new).collect(Collectors.toList());

              newEvtPage = new NewEventPage();
              newEvtPage.load();
              marketsPage = newEvtPage.enterEventDetails(inMinutes, eventName, betInRunType, createMarket, runners);
              marketsPage.verifyLoaded();
              marketsPage.enterPrices(prices);
              marketsPage.verifySuccessStatus("Market Created");
              marketsPage.showMarketManagement();
              marketsPage.updateRaceNumber(raceNumber);
           });

        When("^I result race with the runners and positions$", (DataTable table) -> {
            Map<String, String> winners = table.asMap(String.class, String.class);
            header = new HeaderPage();
            header.pickEvent(category, subcategory, eventName);
            settlementPage = header.navigateToF6();
            settlementPage.resultRace(winners);
        });

        And("^I settle race with prices$", (DataTable table) -> {
            Map<String, String> winnerPrices = table.asMap(String.class, String.class);
            Integer prodId = (Integer) Storage.get(Storage.KEY.PRODUCT_ID);
            settlementPage.updateSettlePrices(winnerPrices, prodId);
            settlementPage.accept();
            settlementPage.settle();
        });
    }


}

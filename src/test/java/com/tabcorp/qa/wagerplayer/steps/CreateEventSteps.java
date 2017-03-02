package com.tabcorp.qa.wagerplayer.steps;

import com.tabcorp.qa.common.Helpers;
import com.tabcorp.qa.wagerplayer.pages.*;
import cucumber.api.DataTable;
import cucumber.api.java8.En;
import org.assertj.core.api.Assertions;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CreateEventSteps implements En {

    NewEventPage newEvtPage;
    MarketsPage marketsPage;
    HeaderPage header;
    int raceNumber = 1;


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
                    List<String> runners = Helpers.generateRunners("Runner_", numberOfRunners);

                    marketsPage = newEvtPage.enterEventDetails(
                            inMinutes,
                            evt.get("event name"),
                            evt.get("bet in run type"),
                            evt.get("create market"),
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

        When("^I collect mpid of selections$", () -> {
              header = new HeaderPage();
              LiabilityPage liabilityPage = header.navigateToF5();
              //Todo - get product id dynamically
              List<ArrayList> selections = liabilityPage.getSelections("280");
        });
      
        When("^I create a default event with details$", (DataTable table) -> {
              Map<String, String> evt = table.asMap(String.class, String.class);
              int inMinutes = 30;
              String betInRunType = "Both Allowed";
              String createMarket = "Racing Live";

              String evtBaseName = (String) Helpers.noNullGet(evt, "base name");
              String evtName = String.format("%d: %d - %s", raceNumber, Helpers.randomBetweenInclusive(1000, 9999), evtBaseName);
              String runnersText = (String) Helpers.noNullGet(evt, "runners");
              List<String> runners = Arrays.asList(runnersText.split(",\\s+"));

              String pricesText = (String) Helpers.noNullGet(evt, "prices");
              List<String> pricesTokens = Arrays.asList(pricesText.split(",\\s+"));
              List<BigDecimal> prices = pricesTokens.stream().map(p -> new BigDecimal(p)).collect(Collectors.toList());

              newEvtPage = new NewEventPage();
              newEvtPage.load();
              marketsPage = newEvtPage.enterEventDetails(inMinutes, evtName, betInRunType, createMarket, runners);
              marketsPage.verifyLoaded();
              marketsPage.enterPrices(prices);
              marketsPage.verifySuccessStatus("Market Created");
              marketsPage.showMarketManagement();
              marketsPage.updateRaceNumber(raceNumber);
           });
      
        When("^I settle race$", () -> {
            //Todo can remove arraylist with some dynamic code
            List<String[]> winners = new ArrayList<>();
            winners.add(new String[]{"1", "1 Runner_1"});
            winners.add(new String[]{"2", "2 Runner_2"});
            header = new HeaderPage();
            SettlementPage settlementPage = header.navigateToF6();
            settlementPage.settleRace(winners);
        });

    }

}

package com.tabcorp.qa.wagerplayer.steps;

import com.tabcorp.qa.common.BetType;
import com.tabcorp.qa.common.Helpers;
import com.tabcorp.qa.common.Storage;
import com.tabcorp.qa.common.StrictHashMap;
import com.tabcorp.qa.wagerplayer.Config;
import com.tabcorp.qa.wagerplayer.pages.*;
import cucumber.api.DataTable;
import cucumber.api.java8.En;
import org.apache.commons.lang3.math.NumberUtils;
import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateEventSteps implements En {

    private static Logger log = LoggerFactory.getLogger(CreateEventSteps.class);

    private NewEventPage newEvtPage;
    private MarketsPage marketsPage;
    private HeaderPage header;
    private SettlementPage settlementPage;
    private int raceNumber = 1;

    public CreateEventSteps() {


        When("^I enter specifics category \"([^\"]*)\" and subcategory \"([^\"]*)\"$", (String category, String subcategory) -> {
            Storage.add(Storage.KEY.CATEGORIES, category);
            Storage.add(Storage.KEY.SUBCATEGORIES, subcategory);
            header = new HeaderPage();
            header.navigateToF3(category, subcategory);
        });


        When("^I enter event details with (\\d+) runners, current 'show time' and 'event date/time' in (\\d+) minutes with data$",
                (Integer numberOfRunners, Integer inMinutes, DataTable table) -> {
                    Map<String, String> evt = table.asMap(String.class, String.class);
                    Assertions.assertThat(evt.keySet()).as("event details").isNotEmpty();
                    List<String> runners = Helpers.generateRunners("Runner_", numberOfRunners);
                    String evtBaseName = (String) Helpers.nonNullGet(evt, "base name");
                    String eventName = Helpers.createUniqueName(evtBaseName);
                    marketsPage = newEvtPage.enterEventDetails(
                            inMinutes,
                            eventName,
                            (String) Helpers.nonNullGet(evt, "bet in run type"),
                            (String) Helpers.nonNullGet(evt, "create market"),
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
            if (Config.isLuxbet()) marketsPage.setHardSoftInterimLimits();
        });

        When("^I enable \"([^\"]*)\" product settings$", (String name, DataTable table) -> {
            List<List<String>> settings = table.raw();
            marketsPage.enableProductSettings(name, settings);
        });

        When("^I enable \"([^\"]*)\" product from Cross Race Exotics table$", (String crossRaceProduct) -> {
            marketsPage.enableCrossRaceProduct(crossRaceProduct);
        });


        When("^I enter market details$", (DataTable table) -> {
            Map<String, String> mkt = table.asMap(String.class, String.class);
            marketsPage.hideMarketManagement();
            marketsPage.showMarketDetails();
            boolean isLive = Helpers.nonNullGet(mkt, "Market Status").equals("Live");
            boolean isEW = mkt.get("E/W").equalsIgnoreCase("yes");
            marketsPage.enterMarketDetail(
                    isLive,
                    (String) Helpers.nonNullGet(mkt, "Bets Allowed"),
                    (String) Helpers.nonNullGet(mkt, "Bets Allowed Place"),
                    (String) Helpers.nonNullGet(mkt, "Place Fraction"),
                    (String) Helpers.nonNullGet(mkt, "No of Places"),
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

            String evtBaseName = evt.get("base name");
            if (null == evtBaseName) evtBaseName = Config.testEventBaseName();
            String eventName = Helpers.createUniqueName(evtBaseName);

            String runnersText = (String) Helpers.nonNullGet(evt, "runners");
            List<String> runners = Helpers.extractCSV(runnersText);

            String pricesText = (String) Helpers.nonNullGet(evt, "prices");
            List<String> pricesTokens = Helpers.extractCSV(pricesText);
            List<BigDecimal> prices = pricesTokens.stream().map(BigDecimal::new).collect(Collectors.toList());

            newEvtPage = new NewEventPage();
            newEvtPage.load();
            marketsPage = newEvtPage.enterEventDetails(inMinutes, eventName, betInRunType, createMarket, runners);
            marketsPage.verifyLoaded();
            marketsPage.enterPrices(prices);
            marketsPage.verifySuccessStatus("Market Created");
            marketsPage.showMarketManagement();
            marketsPage.updateRaceNumber(raceNumber);
            if (Config.isLuxbet()) marketsPage.setHardSoftInterimLimits();
        });

        And("^I update Exotic Prices$", (DataTable table) -> {
            List<List<String>> priceData = table.raw();
            assertThat(priceData.size()).as("price table rows").isGreaterThan(0);
            for (List<String> priceRow : priceData) {
                assertThat(priceRow.size()).as("expecting for example: [STAB, Quinella, 6.50]").isEqualTo(3);
                BigDecimal priceValue = new BigDecimal(priceRow.get(2));
                assertThat(priceValue).isGreaterThan(new BigDecimal(0));
            }
            settlementPage.updateExoticPrices(priceData);
        });

        And("^I update fixed win prices \"([^\"]*)\" and place prices \"([^\"]*)\"$", (String winPricesCSV, String placePricesCSV) -> {
            List<BigDecimal> winPrices = Helpers.extractCSVPrices(winPricesCSV);
            List<BigDecimal> placePrices = Helpers.extractCSVPrices(placePricesCSV);

            String cat = (String) Storage.getLast(Storage.KEY.CATEGORIES);
            String subcat = (String) Storage.getLast(Storage.KEY.SUBCATEGORIES);
            String evName = (String) Storage.getLast(Storage.KEY.EVENT_NAMES);
            header = new HeaderPage();
            header.pickEvent(cat, subcat, evName);
            header.navigateToF5();
            LiabilityPage liabilityPage = new LiabilityPage();
            Integer prodId = (Integer) Storage.getLast(Storage.KEY.PRODUCT_IDS);
            liabilityPage.updatePrices(prodId, BetType.Win.id, winPrices);
            liabilityPage.updatePrices(prodId, BetType.Place.id, placePrices);

        });

        And("^I update fixed place prices \"([^\"]*)\"$", (String placePricesCSV) -> {
            Integer prodId = (Integer) Storage.getLast(Storage.KEY.PRODUCT_IDS);
            updatePlacePricesOnF5(placePricesCSV, prodId);
        });

        And("^I update fixed place prices \"([^\"]*)\" for the first product$", (String placePricesCSV) -> {
            Integer prodId = (Integer) Storage.getFirst(Storage.KEY.PRODUCT_IDS);
            updatePlacePricesOnF5(placePricesCSV, prodId);
        });

        When("^I scratch the runners at position\\(s\\) \"([^\"]*)\"$", (String runnerPositionsCSV) -> {
            List<String> runnerPositions = Helpers.extractCSV(runnerPositionsCSV);
            Assertions.assertThat(runnerPositions).as("Runner positions should be numbers").allMatch(NumberUtils::isNumber);
            String cat = (String) Storage.getLast(Storage.KEY.CATEGORIES);
            String subcat = (String) Storage.getLast(Storage.KEY.SUBCATEGORIES);
            String evName = (String) Storage.getLast(Storage.KEY.EVENT_NAMES);
            header = new HeaderPage();
            header.pickEvent(cat, subcat, evName);
            header.navigateToF4();
            marketsPage.scratchRunners(runnerPositions);
        });

        When("^I result race with Fixed Win prices \"([\\d.,\\s]*)\" and Place prices \"([\\d.,\\s]*)\"$", (String winPricesCSV, String placePricesCSV) -> {
            List<BigDecimal> winPrices = Helpers.extractCSVPrices(winPricesCSV);
            List<BigDecimal> placePrices = Helpers.extractCSVPrices(placePricesCSV);
            assertThat(winPrices).as(BetType.Win.name() + " prices cucumber input").isNotEmpty();
            assertThat(placePrices).as(BetType.Place.name() + " prices cucumber input").isNotEmpty();
            assertThat(winPrices.size()).as("Win and Place prices count match").isEqualTo(placePrices.size());
            settlementPage.updateFixedPrices(winPrices, placePrices);
        });

        When("^I can see the following deduction details on settlement page$", (DataTable table) -> {
            List<List<String>> deductionDetails = table.raw();
            String cat = (String) Storage.getLast(Storage.KEY.CATEGORIES);
            String subcat = (String) Storage.getLast(Storage.KEY.SUBCATEGORIES);
            String evName = (String) Storage.getLast(Storage.KEY.EVENT_NAMES);
            header = new HeaderPage();
            header.pickEvent(cat, subcat, evName);
            header.navigateToF6();
            settlementPage = new SettlementPage();
            settlementPage.verifyRunnerDeductions(deductionDetails);
        });

        When("^I result event with winners \"([^\"]*)\"$", (String winnersCSV) -> {
            Map<String, String> winnerPosMap = extractWinnersAddPositions(winnersCSV);
            resultRace(winnerPosMap);
        });

        When("^I result/settle created event race with winners \"([^\"]*)\"$", (String winnersCSV) -> {
            Map<String, String> winnerPosMap = extractWinnersAddPositions(winnersCSV);
            resultRace(winnerPosMap);
            settleRace();
        });

        When("^I result race with the runners and positions$", (DataTable table) -> {
            Map<String, String> winnerPosMap = table.asMap(String.class, String.class);
            resultRace(winnerPosMap);
        });

        When("^I result race with the runners and positions \"([^\"]*)\"$", (String winnersCSV) -> {
            // Expecting list in format [Position]:[RunnerName]. e.g. 1:Runner01
            List<String> posWinnerPairs = Helpers.extractCSV(winnersCSV);
            Map<String, String> winnerPosMap = posWinnerPairs.stream()
                    .map(pw -> Helpers.extractCSV(pw, ':'))
                    .collect(Collectors.toMap(p -> p.get(1), p -> p.get(0)));
             resultRace(winnerPosMap);
        });

        And("^I settle race$", () -> {
            settleRace();
        });

        When("^I settle race with Exotic prices \"([\\d.,\\s]*)\"$", (String pricesCSV) -> {
            parseUpdateSettlePrices(pricesCSV, BetType.Exotic);
            settleRace();
        });

        When("^I settle the race with Win prices \"([\\d.,\\s]*)\" and Place prices \"([\\d.,\\s]*)\"$", (String winPricesCSV, String placePricesCSV) -> {
            parseUpdateSettlePrices(winPricesCSV, BetType.Win);
            parseUpdateSettlePrices(placePricesCSV, BetType.Place);
            settleRace();
        });
    }

    private Map<String, String> extractWinnersAddPositions(String winnersCSV) {
        List<String> winners = Helpers.extractCSV(winnersCSV);
        Map<String, String> winnerPosMap = new StrictHashMap<>();
        for (int i = 0; i < winners.size(); i++) {
            winnerPosMap.put(winners.get(i), String.valueOf(i + 1));
        }
        return winnerPosMap;
    }

    private void updatePlacePricesOnF5(String placePricesCSV, Integer prodId) {
        List<BigDecimal> placePrices = Helpers.extractCSVPrices(placePricesCSV);
        String cat = (String) Storage.getLast(Storage.KEY.CATEGORIES);
        String subcat = (String) Storage.getLast(Storage.KEY.SUBCATEGORIES);
        String evName = (String) Storage.getLast(Storage.KEY.EVENT_NAMES);
        header = new HeaderPage();
        header.pickEvent(cat, subcat, evName);
        header.navigateToF5();
        LiabilityPage liabilityPage = new LiabilityPage();
        liabilityPage.updatePrices(Integer.valueOf(prodId), BetType.Place.id, placePrices);
    }

    private void resultRace(Map<String, String> winners) {
        header = new HeaderPage();
        String cat = (String) Storage.removeFirst(Storage.KEY.CATEGORIES);
        String subcat = (String) Storage.removeFirst(Storage.KEY.SUBCATEGORIES);
        String event = (String) Storage.removeFirst(Storage.KEY.EVENT_NAMES);
        header.pickEvent(cat, subcat, event);
        settlementPage = header.navigateToF6();
        settlementPage.resultRace(winners);
    }

    private void parseUpdateSettlePrices(String pricesCVS, BetType betType) {
        Integer prodId = (Integer) Storage.getLast(Storage.KEY.PRODUCT_IDS);
        List<BigDecimal> prices = Helpers.extractCSVPrices(pricesCVS);
        Assertions.assertThat(prices).as(betType + " prices cucumber input").isNotEmpty();
        settlementPage.updateSettlePrices(prodId, betType.id, prices);
    }

    private void settleRace() {
        settlementPage.accept();
        settlementPage.settle();
        header.deSelectSettled();
    }

}

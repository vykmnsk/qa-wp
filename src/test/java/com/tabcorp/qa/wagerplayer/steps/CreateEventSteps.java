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

    private HeaderPage header;
    private NewEventPage newEvtPage;
    private MarketsPage marketsPage;
    private SettlementPage settlementPage;
    private CategorySettingsPage catSetPage;
    private int raceNumber = 1;

    public CreateEventSteps() {


        When("^I enter specifics category \"([^\"]*)\" and subcategory \"([^\"]*)\"$", (String category, String subcategory) -> {
            Storage.add(Storage.KEY.CATEGORIES, category);
            Storage.add(Storage.KEY.SUBCATEGORIES, subcategory);
            header = new HeaderPage();
            header.navigateToF3(category, subcategory);
        });

        When("^I edit the category options settings$", (DataTable table) -> {
            StrictHashMap<String, String> settings = new StrictHashMap<>();
            settings.putAll(table.asMap(String.class, String.class));
            catSetPage = new CategorySettingsPage();
            catSetPage.load();
            catSetPage.enterSettings(
                    settings.getBoolean("Number of Places on Bet"),
                    settings.getBoolean("Recalculate Place Price")
            );
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

        When("^I (enter|update) market details$", (String action, DataTable table) -> {
            boolean isUpdate = "update".equals(action);
            StrictHashMap<String, String> mkt = new StrictHashMap<>();
            mkt.putAll(table.asMap(String.class, String.class));

            if (isUpdate) {
                Helpers.retryOnFailure(() -> header.navigateToF4(), 3, 5);
                marketsPage.showMarketDetails();
                marketsPage.updateMarketDetail(mkt.get("Place Fraction"), mkt.get("No of Places"));
            } else {
                boolean isLive = "Live".equalsIgnoreCase(mkt.get("Market Status"));
                boolean isEW = "yes".equalsIgnoreCase(mkt.get("E/W"));
                marketsPage.hideMarketManagement();
                marketsPage.showMarketDetails();
                marketsPage.enterMarketDetail(
                        isLive,
                        mkt.get("Bets Allowed"),
                        mkt.get( "Bets Allowed Place"),
                        mkt.get( "Place Fraction"),
                        mkt.get( "No of Places"),
                        isEW);
            }
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
                assertThat(priceValue).isGreaterThan(BigDecimal.ZERO);
            }
            settlementPage.updateExoticPrices(priceData);
        });

        And("^I update fixed win prices \"([^\"]*)\"$", (String winPricesCSV) -> {
            List<BigDecimal> winPrices = Helpers.extractCSVPrices(winPricesCSV);
            Integer prodId = (Integer) Storage.getLast(Storage.KEY.PRODUCT_IDS);
            reloadLastEvent();
            LiabilityPage lp = header.navigateToF5();
            lp.updatePrices(prodId, BetType.Win.id, winPrices);
        });

        And("^I update fixed place prices \"([^\"]*)\"$", (String placePricesCSV) -> {
            List<BigDecimal> placePrices = Helpers.extractCSVPrices(placePricesCSV);
            Integer prodId = (Integer) Storage.getLast(Storage.KEY.PRODUCT_IDS);
            Helpers.retryOnFailure(() -> {
                reloadLastEvent();
                LiabilityPage lp = header.navigateToF5();
                lp.updatePrices(prodId, BetType.Place.id, placePrices);
            }, 3, 5);
        });

        And("^I update fixed place prices \"([^\"]*)\" for the first product$", (String placePricesCSV) -> {
            List<BigDecimal> placePrices = Helpers.extractCSVPrices(placePricesCSV);
            Integer prodId = (Integer) Storage.getFirst(Storage.KEY.PRODUCT_IDS);
            reloadLastEvent();
            LiabilityPage lp = header.navigateToF5();
            lp.updatePrices(prodId, BetType.Place.id, placePrices);
        });

        And("^I update fixed win prices \"([^\"]*)\" and place prices \"([^\"]*)\"$", (String winPricesCSV, String placePricesCSV) -> {
            List<BigDecimal> winPrices = Helpers.extractCSVPrices(winPricesCSV);
            List<BigDecimal> placePrices = Helpers.extractCSVPrices(placePricesCSV);
            Integer prodId = (Integer) Storage.getLast(Storage.KEY.PRODUCT_IDS);
            reloadLastEvent();
            LiabilityPage lp = header.navigateToF5();
            lp.updatePrices(prodId, BetType.Win.id, winPrices);
            lp.updatePrices(prodId, BetType.Place.id, placePrices);
        });

        When("^I scratch the runners at position\\(s\\) \"([^\"]*)\"$", (String runnerPositionsCSV) -> {
            List<String> runnerPositions = Helpers.extractCSV(runnerPositionsCSV);
            Assertions.assertThat(runnerPositions).as("Runner positions should be numbers").allMatch(NumberUtils::isNumber);
            reloadLastEvent();
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
            reloadLastEvent();
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
            StrictHashMap<String, String> winnerPosMap = new StrictHashMap<>();
            for (String pairToken : posWinnerPairs) {
                List<String> pair = Helpers.extractCSV(pairToken, ':');
                winnerPosMap.put(pair.get(1), pair.get(0));
            }
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

    private void reloadLastEvent() {
        String cat = (String) Storage.getLast(Storage.KEY.CATEGORIES);
        String subcat = (String) Storage.getLast(Storage.KEY.SUBCATEGORIES);
        String evName = (String) Storage.getLast(Storage.KEY.EVENT_NAMES);
        header.pickEvent(cat, subcat, evName);
    }

    private Map<String, String> extractWinnersAddPositions(String winnersCSV) {
        List<String> winners = Helpers.extractCSV(winnersCSV);
        Map<String, String> winnerPosMap = new StrictHashMap<>();
        for (int i = 0; i < winners.size(); i++) {
            winnerPosMap.put(winners.get(i), String.valueOf(i + 1));
        }
        return winnerPosMap;
    }

    private void resultRace(Map<String, String> winners) {
        String cat = (String) Storage.removeFirst(Storage.KEY.CATEGORIES);
        String subcat = (String) Storage.removeFirst(Storage.KEY.SUBCATEGORIES);
        String event = (String) Storage.removeFirst(Storage.KEY.EVENT_NAMES);
        Helpers.retryOnFailure(() -> {
            header.pickEvent(cat, subcat, event);
            settlementPage = header.navigateToF6();
            settlementPage.resultRace(winners);
        }, 5, 3);
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

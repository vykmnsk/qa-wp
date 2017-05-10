package com.tabcorp.qa.wagerplayer.steps;

import com.tabcorp.qa.common.BetType;
import com.tabcorp.qa.common.Helpers;
import com.tabcorp.qa.common.Storage;
import com.tabcorp.qa.wagerplayer.Config;
import com.tabcorp.qa.wagerplayer.api.MOBI_V2;
import com.tabcorp.qa.wagerplayer.api.WAPI;
import com.tabcorp.qa.wagerplayer.api.WagerPlayerAPI;
import cucumber.api.java8.En;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.tabcorp.qa.common.Storage.KEY.*;
import static com.tabcorp.qa.wagerplayer.api.WagerPlayerAPI.KEY.MPID;
import static com.tabcorp.qa.wagerplayer.api.WagerPlayerAPI.KEY.PLACE_PRICE;
import static com.tabcorp.qa.wagerplayer.api.WagerPlayerAPI.KEY.WIN_PRICE;
import static org.assertj.core.api.Assertions.assertThat;


public class APISteps implements En {
    private static Logger log = LoggerFactory.getLogger(APISteps.class);

    private BigDecimal balanceBefore ;
    private BigDecimal balanceAfterBet;
    private WagerPlayerAPI api = Config.getAPI();
    private WAPI wapi = new WAPI();

    public APISteps() {
        Given("^I am logged into WP API$", () -> {
            String accessToken = api.login(Config.customerUsername(), Config.customerPassword(), Config.clientIp());
            assertThat(accessToken).as("session ID / accessToken").isNotEmpty();
            Storage.put(API_ACCESS_TOKEN, accessToken);
        });

        Given("^customer balance is at least \\$(\\d+.\\d\\d)$", (BigDecimal minBalance) -> {
            String accessToken = (String) Storage.get(API_ACCESS_TOKEN);
            balanceBefore = api.getBalance(accessToken);
            assertThat(balanceBefore).as("balance before bet").isGreaterThanOrEqualTo(minBalance);
        });

        When("^I place a single \"([a-zA-Z]+)\" bet on the runner \"([^\"]*)\" for \\$(\\d+.\\d\\d)$",
                (String betTypeName, String runner, BigDecimal stake) -> {
                    String evId = (String) Storage.getLast(EVENT_IDS);
                    Integer prodId = (Integer) Storage.getLast(PRODUCT_IDS);
                    balanceAfterBet = placeSingleBet(betTypeName, evId, prodId, runner, stake, false);
                });

        When("^I place a unfixed single \"([a-zA-Z]+)\" bet on the runner \"([^\"]*)\" for \\$(\\d+.\\d\\d)$",
                (String betTypeName, String runner, BigDecimal stake) -> {
                    String evId = (String) Storage.getLast(EVENT_IDS);
                    Integer prodId = (Integer) Storage.getLast(PRODUCT_IDS);
                    balanceAfterBet = placeSingleBet(betTypeName, evId, prodId, runner, stake, true);
                });


        When("^I place an exotic \"([^\"]*)\" bet on the runners \"([^\"]*)\" for \\$(\\d+.\\d\\d)$",
                (String betTypeName, String runnersCSV, BigDecimal stake) -> {
                    balanceAfterBet = oneEventExoticBetStep(betTypeName, runnersCSV, stake, false);
                });

        When("^I place an exotic \"([^\"]*)\" bet on the runners \"([^\"]*)\" for \\$(\\d+.\\d\\d) with flexi as \"([Y|N])\"$",
                (String betTypeName, String runnersCSV, BigDecimal stake, String flexi) -> {
                    boolean isFlexi = "Y".equalsIgnoreCase(flexi);
                    balanceAfterBet = oneEventExoticBetStep(betTypeName, runnersCSV, stake, isFlexi);
                });

        When("^I place an exotic \"([^\"]*)\" bet on the runners \"([^\"]*)\" across multiple events for \\$(\\d+.\\d\\d) with flexi as \"([^\"]*)\"$",
                (String betTypeName, String runnersCSV, BigDecimal stake, String flexi) -> {
                    assertThat(betTypeName.toUpperCase()).as("Exotic BetTypeName input")
                            .isIn("DAILY DOUBLE", "RUNNING DOUBLE", "QUADDIE");
                    List<String> runners = Helpers.extractCSV(runnersCSV);
                    boolean isFlexi = "Y".equalsIgnoreCase(flexi);
                    String accessToken = (String) Storage.get(API_ACCESS_TOKEN);
                    Integer prodId = (Integer) Storage.getLast(PRODUCT_IDS);
                    List<String> eventIds = (List<String>) Storage.get(EVENT_IDS);
                    Object response = placeExoticBetMultEvents(accessToken, eventIds, prodId, runners, stake, isFlexi);
                    balanceAfterBet = api.readNewBalance(response);
                });

        When("^I place a multi bet \"([^\"]*)\" on the runners \"([^\"]*)\" for \\$(\\d+.\\d\\d) with flexi as \"([^\"]*)\"$",
                (String multiType, String runnersCSV, BigDecimal stake, String flexi) -> {
                    assertThat(multiType.toUpperCase()).as("Multi TypeName input")
                            .isIn("DOUBLE,WIN-WIN","DOUBLE,WIN-PLACE","DOUBLE,PLACE-WIN","DOUBLE,PLACE-PLACE",
                                    "TREBLE", "DOUBLES", "DOUBLES", "TRIXIE", "PATENT", "4-FOLD", "TREBLES",
                                    "YANKEE", "LUCKY 15", "5-FOLD", "4-FOLDS", "CANADIAN", "LUCKY 31");
                    boolean isFlexi = "Y".equalsIgnoreCase(flexi);
                    List<String> runners = Helpers.extractCSV(runnersCSV);
                    String accessToken = (String) Storage.get(API_ACCESS_TOKEN);
                    List<Integer> prodIds = (List<Integer>) Storage.get(PRODUCT_IDS);
                    List<String> eventIds = (List<String>) Storage.get(EVENT_IDS);
                    String uuid = null;
                    if (multiType.contains(",")) {
                        List<String> doubleMultiTypeInfo = Helpers.extractCSV(multiType);
                        assertThat(doubleMultiTypeInfo).hasSize(2);
                        assertThat(doubleMultiTypeInfo.get(0)).isEqualToIgnoringCase("DOUBLE");
                        List<String> doubleBetTypesStr = Helpers.extractCSV(doubleMultiTypeInfo.get(1), '-');
                        assertThat(doubleBetTypesStr).hasSize(2);
                        List<BetType> doubleBetTypes = doubleBetTypesStr.stream().map(str -> BetType.fromName(str)).collect(Collectors.toList());
                        uuid = wapi.prepareSelectionsForDoubleMultiBet(accessToken, eventIds, prodIds, runners, doubleBetTypes);
                    } else {
                        uuid = wapi.prepareSelectionsForMultiBet(accessToken, eventIds, prodIds, runners, multiType);
                    }

                    Object response = wapi.placeAMultiBet(accessToken, uuid, stake, isFlexi);
                    List betIds = api.readBetIds(response);
                    log.info("Bet IDs=" + betIds.toString());
                    balanceAfterBet = api.readNewBalance(response);
                });

        When("^I place \"([^\"]*)\" multi bet \"([^\"]*)\" on the runners \"([^\"]*)\" for \\$(\\d+.\\d\\d)$",
                (String betTypeName, String multiType, String runnersCVS, BigDecimal stake) -> {
                    List<String> runners = Helpers.extractCSV(runnersCVS);
                    String accessToken = (String) Storage.get(API_ACCESS_TOKEN);
                    Integer prodId = (Integer) Storage.getLast(PRODUCT_IDS);
                    List<String> eventIds = (List<String>) Storage.get(EVENT_IDS);
                    assertThat(eventIds.size()).isEqualTo(runners.size());

                    List<Map<WAPI.KEY, String>> selections = new ArrayList();
                    for (int i = 0; i < runners.size(); i++) {
                        Object marketsResponse = wapi.getEventMarkets(accessToken, eventIds.get(i));
                        Map<WAPI.KEY, String> sel = wapi.readSelection(marketsResponse, runners.get(i), prodId);
                        selections.add(sel);
                    }

                    betTypeName = Helpers.toTitleCase(betTypeName);
                    Integer betTypeId = BetType.valueOf(betTypeName).id;

                    Object betResponse;
                    MOBI_V2 mobi_v2 = new MOBI_V2();
                    betResponse = mobi_v2.placeMultiBet(accessToken, prodId,
                            selections, betTypeId, multiType, stake);
                    balanceAfterBet = api.readNewBalance(betResponse);

                });

        Then("^customer balance is equal to \\$(\\d+\\.\\d\\d)$", (BigDecimal expectedBalance) -> {
            String accessToken = (String) Storage.get(API_ACCESS_TOKEN);
            BigDecimal actualBalance = api.getBalance(accessToken);
            assertThat(Helpers.roundOff(actualBalance)).isEqualTo(Helpers.roundOff(expectedBalance));
        });

        Then("^customer balance is decreased by \\$(\\d+\\.\\d\\d)$", (String diffText) -> {
            BigDecimal diff = new BigDecimal(diffText);
            assertThat(Helpers.roundOff(balanceBefore.subtract(balanceAfterBet))).isEqualTo(Helpers.roundOff(diff));
        });

        Then("^customer balance is increased by \\$(\\d+.\\d\\d)$", (String payoutText) -> {
            BigDecimal payout = new BigDecimal(payoutText);
            String accessToken = (String) Storage.get(API_ACCESS_TOKEN);
            class ReloadCheckCustomerBalance implements Runnable {
                public void run() {
                    BigDecimal balanceAfterSettle = api.getBalance(accessToken);
                    assertThat(Helpers.roundOff(balanceAfterSettle.subtract(balanceAfterBet))).isEqualTo(Helpers.roundOff(payout));
                }
            }
            Helpers.retryOnAssertionFailure(new ReloadCheckCustomerBalance(), 5, 2);
        });

        Then("^customer balance is not changed$", () -> {
            String accessToken = (String) Storage.get(API_ACCESS_TOKEN);
            BigDecimal balanceAfterSettle = api.getBalance(accessToken);
            assertThat(Helpers.roundOff(balanceAfterSettle)).isEqualTo(Helpers.roundOff(balanceAfterBet));
        });
    }

    private BigDecimal placeSingleBet(String betTypeName, String eventId, Integer prodId, String runner, BigDecimal stake, boolean useDefaultPrices) {
        String accessToken = (String) Storage.get(API_ACCESS_TOKEN);
        String wapiSessionId;
        if (Config.REDBOOK.equals(Config.appName())){
            wapiSessionId = wapi.login();
        } else {
            wapiSessionId = accessToken;
        }
        Object resp = wapi.getEventMarkets(wapiSessionId, eventId);
        Map<WAPI.KEY, String> selection = wapi.readSelection(resp, runner, prodId, useDefaultPrices);

        Object response;
        switch (betTypeName.toUpperCase()) {
            case "WIN":
                response = api.placeSingleWinBet(accessToken, prodId,
                        selection.get(MPID), selection.get(WIN_PRICE), stake);
                break;
            case "PLACE":
                response = api.placeSinglePlaceBet(accessToken, prodId,
                        selection.get(MPID), selection.get(PLACE_PRICE), stake);
                break;
            case "EACHWAY":
                response = api.placeSingleEachwayBet(accessToken, prodId,
                        selection.get(MPID), selection.get(WIN_PRICE), selection.get(PLACE_PRICE), stake);
                break;
            default:
                throw new RuntimeException("Unknown BetTypeName=" + betTypeName);
        }
        List betIds = api.readBetIds(response);
        log.info("Bet IDs=" + betIds.toString());
        return api.readNewBalance(response);
    }

    private BigDecimal oneEventExoticBetStep(String betTypeName, String runnersCSV, BigDecimal stake, boolean isFlexi) {
        assertThat(betTypeName.toUpperCase()).as("Exotic BetTypeName input")
                .isIn("FIRST FOUR", "TRIFECTA", "EXACTA", "QUINELLA", "EXOTIC");
        List<String> runners = Helpers.extractCSV(runnersCSV);
        String accessToken = (String) Storage.get(API_ACCESS_TOKEN);
        Integer prodId = (Integer) Storage.getLast(PRODUCT_IDS);
        String eventId = (String) Storage.getLast(EVENT_IDS);
        Object response = placeExoticBetOneEvent(accessToken, eventId, prodId, runners, stake, isFlexi);
        List betIds = api.readBetIds(response);
        log.info("Bet IDs=" + betIds.toString());
        return api.readNewBalance(response);
    }

    private Object placeExoticBetOneEvent(String accessToken, String eventId, Integer prodId, List<String> runners, BigDecimal stake, boolean isFlexi) {
        Object resp = wapi.getEventMarkets(accessToken, eventId);
        String marketId = wapi.readMarketId(resp, "Racing Live");
        List<String> selectionIds = wapi.readSelectionIds(resp, marketId, runners);
        return api.placeExoticBet(accessToken, prodId, selectionIds, marketId, stake, isFlexi);
    }

    private Object placeExoticBetMultEvents(String accessToken, List<String> eventIds, Integer prodId, List<String> runners, BigDecimal stake, boolean isFlexi) {
        assertThat(eventIds.size()).as("Events count must match Runners count").isEqualTo(runners.size());
        List<String> marketIds = new ArrayList<>();
        List<String> selectionIds = new ArrayList<>();
        for (int i = 0; i < runners.size(); i++) {
            Object marketsResponse = wapi.getEventMarkets(accessToken, eventIds.get(i));
            String marketId = wapi.readMarketId(marketsResponse, "Racing Live");
            marketIds.add(marketId);
            String selId = wapi.readSelectionId(marketsResponse, marketId, runners.get(i));
            selectionIds.add(selId);
        }
        return wapi.placeExoticBetMultiMarkets(accessToken, prodId, selectionIds, marketIds, stake, isFlexi);
    }

}

package com.tabcorp.qa.wagerplayer.steps;

import com.tabcorp.qa.common.Helpers;
import com.tabcorp.qa.common.Storage;
import com.tabcorp.qa.wagerplayer.Config;
import com.tabcorp.qa.wagerplayer.api.WAPI;
import com.tabcorp.qa.wagerplayer.api.WagerPlayerAPI;
import cucumber.api.DataTable;
import cucumber.api.java8.En;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.tabcorp.qa.common.Storage.KEY.*;
import static org.assertj.core.api.Assertions.assertThat;


public class APISteps implements En {

    private String accessToken = null;
    private BigDecimal balanceBefore = null;
    private BigDecimal balanceAfterBet = null;
    private String customerId = null;
    private WAPI wapi = null;
    private String customer_username = null;

    public APISteps() {
        Given("^I am logged into WP API$", () -> {
            accessToken = Config.getAPI().getAccessToken(Config.customerUsername(), Config.customerPassword());
            assertThat(accessToken).as("session ID / accessToken").isNotEmpty();
        });

        Given("^customer balance is at least \\$(\\d+.\\d\\d)$", (BigDecimal minBalance) -> {
            balanceBefore = Config.getAPI().getBalance(accessToken);
            assertThat(balanceBefore).as("balance before bet").isGreaterThanOrEqualTo(minBalance);
        });

        When("^I place a single \"([a-zA-Z]+)\" bet on the runner \"([^\"]*)\" for \\$(\\d+.\\d\\d)$",
                (String betTypeName, String runner, BigDecimal stake) -> {
                    Integer prodId = (Integer) Storage.get(PRODUCT_ID);
                    if (null == wapi) wapi = new WAPI();
                    Object resp = wapi.getEventMarkets((String) Storage.getLast(EVENT_IDS));  // this is always WAPI.
                    Map<WAPI.KEY, String> sel = WAPI.readSelection(resp, runner, prodId);

                    switch (betTypeName.toUpperCase()) {
                        case "WIN":
                            Config.getAPI().placeSingleWinBet(accessToken, prodId,
                                    sel.get(WagerPlayerAPI.KEY.MPID), sel.get(WagerPlayerAPI.KEY.WIN_PRICE), stake);
                            break;
                        case "PLACE":
                            Config.getAPI().placeSinglePlaceBet(accessToken, prodId,
                                    sel.get(WagerPlayerAPI.KEY.MPID), sel.get(WagerPlayerAPI.KEY.PLACE_PRICE), stake);
                            break;
                        case "EACHWAY":
                            Config.getAPI().placeSingleEachwayBet(accessToken, prodId,
                                    sel.get(WagerPlayerAPI.KEY.MPID), sel.get(WagerPlayerAPI.KEY.WIN_PRICE), sel.get(WagerPlayerAPI.KEY.PLACE_PRICE), stake);
                            break;
                        default:
                            throw new RuntimeException("Unknown BetTypeName=" + betTypeName);
                    }
                    balanceAfterBet = Config.getAPI().getBalance(accessToken);
                });

        When("^I place an exotic \"([^\"]*)\" bet on the runners \"([^\"]*)\" for \\$(\\d+.\\d\\d) with flexi as \"([Y|N| ])\"$",
                (String betTypeName, String runner, BigDecimal stake, String flexi) -> {
                    Integer prodId = (Integer) Storage.get(Storage.KEY.PRODUCT_ID);
                    List<String> runners = new ArrayList<>(Arrays.asList(runner.split(",")));
                    boolean isFlexi = "Y".equalsIgnoreCase(flexi);

                    if (null == wapi) wapi = new WAPI();
                    Object resp = wapi.getEventMarkets((String) Storage.getLast(Storage.KEY.EVENT_IDS));

                    String marketId = wapi.readMarketId(resp, "Racing Live");
                    List<String> selectionIds = wapi.readSelectionIds(resp, marketId, runners);

                    Object response;
                    switch (betTypeName.toUpperCase()) {
                        case "FIRST FOUR":
                        case "TRIFECTA":
                        case "EXACTA":
                        case "QUINELLA":
                        case "EXOTIC":
                            response = Config.getAPI().placeExoticBet(accessToken, prodId,
                                    selectionIds , marketId, stake, isFlexi);
                            break;
                        default:
                            throw new RuntimeException("Unknown BetTypeName=" + betTypeName);
                    }
                    balanceAfterBet = Config.getAPI().readNewBalance(response);
        });

        When("^I place an exotic \"([^\"]*)\" bet on the runners \"([^\"]*)\" across multiple events for \\$(\\d+.\\d\\d) with flexi as \"([^\"]*)\"$",
                (String betTypeName, String runner, BigDecimal stake, String flexi) -> {
                    Integer prodId = (Integer) Storage.get(Storage.KEY.PRODUCT_ID);
                    List<String> runners = new ArrayList<>(Arrays.asList(runner.split(",")));

                    List<String> marketIds = new ArrayList();
                    List<String> selectionIds = new ArrayList();
                    List<String> eventIds = (List<String>) Storage.get(EVENT_IDS);

                    if (null == wapi) wapi = new WAPI();
                    assertThat(eventIds.size()).isEqualTo(runners.size());

                    for (int i = 0; i<runners.size(); i++) {
                        Object marketsResponse = wapi.getEventMarkets(eventIds.get(i));
                        String marketId = wapi.readMarketId(marketsResponse, "Racing Live");
                        marketIds.add(marketId);
                        String selId = wapi.readSelectionId(marketsResponse, marketId, runners.get(i));
                        selectionIds.add(selId);
                    }

                    Object betResponse;
                    switch (betTypeName.toUpperCase()) {
                        case "DAILY DOUBLE":
                        case "RUNNING DOUBLE":
                        case "QUADDIE":
                            betResponse = wapi.placeExoticBetOnMultipleEvents(accessToken, prodId,
                                    selectionIds , marketIds, stake, flexi);
                            break;
                        default:
                            throw new RuntimeException("Unknown BetTypeName=" + betTypeName);
                    }
                    balanceAfterBet = Config.getAPI().readNewBalance(betResponse);
                });

        Then("^customer balance is decreased by \\$(\\d+\\.\\d\\d)$", (String diffText) -> {
            BigDecimal diff = new BigDecimal(diffText);
            assertThat(Helpers.roundOff(balanceBefore.subtract(balanceAfterBet))).isEqualTo(Helpers.roundOff(diff));
        });

        Then("^customer balance is increased by \\$(\\d+.\\d\\d)$", (String payoutText) -> {
            BigDecimal payout = new BigDecimal(payoutText);
            BigDecimal balanceAfterSettle = Config.getAPI().getBalance(accessToken);
            assertThat(Helpers.roundOff(balanceAfterSettle.subtract(balanceAfterBet))).isEqualTo(Helpers.roundOff(payout));
        });

        Then("^customer balance is not changed$", () -> {
            BigDecimal balanceAfterSettle = Config.getAPI().getBalance(accessToken);
            assertThat(Helpers.roundOff(balanceAfterSettle)).isEqualTo(Helpers.roundOff(balanceAfterBet));
        });

        When("^I post customer specifics to create new customer$", (DataTable table) -> {
            Map<String, String> cust = table.asMap(String.class, String.class);

            String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
            String username = "AutoUser" + timeStamp;
            this.customer_username = username;

            if (null == wapi) wapi = new WAPI();
            String successMsg = wapi.createNewCustomer(
                    username,
                    cust
            );
            assertThat(successMsg.toString()).isEqualTo("Customer Created");
        });

        Then("^I verify a new customer created with AML status \"([^\"]*)\" or \"([^\"]*)\"$", (String amlOne, String amlTwo) -> {
            if (null == wapi) wapi = new WAPI();

            String amlStatus = wapi.verifyAmlStatus((customer_username).replaceAll("\\[","").replaceAll("]",""), amlOne, amlTwo);
            assertThat(amlStatus).as("AML status").isIn(amlOne, amlTwo);
        });
    }

}

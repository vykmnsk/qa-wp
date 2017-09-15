package com.tabcorp.qa.wagerplayer.steps;

import com.tabcorp.qa.common.Helpers;
import com.tabcorp.qa.common.Storage;
import com.tabcorp.qa.wagerplayer.Config;
import com.tabcorp.qa.wagerplayer.api.MOBI_V2;
import com.tabcorp.qa.wagerplayer.api.MicroGaming;
import com.tabcorp.qa.wagerplayer.api.PlayTech;
import com.tabcorp.qa.wagerplayer.api.WagerPlayerAPI;
import cucumber.api.java8.En;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.tabcorp.qa.common.Storage.KEY.*;
import static com.tabcorp.qa.wagerplayer.api.MOBI_V2.PromoBalanceTypes.*;
import static org.assertj.core.api.Assertions.assertThat;

public class CasinoSteps implements En {

    private PlayTech playTech;
    private WagerPlayerAPI api = Config.getAPI();

    public CasinoSteps() {

        Then("^the loss limit should be \\$(\\d+.\\d\\d) and loss limit definition should be \"([^\"]*)\"$", (BigDecimal expectedLossLimit, String expectedLossLimitDefinition) -> {
            Map<String, String> custData = (Map<String, String>) Storage.get(Storage.KEY.CUSTOMER);
            String clientIp = custData.getOrDefault("client_ip", null);
            String accessToken = api.login(custData.get("username"), custData.get("password"), clientIp);

            MOBI_V2 mobi_v2 = new MOBI_V2();
            Pair<BigDecimal, String> lossLimitData = mobi_v2.getCustomerLossLimitAndDefinition(accessToken);
            BigDecimal actualLossLimit = lossLimitData.getLeft();
            assertThat(Helpers.roundOff(actualLossLimit)).as("Actual Casino Loss Limit").isEqualTo(Helpers.roundOff(expectedLossLimit));

            String actualLossLimitDefinition = lossLimitData.getRight();
            assertThat(actualLossLimitDefinition).as("Actual Casino Loss Limit Definition").
                    isEqualToIgnoringCase(expectedLossLimitDefinition);
        });

        When("^I update the loss limit to \\$(\\d+.\\d\\d) for (\\d+) hours$", (BigDecimal lossLimit, Integer duration) -> {
            Map<String, String> custData = (Map<String, String>) Storage.get(Storage.KEY.CUSTOMER);
            String clientIp = custData.getOrDefault("client_ip", null);
            String accessToken = api.login(custData.get("username"), custData.get("password"), clientIp);
            MOBI_V2 mobi_v2 = new MOBI_V2();
            mobi_v2.setCustomerLossLimit(accessToken, lossLimit, duration);
        });

        When("^I use a promo code \"([^\"]*)\"$", (String promoCode) -> {
            String accessToken = (String) Storage.get(API_ACCESS_TOKEN);
            MOBI_V2 mobi_v2 = new MOBI_V2();
            mobi_v2.applyPromo(accessToken, promoCode);
        });

        When("^I get a PlayTech token for the new customer$", () -> {
            Map<String, String> custData = (Map<String, String>) Storage.get(Storage.KEY.CUSTOMER);
            playTech = new PlayTech();
            String accessToken = playTech.login(custData.get("username"), custData.get("password"));
            assertThat(accessToken).as("Playtech Access Token").isNotEmpty();
            Storage.put(PLAYTECH_API_ACCESS_TOKEN, accessToken);
        });

        And("^I place multiple spins on \"([^\"]*)\" Casino \"([^\"]*)\" game with a stake of ([^\"]*)$", (String gameProvider, String gameType, String stakesAsString) -> {
            String accessToken = (String) Storage.get(PLAYTECH_API_ACCESS_TOKEN);
            Map<String, String> custData = (Map<String, String>) Storage.get(Storage.KEY.CUSTOMER);
            PlayTech playTech = new PlayTech();
            List<String> stakesAsList = Helpers.extractCSV(stakesAsString);

            List<Double> stakes = Helpers.convertList(stakesAsList, Double::parseDouble);
            stakes.forEach(stake -> {
                //Stake negated because it is loss bet.
                Storage.put(BET_RESPONSE, playTech.placeBet(accessToken, custData, new BigDecimal(stake).negate(), gameProvider, gameType));
            });
        });

        And("^I place multiple Microgaming bets for ([^\"]*)$",(String betAmounts) -> {
            MicroGaming microGaming = new MicroGaming();
            List<String> stakes = Helpers.extractCSV(betAmounts);
            List<Double> bets = Helpers.convertList(stakes, Double::parseDouble);
            bets.forEach(bet -> {
                // For a bet to placed , one needs to place it and then end the bet with the same gameID.
                // And each bet has a different game ID.
                String gameID = RandomStringUtils.randomNumeric(6);
                //In case of loss limit trigger, the error is returned when placing a bet.
                Storage.put(BET_RESPONSE,microGaming.placeBet((String) Storage.get(CUSTOMER_ID),gameID,new BigDecimal(bet)));
                microGaming.placeEndBet((String) Storage.get(CUSTOMER_ID),gameID);
            });
        });

        And("^I place a Microgaming bet for \\$(\\d+.\\d\\d) and expecting to win of \\$(\\d+.\\d\\d)$",(BigDecimal stake, BigDecimal winningStake) -> {
            String gameID = RandomStringUtils.randomNumeric(6);
            MicroGaming microGaming = new MicroGaming();
            // For a Win to be received  , it needs to be associated with a bet , with the same gameID.
            // there can be a scenario where :  Bet 40$ and win 10$ --> net loss 30$
            microGaming.placeBet((String) Storage.get(CUSTOMER_ID),gameID,stake);
            microGaming.placeWinBet((String) Storage.get(CUSTOMER_ID),gameID,winningStake);
            microGaming.placeEndBet((String) Storage.get(CUSTOMER_ID),gameID,stake,winningStake);
        });

        And("^I spin a winning \"([^\"]*)\" Casino \"([^\"]*)\" game with a stake of ([^\"]*)$", (String gameProvider, String gameType, String stakesAsString) -> {
            String accessToken = (String) Storage.get(PLAYTECH_API_ACCESS_TOKEN);
            Map<String, String> custData = (Map<String, String>) Storage.get(Storage.KEY.CUSTOMER);
            PlayTech playTech = new PlayTech();
            List<String> stakesAsList = Helpers.extractCSV(stakesAsString);

            List<Double> stakes = Helpers.convertList(stakesAsList, Double::parseDouble);
            stakes.forEach(stake -> {
                // Stake not negated because it is a win bet.
                Storage.put(BET_RESPONSE, playTech.placeWinBet(accessToken, custData, new BigDecimal(stake), gameProvider, gameType));
            });
        });

        Then("^the message should be \"([^\"]*)\"$", (String message) -> {
            String betResponse = (String) Storage.get(BET_RESPONSE);
            assertThat(betResponse).isNotEmpty();
            String actualMessage = PlayTech.getErrorMessage(betResponse);
            assertThat(actualMessage).as("Playtech Error Description").isEqualToIgnoringCase(message);
        });

        And("^the error code should be (\\d+)$", (Integer expectedErrorCode) -> {
            String betResponse = (String) Storage.get(BET_RESPONSE);
            assertThat(betResponse).isNotEmpty();
            String actualErrorCode = PlayTech.getErrorCode(betResponse);
            assertThat(actualErrorCode).as("PlayTech Error Code").isEqualTo(expectedErrorCode.toString());
        });

        Then("^the Microgaming error message should be \"([^\"]*)\"$", (String message) -> {
            String betResponse = (String) Storage.get(BET_RESPONSE);
            assertThat(betResponse).isNotEmpty();
            String actualMessage = MicroGaming.readErrorMessage(betResponse);
            assertThat(actualMessage).as("Microgaming Error Description").isEqualToIgnoringCase(message);
        });

        And("^the Microgaming error code should be (\\d+)$", (Integer expectedErrorCode) -> {
            String betResponse = (String) Storage.get(BET_RESPONSE);
            assertThat(betResponse).isNotEmpty();
            String actualErrorCode = MicroGaming.readErrorCode(betResponse);
            assertThat(actualErrorCode).as("Microgaming Error Code").isEqualTo(expectedErrorCode.toString());
        });

        And("^the Bonus Balance should be \\$(\\d+\\.\\d\\d)$", (BigDecimal expectedBonusBalance) -> {
            String accessToken = (String) Storage.get(API_ACCESS_TOKEN);
            MOBI_V2 mobi_v2 = new MOBI_V2();
            BigDecimal amount = mobi_v2.getDynamicBalance(accessToken, BONUS_BALANCE);
            assertThat(Helpers.roundOff(amount)).as("Actual Bonus Balance").isGreaterThanOrEqualTo(expectedBonusBalance);
        });

        And("^the Ringfenced Balance should be \\$(\\d+\\.\\d\\d)$", (BigDecimal expectedRingFencedBalance) -> {
            String accessToken = (String) Storage.get(API_ACCESS_TOKEN);
            MOBI_V2 mobi_v2 = new MOBI_V2();
            BigDecimal amount = mobi_v2.getDynamicBalance(accessToken, RINGFENCED_BALANCE);
            assertThat(Helpers.roundOff(amount)).as("Actual Ringfenced Balance").isGreaterThanOrEqualTo(expectedRingFencedBalance);
        });

        And("^the Bonus Pending Winnings should be \\$(\\d+\\.\\d\\d)$", (BigDecimal expectedBonusPendingWinningsBalance) -> {
            String accessToken = (String) Storage.get(API_ACCESS_TOKEN);
            MOBI_V2 mobi_v2 = new MOBI_V2();
            BigDecimal amount = mobi_v2.getDynamicBalance(accessToken, BONUS_PENDING_WINNINGS_BALANCE);
            assertThat(Helpers.roundOff(amount)).as("Actual Bonus Pending Winnings Balance").isGreaterThanOrEqualTo(expectedBonusPendingWinningsBalance);
        });

        Then("^I get a Microgaming token for the customer successfully$", () -> {
            String accessToken = (String) Storage.get(API_ACCESS_TOKEN);
            MOBI_V2 mobi_v2 = new MOBI_V2();
            String microGamingAccessToken = mobi_v2.getMicrogamingToken(accessToken);
            assertThat(microGamingAccessToken).as("Microgaming token").isNotNull();
        });

    }

}


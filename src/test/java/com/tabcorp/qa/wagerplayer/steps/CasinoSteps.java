package com.tabcorp.qa.wagerplayer.steps;

import com.tabcorp.qa.common.Helpers;
import com.tabcorp.qa.common.Storage;
import com.tabcorp.qa.wagerplayer.Config;
import com.tabcorp.qa.wagerplayer.api.MOBI_V2;
import com.tabcorp.qa.wagerplayer.api.PlayTech;
import com.tabcorp.qa.wagerplayer.api.WagerPlayerAPI;
import cucumber.api.java8.En;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.tabcorp.qa.common.Storage.KEY.*;
import static org.assertj.core.api.Assertions.assertThat;

public class CasinoSteps implements En {

    private PlayTech playTech;
    private WagerPlayerAPI api = Config.getAPI();

    public CasinoSteps() {

        Then("^the loss limit should be \\$(\\d+.\\d\\d) and loss limit definition should be \"([^\"]*)\"$", (BigDecimal expectedLossLimit, String expectedLossLimitDefinition) -> {
            Map<String, String> custData = (Map<String, String>) Storage.get(Storage.KEY.CUSTOMER);
            String accessToken = api.login(custData.get("username"), custData.get("password"), null);

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
//                Stake negated because it is loss bet.
                Storage.put(BET_RESPONSE, playTech.placeBet(accessToken, custData, new BigDecimal(stake).negate(), gameProvider, gameType));
            });
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
            assertThat(actualMessage).isEqualToIgnoringCase(message);
        });

        And("^the error code should be (\\d+)$", (Integer expectedErrorCode) -> {
            String betResponse = (String) Storage.get(BET_RESPONSE);
            assertThat(betResponse).isNotEmpty();
            String actualErrorCode = PlayTech.getErrorCode(betResponse);
            assertThat(actualErrorCode).as("Error Code").isEqualTo(expectedErrorCode.toString());
        });

        Then("^I should get a Microgaming token for the customer successfully$", () -> {
            String accessToken = (String) Storage.get(API_ACCESS_TOKEN);
            MOBI_V2 mobi_v2 = new MOBI_V2();
            String microGamingAccessToken = mobi_v2.getMicrogamingToken(accessToken);
            assertThat(microGamingAccessToken).isNotNull();
            Storage.add(MICROGAMING_API_ACCESS_TOKEN,microGamingAccessToken);
        });

    }

}


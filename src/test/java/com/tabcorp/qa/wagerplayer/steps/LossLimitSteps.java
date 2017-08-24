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
import java.util.Map;

import static com.tabcorp.qa.common.Storage.KEY.PLAYTECH_API_ACCESS_TOKEN;
import static org.assertj.core.api.Assertions.assertThat;

public class LossLimitSteps implements En {

    private PlayTech playTech;
    private WagerPlayerAPI api = Config.getAPI();
    private String betResponse;

    public LossLimitSteps() {

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

        When("^I try to get a PlayTech token for the new customer$", () -> {
            Map<String, String> custData = (Map<String, String>) Storage.get(Storage.KEY.CUSTOMER);
            playTech = new PlayTech();
            String accessToken = playTech.login(custData.get("username"), custData.get("password"));
            assertThat(accessToken).as("Playtech Access Token").isNotEmpty();
            Storage.put(PLAYTECH_API_ACCESS_TOKEN, accessToken);
        });

        And("^I spin a \"([^\"]*)\" Casino \"([^\"]*)\" game with a stake of \\$(\\d+.\\d\\d)$", (String gameProvider, String gameType, BigDecimal stake) -> {
            String accessToken = (String) Storage.get(PLAYTECH_API_ACCESS_TOKEN);
            Map<String, String> custData = (Map<String, String>) Storage.get(Storage.KEY.CUSTOMER);

            PlayTech playTech = new PlayTech();
            betResponse = playTech.placeBet(accessToken, custData, stake, gameProvider, gameType);
        });

        Then("^the message should be \"([^\"]*)\"$", (String message) -> {
            assertThat(betResponse).isNotEmpty();
            String myXpath = "/*[local-name()='walletBatchResponse']/*[local-name()='gameMultiBalanceTransactionResponse']/*[local-name()='errorText']";
            String actualMessage = Helpers.extractByXpath(betResponse,myXpath).toString();
            assertThat(actualMessage).isEqualToIgnoringCase(message);
        });

        And("^the error code should be (\\d+)$", (Integer expectedErrorCode) -> {
            assertThat(betResponse).isNotEmpty();
            String myXpath = "/*[local-name()='walletBatchResponse']/*[local-name()='gameMultiBalanceTransactionResponse']/*[local-name()='errorCode']";
            String actualErrorCode = Helpers.extractByXpath(betResponse,myXpath).toString();
            assertThat(actualErrorCode).as("Error Code").isEqualTo(expectedErrorCode.toString());
        });

    }

}


package com.tabcorp.qa.wagerplayer.steps;

import com.tabcorp.qa.common.Helpers;
import com.tabcorp.qa.common.Storage;
import com.tabcorp.qa.wagerplayer.Config;
import com.tabcorp.qa.wagerplayer.api.MOBI_V2;
import com.tabcorp.qa.wagerplayer.api.WagerPlayerAPI;
import cucumber.api.java8.En;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class LossLimitSteps implements En {

    private MOBI_V2 mobi_v2 = new MOBI_V2();
    private WagerPlayerAPI api = Config.getAPI();

    public LossLimitSteps() {

        Then("^the loss limit should be \\$(\\d+.\\d+) and loss limit definition should be \"([^\"]*)\"$", (BigDecimal expectedLossLimit, String expectedLossLimitDefinition) -> {
            Map<String, String> custData = (Map<String, String>) Storage.get(Storage.KEY.CUSTOMER);
            String accessToken = api.login(custData.get("username"), custData.get("password"), null);

            Pair<BigDecimal,String> lossLimitData = mobi_v2.getCustomerLossLimitAndDefinition(accessToken);
            BigDecimal actualLossLimit = lossLimitData.getLeft();
            assertThat(Helpers.roundOff(actualLossLimit)).as("Actual Casino Loss Limit").isEqualTo(Helpers.roundOff(expectedLossLimit));

            String actualLossLimitDefinition = lossLimitData.getRight();
            assertThat(actualLossLimitDefinition).as("Actual Casino Loss Limit Definition").
                    isEqualToIgnoringCase(expectedLossLimitDefinition);
        });

        When("^I update the loss limit to \\$(\\d+.\\d+) for (\\d+) hours$", (BigDecimal lossLimit, Integer duration) -> {
            Map<String, String> custData = (Map<String, String>) Storage.get(Storage.KEY.CUSTOMER);
            String clientIp = custData.getOrDefault("client_ip", null);
            String accessToken = api.login(custData.get("username"), custData.get("password"), clientIp);
            mobi_v2.setCustomerLossLimit(accessToken, lossLimit, duration);
        });

    }

}

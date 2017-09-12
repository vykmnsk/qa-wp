package com.tabcorp.qa.wagerplayer.api;


import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.ReadContext;
import com.tabcorp.qa.common.BetType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.fail;

public interface WagerPlayerAPI {
    enum KEY {
        MPID,
        WIN_PRICE,
        PLACE_PRICE,
        RUNNER_NAME,
        BET_STATUS,
        BET_PAYOUT
    }

    String login(String username, String password, String clientIp);

    BigDecimal getBalance(String sessionToken);

    ReadContext placeSingleWinBetForRacing(String accessToken, Integer productId, String mpid, String winPrice, BigDecimal stake, Integer bonusBetFlag);

    ReadContext placeSinglePlaceBet(String accessToken, Integer productId, String mpid, String placePrice, BigDecimal stake, Integer bonusBetFlag);

    ReadContext placeSingleEachwayBet(String accessToken, Integer productId, String mpid, String winPrice, String placePrice, BigDecimal stake, Integer bonusBetFlag);

    ReadContext placeExoticBet(String accessToken, Integer productId, List<String> selectionIds, String marketId, BigDecimal stake, boolean isFlexi, boolean isBoxed);

    String createNewCustomer(Map custData);

    List<String> createCustomerFails(Map custData);

    String  readAmlStatus(String accessToken);

    BigDecimal readNewBalance(ReadContext resp);

    List<Integer> readBetIds(ReadContext resp);

    List<BetType> getBetTypes(String accessToken, int betId);

    Map<KEY, String> getBetDetails(String accessToken, int betId);

    default ReadContext parseVerifyJSON(Object json, String rootPath){
        ReadContext ctx = null;
        try {
            ctx = JsonPath.parse(json);
            ctx.read(rootPath);
        } catch (PathNotFoundException e) {
            fail(String.format("Response JSON seems invalid: %s, %s", json.toString(), e));
        }
        return ctx;
    }

    default String jfilter(String attr, String value) {
        return String.format("[?(@.%s =~ /^%s$/i)]", attr, value);
    }

}

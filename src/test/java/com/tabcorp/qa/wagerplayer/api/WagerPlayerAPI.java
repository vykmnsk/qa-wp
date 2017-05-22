package com.tabcorp.qa.wagerplayer.api;


import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.ReadContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.fail;

public interface WagerPlayerAPI {
    enum KEY {
        MPID,
        WIN_PRICE,
        PLACE_PRICE
    }

    String login(String username, String password, String clientIp);

    BigDecimal getBalance(String sessionToken);

    ReadContext placeSingleWinBet(String accessToken, Integer productId, String mpid, String winPrice, BigDecimal stake);

    ReadContext placeSinglePlaceBet(String accessToken, Integer productId, String mpid, String placePrice, BigDecimal stake);

    ReadContext placeSingleEachwayBet(String accessToken, Integer productId, String mpid, String winPrice, String placePrice, BigDecimal stake);

    ReadContext placeExoticBet(String accessToken, Integer productId, List<String> selectionIds, String marketId, BigDecimal stake, boolean isFlexi);

    String createNewCustomer(Map custData);

    String  readAmlStatus(String accessToken, String clientIp);

    BigDecimal readNewBalance(ReadContext resp);

    List readBetIds(ReadContext resp);

    default ReadContext parseVerifyJSON(Object json, String rootPath){
        ReadContext ctx = null;
        try {
            ctx = JsonPath.parse(json);
            ctx.read(rootPath);
        } catch (PathNotFoundException e) {
            fail(String.format("Response JSON seems invalid: %s, %s", json.toString(), e.getMessage()));
        }
        return ctx;
    }

}

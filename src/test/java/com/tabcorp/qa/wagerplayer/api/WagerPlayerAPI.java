package com.tabcorp.qa.wagerplayer.api;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface WagerPlayerAPI {
    enum KEY {
        MPID,
        WIN_PRICE,
        PLACE_PRICE
    }

    String login(String username, String password, String clientIp);

    BigDecimal getBalance(String sessionToken);

    Object placeSingleWinBet(String accessToken, Integer productId, String mpid, String winPrice, BigDecimal stake);

    Object placeSinglePlaceBet(String accessToken, Integer productId, String mpid, String placePrice, BigDecimal stake);

    Object placeSingleEachwayBet(String accessToken, Integer productId, String mpid, String winPrice, String placePrice, BigDecimal stake);

    Object placeExoticBet(String accessToken, Integer productId, List<String> selectionIds, String marketId, BigDecimal stake, boolean isFlexi);

    String createNewCustomer(Map custData);

    String  readAmlStatus(String accessToken, String clientIp);

    BigDecimal readNewBalance(Object resp);

    List readBetIds(Object resp);

}

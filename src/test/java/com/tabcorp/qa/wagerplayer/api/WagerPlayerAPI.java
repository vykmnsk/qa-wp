package com.tabcorp.qa.wagerplayer.api;


import com.tabcorp.qa.wagerplayer.dto.Customer;

import java.math.BigDecimal;
import java.util.List;

public interface WagerPlayerAPI {
    enum KEY {
        MPID,
        WIN_PRICE,
        PLACE_PRICE
    }

    String login(String username, String password);

    BigDecimal getBalance(String sessionToken);

    Object placeSingleWinBet(String accessToken, Integer productId, String mpid, String winPrice, BigDecimal stake);

    Object placeSinglePlaceBet(String accessToken, Integer productId, String mpid, String placePrice, BigDecimal stake);

    Object placeSingleEachwayBet(String accessToken, Integer productId, String mpid, String winPrice, String placePrice, BigDecimal stake);

    Object placeExoticBet(String accessToken, Integer productId, List<String> selectionIds, String marketId, BigDecimal stake, boolean isFlexi);

    String createNewCustomer(Customer customer);

    String readAmlStatus(String accessToken);

    BigDecimal readNewBalance(Object resp);

    List readBetIds(Object resp);

}

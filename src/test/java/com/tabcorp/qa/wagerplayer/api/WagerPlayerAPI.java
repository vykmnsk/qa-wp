package com.tabcorp.qa.wagerplayer.api;


import com.tabcorp.qa.wagerplayer.dto.Customer;

import java.math.BigDecimal;
import java.util.List;

public interface WagerPlayerAPI {
    enum KEY {
        MPID,
        WIN_PRICE,
        PLACE_PRICE,
    }

    String getAccessToken(String userName, String Password);

    BigDecimal getBalance(String sessionToken);

    Object placeSingleWinBet(String sessionId, Integer productId, String mpid, String winPrice, BigDecimal stake);

    Object placeSinglePlaceBet(String sessionId, Integer productId, String mpid, String placePrice, BigDecimal stake);

    Object placeSingleEachwayBet(String sessionId, Integer productId, String mpid, String winPrice, String placePrice, BigDecimal stake);

    Object placeExoticBet(String sessionId, Integer productId, List<String> selectionIds, String marketId, BigDecimal stake, boolean isFlexi);

    BigDecimal readNewBalance(Object resp);

    //TODO
//    String createNewCustomer(Customer cust);
//    String readAmlStatus(String customerUsername, String customerPassword);


}

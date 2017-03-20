package com.tabcorp.qa.wagerplayer.api;


import java.math.BigDecimal;

public interface WagerPlayerAPI {
   String getAccessToken(String userName,String Password);
   BigDecimal getBalance(String sessionToken);
   Object placeSingleWinBet(String sessionId, Integer productId, String mpid, String winPrice, BigDecimal stake);
}

package com.tabcorp.qa.wagerplayer.api;

import com.jayway.jsonpath.JsonPath;
import com.tabcorp.qa.common.BetType;
import com.tabcorp.qa.common.REST;
import com.tabcorp.qa.wagerplayer.Config;
import net.minidev.json.JSONArray;
import org.assertj.core.api.Assertions;
import org.json.simple.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MOBI_V2 implements WagerPlayerAPI {

    private static String mobi_v2_url = Config.moby_V2_URL();

    public BigDecimal getBalance(String accessToken) {
        String URL = mobi_v2_url + "/customer/balance" ;

        Map<String, Object> queryParams = new HashMap();
        queryParams.put("access_token", accessToken);
        Object response = REST.get(URL, queryParams);

        JSONArray errors = JsonPath.read(response, "$..error..error_message");
        Assertions.assertThat(errors).as("errors").isEmpty();
        Double balance = Double.parseDouble(JsonPath.read(response, "$.balance"));
        return new BigDecimal(balance);
    }

    public String getAccessToken(String username, String password) {
        String URL = mobi_v2_url + "/login";
        Map<String, Object> fields = new HashMap();
        fields.put("output_type", "json");
        fields.put("username", username);
        fields.put("password", password);

        Object response = REST.post(URL, fields);
        JSONArray errors = JsonPath.read(response, "$..error..error_text");
        Assertions.assertThat(errors).as("Errors in response").isEmpty();

        JSONArray access_token_response = JsonPath.read(response, "$..login_data..access_token");
        String access_token = access_token_response.get(0).toString();
        Assertions.assertThat(access_token).as("Access Token ").isNotEmpty();
        return access_token;
    }

    private static Object placeBet(String reqJSON) {
        String URL = mobi_v2_url + "/betslip/checkout" ;
        Object response = REST.put(URL, reqJSON);

        List<String> errors = JsonPath.read(response, "$..selections..error_message");
        Assertions.assertThat(errors).withFailMessage("Errors in response: %s", errors).isEmpty();

        JSONArray betId = JsonPath.read(response, "$..selections[0].bet_id");
        Assertions.assertThat(betId).as("BetId ").isNotEmpty();
        return response;
    }

    public Object placeSingleWinBet(String accessToken, Integer productId , String mpid, String winPrice, BigDecimal stake) {
        //Ignore productID
        //Added to ensure function signature remains same as to WagerPlayerAPI interface.

        //prices
        JSONObject price = new JSONObject();
        price.put("win_price", winPrice);
        String betPayload =  createBetPayload(accessToken, mpid, stake, BetType.Win.id, price);
        return MOBI_V2.placeBet(betPayload);
    }

    public Object placeSinglePlaceBet(String accessToken, Integer productId , String mpid, String placePrice, BigDecimal stake) {
        //Ignore productID
        //Added to ensure function signature remains same as to WagerPlayerAPI interface.

        //prices
        JSONObject price = new JSONObject();
        price.put("place_price", placePrice);

        String betPayload =  createBetPayload(accessToken, mpid, stake, BetType.Place.id, price);
        return MOBI_V2.placeBet(betPayload);
    }

    private String createBetPayload(String accessToken, String mpid, BigDecimal stake, Integer betId, JSONObject priceObject)  {
        JSONObject obj = new JSONObject();
        JSONArray selections = new JSONArray();
        JSONObject betType = new JSONObject(); //options
        JSONObject bet = new JSONObject();

        obj.put("access_token", accessToken);

        betType.put("bet_type", betId);

        bet.put("type", "single");
        bet.put("stake", stake.toString());
        bet.put("mpid", mpid);
        bet.put("options", betType);
        bet.put("prices", priceObject);
        selections.add(bet);

        obj.put("selections", selections);

        return obj.toJSONString();
    }
    
}
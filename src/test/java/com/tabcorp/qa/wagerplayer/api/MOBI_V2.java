package com.tabcorp.qa.wagerplayer.api;

import com.jayway.jsonpath.JsonPath;
import com.tabcorp.qa.common.REST;
import com.tabcorp.qa.wagerplayer.Config;
import net.minidev.json.JSONArray;
import org.assertj.core.api.Assertions;
import org.json.simple.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MOBI_V2 {

    public static String getAccessToken(String username, String password) {
        String URL = Config.moby_V2_URL();
        Map<String, Object> fields = new HashMap();
        fields.put("output_type", "json");
        fields.put("username", username);
        fields.put("password", password);

        Object resp = REST.post(URL, fields);
        JSONArray errors = JsonPath.read(resp, "$..error..error_text");
        Assertions.assertThat(errors).as("Errors in response").isEmpty();

        JSONArray access_token_response = JsonPath.read(resp, "$..login_data..access_token");
        String access_token = access_token_response.get(0).toString();
        Assertions.assertThat(access_token).withFailMessage("Access Token should not be empty.").isNotEmpty();
        return access_token;
    }

    private static Object placeBet(String reqJSON) {
        Object response = REST.put(Config.moby_V2_betSlipCheckOutURL(), reqJSON);

        List<String> errors = JsonPath.read(response, "$..selections..error_message");
        Assertions.assertThat(errors).withFailMessage("Errors in response: %s", errors).isEmpty();

        List<String> betSlipInfo = JsonPath.read(response, "$.betslip");
        Assertions.assertThat(betSlipInfo).withFailMessage("BetSlip info should not be empty.").isNotEmpty();
        return response;
    }

    public static Object placeSingleWinBet(String accessToken, String type, int betType, String mpid, String winPrice, BigDecimal stake) {

        JSONObject obj = new JSONObject();
        obj.put("access_token", accessToken);

        JSONArray list = new JSONArray();

        JSONObject bet = new JSONObject();
        bet.put("type", type);
        bet.put("stake", stake.toString());
        bet.put("mpid", mpid);
        //options
        JSONObject bet_type = new JSONObject();
        bet_type.put("bet_type", betType);
        //prices
        JSONObject win_price = new JSONObject();
        win_price.put("win_price", winPrice);
        JSONObject options = new JSONObject();
        bet.put("options", bet_type);
        JSONObject prices = new JSONObject();
        bet.put("prices", win_price);
        list.add(bet);
        obj.put("selections",list);

        return MOBI_V2.placeBet(obj.toJSONString());

}

}
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


public class MOBI_V2 implements WagerPlayerAPI {
    private static String mobi_v2_url = Config.moby_V2_URL();

    public BigDecimal getBalance(String accessToken) {
        String URL = mobi_v2_url + "/customer/balance" ;

        Map<String, Object> queryParams = new HashMap();
        queryParams.put("access_token", accessToken);
        Object response = REST.get(URL, queryParams);

        JSONArray errors = JsonPath.read(response, "$..error..error_message");
        Assertions.assertThat(errors).as("errors").isEmpty();
        return new BigDecimal((Double) JsonPath.read(response, "$.available_balance"));
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
        Assertions.assertThat(access_token).withFailMessage("Access Token should not be empty.").isNotEmpty();
        return access_token;
    }

    private static Object placeBet(String reqJSON) {
        String URL = mobi_v2_url + "/betslip/checkout" ;
        Object response = REST.put(URL, reqJSON);

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
        obj.put("selections", list);

        return MOBI_V2.placeBet(obj.toJSONString());

    }

}
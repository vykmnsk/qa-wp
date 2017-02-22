package com.tabcorp.qa.wagerplayer.api;

import com.jayway.jsonpath.JsonPath;
import com.tabcorp.qa.common.REST;
import com.tabcorp.qa.wagerplayer.Config;

import java.util.HashMap;
import java.util.Map;


public class WAPI {

    static String URL = Config.wapiURL();

    public static String login(){
        Map<String, Object> fields = new HashMap<>();
        fields.put("action", "bet_customer_login");
        fields.put("wapi_client_user", Config.wapiUsername());
        fields.put("wapi_client_pass", Config.wapiPassword());
        fields.put("username", Config.customerUsername());
        fields.put("password", Config.customerPassword());
        Object doc = REST.post(URL, fields);
        return JsonPath.read(doc, "$.RSP.login[0].session");
    }
}

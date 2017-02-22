package com.tabcorp.qa.common;

import com.jayway.jsonpath.Configuration;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.Map;


public class REST {

    public static Object post(String url, Map<String, Object> fields) {
        fields.put("output_type", "json");
        HttpResponse<String> jsonResponse = null;
        try {
            jsonResponse = Unirest.post(url)
                    .fields(fields)
                    .asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        String body = jsonResponse.getBody();
        return Configuration.defaultConfiguration().jsonProvider().parse(body);
    }

}

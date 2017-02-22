package com.tabcorp.qa.common;

import com.jayway.jsonpath.Configuration;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.Map;


public class REST {

    public static Object post(String url, Map<String, Object> fields) {
        HttpResponse<String> jsonResponse = null;
        try {
            jsonResponse = Unirest.post(url)
                    .fields(fields)
                    .asString();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
        String body = jsonResponse.getBody();
        return Configuration.defaultConfiguration().jsonProvider().parse(body);
    }
}

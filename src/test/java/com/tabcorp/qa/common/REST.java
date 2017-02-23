package com.tabcorp.qa.common;

import com.jayway.jsonpath.Configuration;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


public class REST {
    public static Logger log = LoggerFactory.getLogger(REST.class);

    public static Object post(String url, Map<String, Object> fields) {
        HttpResponse<String> jsonResponse = null;
        try {
            jsonResponse = Unirest.post(url)
                    .fields(fields)
                    .asString();
        } catch (UnirestException e) {
            log.info("REST URL=" + url);
            log.info("REST fields=" + fields);
            throw new RuntimeException(e);
        }
        String body = jsonResponse.getBody();
        return Configuration.defaultConfiguration().jsonProvider().parse(body);
    }
}

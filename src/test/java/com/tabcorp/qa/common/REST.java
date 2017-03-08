package com.tabcorp.qa.common;

import com.jayway.jsonpath.Configuration;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


public class REST {
    public static Logger log = LoggerFactory.getLogger(REST.class);

    private static void verifyResponse(HttpResponse<String> response) {
        assertThat(response.getStatus()).as("response status=" + response.getStatusText()).isBetween(200, 300);
        assertThat(response.getBody()).as("response body").isNotEmpty();

    }

    public static Object post(String url, Map<String, Object> fields) {
        HttpResponse<String> response;
        try {
            log.info("sending REST fields=" + fields);
            response = Unirest.post(url)
                    .fields(fields)
                    .asString();
        } catch (UnirestException e) {
            log.info("REST URL=" + url);
            throw new RuntimeException(e);
        }
        verifyResponse(response);
        return Configuration.defaultConfiguration().jsonProvider().parse(response.getBody());
    }

    public static Object put(String url, String reqJSON) {
        log.info("PUT Request : " + reqJSON);

        HttpResponse<String> response;
        try {
            response = Unirest.put(url)
                    .header("accept", "application/json")
                    .body(reqJSON)
                    .asString();
        } catch (UnirestException e) {
            log.info("REST URL for PUT=" + url);
            log.info("PUT Request JSON=" + reqJSON);
            throw new RuntimeException(e);
        }

        verifyResponse(response);

        log.info("PUT Response : " + response.getBody());
        return Configuration.defaultConfiguration().jsonProvider().parse(response.getBody());
    }

}

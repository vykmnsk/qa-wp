package com.tabcorp.qa.common;

import com.jayway.jsonpath.Configuration;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


public class REST {
    private static Logger log = LoggerFactory.getLogger(REST.class);

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
        return verifyAndParseResponse(response);
    }

    public static Object postWithQueryStrings(String url, Map<String, Object> fields, Pair<String, List<String>> pair) {
        fields.put("output_type", "json");
        HttpResponse<String> response;
        try {
            response = Unirest.post(url)
                    .queryString(fields)
                    .queryString(pair.getKey(), pair.getValue())
                    .asString();
        } catch (UnirestException e) {
            log.info("REST URL=" + url);
            throw new RuntimeException(e);
        }
        return verifyAndParseResponse(response);
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
        return verifyAndParseResponse(response);
    }

    public static Object get(String url, Map<String, Object> queryParams) {
        HttpResponse<String> response;
        try {
            log.info("sending GET queryPrams=" + queryParams);
            response = Unirest.get(url)
                    .queryString(queryParams)
                    .asString();
        } catch (UnirestException e) {
            log.info("GET URL=" + url);
            throw new RuntimeException(e);
        }
        return verifyAndParseResponse(response);
    }

    public static Object verifyAndParseResponse(HttpResponse<String> response) {
        assertThat(response.getStatus()).as("response status=" + response.getStatusText()).isBetween(200, 300);
        assertThat(response.getBody()).as("response body").isNotEmpty();
        return Configuration.defaultConfiguration().jsonProvider().parse(response.getBody());
    }


}

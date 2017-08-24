package com.tabcorp.qa.common;

import com.jayway.jsonpath.Configuration;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.lang3.tuple.Pair;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


public class REST {
    private static final Logger log = LoggerFactory.getLogger(REST.class);

    public static Object post(String url, Map<String, Object> fields, Map<String, String> headers) {
        HttpResponse<String> response;
        try {
            log.debug("sending POST URL=" + url);
            log.debug("sending POST headers=" + headers);
            log.debug("sending POST fields=" + fields);
            response = Unirest.post(url)
                    .headers(headers)
                    .fields(fields)
                    .asString();
            log.debug("POST response={}", response.getBody());
        } catch (UnirestException e) {
            throw new FrameworkError(String.format("POST to URL='%s': %s", url, e));
        }
        return verifyAndParseResponse(response);
    }

    public static Object post(String url, Map<String, Object> fields) {
        return post(url, fields, null);
    }

    public static Object postWithQueryStrings(String url, Map<String, Object> fields, Pair<String, List<String>> pair) {
        fields.put("output_type", "json");
        HttpResponse<String> response;
        try {
            log.debug("sending POST query string=" + pair);
            log.debug("sending POST fields=" + fields);
            response = Unirest.post(url)
                    .queryString(fields)
                    .queryString(pair.getKey(), pair.getValue())
                    .asString();
        } catch (UnirestException e) {
            throw new FrameworkError(String.format("POST with QueryString to URL='%s': %s", url, e));
        }
        return verifyAndParseResponse(response);
    }

    public static Object put(String url, String reqJSON) {
        HttpResponse<String> response;
        try {
            log.debug("PUT Request : " + reqJSON);
            response = Unirest.put(url)
                    .header("accept", "application/json")
                    .body(reqJSON)
                    .asString();
        } catch (UnirestException e) {
            throw new FrameworkError(String.format("PUT URL='%s': %s", url, e));
        }
        return verifyAndParseResponse(response);
    }

    public static Object get(String url, Map<String, Object> queryParams) {
        HttpResponse<String> response;
        try {
            log.debug("sending GET queryPrams=" + queryParams);
            response = Unirest.get(url)
                    .queryString(queryParams)
                    .asString();
        } catch (UnirestException e) {
            throw new FrameworkError(String.format("GET URL='%s': %s", url, e));
        }
        return verifyAndParseResponse(response);
    }

    private static Object verifyAndParseResponse(HttpResponse<String> response) {
        assertThat(response.getStatus()).as("response status=" + response.getStatusText()).isBetween(200, 300);

        String body = response.getBody();
        assertThat(body).as("response body").isNotEmpty();
        try {
            new JSONParser().parse(body);
        } catch (ParseException e) {
            String errorMsg = String.format("Response body is not JSON: %s JSON Parse error: %s", body, e);
            throw new FrameworkError(errorMsg);
        }
        return Configuration.defaultConfiguration().jsonProvider().parse(body);
    }

    public static String verifyXMLResponse(HttpResponse<String> response) {
        assertThat(response.getStatus()).as("response status=" + response.getStatusText())
                .isBetween(200, 300);
        String body = response.getBody();
        assertThat(body).as("XML response body").isNotEmpty();
        return body;
    }

    public static HttpResponse<String> postXML(String url, String requestBody, Map<String, String> headers) {
        HttpResponse<String> response;

        try {
            log.debug("sending POST XML headers=" + headers);
            response = Unirest.post(url)
                    .headers(headers)
                    .body(requestBody)
                    .asString();
        } catch (UnirestException e) {
            throw new FrameworkError(String.format("POST with QueryString to URL='%s': %s", url, e));
        }
        log.debug("response from xml POST=" + response);
        verifyXMLResponse(response);
        return response;
    }

}

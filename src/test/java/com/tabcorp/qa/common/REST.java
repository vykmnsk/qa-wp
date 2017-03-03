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

    public static Object post(String url, Map<String, Object> fields) {
        HttpResponse<String> resp = null;
        try {
            resp = Unirest.post(url)
                    .fields(fields)
                    .asString();
        } catch (UnirestException e) {
            log.info("REST URL=" + url);
            log.info("REST fields=" + fields);
            throw new RuntimeException(e);
        }
        assertThat(resp.getStatus()).as("response status=" + resp.getStatusText()).isBetween(200, 300);
        String body = resp.getBody();
        assertThat(body).as("response body").isNotEmpty();
        return Configuration.defaultConfiguration().jsonProvider().parse(body);
    }
}

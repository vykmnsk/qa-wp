package com.tabcorp.qa.wagerplayer;

import com.tabcorp.qa.wagerplayer.api.MOBI_V2;
import com.tabcorp.qa.wagerplayer.api.WAPI;
import com.tabcorp.qa.wagerplayer.api.WagerPlayerAPI;
import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class Config {
    private static final String REDBOOK = "redbook";
    private static final String LUXBET = "luxbet";
    private static final String ENV_APP_NAME = "WAGERPLAYER_APP_NAME";
    private static final String ENV_BASE_URL = "WAGERPLAYER_BASE_URL";
    private static final String ENV_USERNAME = "WAGERPLAYER_USERNAME";
    private static final String ENV_PASSWORD = "WAGERPLAYER_PASSWORD";
    private static final String ENV_WAPI_URL = "WAGERPLAYER_WAPI_URL";
    private static final String ENV_LUXMOBILE_URL = "WAGERPLAYER_LUXMOBILE_URL";
    private static final String ENV_WAPI_USERNAME = "WAGERPLAYER_WAPI_USERNAME";
    private static final String ENV_WAPI_PASSWORD = "WAGERPLAYER_WAPI_PASSWORD";
    private static final String ENV_CUSTOMER_USERNAME = "WAGERPLAYER_CUSTOMER_USERNAME";
    private static final String ENV_CUSTOMER_PASSWORD = "WAGERPLAYER_CUSTOMER_PASSWORD";
    private static final String ENV_CLIENT_IP = "WAGERPLAYER_CLIENT_IP";
    private static final String ENV_MOBI_V2_URL = "WAGERPLAYER_MOBIV2_URL";
    public static Logger log = LoggerFactory.getLogger(Config.class);


    public static String appName() {
        String appName = readVerify(ENV_APP_NAME);
        assertThat(appName).as("Application Name").isIn(Arrays.asList(LUXBET, REDBOOK));
        return appName;
    }

    public static boolean isRedbook() { return REDBOOK.equals(appName()); }

    public static boolean isLuxbet() { return LUXBET.equals(appName()); }

    public static boolean isDockerRun() {
        // RUN_MODE needs to be set to "DOCKER" to run it on containers or it runs normally.
        return "DOCKER".equalsIgnoreCase(System.getenv("WAGERPLAYER_RUN_MODE"));
    }

    private static WagerPlayerAPI api;

    public static WagerPlayerAPI getAPI() {
        if (api == null) {
            api = isRedbook() ? new MOBI_V2() : new WAPI();
        }
        log.info("using " + api.getClass().getSimpleName() + " api");
        return api;
    }

    public static String baseUrl() { return readVerify(ENV_BASE_URL); }

    public static String wapiURL() {
        return readVerify(ENV_WAPI_URL);
    }

    public static String moby_V2_URL() {
        return readVerify(ENV_MOBI_V2_URL);
    }

    public static String luxMobileURL() {
        return readVerify(ENV_LUXMOBILE_URL);
    }

    public static String username() { return readVerify(ENV_USERNAME); }

    public static String password() { return readVerify(ENV_PASSWORD); }

    public static String wapiUsername() {
        return readVerify(ENV_WAPI_USERNAME);
    }

    public static String wapiPassword() {
        return readVerify(ENV_WAPI_PASSWORD);
    }

    public static String customerUsername() {
        return readVerify(ENV_CUSTOMER_USERNAME);
    }

    public static String customerPassword() {
        return readVerify(ENV_CUSTOMER_PASSWORD);
    }

    public static String clientIp() {
        return System.getenv(ENV_CLIENT_IP);
    }

    public static String testEventBaseName() {
        return "QA-WP-";
    }


    private static String readVerify(String envVarName) {
        String var = System.getenv(envVarName);
        assertThat(var).withFailMessage(envVarName + " env var is not provided")
                .isNotNull().isNotBlank();
        return var;
    }
}

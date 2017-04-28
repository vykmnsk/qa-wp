package com.tabcorp.qa.wagerplayer;

import com.tabcorp.qa.wagerplayer.api.MOBI_V2;
import com.tabcorp.qa.wagerplayer.api.WAPI;
import com.tabcorp.qa.wagerplayer.api.WagerPlayerAPI;
import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class Config {
    public static Logger log = LoggerFactory.getLogger(Config.class);

    public static final String REDBOOK = "redbook";
    public static final String LUXBET = "luxbet";

    private static final String ENV_APP_NAME = "WAGERPLAYER_APP_NAME";
    private static final String ENV_BASE_URL = "WAGERPLAYER_BASE_URL";
    private static final String ENV_USERNAME = "WAGERPLAYER_USERNAME";
    private static final String ENV_PASSWORD = "WAGERPLAYER_PASSWORD";

    public static String appName() {
        String appName = System.getenv(ENV_APP_NAME);
        verifyExists(appName, ENV_APP_NAME);
        Assertions.assertThat(appName).as("Application Name").isIn(Arrays.asList(LUXBET, REDBOOK));
        return appName;
    }

    public static String baseUrl() {
        String baseUrl = System.getenv(ENV_BASE_URL);
        verifyExists(baseUrl, ENV_BASE_URL);
        return baseUrl;
    }

    public static String username() {
        String username = System.getenv(ENV_USERNAME);
        verifyExists(username, ENV_USERNAME);
        return username;
    }

    public static String password() {
        String password = System.getenv(ENV_PASSWORD);
        verifyExists(password, ENV_PASSWORD);
        return password;
    }

    public static boolean isDockerRun() {
        // RUN_MODE needs to be set to "DOCKER" to run it on containers or it runs normally.
        if ("DOCKER".equalsIgnoreCase(System.getenv("WAGERPLAYER_RUN_MODE"))) {
            return true;
        }
        return false;
    }

    private static void verifyExists(String var, String envVarName) {
        Assertions.assertThat(var).withFailMessage(envVarName + " env var is not provided").isNotNull();
    }

    private static final String ENV_WAPI_URL = "WAGERPLAYER_WAPI_URL";
    private static final String ENV_LUXMOBILE_URL = "WAGERPLAYER_LUXMOBILE_URL";
    private static final String ENV_WAPI_USERNAME = "WAGERPLAYER_WAPI_USERNAME";
    private static final String ENV_WAPI_PASSWORD = "WAGERPLAYER_WAPI_PASSWORD";
    private static final String ENV_CUSTOMER_USERNAME = "WAGERPLAYER_CUSTOMER_USERNAME";
    private static final String ENV_CUSTOMER_PASSWORD = "WAGERPLAYER_CUSTOMER_PASSWORD";

    private static String readVerify(String envVarName) {
        String var = System.getenv((envVarName));
        verifyExists(var, envVarName);
        return var;
    }

    public static String wapiURL() {
        return readVerify(ENV_WAPI_URL);
    }

    public static String luxMobileURL() {
        return readVerify(ENV_LUXMOBILE_URL);
    }

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


    private static final String ENV_MOBI_V2_URL = "WAGERPLAYER_MOBIV2_URL";
    public static String moby_V2_URL() { return readVerify(ENV_MOBI_V2_URL); }

    private static WagerPlayerAPI api;
    public static WagerPlayerAPI getAPI(){
        if( api == null) {
            api = Config.appName().equals(REDBOOK)? new MOBI_V2() : new WAPI() ;
        }
        log.info("using " + api.getClass().getSimpleName() + " api");
        return api;
    }

    public static String testEventBaseName() {
        return "TEST RACE";
    }
}

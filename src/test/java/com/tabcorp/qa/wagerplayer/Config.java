package com.tabcorp.qa.wagerplayer;

import org.assertj.core.api.Assertions;

public class Config {

    private static final String ENV_APP_NAME = "WAGERPLAYER_APP_NAME";
    private static final String ENV_BASE_URL = "WAGERPLAYER_BASE_URL";
    private static final String ENV_USERNAME = "WAGERPLAYER_USERNAME";
    private static final String ENV_PASSWORD = "WAGERPLAYER_PASSWORD";

    public static String appName() {
        String appName = System.getenv(ENV_APP_NAME);
        verifyExists(appName, ENV_APP_NAME);
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

    //----------------------- MYOB V2 ------------------------

    private static final String ENV_MOBI_V2_URL = "WAGERPLAYER_MOBIV2_URL";
    private static final String ENV_MOBI_V2_USERNAME = "WAGERPLAYER_MOBIV2_USERNAME";
    private static final String ENV_MOBI_V2_PASSWORD = "WAGERPLAYER_MOBIV2_PASSWORD";
    private static  final  String ENV_MOBI_V2_BETSLIPCHECKOUT_URL = "WAGERPLAYER_MOBIV2_BETSLIPCHECKOUT_URL";

    public static String moby_V2_URL() {
        return readVerify(ENV_MOBI_V2_URL);
    }

    public static String moby_V2_USERNAME() {
        return readVerify(ENV_MOBI_V2_USERNAME);
    }

    public static String moby_V2_Password() {
        return readVerify(ENV_MOBI_V2_PASSWORD);
    }

    public static  String moby_V2_betSlipCheckOutURL() {
        return  readVerify(ENV_MOBI_V2_BETSLIPCHECKOUT_URL);
    }
}

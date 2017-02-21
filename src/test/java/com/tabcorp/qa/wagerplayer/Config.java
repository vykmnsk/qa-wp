package com.tabcorp.qa.wagerplayer;


import org.assertj.core.api.Assertions;

public class Config {

    private static final String ENV_APP_NAME = "WAGERPLAYER_APP_NAME";
    private static final String ENV_BASE_URL = "WAGERPLAYER_BASE_URL";
    private static final String ENV_USERNAME = "WAGERPLAYER_USERNAME";
    private static final String ENV_PASSWORD = "WAGERPLAYER_PASSWORD";


    public static String appName(){
        String appName = System.getenv(ENV_APP_NAME);
        verifyExists(appName, ENV_APP_NAME);
        return appName;
    }

    public static String baseUrl(){
        String baseUrl = System.getenv(ENV_BASE_URL);
        verifyExists(baseUrl,ENV_BASE_URL);
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

    private static void verifyExists(String var, String envVarName) {
        Assertions.assertThat(var)
                .withFailMessage(envVarName + " env var is not provided")
                .isNotNull();
    }

    private static final String ENV_WAPI_URL = "WAGERPLAYER_WAPI_URL";
    private static final String ENV_WAPI_USERNAME = "WAGERPLAYER_WAPI_USERNAME";
    private static final String ENV_WAPI_PASSWORD = "WAGERPLAYER_WAPI_PASSWORD";
    private static final String ENV_CUSTOMER_USERNAME = "WAGERPLAYER_CUSTOMER_USERNAME";
    private static final String ENV_CUSTOMER_PASSWORD = "WAGERPLAYER_CUSTOMER_PASSWORD";


    private static String readVerify(String envVarName){
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

    public static String wapiPassword(){
        return readVerify(ENV_WAPI_PASSWORD);
    }

    public static String customerUsername(){
        return readVerify(ENV_CUSTOMER_USERNAME);
    }

    public static String customerPassword(){
        return readVerify(ENV_CUSTOMER_PASSWORD);
    }


}

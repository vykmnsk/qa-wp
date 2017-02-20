package com.tabcorp.qa.wagerplayer;


import org.assertj.core.api.Assertions;

public class Config {

    private static final String ENV_APP_NAME = "WAGERPLAYER_APP_NAME";
    private static final String ENV_BASE_URL = "WAGERPLAYER_BASE_URL";
    private static final String ENV_USERNAME = "WAGERPLAYER_USERNAME";
    private static final String ENV_PASSWORD = "WAGERPLAYER_PASSWORD";
    private static final String RUN_MODE = "RUN_MODE";
    // RUN_MODE needs to be set to "DOCKER_RUN" to run it on containers or it runs normally.

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

    public static  boolean isDockerRun() {
        String runMode = System.getenv(RUN_MODE);
        if(runMode != null && runMode.equalsIgnoreCase("DOCKER_RUN"))
            return true;
        return false;
    }

    private static void verifyExists(String var, String envVarName) {
        Assertions.assertThat(var)
                .withFailMessage(envVarName + " env var is not provided")
                .isNotNull();
    }

}

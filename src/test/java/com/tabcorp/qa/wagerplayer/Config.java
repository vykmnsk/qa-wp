package com.tabcorp.qa.wagerplayer;

import com.tabcorp.qa.wagerplayer.api.MOBI_V2;
import com.tabcorp.qa.wagerplayer.api.WAPI;
import com.tabcorp.qa.wagerplayer.api.WagerPlayerAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class Config {
    private static WagerPlayerAPI api;
    private static final Logger log = LoggerFactory.getLogger(Config.class);

    //Env vars
    public static String appName() { return readVerify("WAGERPLAYER_APP_NAME"); }
    public static String baseUrl() { return readVerify("WAGERPLAYER_BASE_URL"); }
    public static String wapiURL() {
        return readVerify("WAGERPLAYER_WAPI_URL");
    }
    public static String moby_V2_URL() {
        return readVerify("WAGERPLAYER_MOBIV2_URL");
    }
    public static String luxMobileURL() {
        return readVerify("WAGERPLAYER_LUXMOBILE_URL");
    }
    public static String username() { return readVerify("WAGERPLAYER_USERNAME"); }
    public static String password() { return readVerify("WAGERPLAYER_PASSWORD"); }
    public static String wapiUsername() {
        return readVerify("WAGERPLAYER_WAPI_USERNAME");
    }
    public static String wapiPassword() {
        return readVerify("WAGERPLAYER_WAPI_PASSWORD");
    }
    public static String customerUsername() {
        return readVerify("WAGERPLAYER_CUSTOMER_USERNAME");
    }
    public static String customerPassword() {
        return readVerify("WAGERPLAYER_CUSTOMER_PASSWORD");
    }
    public static String clientIp() {
        return System.getenv("WAGERPLAYER_CLIENT_IP");
    }

    public static String feedMQPAHost() { return readVerify("WAGERPLAYER_FEEDMQ_PA_HOST"); }
    public static int feedMQPAPort() { return Integer.parseInt(readVerify("WAGERPLAYER_FEEDMQ_PA_PORT")); }
    public static String feedMQPAUsername() { return readVerify("WAGERPLAYER_FEEDMQ_PA_USERNAME"); }
    public static String feedMQPAPassword() { return readVerify("WAGERPLAYER_FEEDMQ_PA_PASSWORD"); }

    public static String feedMQWiftHost() { return readVerify("WAGERPLAYER_FEEDMQ_WIFT_HOST"); }
    public static int feedMQWiftPort() { return Integer.parseInt(readVerify("WAGERPLAYER_FEEDMQ_WIFT_PORT")); }
    public static String feedMQWiftUsername() { return readVerify("WAGERPLAYER_FEEDMQ_WIFT_USERNAME"); }
    public static String feedMQWiftPassword() { return readVerify("WAGERPLAYER_FEEDMQ_WIFT_PASSWORD"); }

    public static String testEventBaseName() {
        return "QAWP-";
    }

    public static boolean isDockerRun() {
        // RUN_MODE needs to be set to "DOCKER" to run it on containers or it runs normally.
        return "DOCKER".equalsIgnoreCase(System.getenv("WAGERPLAYER_RUN_MODE"));
    }

    public static boolean isRedbook() { return "redbook".equalsIgnoreCase(appName()); }
    public static boolean isLuxbet() { return "luxbet".equalsIgnoreCase(appName()); }

    public static WagerPlayerAPI getAPI() {
        if (null == api) {
            api = isRedbook() ? new MOBI_V2() : new WAPI();
        }
        log.debug("using " + api.getClass().getSimpleName() + " api");
        return api;
    }

    private static String readVerify(String envVarName) {
        String var = System.getenv(envVarName);
        assertThat(var).withFailMessage(envVarName + " env var is not provided")
                .isNotNull().isNotBlank();
        return var;
    }

}

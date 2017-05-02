package com.tabcorp.qa.common;


import com.tabcorp.qa.wagerplayer.Config;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DriverWrapper {
    private static final int DEFAULT_TIMEOUT_SECONDS = 10;
    private static DriverWrapper instance;
    private WebDriver driver;
    private WebDriverWait wait;

    public static DriverWrapper getInstance() {
        if (null == instance) {
            instance = new DriverWrapper();
        }
        return instance;
    }

    public WebDriver getDriver() {
        if (driver == null) {
            if (Config.isDockerRun()) {
                driver = getRemoteDriver();
            } else {
                System.setProperty("webdriver.chrome.driver", Helpers.getChromeDriverPath());

                ChromeOptions options = new ChromeOptions();
                options.addArguments("disable-infobars");

                Map<String, Object> prefs = new HashMap<>();
                prefs.put("credentials_enable_service", false);
                prefs.put("profile.password_manager_enabled", false);
                options.setExperimentalOption("prefs", prefs);

                driver = new ChromeDriver(options);
            }
        }
        return driver;
    }

    private WebDriver getRemoteDriver() {
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setJavascriptEnabled(true);
        RemoteWebDriver driver = null;
        try {
             driver = new RemoteWebDriver(new URL("http://seleniumhub:4444/wd/hub"),capabilities);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return driver ;
    }


    public WebDriverWait getDriverWait() {
        if (wait == null) {
            wait = new WebDriverWait(getDriver(), DEFAULT_TIMEOUT_SECONDS);
        }
        return wait;
    }

    public void closeBrowser() {
        if (null != driver) {
            driver.quit();
            driver = null;
        }
        wait = null;
    }

}

package com.tabcorp.qa.common;


import com.tabcorp.qa.wagerplayer.Config;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
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

    public boolean hasDriver() {
        return null != driver;
    }

    public WebDriver getDriver() {
        if (null == driver) {
            driver = Config.isDockerRun() ? getRemoteDriver() : getLocalDriver();
        }
        return driver;
    }

    private WebDriver getLocalDriver() {
        System.setProperty("webdriver.chrome.driver", Helpers.getChromeDriverPath());
        ChromeOptions options = new ChromeOptions();
        options.addArguments("disable-infobars");
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);
        return new ChromeDriver(options);
    }

    private WebDriver getRemoteDriver() {
        final String hostname = "seleniumhub";
        // Enable this instead for local Docker runs
//        final String hostname = "localhost";
        final String seleniumHub = String.format("http://%s:4444/wd/hub", hostname);
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setJavascriptEnabled(true);
        try {
            return new RemoteWebDriver(new URL(seleniumHub), capabilities);
        } catch (MalformedURLException mue) {
            throw new FrameworkError(mue);
        }
    }

    public WebDriverWait getDriverWait() {
        if (null == wait) {
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

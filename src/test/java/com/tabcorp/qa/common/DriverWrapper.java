package com.tabcorp.qa.common;


import com.tabcorp.qa.wagerplayer.Config;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

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
                System.setProperty("webdriver.chrome.driver",Helpers.getChromeDriverPath());

                driver = new ChromeDriver();
            }
        }
        return driver;
    }

    private WebDriver getRemoteDriver() {

        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        // Add the WebDriver proxy capability.
        Proxy proxy = new Proxy();
        // Proxy set to local host because the container would be executed from the same instance.
        proxy.setHttpProxy("localhost:4444");
        capabilities.setCapability("proxy", proxy);
        capabilities.setJavascriptEnabled(true);
        return new RemoteWebDriver(capabilities);
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

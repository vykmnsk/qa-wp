package com.tabcorp.qa.common;

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

    public static void setInstance(DriverWrapper wrapper) {
        instance = wrapper;
    }

    public WebDriver getDriver() {
        if (driver == null) {

            DesiredCapabilities capabilities = DesiredCapabilities.chrome();

            // Add the WebDriver proxy capability.
            Proxy proxy = new Proxy();
            proxy.setHttpProxy("localhost:4444");
            capabilities.setCapability("proxy", proxy);
            capabilities.setJavascriptEnabled(true);
            driver = new RemoteWebDriver(capabilities);
        }
        return driver;
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

package com.tabcorp.qa.common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
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
            System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver");
            driver = new ChromeDriver();
//            Runtime.getRuntime().addShutdownHook(new Thread(() -> driver.quit()));
            driver.manage().window().maximize();
        }
        return driver;
    }

    public WebDriverWait getDriverWait() {
        if (wait == null) {
            wait = new WebDriverWait(getDriver(), DEFAULT_TIMEOUT_SECONDS);
        }
        return wait;
    }

    public void closeBrowser(){
        if (null == driver) return;
        driver.quit();
        driver = null;
    }
}
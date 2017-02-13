package com.tabcorp.qa.common;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Created by suniaram on 13/2/17.
 */
public class SeleniumHelper {
    public static void scrollTo(WebDriver driver, WebElement cell) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", cell);
    }
}

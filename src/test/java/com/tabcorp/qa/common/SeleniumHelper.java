package com.tabcorp.qa.common;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.awt.*;



public class SeleniumHelper {

    public static void scrollTo(WebDriver driver, WebElement cell) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", cell);
    }


    public static void setScreenSizeToMax(WebDriver driver) {
        java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)screenSize.getWidth();
        int height = (int)screenSize.getHeight();
        driver.manage().window().setSize(new org.openqa.selenium.Dimension(width,height));
    }
}

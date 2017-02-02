package com.tabcorp.qa.wagerplayer.pages;

import com.tabcorp.qa.common.DriverWrapper;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class BasePage {
    private final String ENV_APP_NAME = "WAGERPLAYER_APP_NAME";
    private final String ENV_BASE_URL = "WAGERPLAYER_BASE_URL";

    public WebDriver driver = DriverWrapper.getInstance().getDriver();
    public WebDriverWait wait = DriverWrapper.getInstance().getDriverWait();

    public String appName;
    public String baseUrl;

    public BasePage(){

        PageFactory.initElements(driver, this);
        appName = System.getenv(ENV_APP_NAME);
        Assertions.assertThat(appName)
                .withFailMessage(ENV_APP_NAME + " env var is not provided")
                .isNotNull();

        baseUrl = System.getenv(ENV_BASE_URL);
        Assertions.assertThat(baseUrl)
                .withFailMessage(ENV_BASE_URL + " env var is not provided")
                .isNotNull();

    }

    public WebElement findOne(By loc1, By loc2) {
        List<WebElement> elems = driver.findElements(loc1);
        List<WebElement> elems2 = driver.findElements(loc2);
        elems.addAll(elems2);
        Assertions.assertThat(elems.size())
                .withFailMessage("Expected to find one of %s, %s", loc1, loc2)
                .isEqualTo(1);
        return elems.get(0);
    }
}

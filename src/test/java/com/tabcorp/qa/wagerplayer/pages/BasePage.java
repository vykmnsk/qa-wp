package com.tabcorp.qa.wagerplayer.pages;

import com.tabcorp.qa.ui.webdriver.DriverWrapper;
import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {
    private final String ENV_APP_NAME = "WAGERPLAYER_APP_NAME";
    private final String ENV_BASE_URL = "WAGERPLAYER_BASE_URL";

    public WebDriver driver = DriverWrapper.getInstance().getDriver();
    public WebDriverWait wait = DriverWrapper.getInstance().getDriverWait();
    public String appId;
    public String baseUrl;

    public BasePage(){

        PageFactory.initElements(driver, this);
        appId = System.getenv(ENV_APP_NAME);
        Assertions.assertThat(appId)
                .withFailMessage(ENV_APP_NAME + " env var is not provided")
                .isNotNull();

        baseUrl = System.getenv(ENV_BASE_URL);
        Assertions.assertThat(baseUrl)
                .withFailMessage(ENV_BASE_URL + " env var is not provided")
                .isNotNull();
    }
}

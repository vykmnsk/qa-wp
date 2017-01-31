package com.tabcorp.qa.pages;

import com.tabcorp.qa.ui.webdriver.DriverWrapper;
import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {

    public WebDriver driver = DriverWrapper.getInstance().getDriver();
    public WebDriverWait wait = DriverWrapper.getInstance().getDriverWait();
    public String appId;
    public String baseUrl;

    public BasePage(){

        PageFactory.initElements(driver, this);
        appId = System.getenv("WAGERPLAYER_APP_ID");
        Assertions.assertThat(appId)
                .withFailMessage("app.id system property is not provided")
                .isNotNull();

        baseUrl = System.getenv("WAGERPLAYER_BASE_URL");
        Assertions.assertThat(baseUrl)
                .withFailMessage("base.url system property is not provided")
                .isNotNull();
    }
}

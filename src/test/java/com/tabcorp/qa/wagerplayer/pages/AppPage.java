package com.tabcorp.qa.wagerplayer.pages;

import com.tabcorp.qa.common.AnyPage;
import com.tabcorp.qa.wagerplayer.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.Arrays;
import java.util.List;

public class AppPage extends AnyPage{

    public String appName;
    private String baseUrl;

    AppPage(){
        appName = Config.appName();
        baseUrl = Config.baseUrl();
        PageFactory.initElements(driver, this);
    }

    public void load(){
        setScreenSizeToMax();
        driver.get(baseUrl);
    }

    WebElement findEither(By loc1, By loc2) {
        return findOne(Arrays.asList(loc1, loc2));
    }

    List<WebElement> findBoth(By loc1, By loc2) {
        return findAll(Arrays.asList(loc1, loc2));
    }

}

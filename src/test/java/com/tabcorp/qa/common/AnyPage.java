package com.tabcorp.qa.common;

import com.tabcorp.qa.wagerplayer.steps.CreateEventSteps;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class AnyPage {
    public WebDriver driver;
    public WebDriverWait wait;
    public static Logger log = LoggerFactory.getLogger(AnyPage.class);

    public AnyPage(){
        driver = DriverWrapper.getInstance().getDriver();
        wait = DriverWrapper.getInstance().getDriverWait();
    }

    public List<WebElement> findAll(List<By> locators) {
        List<WebElement> elems = new ArrayList<>();

        for (By loc : locators) {
            elems.addAll(driver.findElements(loc));
        }
        return elems;
    }

    public WebElement findOne(List<By> locators) {
        List <WebElement> elems = findAll(locators);
        Assertions.assertThat(elems.size())
                .withFailMessage("Expected to find one of %s", locators)
                .isEqualTo(1);
        return elems.get(0);
    }

    public WebElement findParent(WebElement elem){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return  (WebElement)js.executeScript("return arguments[0].parentNode;", elem);
    }

}

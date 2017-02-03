package com.tabcorp.qa.common;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class AnyPage {
    public WebDriver driver;
    public WebDriverWait wait;

    public AnyPage(){
        driver = DriverWrapper.getInstance().getDriver();
        wait = DriverWrapper.getInstance().getDriverWait();
    }


    public WebElement findOne(List<By> locators) {
        List<WebElement> elems = new ArrayList<>();

        for(By loc: locators){
            elems.addAll(driver.findElements(loc));
        }
        Assertions.assertThat(elems.size())
                .withFailMessage("Expected to find one of %s", locators)
                .isEqualTo(1);
        return elems.get(0);
    }

}

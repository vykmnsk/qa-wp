package com.tabcorp.qa.wagerplayer.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.Arrays;

public class CustomerConfigPage extends AppPage {

    @FindBy(css= "select#aml_status")
    private WebElement aml_status;

    @FindBy (css= "input[name=Process]")
    private WebElement update;

    @FindBy (css= "input[name=Cancel]")
    private WebElement cancel;

    private String configWindow;

    public void load() {
        configWindow = driver.getWindowHandle();
        switchToNewWindow(Arrays.asList(configWindow));
    }

    public void updateAmlStatus(String newamlstatus) {
        wait.until(ExpectedConditions.visibilityOf(aml_status));
        (new Select(aml_status)).selectByVisibleText(newamlstatus);
        update.click();
    }
}


package com.tabcorp.qa.wagerplayer.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerListPage extends AppPage{

    @FindBy(css = "input[value=insert]")
    public WebElement newCustomer;

    public void load() {
        driver.switchTo().defaultContent();
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame_bottom"));
        wait.until(ExpectedConditions.visibilityOf(newCustomer));
    }

    public void verifyLoaded() {
        HeaderPage header = new HeaderPage();
        header.verifyPageTitle("Customers");
    }

    public void insertNew() {
        newCustomer.click();
    }
}

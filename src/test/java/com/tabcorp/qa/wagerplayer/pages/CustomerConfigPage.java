package com.tabcorp.qa.wagerplayer.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.Arrays;

public class CustomerConfigPage extends AppPage {
    @FindBy(css = ("table#main_table th"))
    public WebElement pageTitle;

    @FindBy(css = ("input[alt*=Update]"))
    public WebElement updateButton;

    @FindBy(css = ("input#ab_r_y"))
    public WebElement interceptOnBetRacingYes;

    @FindBy(css= "select#aml_status")
    private WebElement aml_status;

    @FindBy (css= "input[name=Process]")
    private WebElement update;

    @FindBy (css= "input[name=Cancel]")
    private WebElement cancel;

    private String customerConfigWindow;

    public void load() {
        customerConfigWindow = driver.getWindowHandle();
        switchToNewWindow(Arrays.asList(customerConfigWindow));
    }

    public void verifyLoaded() {
        HeaderPage deposit = new HeaderPage();
        deposit.verifyPageTitle("Customer Configuration");
        wait.until(ExpectedConditions.visibilityOf(updateButton));
    }

    public void selectInterceptOnRacingBetPlacement() {
        interceptOnBetRacingYes.click();
        scrollTo(updateButton);
        updateButton.click();

        driver.close();
        driver.switchTo().window(customerConfigWindow);
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame_bottom"));
    }

    public void updateAmlStatus(String newamlstatus) {
        wait.until(ExpectedConditions.visibilityOf(aml_status));
        (new Select(aml_status)).selectByVisibleText(newamlstatus);
        update.click();
    }

}
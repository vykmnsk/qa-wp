package com.tabcorp.qa.wagerplayer.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CustomerListPage extends AppPage{

    @FindBy(css = "table#body_table_inner")
    public WebElement custTable;

    @FindBy(css = "input[class='screen_only wp_button']")
    public WebElement newCustomer;

    @FindBy(css = "#admin_options_box > a:nth-child(3)")
    public WebElement options;

    @FindBy(css = "table#popup_inner_table > tbody > tr:nth-child(6) > td:nth-child(2) > input[name='username']")
    public WebElement customerUsername;

    @FindBy(css = "table#popup_inner_table > tbody > tr:nth-child(24) > td:nth-child(2) > input[value='Go']")
    public WebElement go;

    public void load() {
        driver.switchTo().defaultContent();
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame_bottom"));
        wait.until(ExpectedConditions.visibilityOf(newCustomer));
        newCustomer.click();
        wait.until(ExpectedConditions.visibilityOf(newCustomer));
    }

}

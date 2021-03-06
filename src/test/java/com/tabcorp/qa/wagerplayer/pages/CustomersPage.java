package com.tabcorp.qa.wagerplayer.pages;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class CustomersPage extends AppPage{
    @FindBy(css = "input[value=insert]")
    public WebElement newCustomer;

    @FindBy(css = "table#side_table_inner tbody tr")
    List<WebElement> custInfoRows;

    @FindBy(css = "img[src='images/button_deposit.gif']")
    private WebElement depositButton;

    @FindBy(css = "input[value=promotion]")
    private WebElement promotionButton;

    @FindBy(css = "img[src='images/button_config.gif']")
    private WebElement configButton;

    @FindBy(css = "a[title$=options]")
    public WebElement options;

    @FindBy(css = ("img[src='images/button_config.gif']"))
    public WebElement config;

    @FindBy(css = "input[name=username]")
    public WebElement customerUsername;

    @FindBy(css = "input[value='Go']")
    public WebElement go;

    final String statusLabel = "AML Status:";

    public void load() {
        driver.switchTo().defaultContent();
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame_bottom"));
        wait.until(ExpectedConditions.visibilityOf(newCustomer));
    }

    public void verifyLoaded() {
        HeaderPage header = new HeaderPage();
        header.verifyPageTitle("Customers");
    }

    public NewCustomerPage insertNew() {
        newCustomer.click();
        NewCustomerPage newCustPage = new NewCustomerPage();
        return newCustPage;
    }

    public String readAMLStatus() {
        String statusRow = custInfoRows.stream()
                .map(r -> r.getText().trim())
                .filter(t -> t.startsWith(statusLabel))
                .findFirst()
                .orElse(null);
        Assertions.assertThat(statusRow).as("Found customer info row for " + statusLabel).isNotNull();

        int labelEnd = statusRow.indexOf(statusLabel) + statusLabel.length();
        return statusRow.substring(labelEnd).trim();
    }

    public DepositPage openDepositWindow() {
        depositButton.click();
        DepositPage dp = new DepositPage();
        dp.load();
        return dp;
    }

    public void searchCustomer(String customerName) {
        options.click();
        customerUsername.sendKeys(customerName);
        go.click();
    }

    public PromotionPage openPromotionWindow() {
        promotionButton.click();
        PromotionPage pp = new PromotionPage();
        pp.load();
        return pp;
    }

    public CustomerConfigPage openConfigWindow() {
        configButton.click();
        CustomerConfigPage cp = new CustomerConfigPage();
        cp.load();
        return cp;
    }
}

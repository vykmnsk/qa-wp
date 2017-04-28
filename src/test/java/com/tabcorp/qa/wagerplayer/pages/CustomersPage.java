package com.tabcorp.qa.wagerplayer.pages;

import cucumber.api.java.hu.De;
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
}

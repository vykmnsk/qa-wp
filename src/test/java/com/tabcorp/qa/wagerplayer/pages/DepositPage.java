package com.tabcorp.qa.wagerplayer.pages;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DepositPage extends AppPage {
    @FindBy(css = ("table#main_table th"))
    public WebElement pageTitle;

    @FindBy(css = "a#method_999")
    public WebElement manualTabLink;

    @FindBy(css = "select[name=type_select]")
    public WebElement transactionType;

    @FindBy(css = "input[name=amount]")
    public WebElement amountTextBox;

    @FindBy(css = "input[value=Readback]")
    public WebElement readbackButton;

    @FindBy(css = "input[name=go]")
    public WebElement submit;

    @FindBy(css = "input[name=cancel]")
    public WebElement cancel;

    @FindBy(css = "body > div.transaction_success")
    public WebElement successMessage;

    @FindBy(css = "body > div.transaction_success_message")
    public WebElement transactionId;

    @FindBy(css = "input.wp_button")
    public WebElement close;

    @FindBy(css = "a[id=deposits]")
    public WebElement depositsTabLink;

    @FindBy(css = "#trans_list > tbody > tr > td")
    public List<WebElement> transactionValuesInTable;

    private String windowOne;
    private String windowTwo;

    public void verifyLoaded() {
        windowOne = driver.getWindowHandle();;
        switchToANewWindow(windowOne);
        HeaderPage deposit = new HeaderPage();
        deposit.verifyPageTitle("Transaction Deposit");
        wait.until(ExpectedConditions.visibilityOf(manualTabLink));
    }

    public void selectManualTab() {
        manualTabLink.click();
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("deposit_screen"));
    }

    public void depositCash(String cashAmount) {
        new Select(transactionType).selectByVisibleText("- Cash Deposit");
        wait.until(ExpectedConditions.visibilityOf(amountTextBox));
        amountTextBox.sendKeys(cashAmount.toString());
        windowTwo = driver.getWindowHandle();

        // below step commented as this field is missing from AWS6 but available in Load Test
        //selectOption(currency).from(customerDepositWindow.currency);

        wait.until(ExpectedConditions.elementToBeClickable(readbackButton));
        readbackButton.click();
        switchToANewWindow(windowTwo, windowOne);

        wait.until(ExpectedConditions.visibilityOf(cancel));
        wait.until(ExpectedConditions.elementToBeClickable(cancel));
        wait.until(ExpectedConditions.visibilityOf(submit));
        wait.until(ExpectedConditions.elementToBeClickable(submit));
        submit.click();

        acceptAlert();
    }

    public void verifyTransaction(String amount) {
        String actualSuccessMessage = successMessage.getText();
        assertThat(actualSuccessMessage).isEqualTo("Transaction processed successfully.");
        String transactionID = transactionId.getText();
        log.info("Transaction " + transactionID);

        close.click();
        driver.switchTo().window(windowTwo);

        depositsTabLink.click();
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("deposit_screen"));
        verifyTransactionIdsMatch(transactionID);

        verifyAmountDepositedMatches(amount);
        driver.close();
        driver.switchTo().window(windowOne);
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame_bottom"));
    }

    public void verifyTransactionIdsMatch(String transactionIDValue) {
        wait.until(ExpectedConditions.visibilityOf(transactionValuesInTable.get(0)));
        assertThat(transactionIDValue).contains(transactionValuesInTable.get(0).getText());
    }

    public void verifyAmountDepositedMatches(String expectedAmount) {
        wait.until(ExpectedConditions.visibilityOf(transactionValuesInTable.get(4)));
        assertThat(transactionValuesInTable.get(4).getText()).isEqualTo(expectedAmount.toString());
    }

    /////////////////////////////////////////////////////////

    public void switchToANewWindow(String originalWindow, String secondaryWindow) {
        Iterator var2 = driver.getWindowHandles().iterator();

        while (var2.hasNext()) {
            String window = (String) var2.next();
            if (!window.equals(originalWindow) && !window.equals(secondaryWindow)) {
                driver.switchTo().window(window);
            }
        }
        driver.switchTo().defaultContent();
    }

    public void switchToANewWindow(String secondaryWindow) {
        Iterator var1 = driver.getWindowHandles().iterator();

        while (var1.hasNext()) {
            String window = (String) var1.next();
            if (!window.equals(secondaryWindow)) {
                driver.switchTo().window(window);
            }
        }
        driver.switchTo().defaultContent();
    }

}

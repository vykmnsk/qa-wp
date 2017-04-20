package com.tabcorp.qa.wagerplayer.pages;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.Iterator;

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

    @FindBy(css = "#trans_list > tbody > tr > td:nth-child(1)")
    public WebElement transactionIdValue;

    @FindBy(css = "#trans_list > tbody > tr > td:nth-child(5)")
    public WebElement amountInTable;

    private String windowOne;
    private String windowTwo;

    public void load() {
        windowOne = captureWindowHandle();
        switchToANewWindow(windowOne);
        driver.switchTo().defaultContent();
        wait.until(ExpectedConditions.visibilityOf(manualTabLink));
    }

    public void verifyLoaded() {
        verifyPageTitle("Transaction Deposit");
    }

    public void verifyPageTitle(String expectedTitle) {
        wait.until(ExpectedConditions.visibilityOf(pageTitle));
        Assertions.assertThat(pageTitle.getText()).isEqualTo(expectedTitle);
    }

    public void selectManualTab() {
        manualTabLink.click();
        waitForIframeAndSwitchToIt("deposit_screen");
    }

    public void selectTransactionType() {
        new Select(transactionType).selectByVisibleText("- Cash Deposit");
    }

    public void depositAndCurrencySelections(String cashAmount) {
        wait.until(ExpectedConditions.visibilityOf(amountTextBox));
        amountTextBox.sendKeys(cashAmount);
        windowTwo = captureWindowHandle();

        // below step commented as this field is missing from AWS6 but available in Load Test
        //selectOption(currency).from(customerDepositWindow.currency);
    }

    public void clickReadBack() {
        wait.until(ExpectedConditions.elementToBeClickable(readbackButton));
        readbackButton.click();
        switchToANewWindow(windowTwo, windowOne);
    }

    public void submitManualCashDeposit() {
        wait.until(ExpectedConditions.visibilityOf(cancel));
        wait.until(ExpectedConditions.elementToBeClickable(cancel));
        wait.until(ExpectedConditions.visibilityOf(submit));
        wait.until(ExpectedConditions.elementToBeClickable(submit));
        submit.click();
    }

    public String getTransactionDetails() {
        String actualSuccessMessage = successMessage.getText();
        assertThat(actualSuccessMessage).isEqualTo("Transaction processed successfully.");
        String transactionID = transactionId.getText();
        log.info("Transaction ID details=" + transactionID);
        return transactionID;
    }

    public void closeWindows() {
        close.click();
        switchBackToPreviousWindow(windowTwo);
    }

    public void verifyTransactionStatus(String transactionID, String amount) {
        depositsTabLink.click();
        waitForIframeAndSwitchToIt("deposit_screen");
        verifyTransactionIdsMatch(transactionID);

        verifyAmountDepositedMatches(amount);
        closeMiddleWindow();
        switchBackToPreviousWindow(windowOne);
        waitForIframeAndSwitchToIt("frame_bottom");
    }

    public void verifyTransactionIdsMatch(String transactionIDValue) {
        wait.until(ExpectedConditions.visibilityOf(transactionIdValue));
        assertThat(transactionIDValue).contains(transactionIdValue.getText());
    }

    public void verifyAmountDepositedMatches(String expectedAmount) {
        wait.until(ExpectedConditions.visibilityOf(amountInTable));
        assertThat(amountInTable.getText()).isEqualTo(expectedAmount);
    }

    /////////////////////////////////////////////////////////

    public void acceptAlert() {
        driver.switchTo().alert().accept();
    }

    public String captureWindowHandle() {
        return driver.getWindowHandle();
    }

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

    public  void switchBackToPreviousWindow(String window) {
        driver.switchTo().window(window);
    }

    public void closeMiddleWindow() {
        driver.close();
    }

    public void waitForIframeAndSwitchToIt(String frameId) {
        driver.switchTo().defaultContent();
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameId));
    }

}

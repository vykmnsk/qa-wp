package com.tabcorp.qa.wagerplayer.pages;

import com.tabcorp.qa.common.Helpers;
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

    @FindBy(css = "div.transaction_success")
    public WebElement successMessage;

    @FindBy(css = "div.transaction_success_message")
    public WebElement transactionId;

    @FindBy(css = "input.wp_button")
    public WebElement close;

    @FindBy(css = "a[id=deposits]")
    public WebElement depositsTabLink;

    @FindBy(css = "#trans_list tr td")
    public List<WebElement> transactionData;

    private String depositsWindow;
    private String manualTabWindow;

    public void load() {
        depositsWindow = driver.getWindowHandle();;
        switchToANewWindow(depositsWindow);
    }

    public void verifyLoaded() {
        HeaderPage deposit = new HeaderPage();
        deposit.verifyPageTitle("Transaction Deposit");
        wait.until(ExpectedConditions.visibilityOf(manualTabLink));
    }

    public void selectManualTab() {
        manualTabLink.click();
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("deposit_screen"));
        manualTabWindow = driver.getWindowHandle();
    }

    public void depositCash(BigDecimal amount) {
        new Select(transactionType).selectByVisibleText("- Cash Deposit");
        wait.until(ExpectedConditions.visibilityOf(amountTextBox));
        amountTextBox.sendKeys(amount.toString());

        // below step commented as this field is missing from AWS6 but available in Load Test
        //selectOption(currency).from(customerDepositWindow.currency);

        wait.until(ExpectedConditions.elementToBeClickable(readbackButton));
        readbackButton.click();
//        switchToANewWindow(manualTabWindow, depositsWindow);
        switchToANewWindow(depositsWindow);

        wait.until(ExpectedConditions.visibilityOf(cancel));
        wait.until(ExpectedConditions.elementToBeClickable(cancel));
        wait.until(ExpectedConditions.visibilityOf(submit));
        wait.until(ExpectedConditions.elementToBeClickable(submit));
        submit.click();

        acceptAlert();
    }

    public void verifyTransaction(BigDecimal amount) {
        String actualSuccessMessage = successMessage.getText();
        assertThat(actualSuccessMessage).isEqualTo("Transaction processed successfully.");
        String transactionID = transactionId.getText();
        log.info("Transaction " + transactionID);

        close.click();
        driver.switchTo().window(manualTabWindow);

        depositsTabLink.click();
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("deposit_screen"));
        verifyTransactionIdsMatch(transactionID);

        verifyAmountDepositedMatches(amount);
        driver.close();
        driver.switchTo().window(depositsWindow);
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame_bottom"));
    }

    public void verifyTransactionIdsMatch(String transactionIDValue) {
        wait.until(ExpectedConditions.visibilityOf(transactionData.get(0)));
        assertThat(transactionIDValue).contains(transactionData.get(0).getText());
    }

    public void verifyAmountDepositedMatches(BigDecimal expectedAmount) {
        wait.until(ExpectedConditions.visibilityOf(transactionData.get(4)));
        String actualAmountTxt = transactionData.get(4).getText();
        BigDecimal actualAmount = new BigDecimal(actualAmountTxt);
        assertThat(Helpers.roundOff(actualAmount)).isEqualTo(Helpers.roundOff(expectedAmount));
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

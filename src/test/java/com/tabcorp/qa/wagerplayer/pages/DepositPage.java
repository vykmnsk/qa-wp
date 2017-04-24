package com.tabcorp.qa.wagerplayer.pages;

import com.tabcorp.qa.common.Helpers;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    @FindBy(css = "div.transaction_success_message")
    public WebElement transactionMessage;

    @FindBy(css = "input.wp_button")
    public WebElement close;

    @FindBy(css = "a[id=deposits]")
    public WebElement depositsTabLink;

    @FindBy(css = "table#trans_list")
    public WebElement transactionTable;

    private String depositsWindow;
    private String manualTabWindow;

    public void load() {
        depositsWindow = driver.getWindowHandle();
        switchToNewWindow(Arrays.asList(depositsWindow));
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

    public String depositCash(BigDecimal amount) {
        new Select(transactionType).selectByVisibleText("- Cash Deposit");
        wait.until(ExpectedConditions.visibilityOf(amountTextBox));
        amountTextBox.sendKeys(amount.toString());

        // below step commented as this field is missing from AWS6 but available in Load Test
        //selectOption(currency).from(customerDepositWindow.currency);

        wait.until(ExpectedConditions.elementToBeClickable(readbackButton));
        readbackButton.click();
        switchToNewWindow(Arrays.asList(depositsWindow, manualTabWindow));

        wait.until(ExpectedConditions.visibilityOf(cancel));
        wait.until(ExpectedConditions.elementToBeClickable(cancel));
        wait.until(ExpectedConditions.visibilityOf(submit));
        wait.until(ExpectedConditions.elementToBeClickable(submit));
        submit.click();
        acceptAlert();

        String transMsg = transactionMessage.getText();
        log.info("Deposit transaction message=" + transMsg);

        close.click();
        return transMsg;
    }

    public void verifyTransactionRecord(String transMsg, BigDecimal expectedAmount, String expectedCurrency) {
        driver.switchTo().window(manualTabWindow);
        depositsTabLink.click();
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("deposit_screen"));

        wait.until(ExpectedConditions.visibilityOf(transactionTable));

        Map<String, String> row = Helpers.readTableRow(transactionTable);

        String errPrefix = "Deposit Transactions table: ";
        assertThat(transMsg).as(errPrefix + "checking correct row").contains(row.get("Trans ID"));
        BigDecimal actualAmount = new BigDecimal(row.get("Amount"));
        assertThat(Helpers.roundOff(actualAmount)).as(errPrefix + "amount").isEqualTo(Helpers.roundOff(expectedAmount));
        assertThat(row.get("Currency")).as(errPrefix + "currency").isEqualToIgnoringCase(expectedCurrency);

        driver.close();
        driver.switchTo().window(depositsWindow);
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame_bottom"));
    }

    private void switchToNewWindow(List<String> oldWindows) {
        String newWindow = driver.getWindowHandles().stream()
                .filter(w -> !oldWindows.contains(w))
                .findFirst().orElse(null);
        Assertions.assertThat(newWindow).isNotNull();
        driver.switchTo().window(newWindow);
        driver.switchTo().defaultContent();
    }

}

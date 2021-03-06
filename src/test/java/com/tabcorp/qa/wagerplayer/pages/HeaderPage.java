package com.tabcorp.qa.wagerplayer.pages;

import com.tabcorp.qa.common.Helpers;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class HeaderPage extends AppPage {
    @FindBy(css = ("table#main_table th"))
    public WebElement pageTitle;

    @FindBy(css = "#cat")
    private WebElement category;

    @FindBy(css = "#subcat")
    private WebElement subcategory;

    @FindBy(css = "#event_id")
    private WebElement event;

    @FindBy(css = "#button_settled")
    private WebElement buttonSettled;

    @FindBy(css = "input[value='2 Betting']")
    private WebElement f2;

    @FindBy(css = "input[value='3 Events']")
    private WebElement f3;

    @FindBy(css = "input[value='4 Markets']")
    private WebElement f4;

    @FindBy(css = "input[value='5 Liability']")
    private WebElement f5;

    @FindBy(css = "input[value='6 Settle']")
    private WebElement f6;

    @FindBy(css = "input[value='7 Config']")
    private WebElement f7;

    @FindBy(css = "input[value='8 Selection']")
    private WebElement f8;

    @FindBy(css = "input[value='9 BetData']")
    private WebElement f9;

    @FindBy(css = "input[value='10 Account']")
    private WebElement f10;

    @FindBy(css = "input[value='11 Cust']")
    private WebElement f11;

    @FindBy(css = "input[value='12 Agents']")
    private WebElement f12;

    @FindBy(css = "input[name= button_R]")
    private WebElement reloadPage;

    public void pickCategories(String catVal, String subcatVal) {
        switchToHeaderFrame();
        Helpers.retryOnFailure(()-> {
            wait.until(ExpectedConditions.visibilityOf(category));
            Select catSelect = new Select(category);
            catSelect.selectByVisibleText("Category"); //to refresh when repeated
            catSelect.selectByVisibleText(catVal);
            wait.until(ExpectedConditions.visibilityOf(subcategory));
            wait.until(ExpectedConditions.textToBePresentInElement(subcategory, subcatVal));
            (new Select(subcategory)).selectByVisibleText(subcatVal);
        }, 3, 1);
    }

    public void pickEvent(String catVal, String subcatVal, String eventVal) {
        Helpers.retryOnFailure(()-> {
            pickCategories(catVal, subcatVal);
            wait.until(ExpectedConditions.visibilityOf(event));
            wait.until(ExpectedConditions.textToBePresentInElement(event, eventVal));
            (new Select(event)).selectByVisibleText(eventVal);
            Assertions.assertThat(event.getText()).as("Event Name").contains(eventVal);
        }, 3, 1);
    }

    public void navigateToF3(String catVal, String subcatVal) {
        pickCategories(catVal, subcatVal);
        f3.click();
    }

    public MarketsPage navigateToF4() {
        switchToHeaderFrame();
        f4.click();
        MarketsPage marketsPage = new MarketsPage();
        marketsPage.load();
        return marketsPage;
    }

    public LiabilityPage navigateToF5() {
        switchToHeaderFrame();
        f5.click();
        LiabilityPage liabilityPage = new LiabilityPage();
        liabilityPage.load();
        return liabilityPage;
    }

    public SettlementPage navigateToF6() {
        switchToHeaderFrame();
        f6.click();
        SettlementPage settlementPage = new SettlementPage();
        settlementPage.load();
        return settlementPage;
    }

    public CustomersPage navigateToF11() {
        switchToHeaderFrame();
        f11.click();
        CustomersPage cp = new CustomersPage();
        cp.load();
        return cp;
    }

    private void switchToHeaderFrame() {
        driver.switchTo().defaultContent();
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame_top"));
    }

    public void refreshPage() {
        switchToHeaderFrame();
        assertThat(reloadPage.isDisplayed());
        reloadPage.click();
        driver.switchTo().defaultContent();
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame_bottom"));
    }

    public void verifyPageTitle(String expectedTitle) {
        wait.until(ExpectedConditions.visibilityOf(pageTitle));
        Assertions.assertThat(pageTitle.getText()).isEqualTo(expectedTitle);
    }

    public void deSelectSettled() {
        switchToHeaderFrame();
        wait.until(ExpectedConditions.visibilityOf(buttonSettled));
        String classForSettled = "high_style";
        String classForUnSettled = "low_style";
        if (buttonSettled.getAttribute("class").contains(classForSettled)) {
            buttonSettled.click();
            wait.until(ExpectedConditions.attributeContains(buttonSettled, "class", classForUnSettled));
        }

    }
}

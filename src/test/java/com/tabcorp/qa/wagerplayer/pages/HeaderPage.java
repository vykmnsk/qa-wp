package com.tabcorp.qa.wagerplayer.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class HeaderPage extends AppPage {

    @FindBy(css = "#cat")
    public WebElement category;

    @FindBy(css = "#subcat")
    public WebElement subcategory;

    @FindBy(css = "#event_id")
    public WebElement event;

    @FindBy(css = "#button_settled")
    public WebElement buttonSettled;

    @FindBy(css = "input[value='2 Betting']")
    public WebElement f2;

    @FindBy(css = "input[value='3 Events']")
    public WebElement f3;

    @FindBy(css = "input[value='4 Markets']")
    public WebElement f4;

    @FindBy(css = "input[value='5 Liability']")
    public WebElement f5;

    @FindBy(css = "input[value='6 Settle']")
    public WebElement f6;

    @FindBy(css = "input[value='7 Config']")
    public WebElement f7;

    @FindBy(css = "input[value='8 Selection']")
    public WebElement f8;

    @FindBy(css = "input[value='9 BetData']")
    public WebElement f9;

    @FindBy(css = "input[value='10 Account']")
    public WebElement f10;

    @FindBy(css = "input[value='11 Cust']")
    public WebElement f11;

    @FindBy(css = "input[value='12 Agents']")
    public WebElement f12;

    @FindBy(css = "#header_table > tbody > tr:nth-child(2) > td:nth-child(3) > input:nth-child(1)")
    public WebElement reloadPage;

    public void pickCategories(String catVal, String subcatVal) {
        switchToHeaderFrame();
        wait.until(ExpectedConditions.visibilityOf(category));
        (new Select(category)).selectByVisibleText(catVal);
        wait.until(ExpectedConditions.visibilityOf(subcategory));
        wait.until(ExpectedConditions.textToBePresentInElement(subcategory, subcatVal));
        (new Select(subcategory)).selectByVisibleText(subcatVal);
        f3.click();
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

    private void switchToHeaderFrame() {
        driver.switchTo().defaultContent();
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame_top"));
    }

}

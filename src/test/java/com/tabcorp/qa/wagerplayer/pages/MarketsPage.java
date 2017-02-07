package com.tabcorp.qa.wagerplayer.pages;


import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class MarketsPage extends AppPage {

    @FindBy(css = "#main_table tbody tr th")
    WebElement header;
    @FindBy(css = "input[src='images/button_insert.gif'")
    WebElement insertBtn;
    @FindBy(css = "input[src='images/button_update.gif'")
    WebElement updateBtn;

    @FindBy(css = "table > tbody > tr:nth-child(7) > td > font:nth-child(1) > b")
    WebElement marketStatus;
    @FindBy(css = "#main_table tr[bgcolor='green'")
    WebElement status;

    @FindBy(css = ("#market_details"))
    WebElement marketDetailsSection;
    @FindBy(css = "table.inner_table a[href*='market_details']")
    WebElement showHideMarketDetails;

    @FindBy(css = ("#market_settings"))
    WebElement marketManagementSection;
    @FindBy(css = "table.inner_table a[href*='market_settings']")
    WebElement showHideMarketManagement;

    @FindBy(css = "input[type='radio'][name='MARKET_TYPE_SET_STATUS'][value='3'")
    WebElement marketStatusLive;
    @FindBy(css = "input[type='radio'][name='MARKET_TYPE_SET_STATUS'][value='5'")
    WebElement marketStatusHeld;

    @FindBy(css = ("select[name='MARKET_TYPE_SET_ALLOW_WIN']"))
    WebElement betsAllowedWinSel;

    @FindBy(css = ("select[name='MARKET_TYPE_SET_ALLOW_PLACE']"))
    WebElement betsAllowedPlaceSel;

    @FindBy(css = ("select[name='MARKET_TYPE_SET_EACHWAY_PERCENT']"))
    WebElement placeFractionSel;

    @FindBy(css = ("select[name='MARKET_TYPE_SET_EACHWAY_SELECTIONS']"))
    WebElement numOfPlacesSel;

    @FindBy(css = ("input[name='MARKET_TYPE_SET_ALLOW_EACHWAY']"))
    WebElement ewChk;

    By positionLocators = By.cssSelector("input[name^='position[']");
    By priceLocators = By.cssSelector("input[name^='price[']");

    public void vefifyLoaded() {
        wait.until(ExpectedConditions.visibilityOf(header));
        Assertions.assertThat(header.getText()).as("Markets Page header").isEqualTo("Markets");
    }


    public void enterPrices(List<String> priceVals) {
        List<WebElement> positions = driver.findElements(positionLocators);
        List<WebElement> prices = driver.findElements(priceLocators);
        Assertions.assertThat(positions.size()).as("position size matches prices size").isEqualTo(prices.size());
        Assertions.assertThat(prices.size()).as("price elems size matches cucumber price values size").isEqualTo(priceVals.size());
        for (int i = 0; i < positions.size(); i++) {
            WebElement pos = positions.get(i);
            WebElement price = prices.get(i);
            pos.sendKeys(String.valueOf(i + 1));
            wait.until(ExpectedConditions.elementToBeClickable(price));
            price.clear();
            price.sendKeys(priceVals.get(i));
        }
        insertBtn.click();
    }

    public void verifySuccessStatus(String msg) {
        wait.until(ExpectedConditions.visibilityOf(status));
        String statusMsg = status.getText();
        Assertions.assertThat(statusMsg).as("market status").contains("SUCCESS");
        Assertions.assertThat(statusMsg).as("market status message").contains(msg);
    }

    public void showMarketDetails() {
        if (!marketDetailsSection.isDisplayed()) {
            showHideMarketDetails.click();
        }
        wait.until(ExpectedConditions.visibilityOf(marketDetailsSection));
    }


    public void enterMarketDetail(boolean isLive, String betsAllowedWin, String betsAllowedPlace, String placeFraction, String numOfPlaces, boolean isEW) {
        if (isLive) {
            marketStatusLive.click();
        } else {
            marketStatusHeld.click();
        }
        new Select(betsAllowedWinSel).selectByVisibleText(betsAllowedWin);
        new Select(betsAllowedPlaceSel).selectByVisibleText(betsAllowedPlace);
        new Select(placeFractionSel).selectByVisibleText(placeFraction);
        new Select(numOfPlacesSel).selectByVisibleText(numOfPlaces);
        if (!ewChk.isSelected() && isEW) {
            ewChk.click();
        }
        updateBtn.click();
    }
}

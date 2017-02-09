package com.tabcorp.qa.wagerplayer.pages;


import cucumber.api.PendingException;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Stream;
import java.util.List;
import java.util.Random;

public class MarketsPage extends AppPage {

    @FindBy(css = "#main_table tbody tr th")
    WebElement header;
    @FindBy(css = "input[src='images/button_insert.gif'")
    WebElement insertBtn;
    @FindBy(css = "input[src='images/button_update.gif'")
    WebElement updateBtn;

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

    @FindBy(css = "#market_settings input[name='race_num']")
    @CacheLookup
    WebElement raceNumTxt;

    @FindBy(css = "input[name^='position[']")
    List<WebElement> positionTxts;
    @FindBy(css = "input[type=text][id^=price_][onkeyup^=calculate_selection_percent")
    List<WebElement> priceTxts;

    @FindBy(css = "#bet_types_content_body table.product_options tbody tr")
    @CacheLookup
    public List<WebElement> productRows;


    public void verifyLoaded() {
        wait.until(ExpectedConditions.visibilityOf(header));
        Assertions.assertThat(header.getText()).as("Markets Page header").isEqualTo("Markets");
    }

    public void enterPrices() {
        Random rand = new Random();
        double price;
        Assertions.assertThat(positionTxts.size()).as("position size matches prices size").isEqualTo(priceTxts.size());
        for (int i = 0; i < positionTxts.size(); i++) {
            WebElement pos = positionTxts.get(i);
            WebElement priceTxt = priceTxts.get(i);
            pos.sendKeys(String.valueOf(i + 1));
            priceTxt.clear();
            price = (rand.nextInt(50) * 10) / 10.0;
            priceTxt.sendKeys(Double.toString(price));
        }
        insertBtn.click();
    }

    public void showMarketManagement() {
        if (!marketManagementSection.isDisplayed()) {
            showHideMarketManagement.click();
        }
        wait.until(ExpectedConditions.visibilityOf(marketManagementSection));
    }

    public void updateRaceNumber(String num){
        raceNumTxt.clear();
        raceNumTxt.sendKeys(num);
    }


//    protected static Map<Integer, String> extJava8() {
//        return Collections.unmodifiableMap(Stream.of(
//                entry(0, "zero"),
//                entry(1, "one"),
//                entry(2, "two"),
//                entry(12, "twelve")).
//                collect(entriesToMap()));
//    }

    static Map<List<String>, String> cukeToUI(String prodType) {
        HashMap <List<String>, String> c2ui = new HashMap<>();
        if ("Win / Place".equals(prodType)){
            c2ui.put(Arrays.asList("Betting", "Enabled", "On"), "[2][enabled]");
            c2ui.put(Arrays.asList("Betting", "Enabled", "Auto"), "[1][auto]");
            c2ui.put(Arrays.asList("Betting", "Display Price", "Win"), "[2][win_display_price]");
            c2ui.put(Arrays.asList("Betting", "Display Price", "Place"), "[2][place_display_price]");
            c2ui.put(Arrays.asList("Betting", "Enable Single", "Win"), "[2][win_enabled]");
            //..
            c2ui.put(Arrays.asList("Liability", "Display Price", "Win"), "[1][win_display_price]");
        } else {
            throw new PendingException();
        }
        return c2ui;
    }

    public void enableProductSettings(String prodType, String prodName, List<List<String>> settings){
        WebElement tr = productRows.stream().filter(p -> p.getText().contains("PA SP")).findFirst().orElse(null);
        Assertions.assertThat(tr).as("Product %s is not found on Market Page", prodName).isNotNull();
        for (List<String> option: settings) {
            setOption(prodType, tr, option);
        }
    }

    private void setOption(String prodType, WebElement prodRow, List<String> option) {
        String uiId = cukeToUI(prodType).get(option);
        String inputCSS = String.format("input[name$='%s']", uiId);
        WebElement hiddenChk = prodRow.findElement(By.cssSelector(inputCSS));
        WebElement cell = findParent(hiddenChk);
        String imageFile = cell.findElement(By.tagName("img")).getAttribute("src");
        if (imageFile.endsWith("unselected.png")) {
            cell.click();
        }
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

    public void verifySuccessStatus(String msg) {
        wait.until(ExpectedConditions.visibilityOf(status));
        String statusMsg = status.getText();
        Assertions.assertThat(statusMsg).as("market status").contains("SUCCESS");
        Assertions.assertThat(statusMsg).as("market status message").contains(msg);
    }

}

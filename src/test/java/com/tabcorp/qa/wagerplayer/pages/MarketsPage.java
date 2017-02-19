package com.tabcorp.qa.wagerplayer.pages;


import com.tabcorp.qa.common.Helpers;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class MarketsPage extends AppPage {

    @FindBy(css = "#main_table tbody tr th")
    WebElement header;
    @FindBy(css = "input[src='images/button_insert.gif'")
    WebElement insertBtn;
    @FindBy(css = "input[src='images/button_update.gif'")
    WebElement updateBtn;

    @FindBy(css = "#main_table tr[bgcolor='green'")
    WebElement status;

    @FindBy(css = "td.market_status_cell a[id^='market_status']")
    WebElement marketStatus;

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

    @FindBy(css = ("select[id='MARKET_TYPE_SET_EACHWAY_PERCENT']"))
    WebElement placeFractionSel;

    @FindBy(css = ("select[id='MARKET_TYPE_SET_EACHWAY_SELECTIONS']"))
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
        assertThat(header.getText()).as("Markets Page header").isEqualTo("Markets");
    }

    public void enterPrices(int count) {
        List<String> prices = Helpers.generateRandomPrices(0, 100, count);
        List<Integer> sizes = Arrays.asList(positionTxts.size(), priceTxts.size(), prices.size());
        Integer size0 = sizes.get(0);
        assertThat(sizes).as("position elements, price elements and prices counts are the same").allMatch(size0::equals);
        for (int i = 0; i < positionTxts.size(); i++) {
            WebElement pos = positionTxts.get(i);
            WebElement priceTxt = priceTxts.get(i);
            String priceVal = prices.get(i);
            pos.sendKeys(String.valueOf(i + 1));
            priceTxt.clear();
            priceTxt.sendKeys(priceVal);
        }
        insertBtn.click();
    }

    public void showMarketManagement() {
        if (!marketManagementSection.isDisplayed()) {
            showHideMarketManagement.click();
        }
        wait.until(ExpectedConditions.visibilityOf(marketManagementSection));

    }

    public void updateRaceNumber(String num) {
        raceNumTxt.clear();
        raceNumTxt.sendKeys(num);
    }

    static Map<List<String>, String> productSettingIDs() {
        return Collections.unmodifiableMap(Stream.of(
                new SimpleEntry<>(Arrays.asList("Betting", "Enabled", "On"), "[2][enabled]"),
                new SimpleEntry<>(Arrays.asList("Betting", "Enabled", "Auto"), "[1][auto]"),
                new SimpleEntry<>(Arrays.asList("Betting", "Display Price", "Win"), "[2][win_display_price]"),
                new SimpleEntry<>(Arrays.asList("Betting", "Display Price", "Place"), "[2][place_display_price]"),
                new SimpleEntry<>(Arrays.asList("Betting", "Display Price", "Fluc"), "[2][display_fluctuations]"),
                new SimpleEntry<>(Arrays.asList("Betting", "Enable Single", "Win"), "[2][win_enabled]"),
                new SimpleEntry<>(Arrays.asList("Betting", "Enable Single", "Place"), "[2][place_enabled]"),
                new SimpleEntry<>(Arrays.asList("Betting", "Enable Single", "EW"), "[2][eachway]"),
                new SimpleEntry<>(Arrays.asList("Betting", "Display Column", "Win"), "[2][display_column_win]"),
                new SimpleEntry<>(Arrays.asList("Betting", "Display Column", "Place"), "[2][display_column_place]"),
                new SimpleEntry<>(Arrays.asList("Betting", "Display Column", "EW"), "[2][display_column_eachway]"),
                new SimpleEntry<>(Arrays.asList("Betting", "Enable Multi", "Win"), "[2][multi_win]"),
                new SimpleEntry<>(Arrays.asList("Betting", "Enable Multi", "Place"), "[2][multi_place]"),
                new SimpleEntry<>(Arrays.asList("Betting", "Enable Multi", "EW"), "[2][multi_eachway]"),
                new SimpleEntry<>(Arrays.asList("Betting", "Disp. Result", "Win"), "[2][display_result_win]"),
                new SimpleEntry<>(Arrays.asList("Betting", "Disp. Result", "Place"), "[2][display_result_place]"),

                new SimpleEntry<>(Arrays.asList("Liability", "Display Price", "Win"), "[1][win_display_price]"),
                new SimpleEntry<>(Arrays.asList("Liability", "Display Price", "Place"), "[1][place_display_price]"),
                new SimpleEntry<>(Arrays.asList("Liability", "Single", "Win"), "[1][win_enabled]"),
                new SimpleEntry<>(Arrays.asList("Liability", "Single", "Place"), "[1][place_enabled]"),
                new SimpleEntry<>(Arrays.asList("Liability", "Base", "Win"), "[1][win_base_display_price]"),
                new SimpleEntry<>(Arrays.asList("Liability", "Final Leg Multi", "Win"), "[1][final_leg_win]"),
                new SimpleEntry<>(Arrays.asList("Liability", "Final Leg Multi", "Place"), "[1][final_leg_place]"),
                new SimpleEntry<>(Arrays.asList("Liability", "Multi", "Win"), "[1][multi_win]"),
                new SimpleEntry<>(Arrays.asList("Liability", "Multi", "Place"), "[1][multi_place]"),
                new SimpleEntry<>(Arrays.asList("Liability", "Betback", "Win"), "[1][betback_win]"),
                new SimpleEntry<>(Arrays.asList("Liability", "Betback", "Place"), "[1][betback_place]"),
                new SimpleEntry<>(Arrays.asList("Liability", "Cash Out", "Win"), "[1][liab_single_win_cashout]"),
                new SimpleEntry<>(Arrays.asList("Liability", "Cash Out", "Place"), "[1][liab_single_place_cashout]"),
                new SimpleEntry<>(Arrays.asList("Liability", "Cash Out", "Multi Win"), "[1][liab_multi_win_cashout]"),
                new SimpleEntry<>(Arrays.asList("Liability", "Cash Out", "Multi Place"), "[1][liab_multi_place_cashout]"),
                new SimpleEntry<>(Arrays.asList("Defaults", "Display", "Web"), "[2][is_default]"),
                new SimpleEntry<>(Arrays.asList("Defaults", "Display", "F2"), "[1][is_default]")
        ).collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue)));
    }

    public void enableProductSettings(String prodName, List<List<String>> settings) {
        showMarketManagement();
        WebElement tr = productRows.stream().filter(p -> p.getText().contains(prodName)).findFirst().orElse(null);
        assertThat(tr).as("Product %s is not found on Market Page", prodName).isNotNull();
        for (List<String> option : settings) {
            setOption(tr, option);
        }
    }

    private void setOption(WebElement prodRow, List<String> option) {
        String inputCSS = String.format("input[name$='%s']", noNullKey(productSettingIDs(), option));
        WebElement hiddenChk = prodRow.findElement(By.cssSelector(inputCSS));
        WebElement cell = findParent(hiddenChk);
        String imageFile = cell.findElement(By.tagName("img")).getAttribute("src");
        if (imageFile.endsWith("unselected.png")) {
            scrollTo(cell);
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

    public void verifyMarketStatus(String expectedStatus) {
        String actualStatus = marketStatus.getAttribute("status");
        AssertionsForClassTypes.assertThat(actualStatus)
                .withFailMessage(String.format("Actual status %s doesn't match with expected status %s", actualStatus, expectedStatus))
                .isEqualTo(expectedStatus);
    }

}

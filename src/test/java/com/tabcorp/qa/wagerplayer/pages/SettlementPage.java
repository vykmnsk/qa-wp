package com.tabcorp.qa.wagerplayer.pages;

import com.tabcorp.qa.common.BetType;
import com.tabcorp.qa.common.Helpers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SettlementPage extends AppPage {
    @FindBy(css = "table[id^='results_table_']")
    WebElement resultsTable;

    @FindBy(css = "input[id^='settle_market'] ")
    WebElement winnerCheck;

    @FindBy(css = "select[name^='result_position']")
    List<WebElement> resultPositions;

    @FindBy(css = "select[name^='settle_result']")
    List<WebElement> resultRunners;

    @FindBy(id = "result")
    WebElement result;

    @FindBy(id = "accept_button")
    WebElement accept;

    @FindBy(id = "settle")
    WebElement settle;

    @FindBy(css = ".f6_settle_live_products input[id='precise_price']")
    List<WebElement> settlePrices;

    private By priceSelector = By.cssSelector("input[id^='price']");

    public void load() {
        driver.switchTo().defaultContent();
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame_bottom"));
        wait.until(ExpectedConditions.visibilityOf(resultsTable));
    }

    public void resultRace(Map<String, String> winners) {
        int count = 0;
        winnerCheck.click();
        for (Map.Entry<String, String> winner : winners.entrySet()) {
            Select position = new Select(resultPositions.get(count));
            String winnerName = winner.getValue() + " " + Helpers.toTitleCase(winner.getKey());
            Select result = new Select(resultRunners.get(count));
            position.selectByVisibleText(winner.getValue());
            //example name is 1 Runner_1, cucumber is sending only 1
            result.selectByVisibleText(winnerName);
            count++;
        }

        result.click();

    }

    public void accept() {
        load();
        wait.until(ExpectedConditions.visibilityOf(accept));
        accept.click();
    }

    public void settle() {
        load();
        wait.until(ExpectedConditions.visibilityOf(settle));
        settle.click();
    }

    public void updateSettlePrices(Map<String, String> winnerPrices, Integer productId) {
        winnerPrices.entrySet().forEach(winnerPrice -> {
                    List<BigDecimal> prices = Helpers.extractCSVPrices(winnerPrice.getValue());
                    int betypeId = winnerPrice.getKey().equalsIgnoreCase("win") ? BetType.WIN.id : BetType.PLACE.id;
                    List<WebElement> matchingPriceHiddenFields = getMatchingPrices(productId, betypeId);
                    setPrices(prices, matchingPriceHiddenFields);
                }
        );
    }

    private List<WebElement> getMatchingPrices(int productId, int betypeId) {
        Predicate<WebElement> priceFilter = getProductPriceFilter(productId, betypeId);
        return settlePrices
                .stream()
                .filter(settlePrice -> priceFilter.test(settlePrice))
                .collect(Collectors.toList());

    }

    private void setPrices(List<BigDecimal> prices, List<WebElement> pricesHiddenFields) {
        int size = Math.min(prices.size(), pricesHiddenFields.size());
        IntStream.range(0, size)
                .forEach(i -> {
                                WebElement priceInputField = findParent(pricesHiddenFields.get(i)).findElement(priceSelector);
                                inputPrice(priceInputField, prices.get(i));
                        }
                );
    }

    private Predicate<WebElement> getProductPriceFilter(Integer productId, int betypeId) {
        String productFilter = String.format("[%d]", productId);
        String betTyeFilter = String.format("[%s]", betypeId);
        Predicate<WebElement> filterPredicate = settlePrice ->
                settlePrice.getAttribute("name").contains(productFilter) &&
                        settlePrice.getAttribute("name").contains(betTyeFilter);
        return filterPredicate;
    }

    private void inputPrice(WebElement priceInputField, BigDecimal price) {
        priceInputField.clear();
        priceInputField.sendKeys(price.toString());
    }

}

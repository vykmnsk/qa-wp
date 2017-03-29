package com.tabcorp.qa.wagerplayer.pages;

import com.tabcorp.qa.common.BetType;
import com.tabcorp.qa.common.Helpers;
import com.tabcorp.qa.wagerplayer.Config;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @FindBy(css = "input[name^=product_prices]")
    List<WebElement> luxbetSettlePrices;

    @FindBy(css = ".f6_settle_live_products input[type=hidden][id='precise_price']")
    List<WebElement> hiddenSettlePrices;
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


    public void updateSettlePrices(Integer productId, Integer betTypeId, List<BigDecimal> prices){
        List<WebElement> priceInputs;
        if ("redbook".equals(Config.appName())) {
            List<WebElement> hiddenSettlePricesToUpdate = filterWithIds(hiddenSettlePrices, productId, betTypeId);
            priceInputs = findCorrespondingPriceInputs(hiddenSettlePricesToUpdate, priceSelector);
        } else {
            priceInputs = filterWithIds(luxbetSettlePrices, productId, betTypeId);
        }
        enterPrices(priceInputs, prices);
    }

    private List<WebElement> filterWithIds(List<WebElement> elems, Integer prodId, Integer betTypeId){
        return elems.stream()
                .filter(el -> nameContainsId(el, prodId) && (betTypeId == BetType.Exotic.id || nameContainsId(el, betTypeId) ))
                .collect(Collectors.toList());
    }

    private boolean nameContainsId(WebElement el, int id) {
        return el.getAttribute("name").contains("[" + id + "]");
    }

    private List<WebElement> findCorrespondingPriceInputs(List<WebElement> hiddenPriceElems, By priceSelector) {
        return hiddenPriceElems.stream()
                .map(el -> findParent(el).findElement(priceSelector))
                .collect(Collectors.toList());
    }

    private void enterPrices(List<WebElement> priceInputs, List<BigDecimal> prices) {
        Assertions.assertThat(prices.size()).as("settle price values should fit into UI price inputs").isLessThanOrEqualTo(priceInputs.size());
        for(int i = 0; i < prices.size(); i++){
            WebElement input = priceInputs.get(i);
            String price = prices.get(i).toString();
            input.clear();
            input.sendKeys(price);
        }
    }

}

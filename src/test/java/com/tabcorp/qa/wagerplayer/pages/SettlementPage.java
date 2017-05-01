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
import java.util.function.Predicate;
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

    @FindBy(css = "result")
    WebElement result;

    @FindBy(css = "accept_button")
    WebElement accept;

    @FindBy(css = "settle")
    WebElement settle;

    @FindBy(css = "td[valign=top] > table[id=no_padding_table_inner]")
    WebElement exoticsPriceTable;

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

    public void updateExoticPrices() {

    }

    public void resultRace(Map<String, String> winners) {
        int count = 0;
        wait.until(ExpectedConditions.elementToBeClickable(winnerCheck));
        winnerCheck.click();
        for (Map.Entry<String, String> winner : winners.entrySet()) {
            String positionVal = winner.getValue();
            WebElement positionEl = resultPositions.get(count);
            wait.until(ExpectedConditions.textToBePresentInElement(positionEl, positionVal));
            new Select(positionEl).selectByVisibleText(positionVal);
            //example: "Runner01"
            String visibleRunnerVal = Helpers.toTitleCase(winner.getKey());
            WebElement runnerEl = resultRunners.get(count);
            wait.until(ExpectedConditions.textToBePresentInElement(runnerEl, visibleRunnerVal));
            selectByPartialVisibleText(runnerEl, visibleRunnerVal);
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
        if (Config.REDBOOK.equals(Config.appName())) {
            List<WebElement> hiddenSettlePricesToUpdate = filterWithIds(hiddenSettlePrices, productId, betTypeId);
            priceInputs = findCorrespondingPriceInputs(hiddenSettlePricesToUpdate, priceSelector);
        } else {
            priceInputs = filterWithIds(luxbetSettlePrices, productId, betTypeId);
        }
        enterPrices(priceInputs, prices);
    }

    private List<WebElement> filterWithIds(List<WebElement> elems, Integer prodId, Integer betTypeId){
        Predicate<WebElement> containsProdId = el -> nameContainsId(el, prodId);
        Predicate<WebElement> containsProdBetTypeIds = el -> nameContainsId(el, prodId) && nameContainsId(el, betTypeId);
        Predicate<WebElement> containsIds = (betTypeId == BetType.Exotic.id) ? containsProdId : containsProdBetTypeIds;
        return elems.stream().filter(containsIds).collect(Collectors.toList());
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

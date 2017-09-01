package com.tabcorp.qa.wagerplayer.pages;

import com.tabcorp.qa.common.BetType;
import com.tabcorp.qa.common.Helpers;
import com.tabcorp.qa.wagerplayer.Config;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class SettlementPage extends AppPage {
    @FindBy(css = "table[id^='results_table_']")
    WebElement resultsTable;

    @FindBy(css = "input[id^='settle_market'] ")
    WebElement winnerRacingCheck;

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

    @FindBy(css = "td[valign=top] > table#no_padding_table_inner")
    WebElement exoticsPriceTable;

    @FindBy(css = "input[name^=product_prices]")
    List<WebElement> luxbetSettlePrices;

    @FindBy(css = ".f6_settle_live_products input[type=hidden][id='precise_price']")
    List<WebElement> hiddenSettlePrices;

    @FindBy(css = "table[id=deductions_table] > tbody > tr")
    List<WebElement> deductionsRows;

    @FindBy(css = "td[align=left] table#no_padding_table_inner")
    WebElement resultPricesTable;

    @FindBy(css = "input[id=settle]")
    WebElement disabledSettleButton;

    @FindBy(css = "input[name^=home_score][onblur*=ft]")
    WebElement firstPlayerFT;

    @FindBy(css = "input[name^=away_score][onblur*=ft]")
    WebElement secondPlayerFT;

    @FindBy(css = "input[name^=settle_market]")
    WebElement winnerSportCheck;

    @FindBy(css = "font[color=green]")
    WebElement resultedLabel;

    @FindBy(css ="#main_table tr th")
    WebElement pageHeader;

    private By priceSelector = By.cssSelector("input[id^='price']");

    public void load() {
        driver.switchTo().defaultContent();
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame_bottom"));
        wait.until(ExpectedConditions.visibilityOf(pageHeader));
        assertThat(pageHeader.getText()).as("Page Header").isEqualTo("Settle");
    }

    public void resultRace(Map<String, String> winners) {
        wait.until(ExpectedConditions.visibilityOf(resultsTable));
        int count = 0;
        wait.until(ExpectedConditions.elementToBeClickable(winnerRacingCheck));
        winnerRacingCheck.click();
        for (Map.Entry<String, String> winner : winners.entrySet()) {
            String positionVal = winner.getValue();
            WebElement positionEl = resultPositions.get(count);
            wait.until(ExpectedConditions.textToBePresentInElement(positionEl, positionVal));
            new Select(positionEl).selectByVisibleText(positionVal);
            //example: "Runner01"
            String visibleRunnerVal = Helpers.toTitleCase(winner.getKey());
            WebElement runnerEl = resultRunners.get(count);
            try {
                wait.until(ExpectedConditions.textToBePresentInElement(runnerEl, visibleRunnerVal));
            } catch (TimeoutException te){
                Assertions.fail("No winner name '%s' found in result dropdown: %s", visibleRunnerVal, runnerEl.getText());
            }
            selectByPartialVisibleText(runnerEl, visibleRunnerVal);
            Assertions.assertThat(runnerEl.getText()).as("Runner Name").contains(visibleRunnerVal);
            count++;
        }
        result.click();
        wait.until(ExpectedConditions.visibilityOf(disabledSettleButton));
    }

    public void resultSport(List<String> scores) {
        wait.until(ExpectedConditions.visibilityOf(firstPlayerFT));
        firstPlayerFT.sendKeys(scores.get(0));
        wait.until(ExpectedConditions.visibilityOf(secondPlayerFT));
        secondPlayerFT.sendKeys(scores.get(1));
        winnerSportCheck.click();
        result.click();
        wait.until(ExpectedConditions.visibilityOf(resultedLabel));
    }

    public void accept() {
        load();
        wait.until(ExpectedConditions.visibilityOf(accept));
        scrollTo(accept);
        accept.click();
    }

    public void settle() {
        load();
        wait.until(ExpectedConditions.visibilityOf(settle));
        scrollTo(settle);
        settle.click();
    }


    public void updateExoticPrices(List<List<String>> priceData) {
        List<WebElement> rows = exoticsPriceTable.findElements(By.cssSelector("tbody tr"));
        WebElement headerRow = rows.stream().filter(r -> r.getText().startsWith("Exotics")).findFirst().orElse(null);
        List<String> headers = Helpers.collectElementsTexts(headerRow, By.tagName("td"));

        for (List<String> priceEntry : priceData) {
            String colName = priceEntry.get(0);
            String rowName = priceEntry.get(1);
            String value = priceEntry.get(2);
            WebElement row = rows.stream().filter(r -> r.getText().startsWith(rowName)).findFirst().orElse(null);
            List<WebElement> inputs = row.findElements(By.tagName("input"));
            Map<String, WebElement> inputsWithHeaders = Helpers.zipToMap(headers, inputs);
            WebElement input = inputsWithHeaders.get(colName);
            input.clear();
            input.sendKeys(value);
        }
        result.click();
    }

    public void updateSettlePrices(Integer productId, Integer betTypeId, List<BigDecimal> prices) {
        List<WebElement> priceInputs;
        if (Config.isRedbook()) {
            List<WebElement> hiddenSettlePricesToUpdate = filterWithIds(hiddenSettlePrices, productId, betTypeId);
            priceInputs = findCorrespondingPriceInputs(hiddenSettlePricesToUpdate, priceSelector);
        } else {
            priceInputs = filterWithIds(luxbetSettlePrices, productId, betTypeId);
        }
        enterPrices(priceInputs, prices);
    }

    private List<WebElement> filterWithIds(List<WebElement> elems, Integer prodId, Integer betTypeId) {
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
        for (int i = 0; i < prices.size(); i++) {
            WebElement input = priceInputs.get(i);
            String price = prices.get(i).toString();
            input.clear();
            input.sendKeys(price);
        }
    }

    public void verifyRunnerDeductions(List<List<String>> expectedDeductions) {
        By textInputSelector = By.cssSelector("td input[type=text]");
        Predicate<WebElement> containsTextInputs = el -> el.findElements(textInputSelector).size() > 0;
        List<WebElement> dataRows = deductionsRows.stream().filter(containsTextInputs).collect(Collectors.toList());

        List<List<String>> actualDeductions = new LinkedList<>();
        for (WebElement row : dataRows) {
            String text = row.getText();
            List<WebElement> inputs = row.findElements(textInputSelector);
            List<String> rowValues = Stream.concat(
                    Stream.of(text),
                    inputs.stream().map(i -> i.getAttribute("value"))
            ).collect(Collectors.toList());
            actualDeductions.add(rowValues);
        }
        assertThat(actualDeductions).as("Deductions data").isEqualTo(expectedDeductions);
    }

    public void updateFixedPrices(List<BigDecimal> winPrices, List<BigDecimal> placePrices) {
        final String fixedProdName = "Luxbook DVP";
        final By inputSelector = By.cssSelector("td > input[type=text]");

        List<WebElement> allRows = resultPricesTable.findElements(By.tagName("tr"));
        List<String> headers = Helpers.collectElementsTexts(allRows.get(0), By.cssSelector("td.base_product_heading"));
        List<WebElement> inputRows = allRows.stream()
                .filter(r -> r.findElements(inputSelector).size() > 0)
                .collect(Collectors.toList());
        assertThat(inputRows.size()).isEqualTo(winPrices.size()).isEqualTo(placePrices.size());

        for (int i = 0; i < inputRows.size(); i++) {
            List<WebElement> rowInputs = inputRows.get(i).findElements(inputSelector);
            List<Pair> inputsWithHeaders = Helpers.zipToPairs(headers, rowInputs);
            List<WebElement> fixedInputs = inputsWithHeaders.stream()
                    .filter(pair -> ((String) pair.getKey()).startsWith(fixedProdName))
                    .map(p -> (WebElement) p.getValue()).collect(Collectors.toList());
            assertThat(fixedInputs.size()).isEqualTo(2);
            WebElement winFixedInput = fixedInputs.get(0);
            WebElement placeFixedInput = fixedInputs.get(1);
            winFixedInput.clear();
            winFixedInput.sendKeys(winPrices.get(i).toString());
            placeFixedInput.clear();
            placeFixedInput.sendKeys(placePrices.get(i).toString());
        }
        result.click();
    }

}

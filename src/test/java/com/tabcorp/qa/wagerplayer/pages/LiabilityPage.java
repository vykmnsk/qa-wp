package com.tabcorp.qa.wagerplayer.pages;


import com.tabcorp.qa.common.Helpers;
import org.apache.commons.lang3.math.NumberUtils;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


public class LiabilityPage extends AppPage {
    @FindBy(css = "table[id^='navigable_market_id']")
    private WebElement liabilityTable;

    @FindBy(id = "f5_eventname_para")
    private WebElement eventName;

    By marketPricesSelector = By.cssSelector("td.data_cell[type='racing_price']");

    public void load() {
        driver.switchTo().defaultContent();
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame_bottom"));
        wait.until(ExpectedConditions.visibilityOf(eventName));
        wait.until(ExpectedConditions.visibilityOf(liabilityTable));
        try {
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(marketPricesSelector, 0));
        } catch (TimeoutException se) {
            Assertions.fail("Liability Page Price Table appears to be empty");
        }
    }

    public void updatePrices(int productId, int betTypeId, List<BigDecimal> prices) {
        List<WebElement> marketPriceCells = driver.findElements(marketPricesSelector);
        List<WebElement> filteredPriceCells = marketPriceCells.stream()
                .filter(cell -> cell.getAttribute("bet_type").contains("" + betTypeId)
                        && cell.getAttribute("product_id").contains("" + productId))
                .collect(Collectors.toList());
        Helpers.retryOnFailure(() -> {
            enterPrices(filteredPriceCells, prices);
        }, 3, 1);
    }

    private void enterPrices(List<WebElement> priceCells, List<BigDecimal> priceValues) {
        assertThat(priceCells.size()).as("Enough Input Cells to enter all price values").isGreaterThanOrEqualTo(priceValues.size());
        for (int i = 0; i < priceValues.size(); i++) {
            WebElement priceCell = priceCells.get(i);
            BigDecimal priceValue = priceValues.get(i);
            String origPriceText = priceCell.getText().trim();
            assertThat(NumberUtils.isNumber(origPriceText)).as("Existing Price value is a Number").isTrue();
            BigDecimal origPriceValue = new BigDecimal(origPriceText);
            if (Helpers.roundOff(origPriceValue, 3).equals(
                    Helpers.roundOff(priceValue, 3))) {
                continue;
            }
            String updatedPriceText = updateElementText(priceCell, priceValue.toString());
            assertThat(NumberUtils.isNumber(updatedPriceText))
                    .as(String.format("Price value entered '%s' is a Number", updatedPriceText))
                    .isTrue();
            BigDecimal updatedPriceValue = new BigDecimal(updatedPriceText);
            assertThat(Helpers.roundOff(updatedPriceValue, 3))
                    .as("Price value entered in table cell")
                    .isEqualTo(Helpers.roundOff(priceValue, 3));
        }
    }

    private String updateElementText(WebElement elem, String text) {
        elem.click();
        Actions actions = new Actions(driver);
        actions.sendKeys(text);
        actions.sendKeys(Keys.RETURN);
        actions.build().perform();
        return elem.getText().trim();
    }

}
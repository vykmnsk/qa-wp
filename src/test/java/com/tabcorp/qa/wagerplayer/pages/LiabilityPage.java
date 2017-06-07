package com.tabcorp.qa.wagerplayer.pages;


import com.tabcorp.qa.common.Helpers;
import org.apache.commons.lang3.math.NumberUtils;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


import static org.assertj.core.api.Assertions.assertThat;


public class LiabilityPage extends AppPage {
    @FindBy(css = "table[id^='navigable_market_id'] ")
    private WebElement liabilityTable;

    @FindBy(css = "td.data_cell[type='racing_price']")
    private List<WebElement> marketPrices;

    @FindBy(id = "f5_eventname_para")
    private WebElement eventName;


    public void load() {
        driver.switchTo().defaultContent();
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame_bottom"));
        wait.until(ExpectedConditions.visibilityOf(liabilityTable));
        wait.until(ExpectedConditions.visibilityOf(eventName));
    }

    public void updatePrices(int productId, int betTypeId, List<BigDecimal> prices) {
        List<WebElement> filterPriceCells = filterMarketPrices(productId, betTypeId);
        enterPrices(filterPriceCells, prices);
    }

    private List<WebElement> filterMarketPrices(Integer prodId, Integer betTypeId) {
        Predicate<WebElement> containsBetId = marketPrice -> marketPrice.getAttribute("bet_type").contains(betTypeId.toString());
        Predicate<WebElement> containsProdId = marketPrice -> marketPrice.getAttribute("product_id").contains(prodId.toString());
        return marketPrices.stream().filter(containsBetId).filter(containsProdId).collect(Collectors.toList());
    }

    private void enterPrices(List<WebElement> priceCells, List<BigDecimal> priceValues) {
        assertThat(priceCells.size()).as("Price values should have equivalent UI inputs").isGreaterThanOrEqualTo(priceValues.size());
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
            assertThat(NumberUtils.isNumber(updatedPriceText)).as("Price value entered is a Number").isTrue();
            BigDecimal updatedPriceValue = new BigDecimal(updatedPriceText);
            assertThat(Helpers.roundOff(updatedPriceValue, 3)).as("Price value entered in table cell").isEqualTo(Helpers.roundOff(priceValue, 3));
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
package com.tabcorp.qa.wagerplayer.pages;


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
        for(int i = 0; i < priceValues.size(); i++) {
            WebElement priceCell = priceCells.get(i);
            String priceValue = priceValues.get(i).toString();
            priceCell.click();
            Actions actions = new Actions(driver);
            actions.sendKeys(priceValue);
            actions.sendKeys(Keys.RETURN);
            actions.build().perform();
            assertThat(priceCell.getText()).as("Entered price value in the cell correctly").isEqualTo(priceValue);
        }
    }

}
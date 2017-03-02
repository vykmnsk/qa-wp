package com.tabcorp.qa.wagerplayer.pages;


import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LiabilityPage extends AppPage {
    @FindBy(css = "table[id^='navigable_market_id'] ")
    WebElement liabilityTable;

    @FindBy(css = "td.data_cell[type='racing_price']")
    List<WebElement> marketPrices;

    public void load() {
        driver.switchTo().defaultContent();
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame_bottom"));
        wait.until(ExpectedConditions.visibilityOf(liabilityTable));
    }

    public List<List<String>> getSelections(String productID) {
        //Todo - get product id dynamically
        List<List<String>> selections = new ArrayList<>();
        marketPrices
                .stream()
                .filter(mp -> mp.getAttribute("product_id").equals(productID))
                .collect(Collectors.toList())
                .forEach(mp ->
                        selections.add(
                                new ArrayList<String>() {{
                                    add(mp.getAttribute("market_prices_id"));
                                    add(mp.getText());
                                }}
                        )
                );
        return selections;
    }

}
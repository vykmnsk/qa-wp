package com.tabcorp.qa.wagerplayer.pages;


import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;
import java.util.stream.Collectors;

public class LiabilityPage extends AppPage {
    @FindBy(css = "table[id^='navigable_market_id'] ")
    WebElement liabilityTable;

    @FindBy(css = "tr.f5_data_row")
    List<WebElement> marketSelections;

    public void load() {
        driver.switchTo().defaultContent();
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame_bottom"));
        wait.until(ExpectedConditions.visibilityOf(liabilityTable));
    }

    public List getMarketPriceIds() {
        return marketSelections
                                .stream()
                                .map(ms -> ms.getAttribute("main_market_prices_id"))
                                .collect(Collectors.toList());
    }

}
package com.tabcorp.qa.wagerplayer.pages;


import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

}
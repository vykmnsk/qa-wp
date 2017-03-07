package com.tabcorp.qa.wagerplayer.pages;

import com.tabcorp.qa.common.Helpers;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.Map;

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

    public void load() {
        driver.switchTo().defaultContent();
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame_bottom"));
        wait.until(ExpectedConditions.visibilityOf(resultsTable));
    }

    public void settleRace(Map<String, String> winners) {
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

        load();
        wait.until(ExpectedConditions.visibilityOf(accept));
        accept.click();

        load();
        wait.until(ExpectedConditions.visibilityOf(settle));
        settle.click();

    }

}

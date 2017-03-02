package com.tabcorp.qa.wagerplayer.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

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

    public void settleRace(List<String[]> winners) {
        int count = 0;
        winnerCheck.click();
        for (String[] winner : winners) {
                    Select position = new Select(resultPositions.get(count));
                    Select result = new Select(resultRunners.get(count));
                    position.selectByVisibleText(winner[0]);
                    result.selectByVisibleText(winner[1]);
                    count++;
        }

        result.click();
        load();
        accept.click();
        load();
        settle.click();

    }

}

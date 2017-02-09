package com.tabcorp.qa.wagerplayer.pages;

import cucumber.api.java.it.Ma;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.time.format.DateTimeFormatter.ofPattern;

public class NewEventPage extends AppPage{
    @FindBy(css = "textarea[name=add_selections]")
    public WebElement addSelections;

    @FindBy(css = "#filter_table_inner img[src='images/button_new_event.gif']")
    public WebElement newEvent;

    @FindBy(css = "#filter_table_inner input[name='event_name']")
    public WebElement eventName;

    @FindBy(css = "#filter_table_inner select[name='AlivetbSelMonth']")
    public WebElement showTimeMonth;

    @FindBy(css = "#filter_table_inner select[name='AlivetbSelDay']")
    public WebElement showTimeDay;

    @FindBy(css = "#filter_table_inner select[name='AlivetbSelYear']")
    public WebElement showTimeYear;

    @FindBy(css = "#filter_table_inner select[name='Aliveeventhour']")
    public WebElement showTimeHour;

    @FindBy(css = "#filter_table_inner select[name='Aliveeventmin']")
    public WebElement showTimeMin;

    @FindBy(css = "#filter_table_inner select[name='tbSelMonth']")
    public WebElement eventDateTimeMonth;

    @FindBy(css = "#filter_table_inner select[name='tbSelDay']")
    public WebElement eventDateTimeDay;

    @FindBy(css = "#filter_table_inner select[name='tbSelYear']")
    public WebElement eventDateTimeYear;

    @FindBy(css = "#filter_table_inner select[name='eventhour']")
    public WebElement eventDateTimeHour;

    @FindBy(css = "#filter_table_inner select[name='eventmin']")
    public WebElement eventDateTimeMin;

    @FindBy(css = "#filter_table_inner select[name='zone']")
    public WebElement eventDateTimeZone;

    @FindBy(css = "textarea[name='add_selections'")
    WebElement runnersBox;

    @FindBy(css = "#filter_table_inner select[name='bet_run']")
    public WebElement betInRunType;

    @FindBy(css = "#filter_table_inner select[name='event_market_type']")
    public WebElement createMarket;

    @FindBy(css = "#filter_table input[src='images/button_insert.gif']")
    public WebElement insertEvent;

    public void load() {
        driver.switchTo().defaultContent();
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame_bottom"));
        wait.until(ExpectedConditions.visibilityOf(newEvent));
        newEvent.click();
        wait.until(ExpectedConditions.visibilityOf(insertEvent));
    }

    public MarketsPage enterEventDetails(int inMinutes, String eventNameVal, String betInRunTypeVal, String createMarketVal, int noOfRunners) {
        eventName.sendKeys(eventNameVal);
        (new Select(betInRunType)).selectByVisibleText(betInRunTypeVal);
        createMarket.sendKeys(createMarketVal);

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Australia/Darwin"));
        // no need to set event show time as it is preset already?

        LocalDateTime evTime = now.plusMinutes(inMinutes);
        new Select(eventDateTimeDay).selectByValue(format2d(evTime.getDayOfMonth()));
        new Select(eventDateTimeMonth).selectByValue(format2d(evTime.getMonthValue()));
        new Select(eventDateTimeYear).selectByValue(format2d(evTime.getYear()));
        new Select(eventDateTimeHour).selectByValue(format2d(evTime.getHour()));
        new Select(eventDateTimeMin).selectByValue(format2d(evTime.getMinute()));

        runnersBox.sendKeys(getRunners(noOfRunners));
        insertEvent.click();
        return new MarketsPage();
    }

    private String getRunners(int noOfRunners) {
        String runners = "";
        String runnerInitial = "Runner_";
        noOfRunners++;
        while (noOfRunners > 0)
        {
            runners = runnerInitial + noOfRunners + "\n" + runners;
            noOfRunners--;
        }
        runners = runners.substring(0, runners.length()-2);
        return runners;
    }

    private String format2d(int val){
        return String.format("%02d", val);
    }

}

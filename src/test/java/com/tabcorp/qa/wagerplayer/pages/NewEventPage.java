package com.tabcorp.qa.wagerplayer.pages;

import com.tabcorp.qa.common.AnyPage;
import com.tabcorp.qa.common.Helpers;
import com.tabcorp.qa.common.Storage;
import com.tabcorp.qa.common.Storage.KEY;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class NewEventPage extends AppPage{
    @FindBy(css = "textarea[name=add_selections]")
    private WebElement addSelections;

    @FindBy(css = "#filter_table_inner img[src='images/button_new_event.gif']")
    private WebElement newEvent;

    @FindBy(css = "#filter_table_inner input[name='event_name']")
    private WebElement eventName;

    @FindBy(css = "#filter_table_inner select[name='AlivetbSelMonth']")
    private WebElement showTimeMonth;

    @FindBy(css = "#filter_table_inner select[name='AlivetbSelDay']")
    private WebElement showTimeDay;

    @FindBy(css = "#filter_table_inner select[name='AlivetbSelYear']")
    private WebElement showTimeYear;

    @FindBy(css = "#filter_table_inner select[name='Aliveeventhour']")
    private WebElement showTimeHour;

    @FindBy(css = "#filter_table_inner select[name='Aliveeventmin']")
    private WebElement showTimeMin;

    @FindBy(css = "#filter_table_inner select[name='tbSelMonth']")
    private WebElement eventDateTimeMonth;

    @FindBy(css = "#filter_table_inner select[name='tbSelDay']")
    private WebElement eventDateTimeDay;

    @FindBy(css = "#filter_table_inner select[name='tbSelYear']")
    private WebElement eventDateTimeYear;

    @FindBy(css = "#filter_table_inner select[name='eventhour']")
    private WebElement eventDateTimeHour;

    @FindBy(css = "#filter_table_inner select[name='eventmin']")
    private WebElement eventDateTimeMin;

    @FindBy(css = "#filter_table_inner select[name='zone']")
    private WebElement eventDateTimeZone;

    @FindBy(css = "textarea[name='add_selections'")
    private WebElement runnersBox;

    @FindBy(css = "#filter_table_inner select[name='bet_run']")
    private WebElement betInRunType;

    @FindBy(css = "#filter_table_inner select[name='event_market_type']")
    private WebElement createMarket;

    @FindBy(css = "#filter_table input[src='images/button_insert.gif']")
    private WebElement insertEvent;

    private static Logger log = LoggerFactory.getLogger(NewEventPage.class);

    public void load() {
        driver.switchTo().defaultContent();
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame_bottom"));
        wait.until(ExpectedConditions.visibilityOf(newEvent));
        newEvent.click();
        wait.until(ExpectedConditions.visibilityOf(insertEvent));
    }

    public MarketsPage enterEventDetails(int inMinutes, String eventNameVal, String betInRunTypeVal, String createMarketVal, List<String> runners) {
        eventName.sendKeys(eventNameVal);
        log.info("Event Name=" + eventNameVal);

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

        runnersBox.sendKeys(String.join("\n", runners));
        insertEvent.click();

        MarketsPage mp = new MarketsPage();
        String evtId = mp.readEventID();
        Storage.put(KEY.EVENT_ID, evtId);
        log.info("storing Event ID=" + Storage.get(KEY.EVENT_ID));
        return mp;
    }

    private String format2d(int val){
        return String.format("%02d", val);
    }

}

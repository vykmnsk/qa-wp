package com.tabcorp.qa.wagerplayer.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CategorySettingsPage extends AppPage {

    @FindBy(css = "#filter_table_inner img[src='images/icon_info.gif']")
    private WebElement actionCat;

    @FindBy(css = "a#opener > input[value='edit']")
    private WebElement editCat;

    @FindBy (css= "input[name=places_paid_based_on]")
    private WebElement placesPaid;

    @FindBy (css= "input[name=is_recalc_deducted_place_price]")
    private WebElement recalcPlacePrice;

    @FindBy(css = "form#categories_form input[value='Save']")
    private WebElement saveCat;


    public void load() {
        driver.switchTo().defaultContent();
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame_bottom"));
        wait.until(ExpectedConditions.visibilityOf(actionCat));
        actionCat.click();
        wait.until(ExpectedConditions.visibilityOf(editCat));
        editCat.click();
    }

    public void enterSettings(boolean isNumOfPlacesBet, boolean isRecalcPlacePrice) {
        ensureChecked(placesPaid, isNumOfPlacesBet);
        ensureChecked(recalcPlacePrice, isRecalcPlacePrice);
        saveCat.click();
    }

}
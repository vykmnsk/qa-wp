package com.tabcorp.qa.wagerplayer.pages;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;


public class HomePage extends AppPage {
    @FindBy(css = ("frame[id='frame_top']"))
    private WebElement frameTop;

    @FindBy(css = ("frame[name='frame_bottom']"))
    private WebElement frameBottom;

    @FindBy(css = ("frame[name='frame_ticker']"))
    private WebElement frameTicker;

    public  void verifyLoaded() {
        wait.until(visibilityOf(frameTop));
        Assertions.assertThat(frameTop.isDisplayed() && frameBottom.isDisplayed() && frameTicker.isDisplayed())
            .withFailMessage("Home page is not loaded")
            .isTrue();
    }
}

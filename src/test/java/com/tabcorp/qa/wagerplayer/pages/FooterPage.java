package com.tabcorp.qa.wagerplayer.pages;

import com.tabcorp.qa.common.Helpers;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class FooterPage extends AppPage {
    @FindBy(css = ("span#dticker"))
    public WebElement ticker;

    public void toggleIntercept() {
        switchToFooterFrame();
        Helpers.retryOnFailure(() -> {
            wait.until(ExpectedConditions.visibilityOf(ticker));
            wait.until(ExpectedConditions.elementToBeClickable(ticker));
        },5,4);
        ticker.click();
    }

    private void switchToFooterFrame() {
        driver.switchTo().defaultContent();
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame_ticker"));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("chat_convo"));
    }

}

package com.tabcorp.qa.wagerplayer.pages;

import com.tabcorp.qa.common.FrameworkError;
import com.tabcorp.qa.common.Helpers;
import com.tabcorp.qa.wagerplayer.api.WAPI;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class InterceptPage extends AppPage {
    By rejectBet = By.cssSelector("input[class=reject_button]");
    By acceptBet = By.cssSelector("input[class=accept_button]");

    @FindBy(css = ("a.active_intercept"))
    List<WebElement> betInterceptLinks;

    @FindBy(css = ("input[id^=accept_]"))
    List<WebElement> acceptMultipleMultiBets;

    @FindBy(css = ("input[id^=reject_]"))
    List<WebElement> rejectMultipleMultiBets;

    @FindBy(css = ("input[value='ACCEPT PARTIAL']"))
    WebElement acceptPartial;

    @FindBy(css = ("input[id^=partial_][type=button]"))
    List<WebElement> acceptMultiplePartials;

    @FindBy(css = ("input[name=partial_stake]"))
    WebElement partialAmount;

    @FindBy(css = ("input[id^=partial_][type=text]"))
    List<WebElement> acceptMultiplePartialAmounts;

    public void actionOnBetIntercept(String expectedInterceptText, WAPI.InterceptOption interceptOption, String partialAmountValue) {
        driver.switchTo().defaultContent();
        switchBetweenFramesOnInterceptPage("frame_intercept_ticker");
        Helpers.retryOnFailure(() -> {
            wait.until(ExpectedConditions.visibilityOfAllElements(betInterceptLinks));
        }, 5, 4);
        for (WebElement betInterceptLink : betInterceptLinks) {
            String actualInterceptLinkText = betInterceptLink.getText();
            if (expectedInterceptText.equals(actualInterceptLinkText)) {
                wait.until(ExpectedConditions.visibilityOf(betInterceptLink));
                betInterceptLink.click();
                driver.switchTo().defaultContent();
                switchBetweenFramesOnInterceptPage("frame_ticker_bottom");

                WebElement actionElem;
                switch (interceptOption) {
                    case Accept:
                        actionElem = driver.findElement(acceptBet);
                        break;
                    case Reject:
                        actionElem = driver.findElement(rejectBet);
                        break;
                    case Partial:
                        actionElem = acceptPartial;
                        partialAmount.sendKeys(partialAmountValue);
                        break;
                    default:
                        throw new FrameworkError("Unexpected interceptOption=" + interceptOption);
                }
                wait.until(ExpectedConditions.visibilityOf(actionElem));
                actionElem.click();
                break;
            }
        }
    }

    public void actionOnMultipleBetsOnIntercept(String expectedInterceptText, List<WAPI.InterceptOption> interceptOptions, String partialAmountValue) {
        driver.switchTo().defaultContent();
        switchBetweenFramesOnInterceptPage("frame_intercept_ticker");
        Helpers.retryOnFailure(() -> {
            wait.until(ExpectedConditions.visibilityOfAllElements(betInterceptLinks));
        }, 5, 4);

        WebElement interceptLink = betInterceptLinks.stream().filter(l -> expectedInterceptText.equals(l.getText())).findFirst().orElse(null);
        Assertions.assertThat(interceptLink).withFailMessage("Intercept link not found").isNotNull();
        interceptLink.click();
        driver.switchTo().defaultContent();
        switchBetweenFramesOnInterceptPage("frame_ticker_bottom");

        int i = 0;
        for (WAPI.InterceptOption option : interceptOptions) {
            WebElement actionElem;
            switch (option) {
                case Accept:
                    actionElem = acceptMultipleMultiBets.get(i);
                    break;
                case Reject:
                    actionElem = rejectMultipleMultiBets.get(i);
                    break;
                case Partial:
                    acceptMultiplePartialAmounts.get(i).sendKeys(partialAmountValue);
                    actionElem = acceptMultiplePartials.get(i);
                    break;
                default:
                    throw new FrameworkError("Unexpected interceptOption=" + option);
            }
            actionElem.click();
            i++;
        }
    }

    private void switchBetweenFramesOnInterceptPage(String frameName) {
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame_bottom"));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameName));
    }

}
package com.tabcorp.qa.wagerplayer.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PromotionPage extends AppPage {
    @FindBy(css = ("a[act=new]"))
    WebElement activateBalanceTabLink;

    @FindBy(css = ("select[id=promotion_code_selector][style]"))
    WebElement promotionOptions;

    @FindBy(css = ("textarea[name=n_Bod]"))
    WebElement activationNote;

    @FindBy(css = ("td > input#wp_button"))
    WebElement saveAndActivate;

    private String promotionWindow;

    public void load() {
        promotionWindow = driver.getWindowHandle();
        switchToNewWindow(Arrays.asList(promotionWindow));
    }

    public void verifyLoaded() {
        HeaderPage deposit = new HeaderPage();
        deposit.verifyPageTitle("Customer Bonus Bet Balance");
        wait.until(ExpectedConditions.visibilityOf(activateBalanceTabLink));
    }

    public void activatePromotion(String promoCode, String activationText) {
        activateBalanceTabLink.click();
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("customer_promotion_add"));
        wait.until(ExpectedConditions.visibilityOf(promotionOptions));
        new Select(promotionOptions).selectByVisibleText(promoCode);
        wait.until(ExpectedConditions.visibilityOf(activationNote));
        activationNote.sendKeys(activationText);
        wait.until(ExpectedConditions.visibilityOf(saveAndActivate));
        saveAndActivate.click();

        driver.close();
        driver.switchTo().window(promotionWindow);
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame_bottom"));
    }

}

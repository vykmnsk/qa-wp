package com.tabcorp.qa.wagerplayer.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class MasterControllerPage extends AppPage {

    @FindBy(xpath ="//div[@id='admin_menu_items']//a[text()='More']")
    private WebElement more;

    @FindBy(xpath ="//div[@id='admin_menu_items']//a[text()='Configuration']")
    private WebElement configuration;

    @FindBy(css = ("a[href='/admin/master_controller.php']"))
    private WebElement masterController;

    @FindBy(css = ("input[name='options_action']"))
    private WebElement save;

    @FindBy(css = ("input#DUPLICATE_ACCOUNT_CHECK_t"))
    private WebElement dupAcctCheckYes;

    @FindBy(css = ("input#DUPLICATE_ACCOUNT_CHECK_f"))
    private WebElement dupAcctCheckNo;


    public void load() {
        driver.switchTo().defaultContent();
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame_bottom"));
        wait.until(ExpectedConditions.visibilityOf(more));
        more.click();
        wait.until(ExpectedConditions.visibilityOf(configuration));
        configuration.click();
        wait.until(ExpectedConditions.visibilityOf(masterController));
        masterController.click();
    }

    public void enterSettings(boolean isDupAccts) {

        if (isDupAccts){
            ensureChecked(dupAcctCheckYes, true);
            ensureChecked(dupAcctCheckNo, false);
        } else {
            ensureChecked(dupAcctCheckNo, true);
            ensureChecked(dupAcctCheckYes, false);
        }
        save.click();
        log.info("Duplicate Account Check: " + isDupAccts);
    }
}
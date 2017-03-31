package com.tabcorp.qa.wagerplayer.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerDetailsPage extends AppPage {

    @FindBy(css = ("#main_table > tbody > tr:nth-child(1) > th"))
    public WebElement labelCustomerDetailsPage;

    @FindBy(css = "table#side_table_inner > tbody > tr:nth-child(17) > td:nth-child(1)")
    public WebElement labelClientNo;

    @FindBy(css = "table#side_table_inner > tbody > tr:nth-child(17) > td:nth-child(2)")
    public WebElement valueClientNo;

    @FindBy(css = ("table#side_table_inner > tbody > tr > td:nth-child(1)"))
    public List<WebElement> custDetailFields;

    @FindBy(css = ("table#side_table_inner > tbody"))
    public WebElement custDetailValue;

    @FindBy(css = "table#side_table_inner > tbody > tr:nth-child(33) > td:nth-child(2)")
    public WebElement valueAMLStatus;

    @FindBy(css = "img[src='images/button_deposit.gif']")
    public WebElement depositButton;

    public void verifyLoaded() {
        wait.until(ExpectedConditions.visibilityOf(labelCustomerDetailsPage));
        assertThat(labelCustomerDetailsPage.getText()).isEqualTo("Customers");
    }

    public void verifyAmlStatus(String amlOne, String amlTwo) {
        WebElement amlStatusValue = null;
        List<WebElement> custDetailLabels = custDetailFields;
        for (int i = 0; i < custDetailLabels.size(); i++) {
            if (custDetailLabels.get(i).getText().equals(" AML Status:")) {
                i+=1;
                amlStatusValue = custDetailValue.findElement(By.cssSelector("tr:nth-child(" + i + ") > td:nth-child(2)"));
                break;
            }
        }
        assertThat(amlStatusValue).as("AML status").isIn(amlOne, amlTwo);
    }

}

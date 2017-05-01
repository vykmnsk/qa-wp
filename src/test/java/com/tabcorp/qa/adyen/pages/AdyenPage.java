package com.tabcorp.qa.adyen.pages;


import com.jayway.jsonpath.JsonPath;
import com.tabcorp.qa.common.AnyPage;
import com.tabcorp.qa.common.Helpers;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AdyenPage extends AnyPage {

    @FindBy(id = "adyen-encrypted-form-number")
    private WebElement cardNumber;

    @FindBy(id = "adyen-encrypted-form-cvc")
    private WebElement cvc;

    @FindBy(id = "adyen-encrypted-form-holder-name")
    private WebElement holderName;

    @FindBy(id = "adyen-encrypted-form-expiry-month")
    private WebElement expiryMonth;

    @FindBy(id = "adyen-encrypted-form-expiry-year")
    private WebElement expiryYear;

    @FindBy(id = "adyen-encrypted-form-key")
    private WebElement encryptionKey;

    @FindBy(id = "adyen-encrypted-submit")
    private WebElement submit;

    @FindBy(id = "adyen-encrypted-output-value")
    private WebElement cardEncryption;

    //local html form stored in resources folder for encryption of card details
    //this form and jquery library is provided by adyen
    private final String adyenHtmlForm = "adyen.encrypt.nodom.html";

    public void load() {
        String baseUrl = Helpers.getUriOfResource(adyenHtmlForm);
        driver.get(baseUrl);
        PageFactory.initElements(driver, this);
        wait.until(ExpectedConditions.visibilityOf(submit));
    }

    public String getCardEncryption(String key, String cardNum, String cvcnumber, String expMonth, String expYear, String cardHolderName) {
        cardNumber.sendKeys(cardNum);
        cvc.sendKeys(cvcnumber);
        expiryMonth.sendKeys(expMonth);
        expiryYear.sendKeys(expYear);
        holderName.sendKeys(cardHolderName);
        encryptionKey.sendKeys(key);
        submit.click();
        wait.until(ExpectedConditions.attributeToBeNotEmpty(cardEncryption, "value"));

        String response = cardEncryption.getAttribute("value");
        String encryption = JsonPath.read(response, "$.adyen-encrypted-data");
        Assertions.assertThat(encryption).as("Encrypted card key not found in " + response).isNotEmpty();
        return encryption;
    }

}
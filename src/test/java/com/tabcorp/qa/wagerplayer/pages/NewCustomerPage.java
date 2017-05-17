package com.tabcorp.qa.wagerplayer.pages;

import com.tabcorp.qa.wagerplayer.Config;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.Map;

public class NewCustomerPage extends AppPage {

    @FindBy(css = "select[id=salutation]")
    public WebElement title;

    @FindBy(css = "input[name='details[customers_firstname]']")
    public WebElement firstName;

    @FindBy(css = "input[name='details[customers_lastname]']")
    public WebElement lastName;

    @FindBy(css = "input[id=txtDay]")
    public WebElement doBDay;

    @FindBy(css = "input[id=txtMonth]")
    public WebElement doBMonth;

    @FindBy(css = "input[id=txtYear]")
    public WebElement doBYear;

    @FindBy(css = "input[name='details[customers_email_address]']")
    public WebElement emailID;

    @FindBy(css = "input[id=residential_street_address]")
    public WebElement residentialStreetAddress;

    @FindBy(css = "input[id=residential_suburb]")
    public WebElement residentialSuburb;

    @FindBy(css = "input[id=residential_city]")
    public WebElement residentialCity;

    @FindBy(css = "select[id=residential_state_dropdown]")
    public WebElement residentialState;

    @FindBy(css = "input[id=weekly_deposit_limit]")
    public WebElement weeklyDepositLimit;

    @FindBy(css = "input[id=residential_postcode]")
    public WebElement residentialPostCode;

    @FindBy(css = "select[id=residential_country]")
    public WebElement residentialCountry;

    @FindBy(css = "select[name='info[timezone]']")
    public WebElement residentialTimezone;

    @FindBy(css = "input[id=building]")
    public WebElement mailingBuilding;

    @FindBy(css = "input[id=street_address]")
    public WebElement mailingStreetAddress;

    @FindBy(css = "input[id=suburb]")
    public WebElement mailingSuburb;

    @FindBy(css = "input[id=city]")
    public WebElement mailingCity;

    @FindBy(css = "select[id=postal_state_dropdown]")
    public WebElement mailingState;

    @FindBy(css = "input[id=postcode]")
    public WebElement mailingPostCode;

    @FindBy(css = "select[id=mailing_country]")
    public WebElement mailingCountry;

    @FindBy(css = "select[id=customers_mobile_countrycode]")
    public WebElement countryCode;

    @FindBy(css = "input[name='details[customers_mobile_phone]']")
    public WebElement custMobileNo;

    @FindBy(css = "input[name='details[customers_username]']")
    public WebElement username;

    @FindBy(css = "input[name='details[customers_password]']")
    public WebElement telePassword;

    @FindBy(css = "input[name='details[customers_password_confirmation]']")
    public WebElement telePasswordConfirmation;

    @FindBy(css = "input[name='details[customers_internet_password]']")
    public WebElement internetPassword;

    @FindBy(css = "input[name='details[customers_internet_password_confirmation]']")
    public WebElement internetPasswordConfirmation;

    @FindBy(css = "select[name='details[customers_secret_question]']")
    public WebElement challengeQuestion;

    @FindBy(css = "input[name='details[customers_secret_answer]']")
    public WebElement challengeAnswer;

    @FindBy(css = "select[name='details[customers_currency_code]']")
    public WebElement currency;

    @FindBy(css = "input[id='create_account_submit']")
    public WebElement insert;

    @FindBy(css = "table#main_table .admin_form_errors")
    List<WebElement> formErrors;


    public void verifyLoaded() {
        HeaderPage header = new HeaderPage();
        header.verifyPageTitle("Create Customer");
    }

    public void enterCustomerDetails(Map<String, String> cust) {
        new Select(title).selectByValue(cust.get("salutation"));
        firstName.sendKeys(cust.get("firstname"));
        lastName.sendKeys(cust.get("lastname"));
        doBDay.sendKeys(String.valueOf(cust.get("dob-day")));
        doBMonth.sendKeys(String.valueOf(cust.get("dob-month")));
        doBYear.sendKeys(String.valueOf(cust.get("dob-year")));
        emailID.sendKeys(cust.get("email_address"));
        new Select(countryCode).selectByVisibleText("+61");
        custMobileNo.sendKeys(cust.get("telephone"));
        residentialStreetAddress.sendKeys(cust.get("street"));
        residentialSuburb.sendKeys(cust.get("suburb"));
        residentialCity.sendKeys(cust.get("city"));
        residentialPostCode.sendKeys(cust.get("postcode"));
        new Select(residentialCountry).selectByVisibleText(cust.get("country"));
        new Select(residentialTimezone).selectByValue(cust.get("timezone"));
        if (Config.isRedbook()) {
            mailingBuilding.sendKeys(cust.get("building"));
        }
        mailingStreetAddress.sendKeys(cust.get("street"));
        mailingSuburb.sendKeys(cust.get("suburb"));
        mailingCity.sendKeys(cust.get("city"));
        mailingPostCode.sendKeys(cust.get("postcode"));
        mailingCountry.sendKeys(cust.get("country"));
        if (Config.isLuxbet()) {
            new Select(residentialState).selectByVisibleText(cust.get("state"));
            new Select(mailingState).selectByVisibleText(cust.get("state"));
            if (!cust.get("deposit_limit").isEmpty()) {
                weeklyDepositLimit.sendKeys(cust.get("deposit_limit"));
            }
        }
        username.sendKeys(cust.get("username"));
        telePassword.sendKeys(cust.get("password"));
        telePasswordConfirmation.sendKeys(cust.get("password"));
        internetPassword.sendKeys(cust.get("password"));
        internetPasswordConfirmation.sendKeys(cust.get("password"));
        new Select(challengeQuestion).selectByVisibleText(cust.get("secret_question"));
        challengeAnswer.sendKeys(cust.get("secret_answer"));
        new Select(currency).selectByValue(cust.get("currency_code"));

        insert.click();
        verifyNoFormErrors();
    }

    private void verifyNoFormErrors() {
        if (formErrors.size() > 0) {
            Assertions.fail("Input validation errors: " + formErrors.get(0).getText());
        }
    }


}

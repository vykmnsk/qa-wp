package com.tabcorp.qa.wagerplayer.pages;

import com.tabcorp.qa.wagerplayer.Config;
import com.tabcorp.qa.wagerplayer.dto.Customer;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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

    public void enterCustomerDetails(Customer cust) {
        new Select(title).selectByValue(cust.title);
        firstName.sendKeys(cust.firstName);
        lastName.sendKeys(cust.lastName);

        doBDay.sendKeys(String.valueOf(cust.dob.getDayOfMonth()));
        doBMonth.sendKeys(String.valueOf(cust.dob.getMonthValue()));
        doBYear.sendKeys(String.valueOf(cust.dob.getYear()));

        emailID.sendKeys(cust.email);
        new Select(countryCode).selectByVisibleText("+61");
        custMobileNo.sendKeys(cust.telephoneNo);

        residentialStreetAddress.sendKeys(cust.street);
        residentialSuburb.sendKeys(cust.suburb);
        residentialCity.sendKeys(cust.city);
        residentialPostCode.sendKeys(cust.postCode);
        residentialCountry.sendKeys(cust.country);
        new Select(residentialTimezone).selectByValue(cust.timezone);

        if (Config.REDBOOK.equals(Config.appName())) {
            mailingBuilding.sendKeys(cust.building);
        }
        mailingStreetAddress.sendKeys(cust.street);
        mailingSuburb.sendKeys(cust.suburb);
        mailingCity.sendKeys(cust.city);
        mailingPostCode.sendKeys(cust.postCode);
        mailingCountry.sendKeys(cust.country);

        if (Config.LUXBET.equals(Config.appName())) {
            new Select(residentialState).selectByVisibleText(cust.state);
            new Select(mailingState).selectByVisibleText(cust.state);
            if (!cust.weeklyDepositLimit.isEmpty()) {
                weeklyDepositLimit.sendKeys(cust.weeklyDepositLimit);
            }
        }

        username.sendKeys(cust.username);

        telePassword.sendKeys(cust.telephonePassword);
        telePasswordConfirmation.sendKeys(cust.telephonePassword);
        internetPassword.sendKeys(cust.internetPassword);
        internetPasswordConfirmation.sendKeys(cust.internetPassword);

        new Select(challengeQuestion).selectByVisibleText(cust.securityQuestion);
        challengeAnswer.sendKeys(cust.securityAnswer);

        new Select(currency).selectByValue(cust.currency);

        insert.click();
        verifyNoFormErrors();
    }

    private void verifyNoFormErrors() {
        if (formErrors.size() > 0) {
            Assertions.fail("Input validation errors: " + formErrors.get(0).getText());
        }
    }


}

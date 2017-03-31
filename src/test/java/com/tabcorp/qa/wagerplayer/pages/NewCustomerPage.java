package com.tabcorp.qa.wagerplayer.pages;

import com.tabcorp.qa.common.Helpers;
import com.tabcorp.qa.wagerplayer.Config;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class NewCustomerPage extends AppPage {

    @FindBy(css = ("#main_table > tbody > tr:nth-child(1) > th"))
    public WebElement labelCreateCustomer;

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

    @FindBy(css = "input[id=residential_building]")
    public WebElement residentialBuildingNumber;

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
    public WebElement mailingBuildingNumber;

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
    public WebElement userName;

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

    private static Logger log = LoggerFactory.getLogger(NewCustomerPage.class);

    public void verifyLoaded() {
        wait.until(ExpectedConditions.visibilityOf(labelCreateCustomer));
        assertThat(labelCreateCustomer.getText()).isEqualTo("Create Customer");
    }

    public void enterCustomerDetails(String username, LocalDate custDob, Map<String, String> cust) {
        log.info("Customer Username=" + username);

        String custTitle = (String) Helpers.nonNullGet(cust, "title");
        String custFirstName = (String) Helpers.nonNullGet(cust, "firstname");
        String custLastName = (String) Helpers.nonNullGet(cust, "lastname");
        String custTelephoneNo = (String) Helpers.nonNullGet(cust, "phonenumber");
        String custEmail = ((String) Helpers.nonNullGet(cust, "email_address")).replace("random", username);
        String[] custAddress = ((String) Helpers.nonNullGet(cust, "address")).split(",");
        String custCountry = (String) Helpers.nonNullGet(cust, "country");
        String custWeeklyLimit = (String) Helpers.nonNullGet(cust, "weekly_deposit_limit");
        String custSecurityQuestion = (String) Helpers.nonNullGet(cust, "security_question");
        String custSecurityAnswer = (String) Helpers.nonNullGet(cust, "customer_answer");
        String currencyValue = (String) Helpers.nonNullGet(cust, "currency");
        String custTimezone = (String) Helpers.nonNullGet(cust, "timezone");

        new Select(title).selectByValue(custTitle);
        firstName.sendKeys(custFirstName);
        lastName.sendKeys(custLastName);

        doBDay.sendKeys(String.valueOf(custDob.getDayOfMonth()));
        doBMonth.sendKeys(String.valueOf(custDob.getMonthValue()));
        doBYear.sendKeys(String.valueOf(custDob.getYear()));

        emailID.sendKeys(custEmail);
        new Select(countryCode).selectByVisibleText("+61");
        custMobileNo.sendKeys(custTelephoneNo);

        residentialStreetAddress.sendKeys(custAddress[0]);
        residentialSuburb.sendKeys(custAddress[1]);
        residentialCity.sendKeys(custAddress[1]);
        residentialPostCode.sendKeys(custAddress[3]);
        residentialCountry.sendKeys(custCountry);
        new Select(residentialState).selectByVisibleText(custAddress[2]);

        if(!custWeeklyLimit.equals("")) { weeklyDepositLimit.sendKeys(custWeeklyLimit); }

        new Select(residentialTimezone).selectByValue(custTimezone);

        mailingStreetAddress.sendKeys(custAddress[0]);
        mailingSuburb.sendKeys(custAddress[1]);
        mailingCity.sendKeys(custAddress[1]);
        mailingPostCode.sendKeys(custAddress[3]);
        mailingCountry.sendKeys(custCountry);
        new Select(mailingState).selectByVisibleText(custAddress[2]);

        userName.sendKeys(username);

        telePassword.sendKeys(Config.password());
        telePasswordConfirmation.sendKeys(Config.password());
        internetPassword.sendKeys(Config.customerPassword());
        internetPasswordConfirmation.sendKeys(Config.customerPassword());

        new Select(challengeQuestion).selectByVisibleText(custSecurityQuestion);
        challengeAnswer.sendKeys(custSecurityAnswer);

        new Select(currency).selectByVisibleText(currencyValue);

        insert.click();
    }

}

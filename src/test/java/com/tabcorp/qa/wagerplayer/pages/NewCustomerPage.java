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

    public void enterCustomerDetails (
            String usernameValue,
            String titleValue,
            String firstNameValue,
            String lastNameValue,
            LocalDate dobValue,
            String telephoneNoValue,
            String emailValue,
            String streetAddressValue,
            String suburbValue,
            String cityValue,
            String stateValue,
            String postCodeValue,
            String countryValue,
            String weeklyLimitValue,
            String securityQuestionValue,
            String securityAnswerValue,
            String currencyValue,
            String timezoneValue
    ) {

        new Select(title).selectByValue(titleValue);
        firstName.sendKeys(firstNameValue);
        lastName.sendKeys(lastNameValue);

        doBDay.sendKeys(String.valueOf(dobValue.getDayOfMonth()));
        doBMonth.sendKeys(String.valueOf(dobValue.getMonthValue()));
        doBYear.sendKeys(String.valueOf(dobValue.getYear()));

        emailID.sendKeys(emailValue);
        new Select(countryCode).selectByVisibleText("+61");
        custMobileNo.sendKeys(telephoneNoValue);

        residentialStreetAddress.sendKeys(streetAddressValue);
        residentialSuburb.sendKeys(suburbValue);
        residentialCity.sendKeys(cityValue);
        residentialPostCode.sendKeys(postCodeValue);
        residentialCountry.sendKeys(countryValue);
        new Select(residentialState).selectByVisibleText(stateValue);

        if(!weeklyLimitValue.isEmpty()) { weeklyDepositLimit.sendKeys(weeklyLimitValue); }

        new Select(residentialTimezone).selectByValue(timezoneValue);

        mailingStreetAddress.sendKeys(streetAddressValue);
        mailingSuburb.sendKeys(suburbValue);
        mailingCity.sendKeys(cityValue);
        mailingPostCode.sendKeys(postCodeValue);
        mailingCountry.sendKeys(countryValue);
        new Select(mailingState).selectByVisibleText(stateValue);

        userName.sendKeys(usernameValue);

        telePassword.sendKeys(Config.password());
        telePasswordConfirmation.sendKeys(Config.password());
        internetPassword.sendKeys(Config.customerPassword());
        internetPasswordConfirmation.sendKeys(Config.customerPassword());

        new Select(challengeQuestion).selectByVisibleText(securityQuestionValue);
        challengeAnswer.sendKeys(securityAnswerValue);

        new Select(currency).selectByVisibleText(currencyValue);

        insert.click();
    }

}

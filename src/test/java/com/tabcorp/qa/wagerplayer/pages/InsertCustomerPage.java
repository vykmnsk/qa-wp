package com.tabcorp.qa.wagerplayer.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InsertCustomerPage extends AppPage {
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

    @FindBy(css = "select[name='details[customers_telephone_countrycode]']")
    public WebElement countryCode;

    @FindBy(css = "input[name='details[customers_telephone]']")
    public WebElement custTelephoneNo;

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

    private static Logger log = LoggerFactory.getLogger(InsertCustomerPage.class);



}

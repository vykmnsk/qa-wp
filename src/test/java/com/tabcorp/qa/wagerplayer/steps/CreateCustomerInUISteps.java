package com.tabcorp.qa.wagerplayer.steps;

import com.tabcorp.qa.common.Helpers;
import com.tabcorp.qa.wagerplayer.pages.*;
import cucumber.api.DataTable;
import cucumber.api.java8.En;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;


public class CreateCustomerInUISteps implements En {
    private HeaderPage header;
    private CustomersPage customersPage;
    private NewCustomerPage newCustPage;


    public CreateCustomerInUISteps() {

        When("^I navigate to Customers Page to insert new customer$", () -> {
            header = new HeaderPage();
            customersPage = header.navigateToF11();;
            customersPage.verifyLoaded();
            newCustPage = customersPage.insertNew();
            newCustPage.verifyLoaded();
        });

        When("^I enter the following data on Create New Customer page$", (DataTable table) -> {
            Map<String, String> custData = table.asMap(String.class, String.class);

            String timeStamp = new SimpleDateFormat("HHmmssSSS").format(new Date());
            String username = "AutoUser" + timeStamp;

            String title = (String) Helpers.nonNullGet(custData, "title");
            String firstName = (String) Helpers.nonNullGet(custData, "firstname");
            String lastName = (String) Helpers.nonNullGet(custData, "lastname");
            String dobTxt = (String) Helpers.nonNullGet(custData, "date_of_birth");
            LocalDate dob = LocalDate.parse(dobTxt, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String telephoneNo = (String) Helpers.nonNullGet(custData, "phonenumber");
            String email = ((String) Helpers.nonNullGet(custData, "email_address")).replace("#username#", username);
            String streetAddress = (String) Helpers.nonNullGet(custData, "street_address");
            String suburb = (String) Helpers.nonNullGet(custData, "suburb");
            String city = (String) Helpers.nonNullGet(custData, "city");
            String state = (String) Helpers.nonNullGet(custData, "state");
            String postCode = (String) Helpers.nonNullGet(custData, "postcode");
            String country = (String) Helpers.nonNullGet(custData, "country");
            String weeklyLimit = (String) Helpers.nonNullGet(custData, "weekly_deposit_limit");
            String securityQuestion = (String) Helpers.nonNullGet(custData, "security_question");
            String currencyValue = (String) Helpers.nonNullGet(custData, "currency");
            String timezone = (String) Helpers.nonNullGet(custData, "timezone");

            String securityAnswer = RandomStringUtils.randomAlphanumeric(10);
            String telephonePassword = RandomStringUtils.randomAlphanumeric(10);
            String internetPassword = RandomStringUtils.randomAlphabetic(7) + RandomStringUtils.randomNumeric(3);

            newCustPage.enterCustomerDetails(
                    username,
                    title,
                    firstName,
                    lastName,
                    dob,
                    telephoneNo,
                    email,
                    streetAddress,
                    suburb,
                    city,
                    state,
                    postCode,
                    country,
                    weeklyLimit,
                    securityQuestion,
                    securityAnswer,
                    currencyValue,
                    timezone,
                    telephonePassword,
                    internetPassword
            );
        });

        When("^I see new customer created with AML status updated to \"([^\"]*)\" or \"([^\"]*)\"$", (String amlOne, String amlTwo) -> {
            customersPage.verifyLoaded();

            class ReloadCheckAMLStatus implements Runnable {
                public void run() {
                    header.refreshPage();
                    String status = customersPage.readAMLStatus();
                    Assertions.assertThat(status).as("AML status").isIn(amlOne, amlTwo);
                }
            }
            Helpers.retryOnAssertionFailure(new ReloadCheckAMLStatus(), 5, 3);
        });

    }

}

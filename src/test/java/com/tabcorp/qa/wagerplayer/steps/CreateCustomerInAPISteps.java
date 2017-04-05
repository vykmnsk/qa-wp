package com.tabcorp.qa.wagerplayer.steps;

import com.tabcorp.qa.common.Helpers;
import com.tabcorp.qa.wagerplayer.api.WAPI;
import cucumber.api.DataTable;
import cucumber.api.java8.En;
import org.apache.commons.lang3.RandomStringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


public class CreateCustomerInAPISteps implements En {
    private String customerUsername;
    private String customerPassword;
    private WAPI wapi;

    public CreateCustomerInAPISteps() {
        When("^I post customer specifics to create new customer$", (DataTable table) -> {
            Map<String, String> cust = table.asMap(String.class, String.class);

            String timeStamp = new SimpleDateFormat("HHmmssSSS").format(new Date());
            String username = "AutoUser" + timeStamp;
            this.customerUsername = username;

            if (null == wapi) wapi = new WAPI();

            String title = (String) Helpers.nonNullGet(cust, "title");
            String firstName = (String) Helpers.nonNullGet(cust, "firstname");
            String lastName = (String) Helpers.nonNullGet(cust, "lastname");
            String dob = (String) Helpers.nonNullGet(cust, "date_of_birth");
            String telephoneNo = (String) Helpers.nonNullGet(cust, "phonenumber");
            String email = ((String) Helpers.nonNullGet(cust, "email_address")).replace("#username#", username);
            String streetAddress = (String) Helpers.nonNullGet(cust, "street_address");
            String suburb = (String) Helpers.nonNullGet(cust, "suburb");
            String state = (String) Helpers.nonNullGet(cust, "state");
            String postCode = (String) Helpers.nonNullGet(cust, "postcode");
            String country = (String) Helpers.nonNullGet(cust, "country");
            String weeklyLimit = (String) Helpers.nonNullGet(cust, "weekly_deposit_limit");
            String securityQuestion = (String) Helpers.nonNullGet(cust, "security_question");
            String answer = (String) Helpers.nonNullGet(cust, "customer_answer");
            String clientIP = (String) Helpers.nonNullGet(cust, "client_ip");
            String currencyValue = (String) Helpers.nonNullGet(cust, "currency");
            String timezone = (String) Helpers.nonNullGet(cust, "timezone");

            String password = RandomStringUtils.randomAlphanumeric(10);
            String telephonePassword = RandomStringUtils.randomAlphanumeric(10);
            String internetPassword = RandomStringUtils.randomAlphabetic(7) + RandomStringUtils.randomNumeric(3);
            this.customerPassword = internetPassword;

            String successMsg = wapi.createNewCustomer(
                    username,
                    title,
                    firstName,
                    lastName,
                    dob,
                    telephoneNo,
                    email,
                    streetAddress,
                    suburb,
                    state,
                    postCode,
                    country,
                    weeklyLimit,
                    securityQuestion,
                    answer,
                    currencyValue,
                    timezone,
                    clientIP,
                    password,
                    telephonePassword,
                    internetPassword
            );
            assertThat(successMsg).as("Success Message from API").isEqualTo("Customer Created");
        });

        Then("^I verify a new customer created with AML status \"([^\"]*)\" or \"([^\"]*)\"$", (String amlOne, String amlTwo) -> {
            if (null == wapi) wapi = new WAPI();
            String amlStatus = wapi.readAmlStatus(customerUsername, customerPassword);
            assertThat(amlStatus).as("AML status").isIn(amlOne, amlTwo);
        });
    }

}

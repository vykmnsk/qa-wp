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
    private String customer_username = null;
    private WAPI wapi = null;

    public CreateCustomerInAPISteps() {
        When("^I post customer specifics to create new customer$", (DataTable table) -> {
            Map<String, String> cust = table.asMap(String.class, String.class);

            String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
            String username = "AutoUser" + timeStamp;
            this.customer_username = username;

            if (null == wapi) wapi = new WAPI();

            String custTitle = (String) Helpers.nonNullGet(cust, "title");
            String custFirstName = (String) Helpers.nonNullGet(cust, "firstname");
            String custLastName = (String) Helpers.nonNullGet(cust, "lastname");
            String custDob = (String) Helpers.nonNullGet(cust, "date_of_birth");
            String custTelephoneNo = (String) Helpers.nonNullGet(cust, "phonenumber");
            String custEmail = ((String) Helpers.nonNullGet(cust, "email_address")).replace("random", username);
            String custStreetAddress = (String) Helpers.nonNullGet(cust, "street_address");
            String custSuburb = (String) Helpers.nonNullGet(cust, "suburb");
            String custState = (String) Helpers.nonNullGet(cust, "state");
            String custPostCode = (String) Helpers.nonNullGet(cust, "postcode");
            String custCountry = (String) Helpers.nonNullGet(cust, "country");
            String custWeeklyLimit = (String) Helpers.nonNullGet(cust, "weekly_deposit_limit");
            String custSecurityQuestion = (String) Helpers.nonNullGet(cust, "security_question");
            String custAnswer = (String) Helpers.nonNullGet(cust, "customer_answer");
            String custClientIp = (String) Helpers.nonNullGet(cust, "client_ip");
            String currencyValue = (String) Helpers.nonNullGet(cust, "currency");
            String custTimezone = (String) Helpers.nonNullGet(cust, "timezone");
            String custPassword = RandomStringUtils.randomAlphanumeric(10).toUpperCase();
            String custTelephonePassword = RandomStringUtils.randomAlphanumeric(10).toUpperCase();
            String custInternetPassword = RandomStringUtils.randomAlphanumeric(10).toUpperCase();

            String successMsg = wapi.createNewCustomer(
                    username,
                    custTitle,
                    custFirstName,
                    custLastName,
                    custDob,
                    custTelephoneNo,
                    custEmail,
                    custStreetAddress,
                    custSuburb,
                    custState,
                    custPostCode,
                    custCountry,
                    custWeeklyLimit,
                    custSecurityQuestion,
                    custAnswer,
                    currencyValue,
                    custTimezone,
                    custClientIp,
                    custPassword,
                    custTelephonePassword,
                    custInternetPassword
            );
            assertThat(successMsg).isEqualTo("Customer Created");
        });

        Then("^I verify a new customer created with AML status \"([^\"]*)\" or \"([^\"]*)\"$", (String amlOne, String amlTwo) -> {
            if (null == wapi) wapi = new WAPI();

            String amlStatus = wapi.verifyAmlStatus((customer_username).replaceAll("\\[","").replaceAll("]",""), amlOne, amlTwo);
            assertThat(amlStatus).as("AML status").isIn(amlOne, amlTwo);
        });
    }

}

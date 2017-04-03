package com.tabcorp.qa.wagerplayer.steps;

import com.tabcorp.qa.common.Helpers;
import com.tabcorp.qa.wagerplayer.pages.CustomerDetailsPage;
import com.tabcorp.qa.wagerplayer.pages.CustomerListPage;
import com.tabcorp.qa.wagerplayer.pages.HeaderPage;
import com.tabcorp.qa.wagerplayer.pages.NewCustomerPage;
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
    private CustomerListPage custListPage;
    private NewCustomerPage newCustPage;
    private CustomerDetailsPage custDetailsPage;

    public CreateCustomerInUISteps() {

        When("^I navigate to customer list page to insert new customer$", () -> {
            header = new HeaderPage();
            header.navigateToF11();
            custListPage = new CustomerListPage();
            custListPage.verifyLoaded();
            custListPage.insertNew();
            newCustPage = new NewCustomerPage();
            newCustPage.verifyLoaded();
        });

        When("^I enter the following data on Create New Customer page$", (DataTable table) -> {
            Map<String, String> custData = table.asMap(String.class, String.class);

            String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
            String username = "AutoUser" + timeStamp;

            String title = (String) Helpers.nonNullGet(custData, "title");
            String firstName = (String) Helpers.nonNullGet(custData, "firstname");
            String lastName = (String) Helpers.nonNullGet(custData, "lastname");
            String dobTxt = (String) Helpers.nonNullGet(custData, "date_of_birth");
            LocalDate dob = LocalDate.parse(dobTxt, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String telephoneNo = (String) Helpers.nonNullGet(custData, "phonenumber");
            String email = ((String) Helpers.nonNullGet(custData, "email_address")).replace("random", username);
            String streetAddress = (String) Helpers.nonNullGet(custData, "street_address");
            String suburb = (String) Helpers.nonNullGet(custData, "suburb");
            String city = (String) Helpers.nonNullGet(custData, "city");
            String state = (String) Helpers.nonNullGet(custData, "state");
            String postCode = (String) Helpers.nonNullGet(custData, "postcode");
            String country = (String) Helpers.nonNullGet(custData, "country");
            String weeklyLimit = (String) Helpers.nonNullGet(custData, "weekly_deposit_limit");
            String securityQuestion = (String) Helpers.nonNullGet(custData, "security_question");
            String securityAnswer = RandomStringUtils.randomAlphanumeric(10).toUpperCase();
            String currencyValue = (String) Helpers.nonNullGet(custData, "currency");
            String timezone = (String) Helpers.nonNullGet(custData, "timezone");
            String telephonePassword = RandomStringUtils.randomAlphanumeric(10).toUpperCase();
            String internetPassword = RandomStringUtils.randomAlphanumeric(10).toUpperCase();

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
            custDetailsPage = new CustomerDetailsPage();
            custDetailsPage.verifyLoaded();

            class ReloadCheckAMLStatus implements Runnable {
                public void run() {
                    header.refreshPage();
                    String status = custDetailsPage.readAMLStatus();
                    Assertions.assertThat(status).as("AML status").isIn(amlOne, amlTwo);
                }
            }
            Helpers.retryOnAssertionFailure(new ReloadCheckAMLStatus(), 5, 3);
        });

    }

}

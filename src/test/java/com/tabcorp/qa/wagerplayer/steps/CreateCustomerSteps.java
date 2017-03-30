package com.tabcorp.qa.wagerplayer.steps;

import com.tabcorp.qa.common.Helpers;
import com.tabcorp.qa.common.Storage;
import com.tabcorp.qa.wagerplayer.pages.CustomerDetailsPage;
import com.tabcorp.qa.wagerplayer.pages.CustomerListPage;
import com.tabcorp.qa.wagerplayer.pages.HeaderPage;
import com.tabcorp.qa.wagerplayer.pages.NewCustomerPage;
import cucumber.api.DataTable;
import cucumber.api.java8.En;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static com.tabcorp.qa.common.Storage.KEY.CUSTOMER_ID;

public class CreateCustomerSteps implements En {

    private HeaderPage header;
    private CustomerListPage custListPage;
    private NewCustomerPage newCustPage;
    private CustomerDetailsPage custDetailsPage;

    public CreateCustomerSteps() {

        When("^I navigate to customer list page to insert new customer$", () -> {
            header = new HeaderPage();
            header.navigateToF11();
            custListPage = new CustomerListPage();
            custListPage.verifyLoaded();
            custListPage.insertNew();
            newCustPage = new NewCustomerPage();
            newCustPage.verifyLoaded();
        });

        When("^I enter specifics to insert new customer through Wagerplayer$", (DataTable table) -> {
            Map<String, String> cust = table.asMap(String.class, String.class);

            String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
            String username = "AutoUser" + timeStamp;
            Storage.add(CUSTOMER_ID, username);

            String custTitle = (String) Helpers.nonNullGet(cust, "title");
            String custFirstName = (String) Helpers.nonNullGet(cust, "firstname");
            String custLastName = (String) Helpers.nonNullGet(cust, "lastname");
            String custDob = (String) Helpers.nonNullGet(cust, "date_of_birth");
            String custTelephoneNo = (String) Helpers.nonNullGet(cust, "phonenumber");
            String custEmail = ((String) Helpers.nonNullGet(cust, "email_address")).replace("random", username);
            String[] custAddress = ((String) Helpers.nonNullGet(cust, "address")).split(",");
            String custCountry = (String) Helpers.nonNullGet(cust, "country");
            String custWeeklyLimit = (String) Helpers.nonNullGet(cust, "weekly_deposit_limit");
            String custSecurityQuestion = (String) Helpers.nonNullGet(cust, "security_question");
            String custSecurityAnswer = (String) Helpers.nonNullGet(cust, "customer_answer");
            String currencyValue = (String) Helpers.nonNullGet(cust, "currency");
            String custTimezone = (String) Helpers.nonNullGet(cust, "timezone");

            newCustPage.enterCustomerDetails(
                    username,
                    custTitle,
                    custFirstName,
                    custLastName,
                    custDob,
                    custTelephoneNo,
                    custEmail,
                    custAddress,
                    custCountry,
                    custWeeklyLimit,
                    custSecurityQuestion,
                    custSecurityAnswer,
                    currencyValue,
                    custTimezone
            );
        });

        When("^I see new customer created with AML status updated to \"([^\"]*)\" or \"([^\"]*)\"$", (String amlOne, String amlTwo) -> {
            custDetailsPage = new CustomerDetailsPage();
            custDetailsPage.verifyLoaded();
            header.refreshPage();
            custDetailsPage.verifyAmlStatus(amlOne, amlTwo);
        });

    }

}

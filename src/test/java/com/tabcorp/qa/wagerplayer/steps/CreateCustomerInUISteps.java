package com.tabcorp.qa.wagerplayer.steps;

import com.tabcorp.qa.common.Helpers;
import com.tabcorp.qa.common.Storage;
import com.tabcorp.qa.wagerplayer.pages.CustomerDetailsPage;
import com.tabcorp.qa.wagerplayer.pages.CustomerListPage;
import com.tabcorp.qa.wagerplayer.pages.HeaderPage;
import com.tabcorp.qa.wagerplayer.pages.NewCustomerPage;
import cucumber.api.DataTable;
import cucumber.api.java8.En;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

import static com.tabcorp.qa.common.Storage.KEY.CUSTOMER_USERNAME;

public class CreateCustomerInUISteps implements En {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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
            Map<String, String> cust = table.asMap(String.class, String.class);
            String custDob = (String) Helpers.nonNullGet(cust, "date_of_birth");
            LocalDate dob = LocalDate.parse(custDob, DATE_TIME_FORMATTER);
            String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
            String username = "AutoUser" + timeStamp;
            Storage.add(CUSTOMER_USERNAME, username);

            newCustPage.enterCustomerDetails(
                    username,
                    dob,
                    cust
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

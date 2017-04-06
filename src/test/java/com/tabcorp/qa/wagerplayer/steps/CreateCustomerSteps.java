package com.tabcorp.qa.wagerplayer.steps;

import com.tabcorp.qa.common.Helpers;
import com.tabcorp.qa.wagerplayer.api.WAPI;
import com.tabcorp.qa.wagerplayer.dto.Customer;
import com.tabcorp.qa.wagerplayer.pages.CustomersPage;
import com.tabcorp.qa.wagerplayer.pages.HeaderPage;
import com.tabcorp.qa.wagerplayer.pages.LoginPage;
import com.tabcorp.qa.wagerplayer.pages.NewCustomerPage;
import cucumber.api.DataTable;
import cucumber.api.java8.En;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateCustomerSteps implements En {
    //for API
    private String customerUsername;
    private String customerPassword;
    //TODO change to WagerPlayerAPI
    private WAPI api = new WAPI();
    //for UI
    private HeaderPage header;
    private CustomersPage customersPage;
    private NewCustomerPage newCustPage;



    public CreateCustomerSteps() {

        When("^I create a new customer via API with data$", (DataTable table) -> {
            Customer custData = parseUpdateCustomerData(table);
            String successMsg = api.createNewCustomer(custData);
            assertThat(successMsg).as("Success Message from API").isEqualTo("Customer Created");
            this.customerUsername = custData.username;
            this.customerPassword = custData.internetPassword;
        });

        When("^I create a new customer via UI with data$", (DataTable table) -> {
            Customer custData = parseUpdateCustomerData(table);
            loginGoToCustomersPage();
            newCustPage.enterCustomerDetails(custData);
        });

        Then("^the customer AML status in API is updated to \"([^\"]*)\" or \"([^\"]*)\"$", (String amlOne, String amlTwo) -> {
            String amlStatus = api.readAmlStatus(customerUsername, customerPassword);
            assertThat(Helpers.norm(amlStatus)).as("AML status").isIn(Helpers.norm(amlOne), Helpers.norm(amlTwo));
        });

        Then("^the customer AML status in UI is updated to \"([^\"]*)\" or \"([^\"]*)\"$", (String amlOne, String amlTwo) -> {
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

    private Customer parseUpdateCustomerData(DataTable table) {
        List<Customer> customers = table.transpose().asList(Customer.class);
        Customer cust = customers.get(0);

        cust.username = "AutoUser" + RandomStringUtils.randomNumeric(7);
        cust.email = cust.email.replace("#username#", cust.username);
        cust.dob = LocalDate.parse(cust.dateOfBirth, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        cust.securityAnswer = RandomStringUtils.randomAlphanumeric(10);
        cust.password = RandomStringUtils.randomAlphanumeric(10);
        cust.telephonePassword = RandomStringUtils.randomAlphanumeric(10);
        cust.internetPassword = RandomStringUtils.randomAlphabetic(7) + RandomStringUtils.randomNumeric(3);
        return cust;
    }

    private void loginGoToCustomersPage(){
        LoginPage lp = new LoginPage();
        lp.login();
        header = new HeaderPage();
        customersPage = header.navigateToF11();;
        customersPage.verifyLoaded();
        newCustPage = customersPage.insertNew();
        newCustPage.verifyLoaded();
    }
}

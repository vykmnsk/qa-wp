package com.tabcorp.qa.wagerplayer.steps;

import com.tabcorp.qa.common.Helpers;
import com.tabcorp.qa.common.Storage;
import com.tabcorp.qa.mobile.pages.LuxbetMobilePage;
import com.tabcorp.qa.wagerplayer.Config;
import com.tabcorp.qa.wagerplayer.api.WAPI;
import com.tabcorp.qa.wagerplayer.api.WagerPlayerAPI;
import com.tabcorp.qa.wagerplayer.dto.Customer;
import com.tabcorp.qa.wagerplayer.pages.*;
import cucumber.api.DataTable;
import cucumber.api.java8.En;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateCustomerSteps implements En {
    //for API
    private String customerUsername;
    private String customerPassword;
    private String currency;
    private WagerPlayerAPI api = Config.getAPI();
    private WAPI wapi = new WAPI();
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
            this.currency = custData.currency;
        });

        When("^I create a new customer via UI with data$", (DataTable table) -> {
            Customer custData = parseUpdateCustomerData(table);
            loginGoToCustomersPage();
            newCustPage.enterCustomerDetails(custData);
            this.currency = custData.currency;
        });

        Then("^the customer AML status in UI is updated to ([^\"]*)$", (String expectedAmlStatus) -> {
            customersPage.verifyLoaded();

            class ReloadCheckAMLStatus implements Runnable {
                public void run() {
                    header.refreshPage();
                    String actualAmlStatus = customersPage.readAMLStatus();
                    Assertions.assertThat(actualAmlStatus).as("AML status").isEqualToIgnoringCase(expectedAmlStatus);
                }
            }
            Helpers.retryOnAssertionFailure(new ReloadCheckAMLStatus(), 5, 3);
        });

        Then("^the customer AML status in API is updated to ([^\"]*)$", (String expectedAmlStatus) -> {
            String accessToken = api.login(customerUsername, customerPassword);
            String actualAmlStatus = api.readAmlStatus(accessToken);
            assertThat(actualAmlStatus).isEqualToIgnoringCase(expectedAmlStatus);
            Storage.put(Storage.KEY.API_ACCESS_TOKEN, accessToken);
        });

        Then("^the affiliate customer should be able to login to mobile site successfully$", () -> {
            String sessionId = wapi.login(customerUsername, customerPassword);
            String mobileAccessToken = wapi.generateAffiliateLoginToken(sessionId);

            LuxbetMobilePage lmp = new LuxbetMobilePage();
            lmp.load(mobileAccessToken);
            lmp.verifyNoLoginError();
            lmp.verifyDisplaysUsername(customerUsername);
        });
        When("^the customer deposits (\\d+\\.\\d\\d) cash via API$", (BigDecimal cashAmount) -> {
            String accessToken = (String) Storage.get(Storage.KEY.API_ACCESS_TOKEN);

            //TODO implement depositCash in MOBI and remove the following login
            String sessionId = wapi.login(customerUsername, customerPassword);

            String statusMsg = wapi.depositCash(sessionId, cashAmount);
            assertThat(statusMsg).isEqualToIgnoringCase(cashAmount + " " + currency + " successfully deposited");
        });

        When("^the customer deposits (\\d+\\.\\d\\d) cash via UI$", (BigDecimal cashAmount) -> {
            DepositPage depositPage = customersPage.openDepositWindow();
            depositPage.verifyLoaded();
            depositPage.selectManualTab();

            String transMsg = depositPage.depositCash(cashAmount);
            assertThat(transMsg).contains("successfully");

            depositPage.verifyTransactionRecord(transMsg, cashAmount, currency);
        });

    }

    private Customer parseUpdateCustomerData(DataTable table) {
        List<Customer> customers = table.transpose().asList(Customer.class);
        Customer customer = customers.get(0);
        customer.username = "AutoUser" + RandomStringUtils.randomNumeric(7);
        customer.email = customer.email.replace("#username#", customer.username);
        customer.dob = LocalDate.parse(customer.dateOfBirth, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        customer.securityAnswer = RandomStringUtils.randomAlphanumeric(10);
        customer.password = RandomStringUtils.randomAlphabetic(7) + RandomStringUtils.randomNumeric(3);
        customer.telephonePassword = RandomStringUtils.randomAlphanumeric(10);
        customer.internetPassword = customer.password;
        customer.manualVerification = customer.manualVerification.equalsIgnoreCase("Y") ? "1" : "0";

        return customer;
    }

    private void loginGoToCustomersPage() {
        LoginPage lp = new LoginPage();
        lp.login();
        header = new HeaderPage();
        customersPage = header.navigateToF11();
        customersPage.verifyLoaded();
        newCustPage = customersPage.insertNew();
        newCustPage.verifyLoaded();
    }
}

package com.tabcorp.qa.wagerplayer.steps;

import com.tabcorp.qa.common.Helpers;
import com.tabcorp.qa.common.Storage;
import com.tabcorp.qa.common.Storage.KEY;
import com.tabcorp.qa.common.StrictHashMap;
import com.tabcorp.qa.mobile.pages.LuxbetMobilePage;
import com.tabcorp.qa.wagerplayer.Config;
import com.tabcorp.qa.wagerplayer.api.WAPI;
import com.tabcorp.qa.wagerplayer.api.WagerPlayerAPI;
import com.tabcorp.qa.wagerplayer.pages.CustomersPage;
import com.tabcorp.qa.wagerplayer.pages.DepositPage;
import com.tabcorp.qa.wagerplayer.pages.HeaderPage;
import com.tabcorp.qa.wagerplayer.pages.LoginPage;
import com.tabcorp.qa.wagerplayer.pages.NewCustomerPage;
import cucumber.api.DataTable;
import cucumber.api.java8.En;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static com.tabcorp.qa.common.Storage.KEY.API_ACCESS_TOKEN;
import static com.tabcorp.qa.common.Storage.KEY.BALANCE_BEFORE;
import static com.tabcorp.qa.common.Storage.KEY.CUSTOMER;
import static org.assertj.core.api.Assertions.assertThat;

public class CreateCustomerSteps implements En {
    //for API
    private WagerPlayerAPI api = Config.getAPI();
    private WAPI wapi = new WAPI();
    //for UI
    private HeaderPage header;
    private CustomersPage customersPage;
    private NewCustomerPage newCustPage;
    private static Logger log = LoggerFactory.getLogger(CreateCustomerSteps.class);

    public CreateCustomerSteps() {
        Given("^Existing customer with at least \\$(\\d+\\.\\d\\d) balance is logged in API$", (BigDecimal minBalance) -> {
            String accessToken = api.login(Config.customerUsername(), Config.customerPassword(), Config.clientIp());
            BigDecimal currentBalance = api.getBalance(accessToken);
            Storage.put(BALANCE_BEFORE, currentBalance);
            assertThat(Helpers.roundOff(currentBalance)).as("Min balance").isGreaterThanOrEqualTo(minBalance);
            Storage.put(API_ACCESS_TOKEN, accessToken);
        });

        Given("^A new default customer with \\$(\\d+\\.\\d\\d) balance is created and logged in API$", (BigDecimal requiredBalance) -> {
            final String resourcesPath = "src/test/resources/";

            final String filename;
            if (Config.REDBOOK.equals(Config.appName())) {
                filename = "customer-default-RB.yml";
            } else if (Config.LUXBET.equals(Config.appName())){
                filename = "customer-default-LB.yml";
            } else {
                throw new RuntimeException("Unknown App name=" + Config.appName());
            }
            InputStream input;
            try {
                input = new FileInputStream(new File(resourcesPath + filename));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            Map<String, String> custRaw = (Map<String, String>) (new Yaml()).load(input);
            Map<String, String> custData = adjustCustomerData(custRaw);

            String successMsg = api.createNewCustomer(custData);
            assertThat(successMsg).as("Success Message from API").isEqualTo("Customer Created");
            log.info("Created a new customer: " + custData);

            Storage.put(CUSTOMER, custData);
            String clientIp = custData.containsKey("client_ip") ? custData.get("client_ip") : null;
            String accessToken = api.login(custData.get("username"), custData.get("password"), clientIp);
            assertThat(accessToken).as("session ID / accessToken").isNotEmpty();
            Storage.put(API_ACCESS_TOKEN, accessToken);

            String statusMsg = wapi.depositCash(accessToken, requiredBalance);
            assertThat(statusMsg).isEqualToIgnoringCase(requiredBalance + " " + custData.get("currency_code") + " successfully deposited");
        });

        When("^I create a new customer via API with data$", (DataTable table) -> {
            Map<String, String> custDataInput = table.asMap(String.class, String.class);
            Map<String, String> custData = adjustCustomerData(custDataInput);
            String successMsg = api.createNewCustomer(custData);
            assertThat(successMsg).as("Success Message from API").isEqualTo("Customer Created");
            Storage.put(CUSTOMER, custData);
            log.info("Created a new customer: " + custData);
        });

        When("^I create a new customer via UI with data$", (DataTable table) -> {
            Map<String, String> custDataInput = table.asMap(String.class, String.class);
            Map<String, String> custData = adjustCustomerData(custDataInput);
            loginGoToCustomersPage();
            newCustPage.enterCustomerDetails(custData);
            Storage.put(CUSTOMER, custData);
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
            String accessToken = storedCustomerLogin();
            String clientIp = storedCustomerClientIP();
            class ReloadCheckAMLStatus implements Runnable {
                public void run() {
                    String actualAmlStatus = api.readAmlStatus(accessToken, clientIp);
                    assertThat(actualAmlStatus).isEqualToIgnoringCase(expectedAmlStatus);
                }
            }
            Helpers.retryOnAssertionFailure(new ReloadCheckAMLStatus(), 5, 2);
            Storage.put(Storage.KEY.API_ACCESS_TOKEN, accessToken);
        });

        Then("^the affiliate customer should be able to login to mobile site successfully$", () -> {
            String sessionId = storedCustomerLogin();
            String mobileAccessToken = wapi.generateAffiliateLoginToken(sessionId);

            LuxbetMobilePage lmp = new LuxbetMobilePage();
            lmp.load(mobileAccessToken);
            lmp.verifyNoLoginError();
            Map<String, String> custData = (Map<String, String>) Storage.get(KEY.CUSTOMER);
            lmp.verifyDisplaysUsername(custData.get("username"));
        });
        When("^the customer deposits (\\d+\\.\\d\\d) cash via API$", (BigDecimal cashAmount) -> {
            String accessToken = (String) Storage.get(Storage.KEY.API_ACCESS_TOKEN);

            //TODO implement depositCash in MOBI and remove the following login
            String sessionId = storedCustomerLogin();
            String statusMsg = wapi.depositCash(sessionId, cashAmount);

            Map<String, String> custData = (Map<String, String>) Storage.get(KEY.CUSTOMER);
            String currency = (String) Helpers.nonNullGet(custData, "currency_code");
            assertThat(statusMsg).isEqualToIgnoringCase(cashAmount + " " + currency + " successfully deposited");
        });

        When("^the customer deposits (\\d+\\.\\d\\d) cash via UI$", (BigDecimal cashAmount) -> {
            DepositPage depositPage = customersPage.openDepositWindow();
            depositPage.verifyLoaded();
            depositPage.selectManualTab();

            String transMsg = depositPage.depositCash(cashAmount);
            assertThat(transMsg).contains("successfully");

            Map<String, String> custData = (Map<String, String>) Storage.get(KEY.CUSTOMER);
            String currency = (String) Helpers.nonNullGet(custData, "currency_code");
            depositPage.verifyTransactionRecord(transMsg, cashAmount, currency);
        });

    }

    private String storedCustomerLogin() {
        Map<String, String> custData = (Map<String, String>) Storage.get(KEY.CUSTOMER);
        String clientIp = storedCustomerClientIP();
        return api.login(custData.get("username"),  custData.get("password"), clientIp);
    }

    private String storedCustomerClientIP() {
        Map<String, String> custData = (Map<String, String>) Storage.get(KEY.CUSTOMER);
        return Config.LUXBET.equals(Config.appName()) ? custData.get("client_ip") : null;
    }

    private Map<String, String> adjustCustomerData(Map<String, String> custInput) {
        Map custFiltered = custInput.entrySet().stream()
                .filter(entry -> !"N/A".equals(entry.getValue()))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
        StrictHashMap<String, String> cust = new StrictHashMap<>();
        cust.putAll(custFiltered);
        cust.put("username", "AutoUser" + RandomStringUtils.randomNumeric(7));
        String password = RandomStringUtils.randomAlphabetic(7) + RandomStringUtils.randomNumeric(3);
        cust.put("password", password);
        cust.put("internet_password", password);
        cust.put("telephone_password", RandomStringUtils.randomAlphabetic(7) + RandomStringUtils.randomNumeric(3));
        cust.put("secret_answer", RandomStringUtils.randomAlphanumeric(10));
        cust.put("email_address", cust.get("username") + cust.get("_email_address_suffix"));
        if (cust.containsKey("manual_verification")) {
            cust.put("manual_verification", "Y".equalsIgnoreCase(cust.get("manual_verification")) ? "1" : "0");
        }
        LocalDate dob = LocalDate.parse(cust.get("dob"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        cust.put("dob-year", String.valueOf(dob.getYear()));
        cust.put("dob-month", String.valueOf(dob.getMonthValue()));
        cust.put("dob-day", String.valueOf(dob.getDayOfMonth()));
        return cust;
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

package com.tabcorp.qa.wagerplayer.steps;

import com.tabcorp.qa.adyen.Card;
import com.tabcorp.qa.common.Helpers;
import com.tabcorp.qa.common.Storage;
import com.tabcorp.qa.common.Storage.KEY;
import com.tabcorp.qa.common.StrictHashMap;
import com.tabcorp.qa.mobile.pages.LuxbetMobilePage;
import com.tabcorp.qa.wagerplayer.Config;
import com.tabcorp.qa.wagerplayer.api.MOBI_V2;
import com.tabcorp.qa.wagerplayer.api.WAPI;
import com.tabcorp.qa.wagerplayer.api.WagerPlayerAPI;
import com.tabcorp.qa.wagerplayer.pages.CustomersPage;
import com.tabcorp.qa.wagerplayer.pages.DepositPage;
import com.tabcorp.qa.wagerplayer.pages.HeaderPage;
import com.tabcorp.qa.wagerplayer.pages.LoginPage;
import com.tabcorp.qa.wagerplayer.pages.NewCustomerPage;
import com.tabcorp.qa.wagerplayer.pages.PromotionPage;
import cucumber.api.DataTable;
import cucumber.api.java8.En;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static com.tabcorp.qa.common.Storage.KEY.API_ACCESS_TOKEN;
import static com.tabcorp.qa.common.Storage.KEY.BALANCE_BEFORE;
import static com.tabcorp.qa.common.Storage.KEY.CUSTOMER;
import static org.assertj.core.api.Assertions.assertThat;

public class CustomerSteps implements En {
    //for API
    private WagerPlayerAPI api = Config.getAPI();
    private WAPI wapi = new WAPI();
    //for UI
    private HeaderPage header;
    private CustomersPage customersPage;
    private NewCustomerPage newCustPage;
    private static Logger log = LoggerFactory.getLogger(CustomerSteps.class);

    public CustomerSteps() {
        Given("^Existing customer with at least \\$(\\d+\\.\\d\\d) balance is logged in API$", (BigDecimal minBalance) -> {
            String accessToken = api.login(Config.customerUsername(), Config.customerPassword(), Config.clientIp());
            BigDecimal currentBalance = api.getBalance(accessToken);
            Storage.put(BALANCE_BEFORE, currentBalance);
            assertThat(Helpers.roundOff(currentBalance)).as("Min balance").isGreaterThanOrEqualTo(minBalance);
            Storage.put(API_ACCESS_TOKEN, accessToken);
        });

        Given("^A new default customer with \\$(\\d+\\.\\d\\d) balance is created and logged in API$", (BigDecimal requiredBalance) -> {
            final String filename;
            if (Config.isRedbook()) {
                filename = "customer-default-RB.yml";
            } else if (Config.isLuxbet()) {
                filename = "customer-default-LB.yml";
            } else {
                throw new RuntimeException("Unknown App name=" + Config.appName());
            }
            Map<String, String> custRaw = Helpers.loadYamlResource(filename);
            Map<String, String> custData = adjustCustomerData(custRaw);

            String successMsg = api.createNewCustomer(custData);
            assertThat(successMsg).as("Success Message from API").isEqualTo("Customer Created");
            log.info("Created a new customer: " + custData);

            Storage.put(CUSTOMER, custData);
            String clientIp = custData.containsKey("client_ip") ? custData.get("client_ip") : null;
            String accessToken = api.login(custData.get("username"), custData.get("password"), clientIp);
            assertThat(accessToken).as("session ID / accessToken").isNotEmpty();
            Storage.put(API_ACCESS_TOKEN, accessToken);

            if (Config.isLuxbet()) {
                String statusMsg = wapi.depositCash(accessToken, requiredBalance);
                assertThat(statusMsg).isEqualToIgnoringCase(requiredBalance + " " + custData.get("currency_code") + " successfully deposited");
            } else if (Config.isRedbook()) {
                addCCMakeDeposit(
                        custData.get("CardNumber"),
                        custData.get("CVC"),
                        custData.get("ExpiryMonth"),
                        custData.get("ExpiryYear"),
                        custData.get("CardHolderName"),
                        custData.get("CardType"),
                        requiredBalance);
            } else {
                throw new RuntimeException("Unknown App name=" + Config.appName());
            }

            Storage.put(BALANCE_BEFORE, requiredBalance);
        });

        When("^I create a new customer via API with data$", (DataTable table) -> {
            Map<String, String> custDataInput = table.asMap(String.class, String.class);
            Map<String, String> custData = adjustCustomerData(custDataInput);
            String successMsg = api.createNewCustomer(custData);
            assertThat(successMsg).as("Success Message from API").isEqualTo("Customer Created");
            Storage.put(CUSTOMER, custData);
            log.info("New Customer created: " + custData);
        });

        When("^I create a new customer via UI with data$", (DataTable table) -> {
            Map<String, String> custDataInput = table.asMap(String.class, String.class);
            Map<String, String> custData = adjustCustomerData(custDataInput);
            loginGoToCustomersPage();
            newCustPage.enterCustomerDetails(custData);
            Storage.put(CUSTOMER, custData);
            log.info("New Customer created: " + custData);
        });

        Then("^the customer AML status in UI is updated to ([^\"]*)$", (String expectedAmlStatus) -> {
            customersPage.verifyLoaded();
            Helpers.retryOnFailure(() -> {
                header.refreshPage();
                String actualAmlStatus = customersPage.readAMLStatus();
                Assertions.assertThat(actualAmlStatus).as("AML status").isEqualToIgnoringCase(expectedAmlStatus);
            }, 10, 3);
        });

        Then("^the customer AML status in API is updated to ([^\"]*)$", (String expectedAmlStatus) -> {
            String accessToken = loginStoredCustomer();
            Helpers.retryOnFailure(() -> {
                String actualAmlStatus = api.readAmlStatus(accessToken);
                assertThat(actualAmlStatus).isEqualToIgnoringCase(expectedAmlStatus);
            }, 10, 2);
            Storage.put(Storage.KEY.API_ACCESS_TOKEN, accessToken);
        });

        Then("^the affiliate customer should be able to login to mobile site successfully$", () -> {
            String sessionId = loginStoredCustomer();
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
            String sessionId = loginStoredCustomer();
            String statusMsg = wapi.depositCash(sessionId, cashAmount);

            Map<String, String> custData = (Map<String, String>) Storage.get(KEY.CUSTOMER);
            String expectedMsg = String.format("%s %s successfully deposited", cashAmount, custData.get("currency_code"));
            assertThat(statusMsg).as("deposit status message").isEqualToIgnoringCase(expectedMsg);
        });

        When("^the customer deposits (\\d+\\.\\d\\d) cash via UI$", (BigDecimal cashAmount) -> {
            DepositPage depositPage = customersPage.openDepositWindow();
            depositPage.verifyLoaded();
            depositPage.selectManualTab();

            String transMsg = depositPage.depositCash(cashAmount);
            assertThat(transMsg).contains("successfully");

            Map<String, String> custData = (Map<String, String>) Storage.get(KEY.CUSTOMER);
            depositPage.verifyTransactionRecord(transMsg, cashAmount, custData.get("currency_code"));
        });

        When("^I activate promotion code \"([^\"]*)\" for default customer in WP with activation note \"([^\"]*)\"", (String promoCode, String activationNote) -> {
            header = new HeaderPage();
            customersPage = header.navigateToF11();
            Map<String, String> custData = (Map<String, String>) Storage.get(KEY.CUSTOMER);
            customersPage.searchCustomer(custData.get("username"));
            PromotionPage promotionPage = customersPage.openPromotionWindow();
            promotionPage.verifyLoaded();
            promotionPage.activatePromotion(promoCode, activationNote);
        });

        When("^I add credit card to customer and I make a deposit of \\$(\\d+.\\d\\d)$", (BigDecimal deposit, DataTable table) -> {
            Map<String, String> cardInfoInput = table.asMap(String.class, String.class);
            StrictHashMap<String, String> cardInfo = new StrictHashMap<>();
            cardInfo.putAll(cardInfoInput);
            addCCMakeDeposit(
                    cardInfo.get("CardNumber"),
                    cardInfo.get("CVC"),
                    cardInfo.get("ExpiryMonth"),
                    cardInfo.get("ExpiryYear"),
                    cardInfo.get("CardHolderName"),
                    cardInfo.get("CardType"),
                    deposit);
        });

        Then("^I withdraw \\$(\\d+.\\d\\d) using stored \"([^\"]*)\" card$", (BigDecimal withdrawAmount, String cardType) -> {
            String accessToken = (String) Storage.get(Storage.KEY.API_ACCESS_TOKEN);
            MOBI_V2 api = new MOBI_V2();
            String withdrawReference = api.getPaymentRefence(accessToken);
            String storedCardReference = api.getStoredCardReference(accessToken);
            api.withdrawWithCreditCard(accessToken, cardType, withdrawAmount, storedCardReference, withdrawReference);
        });

        Given("^customer balance is at least \\$(\\d+.\\d\\d)$", (BigDecimal minBalance) -> {
            String accessToken = (String) Storage.get(API_ACCESS_TOKEN);
            BigDecimal currentBalance = api.getBalance(accessToken);
            assertThat(currentBalance).as("current customer balance").isGreaterThanOrEqualTo(minBalance);
            Storage.put(BALANCE_BEFORE, currentBalance);
        });

        Then("^customer balance is equal to \\$(\\d+\\.\\d\\d)$", (BigDecimal expectedBalance) -> {
            String accessToken = (String) Storage.get(API_ACCESS_TOKEN);
            BigDecimal currentBalance = api.getBalance(accessToken);
            assertThat(Helpers.roundOff(currentBalance)).isEqualTo(Helpers.roundOff(expectedBalance));
            Storage.put(BALANCE_BEFORE, currentBalance);
        });

        Then("^customer bonus bet balance is equal to \\$(\\d+\\.\\d\\d)$", (BigDecimal expectedBalance) -> {
            String accessToken = (String) Storage.get(API_ACCESS_TOKEN);
            BigDecimal currentBalance = wapi.getBonusBalance(accessToken);
            assertThat(Helpers.roundOff(currentBalance)).isEqualTo(Helpers.roundOff(expectedBalance));
        });

    }

    private void addCCMakeDeposit(String cardNumber, String cvc, String expMonth, String expYear, String cardHolderName, String cardType, BigDecimal deposit) {
        List<String> cardNumbers = Arrays.asList(cardNumber, cvc, expMonth, expYear);
        assertThat(cardNumbers).as("Card data contains only numbers").allMatch(NumberUtils::isNumber);
        Helpers.verifyNotExpired(Integer.parseInt(expMonth), Integer.parseInt(expYear));

        final MOBI_V2 mobi = new MOBI_V2();
        String accessToken = (String) Storage.get(Storage.KEY.API_ACCESS_TOKEN);

        String publicKey = Helpers.readResourceFile("adyen-encryption-key.txt");
        // if becomes obsolete try:
        // String encryptionKey = mobi.getEncryptionKey(accessToken);
        Card card = new Card();
        card.setNumber(cardNumber);
        card.setCvc(cvc);
        card.setExpiryMonth(expMonth);
        card.setExpiryYear(expYear);
        card.setCardHolderName(cardHolderName);
        card.setGenerationTime(new Date());
        String cardEncryption;
        try {
            cardEncryption = card.serialize(publicKey);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        String paymentReference = mobi.getPaymentRefence(accessToken);
        mobi.addCardAndDeposit(accessToken, paymentReference, cardEncryption, cardType, deposit);
    }

    private String loginStoredCustomer() {
        String storedToken = (String) Storage.getOrElse(KEY.API_ACCESS_TOKEN, null);
        if (null != storedToken) {
            return storedToken;
        }
        Map<String, String> custData = (Map<String, String>) Storage.get(KEY.CUSTOMER);
        String clientIp = Config.isLuxbet() ? custData.get("client_ip") : null;
        String newToken = api.login(custData.get("username"), custData.get("password"), clientIp);
        Storage.put(KEY.API_ACCESS_TOKEN, newToken);
        return newToken;
    }

    private Map<String, String> adjustCustomerData(Map<String, String> custInput) {
        Map<String, String> custFiltered = custInput.entrySet().stream()
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

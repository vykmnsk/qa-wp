package com.tabcorp.qa.wagerplayer.steps;

import com.jayway.jsonpath.ReadContext;
import com.tabcorp.qa.adyen.ClientSideEncrypter;
import com.tabcorp.qa.common.*;
import com.tabcorp.qa.common.Storage.KEY;
import com.tabcorp.qa.mobile.pages.LuxbetMobilePage;
import com.tabcorp.qa.wagerplayer.Config;
import com.tabcorp.qa.wagerplayer.api.MOBI_V2;
import com.tabcorp.qa.wagerplayer.api.WAPI;
import com.tabcorp.qa.wagerplayer.api.WagerPlayerAPI;
import com.tabcorp.qa.wagerplayer.pages.*;
import cucumber.api.DataTable;
import cucumber.api.java8.En;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.stream.Collectors;

import static com.tabcorp.qa.common.Storage.KEY.*;
import static com.tabcorp.qa.wagerplayer.api.WagerPlayerAPI.KEY.MPID;
import static com.tabcorp.qa.wagerplayer.api.WagerPlayerAPI.KEY.WIN_PRICE;
import static org.assertj.core.api.Assertions.assertThat;

public class CustomerSteps implements En {
    //for API
    private WagerPlayerAPI api = Config.getAPI();
    private WAPI wapi = new WAPI();
    List<String> responseMessages;
    //for UI
    private HeaderPage header;
    private FooterPage footer;
    private InterceptPage intercept;
    private CustomersPage customersPage;
    private NewCustomerPage newCustPage;
    private MasterControllerPage masCtrlPage;
    private DepositPage depositPage;
    private static final Logger log = LoggerFactory.getLogger(CustomerSteps.class);

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
                throw new FrameworkError("Unknown App name=" + Config.appName());
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

            makeDeposit(accessToken, custData, requiredBalance);
            Storage.put(BALANCE_BEFORE, requiredBalance);
        });

        Given("^I edit the Master Controller Settings$", (DataTable table) -> {
            StrictHashMap<String, String> settings = new StrictHashMap<>();
            settings.putAll(table.asMap(String.class, String.class));
            masCtrlPage = new MasterControllerPage();
            masCtrlPage.load();
            masCtrlPage.enterSettings(settings.getBoolean("Enable Duplicate Account Checking on Customer Sign-Up"));
        });

        When("^I create a new customer via API with data$", (DataTable table) -> {
            Map<String, String> custDataInput = table.asMap(String.class, String.class);
            Map<String, String> custData = adjustCustomerData(custDataInput);
            String successMsg = api.createNewCustomer(custData);
            assertThat(successMsg).as("Success Message from API").isEqualTo("Customer Created");
            Storage.put(CUSTOMER, custData);
            Storage.put(PREV_CUSTOMER, custData);
            log.info("New Customer created: " + custData);
        });

        When("^I verify customer statement via API$", (DataTable table) -> { ;
            StrictHashMap<String, String> txnData = new StrictHashMap<>();
            txnData.putAll(table.asMap(String.class, String.class));
            String accessToken = (String) Storage.get(Storage.KEY.API_ACCESS_TOKEN);
            MOBI_V2 mobi = new MOBI_V2();
            ReadContext custTransRecords = mobi.getCustomerStatement(accessToken);
            String transId = Storage.get(Storage.KEY.TXN_ID).toString();
            Map transRecord = mobi.extractTransRecord(custTransRecords, transId);

            Assertions.assertThat(transRecord.get("type")).as("Transaction Type").isEqualTo(txnData.get("type"));
            Assertions.assertThat(transRecord.get("dr")).as("Transaction Settle Amount").isEqualTo(txnData.get("debit amount"));
            Assertions.assertThat(transRecord.get("cr")).as("Trnsaction Credit Amount").isEqualTo(txnData.get("credit amount"));
            Assertions.assertThat(transRecord.get("balance")).as("Customer Balance").isEqualTo(txnData.get("balance"));

            String txnDesc = (txnData.get("type")+ ": " + Storage.get(Storage.KEY.BET_EXT_ID) + " - " + txnData.get("description"));
            log.debug("Transaction Desc=" + txnDesc);
            Assertions.assertThat(transRecord.get("description")).isEqualTo(txnDesc);
        });

        And("^I update the daily deposit limit to \\$(\\d+.\\d\\d)$", (BigDecimal cashAmount) -> {
            DepositPage depositPage = customersPage.openDepositWindow();
            depositPage.verifyLoaded();
            depositPage.selectDepositRestrictionsTab();
            depositPage.updateDailyDepositLimit(cashAmount);
        });

        When("^I try to create a new customer via API with data from a non approved country$", (DataTable table) -> {
            Map<String, String> custDataInput = table.asMap(String.class, String.class);
            Map<String, String> custData = adjustCustomerData(custDataInput);
            responseMessages = api.createCustomerFails(custData);
        });

        When("^I create a new customer via API with old customer data$", (DataTable table) -> {
            Map<String, String> custDataInput = table.asMap(String.class, String.class);
            Map<String, String> custData = adjustCustomerData(custDataInput);
            Map<String, String> prevCustData = (Map<String, String>) Storage.get(KEY.PREV_CUSTOMER);
            String prevLastName = (String) prevCustData.get("lastname");
            custData.replace("lastname", prevLastName);
            String successMsg = api.createNewCustomer(custData);
            assertThat(successMsg).as("Success Message from API").isEqualTo("Customer Created");
            Storage.put(CUSTOMER, custData);
            log.info("New Customer created: " + custData);
        });

        When("^I attempt to create a new customer via API with (invalid|existing) data$", (String action, DataTable table) -> {
            boolean isExisting = "existing".equals(action);
            Map<String, String> custDataInput = table.asMap(String.class, String.class);
            Map<String, String> custData = adjustCustomerData(custDataInput);
            if (isExisting) {
                Map<String, String> existCustData = (Map<String, String>) Storage.get(KEY.CUSTOMER);
                custData.replace("lastname", existCustData.get("lastname"));
                custData.replace("postcode", existCustData.get("postcode"));
                custData.replace("dob", existCustData.get("dob"));
            }
            List<String> errors = api.createCustomerFails(custData);
            assertThat(errors).as("Errors Messages from API").contains(custData.get("api_resp_message"));
        });

        When("^I create a new customer via UI with data$", (DataTable table) -> {
            Map<String, String> custDataInput = table.asMap(String.class, String.class);
            Map<String, String> custData = adjustCustomerData(custDataInput);
            loginGoToCustomersPage();
            newCustPage.enterCustomerDetails(custData);
            Storage.put(CUSTOMER, custData);
            Storage.put(PREV_CUSTOMER, custData);
            log.info("New Customer created: " + custData);
        });

        When("^I update the customer details$", (DataTable table) -> {
            String accessToken = loginStoredCustomer();
            Map<String, String> custDataInput = table.asMap(String.class, String.class);
            Map<String, String> custData = updateCustomerData(custDataInput);
            MOBI_V2 mobi = new MOBI_V2();
            String successMsg = mobi.updateCustomer(accessToken, custData);
            assertThat(successMsg).as("Success Message from API").isEqualTo("Customer Updated");
            Storage.put(CUSTOMER, custData);
            log.info("Customer updated: " + custData);
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

        Then("the customer should not be created", () -> {
            assertThat(responseMessages.get(0)).as("Reject Message from API").isEqualTo("Access from your current location is prohibited");
        });

        When("^I update the customer AML status to \"([^\"]*)\"$", (String newamlstatus) -> {
            header = new HeaderPage();
            customersPage = header.navigateToF11();
            Map<String, String> custData = (Map<String, String>) Storage.get(KEY.CUSTOMER);
            customersPage.searchCustomer(custData.get("username"));
            CustomerConfigPage customerConfigPage = customersPage.openConfigWindow();
            customerConfigPage.updateAmlStatus(newamlstatus);
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
        When("^the customer deposits \\$(\\d+.\\d\\d) as \"([^\"]*)\" via API$", (BigDecimal cashAmount, String depositType) -> {
            //TODO implement depositCash in MOBI and remove the following login
            String sessionId = loginStoredCustomer();
            String statusMsg = wapi.depositCash(sessionId, cashAmount, WAPI.DepositType.valueOf(depositType));

            Map<String, String> custData = (Map<String, String>) Storage.get(KEY.CUSTOMER);
            String expectedMsg = String.format("%s %s successfully deposited", cashAmount, custData.get("currency_code"));
            assertThat(statusMsg).as("deposit status message").isEqualToIgnoringCase(expectedMsg);
        });

        When("^the customer deposits \\$(\\d+.\\d\\d) as \"([^\"]*)\" via UI$", (BigDecimal cashAmount, WAPI.DepositType depositType) -> {
            DepositPage depositPage = customersPage.openDepositWindow();
            depositPage.verifyLoaded();
            depositPage.selectManualTab();

            String transMsg = depositPage.depositCash(cashAmount, depositType);
            assertThat(transMsg).contains("successfully");

            Map<String, String> custData = (Map<String, String>) Storage.get(KEY.CUSTOMER);
            depositPage.verifyTransactionRecord(transMsg, cashAmount, custData.get("currency_code"));
        });

        When("^the customer deposits \\$(\\d+.\\d\\d) as \"([^\"]*)\" using BankToBank tab via UI$", (BigDecimal cashAmount, String depositType) -> {
            depositPage = customersPage.openDepositWindow();
            depositPage.verifyLoaded();
            depositPage.depositBankToBank(cashAmount);
        });

        Then("the error message \"([^\"]*)\" is received$", (String errorMsg) -> {
            depositPage.verifyDepositBankToBankFails(errorMsg);
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

        Then("^I deposit \\$(\\d+.\\d\\d) using stored \"([^\"]*)\" card using promo code \"([^\"]*)\"$", (BigDecimal depositAmount, String cardType, String promoCode) -> {
            String accessToken = (String) Storage.get(Storage.KEY.API_ACCESS_TOKEN);
            MOBI_V2 api = new MOBI_V2();
            String depositReference = api.getPaymentRefence(accessToken);
            String storedCardReference = api.getStoredCardReference(accessToken);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("cvc", "737");
            String encryptedCVV = adyenEncode(jsonObject);
            api.useExistingCardToDeposit(accessToken, cardType, depositAmount, depositReference, encryptedCVV, storedCardReference, promoCode);
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
            //added the retryOnFailure because the customer balance  for a few features
            // such as Casino Bonus BuyIn takes some time to reflect the promo code changes.
            Helpers.retryOnFailure(() -> {
                BigDecimal currentBalance = api.getBalance(accessToken);
                assertThat(Helpers.roundOff(currentBalance)).isEqualTo(Helpers.roundOff(expectedBalance));
                Storage.put(BALANCE_BEFORE, currentBalance);
            }, 5, 5);
        });

        Then("^customer bonus bet balance is equal to \\$(\\d+\\.\\d\\d)$", (BigDecimal expectedBalance) -> {
            String accessToken = (String) Storage.get(API_ACCESS_TOKEN);
            BigDecimal currentBalance = wapi.getBonusBalance(accessToken);
            assertThat(Helpers.roundOff(currentBalance)).isEqualTo(Helpers.roundOff(expectedBalance));
        });

        And("^I modify Intercept on Bet Placement Racing to \"([^\"]*)\" on customer config for default customer$",
                (String option) -> {
                    header = new HeaderPage();
                    customersPage = header.navigateToF11();
                    Map<String, String> custData = (Map<String, String>) Storage.get(KEY.CUSTOMER);
                    customersPage.searchCustomer(custData.get("username"));
                    CustomerConfigPage customerConfigPage = customersPage.openConfigWindow();
                    customerConfigPage.verifyLoaded();
                    customerConfigPage.selectInterceptOnRacingBetPlacement();
                });

        When("^I place a single \"([^\"]*)\" bet on the runner \"([^\"]*)\" for \\$(\\d+\\.\\d\\d) and \"([^\"]*)\" the intercept$",
                (String betType, String runner, BigDecimal stake, WAPI.InterceptOption interceptOption) -> {
                    String partialAmount = "";
                    placeInterceptSingleBet(runner, stake, interceptOption, partialAmount);
                });

        When("^I place a single \"([^\"]*)\" bet on the runner \"([^\"]*)\" for \\$(\\d+\\.\\d\\d) and do Partial the intercept for \\$(\\d+\\.\\d\\d)$",
                (String betType, String runner, BigDecimal stake, String partialAmount) -> {
                    WAPI.InterceptOption interceptOption = WAPI.InterceptOption.Partial;
                    placeInterceptSingleBet(runner, stake, interceptOption, partialAmount);
                });

        When("^I place a Luxbet Treble Bet \"([A-Za-z]+)\"-\"([A-Za-z]+)\"-\"([A-Za-z]+)\" on the runners \"([^\"]*)\" for \\$(\\d+.\\d\\d) with flexi as \"([^\"]*)\" and do \"([^\"]*)\" the intercept$",
                (String betTypeName1, String betTypeName2, String betTypeName3, String runnersCSV, BigDecimal stake, String flexi, WAPI.InterceptOption interceptOption) -> {
                    String partialAmount = "0";
                    placeInterceptMultiBet(betTypeName1, betTypeName2, betTypeName3, runnersCSV, stake, flexi, interceptOption, partialAmount);
                });

        When("^I place a Luxbet Treble Bet \"([A-Za-z]+)\"-\"([A-Za-z]+)\"-\"([A-Za-z]+)\" on the runners \"([^\"]*)\" for \\$(\\d+.\\d\\d) with flexi as \"([^\"]*)\" and do Partial the intercept for \\$(\\d+\\.\\d\\d)$",
                (String betTypeName1, String betTypeName2, String betTypeName3, String runnersCSV, BigDecimal stake, String flexi, String partialAmount) -> {
                    WAPI.InterceptOption interceptOption = WAPI.InterceptOption.Partial;
                    placeInterceptMultiBet(betTypeName1, betTypeName2, betTypeName3, runnersCSV, stake, flexi, interceptOption, partialAmount);
                });

        When("^I place a Luxbet Multi Bet \"([^\"]*)\" on the runners \"([^\"]*)\" for \\$(\\d+.\\d\\d) with flexi as \"([^\"]*)\" and do \"([^\"]*)\" the intercepts with partial amount as \\$(\\d+\\.\\d\\d)$",
                (String multiTypeName, String runnersCSV, BigDecimal stake, String flexi, String interceptOptionCSV, String partialAmount) -> {
                    assertThat(Config.isLuxbet()).as("LUXBET only step").isTrue();
                    WAPI.MultiType multiType = WAPI.MultiType.fromName(multiTypeName);
                    List<String> runners = Helpers.extractCSV(runnersCSV);
                    List<String> interceptOptions = Helpers.extractCSV(interceptOptionCSV);
                    List<WAPI.InterceptOption> newInterceptOptions = new ArrayList<>();
                    for (String interceptOpt : interceptOptions) {
                        newInterceptOptions.add(WAPI.InterceptOption.valueOf(interceptOpt));
                    }
                    boolean isFlexi = "Y".equalsIgnoreCase(flexi);
                    String interceptText = "Patent  (See panel for details)";
                    new Thread(() -> {
                        ReadContext response = wapi.placeMultipleBetsInIntercept(multiType, null, runners, stake, isFlexi, interceptOptions, partialAmount);
                        List<Integer> betIds = wapi.readBetIds(response);
                        assertThat(betIds.size()).isGreaterThan(0);
                        log.info("Bet IDs=" + betIds);
                    }).start();
                    Helpers.delayInMillis(5000);
                    actionOnIntercept(interceptText, newInterceptOptions, partialAmount);
                });

    }

    private void makeDeposit(String accessToken, Map<String, String> custData, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }
        if (Config.isLuxbet()) {
            String statusMsg = wapi.depositCash(accessToken, amount, WAPI.DepositType.CashDeposit);
            assertThat(statusMsg).isEqualToIgnoringCase(amount + " " + custData.get("currency_code") + " successfully deposited");
        } else if (Config.isRedbook()) {
            JSONObject card = new JSONObject();
            card.put("number", custData.get("CardNumber"));
            card.put("cvc", custData.get("CVC"));
            card.put("expiryMonth", custData.get("ExpiryMonth"));
            card.put("expiryYear", custData.get("ExpiryYear"));
            card.put("holderName", custData.get("CardHolderName"));
            String encryptedCard = adyenEncode(card);

            final MOBI_V2 mobi = new MOBI_V2();
            String paymentReference = mobi.getPaymentRefence(accessToken);
            mobi.addCardAndDeposit(accessToken, paymentReference, encryptedCard, custData.get("CardType").toLowerCase(), amount);
        } else {
            throw new FrameworkError("Unknown App name=" + Config.appName());
        }
    }

    public static String adyenEncode(JSONObject json){
        String publicKey = Helpers.readResourceFile("adyen-encryption-key.txt");
        // if above becomes obsolete try:
        // String encryptionKey = mobi.getEncryptionKey(accessToken);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        json.put("generationtime", simpleDateFormat.format(new Date()));
        ClientSideEncrypter encrypter;
        String encryptedData;
        try {
            encrypter = new ClientSideEncrypter(publicKey);
            encryptedData = encrypter.encrypt(json.toString());
        } catch (Exception e) {
            throw new FrameworkError(String.format("Adyen encrypt failed for %s with error %s", json.toString(), e));
        }
        return encryptedData;
    }

    private void placeInterceptSingleBet(String runner, BigDecimal stake, WAPI.InterceptOption interceptOption, String partialAmount) {
        String accessToken = (String) Storage.get(API_ACCESS_TOKEN);
        String eventId = (String) Storage.getLast(EVENT_IDS);
        Integer prodId = (Integer) Storage.getLast(PRODUCT_IDS);
        Integer bonusBetFlag = 0;
        String catg = (String) Storage.getLast(KEY.CATEGORIES);
        String subCatg = (String) Storage.getLast(KEY.SUBCATEGORIES);
        String eventName = (String) Storage.getLast(KEY.EVENT_NAMES);
        String interceptText = catg + ": " + subCatg + ": " + eventName + " (Racing Live): 1 " + runner;
        ReadContext resp = wapi.getEventMarkets(accessToken, eventId);
        Map<WagerPlayerAPI.KEY, String> selection = wapi.readSelectionForRacing(resp, runner, prodId, true);
        new Thread(() -> {
            ReadContext response = wapi.placeSingleWinBetForIntercept(accessToken, prodId, selection.get(MPID), selection.get(WIN_PRICE), stake, bonusBetFlag, interceptOption, partialAmount);
            List<Integer> betIds;
            switch (interceptOption) {
                case Accept:
                    betIds = wapi.readBetIds(response);
                    assertThat(betIds.size()).isGreaterThan(0);
                    log.info("Bet IDs=" + betIds);
                    break;
                case Reject:
                    String readResp = wapi.readInterceptStatus(response);
                    assertThat(readResp).isNotEmpty();
                    log.info("Bet IDs not generated as bet in intercept was Rejected");
                    break;
                case Partial:
                    betIds = wapi.readBetIds(response);
                    assertThat(betIds.size()).isGreaterThan(0);
                    log.info("Bet IDs=" + betIds);
                    String newStake = wapi.readInterceptNewStake(response);
                    assertThat(newStake).isEqualTo(partialAmount);
                    log.info("Bet placed using partial stake amount");
                    break;
                default:
                    throw new FrameworkError("Unexpected interceptOption=" + interceptOption);
            }
        }).start();
        actionOnIntercept(interceptText, Arrays.asList(interceptOption), partialAmount);
    }

    private void placeInterceptMultiBet(String betTypeName1, String betTypeName2, String betTypeName3, String runnersCSV, BigDecimal stake, String flexi, WAPI.InterceptOption interceptOption, String partialAmount) {
        assertThat(Config.isLuxbet()).as("LUXBET only step").isTrue();
        BetType betType1 = BetType.fromName(betTypeName1);
        BetType betType2 = BetType.fromName(betTypeName2);
        BetType betType3 = BetType.fromName(betTypeName3);
        List<String> runners = Helpers.extractCSV(runnersCSV);
        boolean isFlexi = "Y".equalsIgnoreCase(flexi);
        String interceptText = "Treble  (See panel for details)";
        new Thread(() -> {
            ReadContext response = wapi.placeMultiBetInIntercept(WAPI.MultiType.Treble, Arrays.asList(betType1, betType2, betType3), runners, stake, isFlexi, interceptOption, partialAmount);
            switch (interceptOption) {
                case Accept:
                    String accessToken = (String) Storage.get(Storage.KEY.API_ACCESS_TOKEN);
                    List<Integer> betIds = wapi.readBetIds(response);
                    log.info("Bet IDs=" + betIds);
                    if (!Arrays.asList(betType1, betType2, betType3).isEmpty()) {
                        for (int betId : betIds) {
                            List<BetType> placedBetTypes = wapi.getBetTypes(accessToken, betId);
                            assertThat(placedBetTypes)
                                    .as(String.format("Expected Bet Types for %s bet, betId=%d", WAPI.MultiType.Treble.exactName, betId))
                                    .isEqualTo(Arrays.asList(betType1, betType2, betType3));
                        }
                    }
                    break;
                case Reject:
                    String readResp = wapi.readInterceptRejectedCombin(response);
                    assertThat(readResp).isNotEmpty();
                    log.info("Bet IDs not generated as bet in intercept was Rejected");
                    break;
                case Partial:
                    String newStake = wapi.readInterceptNewStake(response);
                    assertThat(newStake).isEqualTo(partialAmount);
                    break;
                default:
                    throw new FrameworkError("Unexpected interceptOption=" + interceptOption);
            }

            if (interceptOption.equals(WAPI.InterceptOption.Accept)) {
            } else if (interceptOption.equals(WAPI.InterceptOption.Reject)) {

            } else if (interceptOption.equals(WAPI.InterceptOption.Partial)) {
            }
        }).start();
        Helpers.delayInMillis(5000);
        actionOnIntercept(interceptText, Arrays.asList(interceptOption), partialAmount);
    }

    public void actionOnIntercept(String interceptText, List<WAPI.InterceptOption> interceptOptions, String partialAmount) {
        footer = new FooterPage();
        footer.toggleIntercept();
        intercept = new InterceptPage();
        if (interceptOptions.size() == 1) {
            intercept.actionOnBetIntercept(interceptText, interceptOptions.get(0), partialAmount);
        } else if (interceptOptions.size() > 1) {
            intercept.actionOnMultipleBetsOnIntercept(interceptText, interceptOptions, partialAmount);
        }
        footer.toggleIntercept();
    }

    private void addCCMakeDeposit(String cardNumber, String cvc, String expMonth, String expYear, String cardHolderName, String cardType, BigDecimal deposit) {
        List<String> cardNumbers = Arrays.asList(cardNumber, cvc, expMonth, expYear);
        assertThat(cardNumbers).as("Card data contains only numbers").allMatch(NumberUtils::isNumber);
        Helpers.verifyNotExpired(Integer.parseInt(expMonth), Integer.parseInt(expYear));

        final MOBI_V2 mobi = new MOBI_V2();
        String accessToken = (String) Storage.get(Storage.KEY.API_ACCESS_TOKEN);

        JSONObject card = new JSONObject();
        card.put("number", cardNumber);
        card.put("holderName", cardHolderName);
        card.put("cvc", cvc);
        card.put("expiryMonth", expMonth);
        card.put("expiryYear", expYear);
        String encryptedCard = adyenEncode(card);

        String paymentReference = mobi.getPaymentRefence(accessToken);
        mobi.addCardAndDeposit(accessToken, paymentReference, encryptedCard, cardType, deposit);
    }

    private String loginStoredCustomer() {
        String storedToken = (String) Storage.getOrElse(KEY.API_ACCESS_TOKEN, null);
        if (null != storedToken) {
            return storedToken;
        }
        Map<String, String> custData = (Map<String, String>) Storage.get(KEY.CUSTOMER);
        String clientIp = custData.getOrDefault("client_ip", null);
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
        if (Config.isRedbook()) {
            cust.put("lastname", "Auto" + RandomStringUtils.randomAlphabetic(30));
        }
        cust.put("username", "AutoUser" + RandomStringUtils.randomNumeric(9));
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

    private Map<String, String> updateCustomerData(Map<String, String> custInput) {
        Map<String, String> custFiltered = custInput.entrySet().stream()
                .filter(entry -> !"N/A".equals(entry.getValue()))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
        StrictHashMap<String, String> cust = new StrictHashMap<>();
        cust.putAll(custFiltered);
        Map<String, String> custgetData = (Map<String, String>) Storage.get(KEY.CUSTOMER);
        cust.put("email_address", custgetData.get("email_address"));
        cust.put("res_street_address", custgetData.get("street"));
        cust.put("res_city", custgetData.get("city"));
        cust.put("street", custgetData.get("street"));
        cust.put("city", custgetData.get("city"));
        cust.put("country", custgetData.get("country"));
        cust.put("telephone", custgetData.get("telephone"));
        cust.put("currency_code", custgetData.get("currency_code"));
        cust.put("res_postcode", custgetData.get("postcode"));
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

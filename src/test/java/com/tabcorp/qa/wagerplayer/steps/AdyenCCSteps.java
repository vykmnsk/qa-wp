package com.tabcorp.qa.wagerplayer.steps;

import com.tabcorp.qa.common.Helpers;
import com.tabcorp.qa.common.Storage;
import com.tabcorp.qa.wagerplayer.api.MOBI_V2;
import com.tabcorp.qa.wagerplayer.dto.Customer;
import com.tabcorp.qa.adyen.pages.AdyenPage;
import cucumber.api.DataTable;
import cucumber.api.java8.En;
import java.math.BigDecimal;
import java.util.Map;

public class AdyenCCSteps implements En {
    public AdyenCCSteps() {
        When("^I add credit card to customer and I make a deposit of \\$(\\d+.\\d\\d)$", (BigDecimal deposit,  DataTable table) -> {
            Map<String, String> cardDetails = table.asMap(String.class, String.class);

            String expMonth = Helpers.nonNullGet(cardDetails, "ExpiryMonth").toString();
            String expYear = Helpers.nonNullGet(cardDetails, "ExpiryYear").toString();
            Helpers.verifyNotExpired(Integer.parseInt(expMonth), Integer.parseInt(expYear));
            String cardNumber = (String) Helpers.nonNullGet(cardDetails, "CardNumber");
            String cvc = (String) Helpers.nonNullGet(cardDetails, "Cvc");
            String cardHolderName = (String) Helpers.nonNullGet(cardDetails, "CardHolderName");
            String cardType = (String) Helpers.nonNullGet(cardDetails, "CardType");

            Customer customer = (Customer) Storage.get(Storage.KEY.CUSTOMER);
            MOBI_V2 api = new MOBI_V2();
            String accessToken = api.login(customer.username, customer.password);
            String encryptionKey = api.getEncryptionKey(accessToken);

            AdyenPage adyenPage = new AdyenPage();
            adyenPage.load();
            String cardEncryption = adyenPage.getCardEncryption(
                    encryptionKey,
                    cardNumber,
                    cvc,
                    expMonth,
                    expYear,
                    cardHolderName
            );

            String paymentReference = api.getPaymentRefence(accessToken);
            api.addCardAndDeposit(accessToken, paymentReference, cardEncryption, cardType, deposit);
        });
    }
}
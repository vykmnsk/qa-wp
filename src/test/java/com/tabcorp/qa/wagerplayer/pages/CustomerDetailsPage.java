package com.tabcorp.qa.wagerplayer.pages;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerDetailsPage extends AppPage {

    @FindBy(css = "table#side_table_inner tbody tr")
    List<WebElement> custInfoRows;
    final String statusLabel = "AML Status:";

    public void verifyLoaded() {
        HeaderPage header = new HeaderPage();
        header.verifyPageTitle("Customers");
    }

    public String readAMLStatus() {
        String statusRow = custInfoRows.stream()
                .map(r -> r.getText().trim())
                .filter(t -> t.startsWith(statusLabel))
                .findFirst()
                .orElse(null);
        Assertions.assertThat(statusRow).as("Found customer info row for " + statusLabel).isNotNull();

        int labelEnd = statusRow.indexOf(statusLabel) + statusLabel.length();
        return statusRow.substring(labelEnd).trim();
    }

}

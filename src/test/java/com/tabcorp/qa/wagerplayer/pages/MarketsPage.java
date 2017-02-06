package com.tabcorp.qa.wagerplayer.pages;


import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class MarketsPage extends AppPage{

    @FindBy(css = "#main_table tbody tr th")
    WebElement header;

    public void vefifyLoaded(){
        wait.until(ExpectedConditions.visibilityOf(header));
        Assertions.assertThat(header.getText()).as("Markets Page header").isEqualTo("Markets");
    }

}

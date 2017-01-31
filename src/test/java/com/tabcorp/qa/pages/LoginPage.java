package com.tabcorp.qa.pages;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.CacheLookup;

import java.util.List;


public class LoginPage extends BasePage {

    @CacheLookup
    @FindBy(css = ("input[name='entered_login']"))
    public WebElement username;

    @CacheLookup
    @FindBy(css = ("input[name='entered_password']"))
    public WebElement password;

    private String submitRedbookCSS = "input[name='login']";
    private String submitLuxbetCSS = "input[alt^='Enter']";

    @FindBy(css = ("div[class='login-error']"))
    public WebElement errorDiv;

    public void load(){
        driver.get(baseUrl);
        Assertions.assertThat(username.isDisplayed())
                .withFailMessage("could not load login page with username input")
                .isTrue();
    }

    public HomePage enterValidCredentials(){

        String usernameValue = System.getenv("WAGERPLAYER_USERNAME");
        Assertions.assertThat(usernameValue)
                .withFailMessage("username not provided")
                .isNotNull();
        username.sendKeys(usernameValue);

        String passwordValue = System.getenv("WAGERPLAYER_PASSWORD");

        Assertions.assertThat(passwordValue)
                .withFailMessage("password not provided")
                .isNotNull();
        password.sendKeys(passwordValue);

        WebElement submit = findSubmitButton();
        Assertions.assertThat(submit)
                .withFailMessage("could not find submit button")
                .isNotNull();
        submit.click();

        return new HomePage();
    }

    private WebElement findSubmitButton(){
        List<WebElement> elems = driver.findElements(By.cssSelector(submitRedbookCSS));
        List<WebElement> elems2 =  driver.findElements(By.cssSelector(submitLuxbetCSS));
        elems.addAll(elems2);
        if (elems.size() > 0) return elems.get(0);
        return null;
    }

}

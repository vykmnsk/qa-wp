package com.tabcorp.qa.wagerplayer.pages;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.CacheLookup;

import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;


public class LoginPage extends BasePage {

    private final String ENV_USERNAME = "WAGERPLAYER_USERNAME";
    private final String ENV_PASSWORD = "WAGERPLAYER_PASSWORD";

    @CacheLookup
    @FindBy(css = ("input[name='entered_login']"))
    private WebElement username;

    @CacheLookup
    @FindBy(css = ("input[name='entered_password']"))
    private WebElement password;

    private By submitLuxbet = By.cssSelector("input[alt^='Enter']");
    private By submitRedbook = By.cssSelector("input[name='login']");

    private By errorLuxbet = By.cssSelector("tbody td b i");
    private By errorRedbook = By.cssSelector("div[class='login-error']");

    public void load(){
        driver.get(baseUrl);

        wait.until(visibilityOf(username));
        Assertions.assertThat(username.isDisplayed())
                .withFailMessage("could not load login page with username input")
                .isTrue();
    }

    public HomePage enterValidCredentials(){
        String usernameValue = System.getenv(ENV_USERNAME);
        Assertions.assertThat(usernameValue)
                .withFailMessage(ENV_USERNAME + " env var is not provided")
                .isNotNull();
        username.sendKeys(usernameValue);

        String passwordValue = System.getenv(ENV_PASSWORD);
        Assertions.assertThat(passwordValue)
                .withFailMessage(ENV_PASSWORD + " env var is not provided")
                .isNotNull();
        password.sendKeys(passwordValue);

        WebElement submit = findOne(submitLuxbet, submitRedbook);
        Assertions.assertThat(submit)
                .withFailMessage("could not find submit button")
                .isNotNull();
        submit.click();

        return new HomePage();
    }

    public LoginPage enterInvalidCredentials(){
        username.sendKeys("invalid username");
        password.sendKeys("invalid passowrd");
        WebElement submit = findOne(submitLuxbet, submitRedbook);
        submit.click();
        return this;
    }

    public void verifyMessage(String msg){
        String actualMsg = findOne(errorLuxbet, errorRedbook).getText();
        Assertions.assertThat(actualMsg)
            .withFailMessage(String.format("Expected %s, but got %s", msg, actualMsg))
            .contains(msg);
    }

}

package com.tabcorp.qa.wagerplayer.pages;

import com.tabcorp.qa.wagerplayer.Config;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.CacheLookup;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;


public class LoginPage extends AppPage {

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
        super.load();
        wait.until(visibilityOf(username));
        Assertions.assertThat(username.isDisplayed())
                .withFailMessage("could not load login page with username input")
                .isTrue();
    }

    public HomePage enterValidCredentials(){
        username.sendKeys(Config.username());
        password.sendKeys(Config.password());


        WebElement submit = findEither(submitLuxbet, submitRedbook);
        Assertions.assertThat(submit)
                .withFailMessage("could not find submit button")
                .isNotNull();
        submit.click();

        return new HomePage();
    }

    public LoginPage enterInvalidCredentials(){
        username.sendKeys("invalid username");
        password.sendKeys("invalid passowrd");
        WebElement submit = findEither(submitLuxbet, submitRedbook);
        submit.click();
        return this;
    }

    public void verifyErrorMessage(String msg){
        String actualMsg = findEither(errorLuxbet, errorRedbook).getText();
        Assertions.assertThat(actualMsg)
            .withFailMessage(String.format("Expected %s, but got %s", msg, actualMsg))
            .contains(msg);
    }

}

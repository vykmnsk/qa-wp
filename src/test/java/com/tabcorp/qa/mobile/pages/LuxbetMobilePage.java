package com.tabcorp.qa.mobile.pages;

import com.tabcorp.qa.wagerplayer.Config;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class LuxbetMobilePage extends AppPage {
    private static Logger log = LoggerFactory.getLogger(LuxbetMobilePage.class);

    @FindBy(css = ("#login .validationMessageContainer"))
    private List<WebElement> loginErrors;

    final String ribbonCSS = "nav .mainNav .ribbon";
    @FindBy(css = (ribbonCSS + " .ribbonText"))
    private WebElement ribbonKey;
    @FindBy(css = (ribbonCSS + " .ribbonValue"))
    private WebElement ribbonValue;

    public void load(String accessToken) {
        String url = Config.luxMobileURL() + accessToken;
        driver.get(url);
        log.info("Loaded URL: " + url);
    }

    public void verifyNoLoginError() {
        List <String> messages = loginErrors.stream().map(el -> el.getText()).collect(Collectors.toList());
        Assertions.assertThat(messages).as("Login error messages").isEmpty();
    }

    public void verifyDisplaysUsername(String username) {
        Assertions.assertThat(ribbonKey.getText()).as("Account record key").isEqualToIgnoringCase("Acc");
        Assertions.assertThat(ribbonValue.getText()).as("Account record value").isEqualToIgnoringCase(username);
    }
}

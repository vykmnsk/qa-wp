package com.tabcorp.qa.wagerplayer.steps;

import com.tabcorp.qa.wagerplayer.pages.HomePage;
import com.tabcorp.qa.wagerplayer.pages.LoginPage;
import cucumber.api.java8.En;

    public class LoginSteps implements En {
        LoginPage lp;
        HomePage hp;

        public LoginSteps() {
            Given("^the user is on WagerPlayer Login page$", () -> {
                lp = new LoginPage();
                lp.load();
            });

            When("^the user enters username and password", () -> {
                hp = lp.enterValidCredentials();
            });

            Then("^homepage is displayed$", () -> {
                hp.verifyLoaded();
            });

            When("^I login with wrong username and password$", () -> {
                lp = new LoginPage();
                lp.load();
                lp = lp.enterInvalidCredentials();
            });

            Then("^I see \"([^\"]*)\" message$", (String msg) -> {
                lp.verifyMessage(msg);
            });

        }
}

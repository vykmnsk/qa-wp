package com.tabcorp.qa.steps;

import com.tabcorp.qa.pages.HomePage;
import com.tabcorp.qa.pages.LoginPage;
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

        }
}

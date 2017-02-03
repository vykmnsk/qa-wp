package com.tabcorp.qa.wagerplayer.steps;

import com.tabcorp.qa.wagerplayer.pages.HomePage;
import com.tabcorp.qa.wagerplayer.pages.LoginPage;
import cucumber.api.java8.En;

    public class LoginSteps implements En {
        LoginPage loginPage;
        HomePage homePage;

        public LoginSteps() {
//            Given("^the user is on WagerPlayer Login page$", () -> {
//                loginPage = new LoginPage();
//                loginPage.load();
//            });
//
//            When("^the user enters username and password", () -> {
//                homePage = loginPage.enterValidCredentials();
//            });

            When("^I login with valid username and password$", () -> {
                loginPage = new LoginPage();
                loginPage.load();
                homePage = loginPage.enterValidCredentials();
            });

            Then("^homepage is displayed$", () -> {
                homePage.verifyLoaded();
            });

            When("^I login with invalid username and password$", () -> {
                loginPage = new LoginPage();
                loginPage.load();
                loginPage = loginPage.enterInvalidCredentials();
            });

            Then("^I see \"([^\"]*)\" message$", (String msg) -> {
                loginPage.verifyErrorMessage(msg);
            });

        }
}

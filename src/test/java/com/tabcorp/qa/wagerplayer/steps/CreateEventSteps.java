package com.tabcorp.qa.wagerplayer.steps;

import com.tabcorp.qa.wagerplayer.pages.*;
import cucumber.api.DataTable;
import cucumber.api.java8.En;
import org.assertj.core.api.Assertions;

import java.util.Map;

public class CreateEventSteps implements En {

    NewEventPage newEvtPage;
    MarketsPage marketsPage;

    public CreateEventSteps() {

        Given("^I am logged in and on Home Page$", () -> {
            LoginPage lp = new LoginPage();
            lp.load();
            HomePage hp = lp.enterValidCredentials();
            hp.verifyLoaded();
        });

        When("^I enter specifics category \"([^\"]*)\" and subcategory \"([^\"]*)\"$", (String category, String subcategory) -> {
            HeaderPage header = new HeaderPage();
            header.pickCategories(category, subcategory);
        });

        Then("^I see New Event page$", () -> {
            newEvtPage = new NewEventPage();
            newEvtPage.load();
        });

        When("^I enter event details with current 'show time' and 'event date/time' in (\\d+) minutes with data$",
                (Integer inMinutes, DataTable table) -> {
                    Map<String, String> evt = table.asMap(String.class, String.class);
                    Assertions.assertThat(evt.keySet()).as("event details").isNotEmpty();
                    marketsPage = newEvtPage.enterEventDetails(evt.get("event name"),
                            evt.get("bet in run type"),
                            evt.get("create market"),
                            Integer.valueOf(inMinutes));
        });

        Then("^I see Create Market page$", () -> {
            marketsPage.vefifyLoaded();
        });


    }


}

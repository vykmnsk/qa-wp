package com.tabcorp.qa.wagerplayer;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features={"src/test/features"},
        plugin={"pretty", "html:target/cucumber", "json:target/cucumber.json"}
)

public class RunCukesTest {

}
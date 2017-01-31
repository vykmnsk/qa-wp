package com.tabcorp.qa;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features={"src/test/features"},
        glue={"com.tabcorp.qa.steps"},
        plugin={"pretty", "html:target/cucumber"}
)

public class RunCukesTest {}
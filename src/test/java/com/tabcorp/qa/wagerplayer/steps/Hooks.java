package com.tabcorp.qa.wagerplayer.steps;

import com.tabcorp.qa.common.DriverWrapper;
import com.tabcorp.qa.common.Helpers;
import com.tabcorp.qa.common.Storage;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hooks {
    private static final Logger log = LoggerFactory.getLogger(Hooks.class);

    @Before
    public void beforeScenario(){
        Storage.init();
    }

    @After
    public void afterScenario(Scenario scenario){
        DriverWrapper dw = DriverWrapper.getInstance();
        if (scenario.isFailed() && dw.hasDriver()) {
            try {
                Helpers.saveScreenshot(dw.getDriver(), scenario.getName());
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        dw.closeBrowser();
    }
}

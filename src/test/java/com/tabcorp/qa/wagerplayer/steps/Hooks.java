package com.tabcorp.qa.wagerplayer.steps;

import com.tabcorp.qa.common.DriverWrapper;
import com.tabcorp.qa.common.Helpers;
import com.tabcorp.qa.common.Storage;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;

public class Hooks {
    @Before
    public void beforeScenario(){
        Storage.init();
    }

    @After
    public void afterScenario(Scenario scenario){
        DriverWrapper dw = DriverWrapper.getInstance();
        if (scenario.isFailed() && dw.hasDriver()) {
            Helpers.saveScreenshot(dw.getDriver(), scenario.getName());
        }
        dw.closeBrowser();
    }
}

package com.tabcorp.qa.wagerplayer.steps;

import com.tabcorp.qa.common.DriverWrapper;
import com.tabcorp.qa.common.Storage;
import cucumber.api.java.After;
import cucumber.api.java.Before;

public class Hooks {
    @Before
    public void beforeScenario(){
        Storage.init();
    }

    @After
    public void afterScenario(){
        DriverWrapper.getInstance().closeBrowser();
    }

}

package com.tabcorp.qa.wagerplayer.steps;

import com.tabcorp.qa.common.DriverWrapper;
import cucumber.api.java.After;

public class Hooks {
    @After
    public void afterScenario(){
        DriverWrapper.getInstance().closeBrowser();
    }

}

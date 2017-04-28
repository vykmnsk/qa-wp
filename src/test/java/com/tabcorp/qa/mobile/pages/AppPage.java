package com.tabcorp.qa.mobile.pages;

import com.tabcorp.qa.common.AnyPage;
import org.openqa.selenium.support.PageFactory;

public class AppPage extends AnyPage{

    AppPage(){
        PageFactory.initElements(driver, this);
    }

}

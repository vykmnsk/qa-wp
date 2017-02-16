package com.tabcorp.qa.wagerplayer.pages;

import com.tabcorp.qa.common.AnyPage;
import com.tabcorp.qa.common.DriverWrapper;
import javafx.beans.binding.When;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.Driver;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(Parameterized.class)
public class NewEventPageTest {

    NewEventPage systemUnderTest;

    @Mock
    DriverWrapper mockDriverWrapper;

    @Mock
    WebDriver mockDriver;

    @Mock
    WebDriverWait mockDriverWait;

    private int noOfRunner;

    @Before
    public void setUp() {
        initMocks(this);
        DriverWrapper.setInstance(mockDriverWrapper);
        when(mockDriverWrapper.getDriver()).thenReturn(mockDriver);
        when(mockDriverWrapper.getDriverWait()).thenReturn(mockDriverWait);
        systemUnderTest = new NewEventPage();
    }

    @After
    public void tearDown() {
        DriverWrapper.setInstance(null);
    }

    public NewEventPageTest(int inputRunner) {
        this.noOfRunner = inputRunner;
    }


    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { 0},
                { 1 },
                { 10 },
        });
    }

    @Test
    public void canGenerateRunners() {
        List runners = systemUnderTest.generateRunnersString("Runner_",noOfRunner);
        assertThat(runners.size())
                .withFailMessage(String.format("Expected number of runners were %d, but found %d ", noOfRunner, runners.size()))
                .isEqualTo(noOfRunner);

    }
}
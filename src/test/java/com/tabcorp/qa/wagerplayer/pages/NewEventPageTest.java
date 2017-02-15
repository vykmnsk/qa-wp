package com.tabcorp.qa.wagerplayer.pages;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NewEventPageTest {

    NewEventPage systemUnderTest;
    String expectedRunners;

    @Before
    public void setUp() {
        systemUnderTest = new NewEventPage();
    }

    @Test
    public void canGenerateRunners() {
        String runners = systemUnderTest.generateRunnersString(10);
        expectedRunners = "Runner_1" + "\n" +
                "Runner_2" + "\n" +
                "Runner_3" + "\n" +
                "Runner_4" + "\n" +
                "Runner_5" + "\n" +
                "Runner_6" + "\n" +
                "Runner_7" + "\n" +
                "Runner_8" + "\n" +
                "Runner_9" + "\n" +
                "Runner_10";

                assertThat(runners)
                        .withFailMessage("String is not matching with expected string " + runners)
                        .isEqualTo(expectedRunners);

    }

    @Test
    public void canGenerateRunnersWithOne() {
        String runners = systemUnderTest.generateRunnersString(1);
        expectedRunners = "Runner_1";

        assertThat(runners)
                .withFailMessage("String is not matching with expected string " + runners)
                .isEqualTo(expectedRunners);

    }

    @Test
    public void canGenerateRunnersWithZero() {
        String runners = systemUnderTest.generateRunnersString(0);
        expectedRunners = "";

        assertThat(runners)
                .withFailMessage("String is not matching with expected string " + runners)
                .isEqualTo(expectedRunners);

    }

}
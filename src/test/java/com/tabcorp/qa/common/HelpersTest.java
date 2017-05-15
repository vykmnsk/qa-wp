package com.tabcorp.qa.common;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class HelpersTest {

    @Test
    public void canRandomBetweenInclusiveBeWithinRange() {
        for (int i = 0; i < 100; i++) {
            int rand = Helpers.randomBetweenInclusive(1, 3);
            assertThat(rand).isIn(1, 2, 3);
        }
    }

    @Test
    public void canGenerateRandomPricesAsManyAsExpected() {
        final int COUNT = 5;
        List<BigDecimal> prices = Helpers.generateRandomPrices(1, 100, COUNT);
        assertThat(prices.size()).as("generated prices count").isEqualTo(COUNT);
    }

    @Test
    public void canGenerateRandomPricesContainDecimalPoints() {
        List<BigDecimal> prices = Helpers.generateRandomPrices(1, 100, 10);
        assertThat(prices).allMatch(p -> p.toString().contains("."));
    }

    @Test
    public void canGenerateRandomPricesBeWithinRange() {
        final int MIN = 10;
        final int UPTO = 100;
        List<BigDecimal> prices = Helpers.generateRandomPrices(MIN, UPTO, 10);
        assertThat(prices).as("prices greater than MIN").allMatch(p -> p.compareTo(new BigDecimal(MIN)) > 0);
        assertThat(prices).as("prices less than %d", UPTO).allMatch(p -> p.compareTo(new BigDecimal(UPTO)) <= 0);
    }

    @Test
    public void canGenerateRunnersAsManyAsExpected() {
        final int COUNT = 5;
        List runners = Helpers.generateRunners("Player_", COUNT);
        assertThat(runners.size()).as("generated runners count").isEqualTo(COUNT);
    }

    @Test
    public void canGenerateRunnersWithCorrectNames() {
        final String INITIAL = "Player_";
        final int COUNT = 3;
        List<String> runners = Helpers.generateRunners(INITIAL, COUNT);
        assertThat(runners).as("generated runners names").allMatch((n -> n.startsWith(INITIAL)));
        for (int i = 1; i <= COUNT; i++) {
            assertThat(runners.get(i - 1)).as("runner name").isEqualTo(INITIAL + i);
        }
    }

    @Test
    public void canCreateUniqueNameGenerateDifferentNames() {
        final String BASE = "Test Base Name";
        String name1 = Helpers.createUniqueName(BASE);
        String name2 = Helpers.createUniqueName(BASE);
        assertThat(name1).as("names are different").isNotEqualToIgnoringCase(name2);
    }

    @Test
    public void canCreateUniqueNameKeepBaseNameSame() {
        final String BASE = "Test Base Name";
        String name1 = Helpers.createUniqueName(BASE);
        String name2 = Helpers.createUniqueName(BASE);
        assertThat(name1).contains(BASE);
        assertThat(name2).contains(BASE);
    }

    @Test
    public void canNormalize() {
        final String input = "Active - KYC verified";
        final String expectedOutput = "ACTIVE_KYC_VERIFIED";
        String output = Helpers.norm(input);
        assertThat(output).isEqualTo(expectedOutput);
    }

    @Test
    public void canExtractCSV() {
        List<String> inputs = Arrays.asList(
                "one, two, three",
                "one,two,three",
                "one,  two , three"
        );
        List<String> expected = Arrays.asList("one", "two", "three");
        for (String input : inputs) {
            List<String> actual = Helpers.extractCSV(input);
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Test(expected = AssertionError.class)
    public void canAssertOnEmptyCSV() {
        Helpers.extractCSV(" ");
    }

    @Test
    public void canExtractSingleCSV() {
       String input = "12";
       List<String> expected = Arrays.asList("12");
       List<String> actual = Helpers.extractCSV(input);
       assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void canExtractCSVWithWhitespaces() {
        String input = "Black Caviar; White Lady";
        List<String> expected = Arrays.asList("Black Caviar", "White Lady");
        List<String> actual = Helpers.extractCSV(input, ';');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void canZipToMap() {
        List<String> headers = Arrays.asList("ONE", "TWO", "THREE");
        List<Integer> data = Arrays.asList(1, 2, 3);
        Map<String, Integer> expected = new HashMap<>();
        expected.put("ONE", 1);
        expected.put("TWO", 2);
        expected.put("THREE", 3);
        Map<String, Integer> actual = (Map<String, Integer>) Helpers.zipToMap(headers, data);
        assertThat(actual).isEqualTo(expected);
    }
}


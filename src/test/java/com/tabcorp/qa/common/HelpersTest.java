package com.tabcorp.qa.common;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class HelpersTest {

    @Test
    public void canRandomBetweenInclusiveBeWithinRange(){
        for (int i=0; i<100; i++) {
            int rand = Helpers.randomBetweenInclusive(1,3);
            Assertions.assertThat(rand).isIn(1,2,3);
        }
    }
    @Test
    public void canGenerateRandomPricesAsManyAsExpected() {
        final int COUNT = 5;
        List<BigDecimal> prices = Helpers.generateRandomPrices(1, 100, COUNT);
        Assertions.assertThat(prices.size()).as("generated prices count").isEqualTo(COUNT);
    }
    @Test
    public void canGenerateRandomPricesContainDecimalPoints() {
        List<BigDecimal> prices = Helpers.generateRandomPrices(1, 100, 10);
        Assertions.assertThat(prices).allMatch(p -> p.toString().contains("."));
    }
    @Test
    public void canGenerateRandomPricesBeWithinRange() {
        final int MIN = 10;
        final int UPTO = 100;
        List<BigDecimal> prices = Helpers.generateRandomPrices(MIN, UPTO, 10);
        Assertions.assertThat(prices).as("prices greater than MIN").allMatch(p -> p.compareTo(new BigDecimal(MIN)) > 0);
        Assertions.assertThat(prices).as("prices less than %d", UPTO).allMatch(p -> p.compareTo(new BigDecimal(UPTO)) <= 0);
    }

    @Test
    public void canGenerateRunnersAsManyAsExpected(){
        final int COUNT = 5;
        List runners = Helpers.generateRunners("Player_", COUNT);
        Assertions.assertThat(runners.size()).as("generated runners count").isEqualTo(COUNT);
    }
    @Test
    public void canGenerateRunnersWithCorrectNames(){
        final String INITIAL = "Player_";
        final int COUNT = 3;
        List<String> runners = Helpers.generateRunners(INITIAL, COUNT);
        Assertions.assertThat(runners).as("generated runners names").allMatch((n -> n.startsWith(INITIAL)));
        for (int i=1; i<=COUNT; i++){
            Assertions.assertThat(runners.get(i-1)).as("runner name").isEqualTo(INITIAL + i);
        }

    }
}


package com.tabcorp.qa.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Helpers {

    public static int randomBetweenInclusive(int min, int max){
        return (new Random()).nextInt(max - min + 1) + min;
    }

    public static List<String> generateRandomPrices(int min, int upTo, int count){
        List<String> prices = new ArrayList<>();
        for (int i=0; i < count; i++){
            int dollars = randomBetweenInclusive(min, upTo - 1);
            int cents = randomBetweenInclusive(0, 99);
            prices.add(String.format("%d.%02d", dollars, cents));
        }
        return prices;
    }

    public static List<String> generateRunners(String initial, int count) {
        List<String> runners = new ArrayList(count);
        for(int i = 0; i < count; i++,  runners.add(initial + i));
        return runners;
    }

}

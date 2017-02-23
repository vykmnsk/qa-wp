package com.tabcorp.qa.common;

import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
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

    public static String getChromeDriverPath() {

        List<String> strings = new LinkedList<>();
        strings.add("src");
        strings.add("test");
        strings.add("resources");
        strings.add(SystemUtils.IS_OS_WINDOWS ? "chromedriver.exe" : "chromedriver");

        return String.join(File.separator, strings);

    }

}

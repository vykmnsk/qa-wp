package com.tabcorp.qa.common;

import org.apache.commons.lang3.SystemUtils;
import org.assertj.core.api.Assertions;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


public class Helpers {

    public static int randomBetweenInclusive(int min, int max){
        return (new Random()).nextInt(max - min + 1) + min;
    }

    public static List<BigDecimal> generateRandomPrices(int min, int upTo, int count){
        List<BigDecimal> prices = new ArrayList<>();
        for (int i=0; i < count; i++){
            int dollars = randomBetweenInclusive(min, upTo - 1);
            int cents = randomBetweenInclusive(0, 99);
            String priceText = String.format("%d.%02d", dollars, cents);
            BigDecimal price = new BigDecimal(priceText);
            prices.add(price);
        }
        return prices;
    }

    public static List<String> generateRunners(String initial, int count) {
        List<String> runners = new ArrayList(count);
        for(int i = 0; i < count; i++,  runners.add(initial + i));
        return runners;
    }

    public static String toTitleCase(String name) {
        return (name.substring(0,1).toUpperCase()) + (name.substring(1));
    }

    public static String getChromeDriverPath() {

        List<String> strings = new LinkedList<>();
        strings.add("src");
        strings.add("test");
        strings.add("resources");
        strings.add(SystemUtils.IS_OS_WINDOWS ? "chromedriver.exe" : "chromedriver");

        return String.join(File.separator, strings);

    }

    public static Object noNullGet(Map map, Object key){
        Assertions.assertThat(map.get(key))
                .withFailMessage("Map key='%s' does not exist in: %s", key, map.keySet())
                .isNotNull();
        return map.get(key);
    }

    public static String createUniqueName(String baseName){
        return String.format("%s %d", baseName, randomBetweenInclusive(1000, 9999));
    }

    public static List<BigDecimal> extractCSVPrices(String winnerPriceCSV) {
        List<String> pricesTxts = Arrays.asList(winnerPriceCSV.split(",(\\s)*"));
        return pricesTxts.stream().map(BigDecimal::new).collect(Collectors.toList());
    }
}

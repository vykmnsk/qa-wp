package com.tabcorp.qa.common;

import org.apache.commons.lang3.SystemUtils;
import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


public class Helpers {
    private static Logger log = LoggerFactory.getLogger(Helpers.class);

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

    public static String norm(String input) {
        String output = input.replaceAll("(\\s|-)+", "_");
        return output.toUpperCase();
    }

    public static String getChromeDriverPath() {

        List<String> strings = new LinkedList<>();
        strings.add("src");
        strings.add("test");
        strings.add("resources");
        strings.add(SystemUtils.IS_OS_WINDOWS ? "chromedriver.exe" : "chromedriver");

        return String.join(File.separator, strings);

    }

    public static Object nonNullGet(Map map, Object key){
        Assertions.assertThat(map.get(key))
                .withFailMessage("Map key='%s' does not exist in: %s", key, map.keySet())
                .isNotNull();
        return map.get(key);
    }

    public static String createUniqueName(String baseName){
        return String.format("%s %d", baseName, randomBetweenInclusive(1000, 9999));
    }

    public static List<String> extractCSV(String csv) {
        return Arrays.asList(csv.split(",(\\s)*"));
    }

    public static List<BigDecimal> extractCSVPrices(String pricesCSV) {
        List<String> pricesTxts = extractCSV(pricesCSV);
        return pricesTxts.stream().map(BigDecimal::new).collect(Collectors.toList());
    }

    public static BigDecimal roundOff(BigDecimal value) {
        int decimalPlaces = 2;
        return value.setScale(decimalPlaces,BigDecimal.ROUND_HALF_UP);
    }

    public static void retryOnAssertionFailure(Runnable block, int maxTries, int sleepSeconds) {
        for (int i = 1; i <= maxTries; i++) {
            try {
                Thread.sleep(sleepSeconds * 1000);
                block.run();
                return;
            } catch (AssertionError ae) {
                log.info(String.format("Re-trying %d. Exception: %s", i, ae.getMessage()));
            } catch (InterruptedException e) {
                //ignore
            }
        }
        throw new RuntimeException(String.format("Exhausted %d attempts for previous Exceptions", maxTries));
    }

}

package com.tabcorp.qa.common;

import com.tabcorp.qa.adyen.ClientSideEncrypter;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.yaml.snakeyaml.Yaml;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class Helpers {

    private static final String FREE_MARKER_VERSION = "2.3.23";
    private static final Logger log = LoggerFactory.getLogger(Helpers.class);

    public static String getRequestPayLoad(Map<String, Object> templateData, String templateFileName) {
        String requestPayload;
        // Directory name of where all the templates reside.
        String directoryName = "casino";
        // May produce NPE when directory doesn't exist.
        File file = new File(Helpers.class.getClassLoader().getResource(directoryName).getPath());
        Configuration cfg = new Configuration(new Version(FREE_MARKER_VERSION));
        cfg.setDefaultEncoding("UTF-8");
        try {
            // throws IOException
            cfg.setDirectoryForTemplateLoading(file);
            Template template = cfg.getTemplate(templateFileName);
            try (StringWriter out = new StringWriter()) {
                template.process(templateData, out);
                requestPayload = out.getBuffer().toString();
                out.flush();
            }
        } catch (Exception e) {
            throw new FrameworkError(String.format("Something went wrong in Helpers::getRequestPayload method : %s", e));
        }
        return requestPayload;
    }

    public static int randomBetweenInclusive(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }

    public static List<BigDecimal> generateRandomPrices(int min, int upTo, int count) {
        List<BigDecimal> prices = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int dollars = randomBetweenInclusive(min, upTo - 1);
            int cents = randomBetweenInclusive(0, 99);
            String priceText = String.format("%d.%02d", dollars, cents);
            BigDecimal price = new BigDecimal(priceText);
            prices.add(price);
        }
        return prices;
    }

    public static List<String> generateRunners(String initial, int count) {
        return IntStream.range(1, count + 1).mapToObj(i -> initial + i).collect(Collectors.toList());
    }

    public static String toTitleCase(String name) {
        return name.substring(0, 1).toUpperCase() + (name.substring(1));
    }

    public static String normalize(String input) {
        return input.trim().replaceAll("\\W+", "_");
    }

    public static String getChromeDriverPath() {

        List<String> strings = new LinkedList<>();
        strings.add("src");
        strings.add("test");
        strings.add("resources");
        strings.add(SystemUtils.IS_OS_WINDOWS ? "chromedriver.exe" : "chromedriver");

        return String.join(File.separator, strings);

    }

    public static Object nonNullGet(Map map, Object key) {
        assertThat(map.get(key))
                .withFailMessage("Map key='%s' does not exist in: %s", key, map.keySet())
                .isNotNull();
        return map.get(key);
    }

    public static void verifyNotExpired(int expMonth, int expYear) {
        assertThat(expMonth).as("Expiry month").isBetween(1, 12);
        assertThat(expYear).as("Expiry Year").isBetween(1950, 3000);
        LocalDate nextMonthStart = LocalDate.of(expYear, expMonth + 1, 1);
        LocalDate lastValidDate = nextMonthStart.minusDays(1);
        LocalDate today = LocalDate.now();
        assertThat(lastValidDate).as("CC month=%d year=%d is expired", expMonth, expYear).isAfterOrEqualTo(today);
    }

    public static String timestamp(String pattern) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String createUniqueName(String baseName) {
        int uniqPart = randomBetweenInclusive(0, 999);
        return String.format("%s%s~%d", baseName, timestamp("yy:MM:dd:HH:mm:ss"), uniqPart);
    }

    public static String createUniqueNameCompact(String baseName) {
        int uniqPart = randomBetweenInclusive(0, 999);
        return String.format("%s%s%d", baseName, timestamp("yyMMddHHmmss"), uniqPart);
    }

    public static List<String> extractCSV(String csv) {
        char separator = ',';
        return extractCSV(csv, separator);
    }

    public static List<String> extractCSV(String csv, char separator) {
        assertThat(csv).as("CSV value").isNotBlank();
        String regex = String.format("(\\s)*%s(\\s)*", separator);
        return Arrays.asList(csv.split(regex));
    }

    public static List<BigDecimal> extractCSVPrices(String pricesCSV) {
        List<String> pricesTxts = extractCSV(pricesCSV);
        return pricesTxts.stream().map(BigDecimal::new).collect(Collectors.toList());
    }

    public static BigDecimal roundOff(BigDecimal value) {
        return roundOff(value, 2);
    }

    public static BigDecimal roundOff(BigDecimal value, int decimalPlaces) {
        return value.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
    }

    public static void retryOnFailure(Runnable block, int maxTries, int sleepSeconds) {
        for (int i = 1; i <= maxTries; i++) {
            try {
                if (i > 1) Thread.sleep((long) sleepSeconds * 1000);
                block.run();
                return;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (AssertionError | WebDriverException e) {
                log.info(String.format("Tried %d. Exception: %s", i, e.getMessage()));
            }
        }
        throw new FrameworkError(String.format("Exhausted %d attempts for previous Exceptions", maxTries));
    }

    public static List<String> collectElementsTexts(WebElement parent, By childrenSelector) {
        return parent.findElements(childrenSelector)
                .stream().map(e -> e.getText().trim())
                .collect(Collectors.toList());
    }

    public static Map<String, String> readTableRow(WebElement table) {
        List<String> headers = collectElementsTexts(table, By.cssSelector("tr th"));
        List<String> data = collectElementsTexts(table, By.cssSelector("tr td"));
        return (Map<String, String>) zipToMap(headers, data);
    }

    public static Map zipToMap(List keys, List values) {
        return IntStream.range(0, Math.min(keys.size(), values.size()))
                .mapToObj(i -> Pair.of(keys.get(i), values.get(i)))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    public static List<Pair> zipToPairs(List keys, List values) {
        return IntStream.range(0, Math.min(keys.size(), values.size()))
                .mapToObj(i -> Pair.of(keys.get(i), values.get(i)))
                .collect(Collectors.toList());
    }

    public static Map loadYamlResource(String filename) {
        InputStream input = Helpers.class.getClassLoader().getResourceAsStream(filename);
        return (Map) (new Yaml()).load(input);
    }

    public static String getTargetDirPath() {
        return System.getProperty("user.dir") + File.separator + "target" + File.separator;
    }

    public static void saveScreenshot(WebDriver driver, String baseName) {
        String shotFilename = String.format("screen%s--%s.png",
                timestamp("HHmmssS"),
                normalize(StringUtils.left(baseName, 50)));
        String savePath = Helpers.getTargetDirPath() + shotFilename;
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(srcFile, new File(savePath));
        } catch (IOException ioe) {
            log.error(ioe.toString());
        }
    }

    public static String readResourceFile(String filename) {
        String content;
        try {
            File file = new File(Helpers.class.getClassLoader().getResource(filename).getFile());
            content = new String(Files.readAllBytes(file.toPath()));
        } catch (Exception e) {
            throw new FrameworkError(String.format("Could not read java resource file='%s' exception=%s", filename, e));
        }
        return content;
    }

    public static void delayInMillis(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static JSONObject readJSON(String templateFile) {
        JSONParser parser = new JSONParser();
        JSONObject json;
        try {
            json = (JSONObject) parser.parse(readResourceFile(templateFile));
        } catch (ParseException pe) {
            throw new FrameworkError(pe);
        }
        return json;
    }

    public static Object extractByXpath(String response, String Xpath) {
        InputSource source = new InputSource(new StringReader(response));
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();
        try {
            XPathExpression expr = xpath.compile(Xpath);
            return expr.evaluate(source);
        } catch (Exception e) {
            throw new FrameworkError(String.format("Something went wrong in Helpers::extractByXpath method : %s", e));
        }
    }

    // helper method to convert a list from one data type to another
    public static <T, U> List<U> convertList(List<T> from, Function<T, U> function) {
        return from.stream().map(function).collect(Collectors.toList());
    }

}


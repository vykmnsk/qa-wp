package com.tabcorp.qa.wagerplayer.pages;


import com.tabcorp.qa.common.Helpers;
import com.tabcorp.qa.common.Storage;
import org.apache.commons.collections4.ListUtils;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class MarketsPage extends AppPage {

    @FindBy(css = "#main_table tbody tr th")
    private WebElement header;
    @FindBy(css = "input[src='images/button_insert.gif'")
    private WebElement insertBtn;
    @FindBy(css = "input[src='images/button_update.gif'")
    private WebElement updateBtn;

    @FindBy(css = "#main_table tr[bgcolor='green'")
    private WebElement status;

    @FindBy(css = "td.market_status_cell a[id^='market_status']")
    private WebElement marketStatus;

    @FindBy(css = ("#market_details"))
    private WebElement marketDetailsSection;
    @FindBy(css = "table.inner_table a[href*='market_details']")
    private WebElement showHideMarketDetails;

    @FindBy(css = ("#market_settings"))
    private WebElement marketManagementSection;
    @FindBy(css = "table.inner_table a[href*='market_settings']")
    private WebElement showHideMarketManagement;

    @FindBy(css = "input[type='radio'][name='MARKET_TYPE_SET_STATUS'][value='3'")
    private WebElement marketStatusLive;
    @FindBy(css = "input[type='radio'][name='MARKET_TYPE_SET_STATUS'][value='5'")
    private WebElement marketStatusHeld;

    @FindBy(css = ("select[name='MARKET_TYPE_SET_ALLOW_WIN']"))
    private WebElement betsAllowedWinSel;

    @FindBy(css = ("select[name='MARKET_TYPE_SET_ALLOW_PLACE']"))
    private WebElement betsAllowedPlaceSel;

    @FindBy(css = ("select[id='MARKET_TYPE_SET_EACHWAY_PERCENT']"))
    private WebElement placeFractionSel;

    @FindBy(css = ("select[id='MARKET_TYPE_SET_EACHWAY_SELECTIONS']"))
    private WebElement numOfPlacesSel;

    @FindBy(css = ("input[name='MARKET_TYPE_SET_ALLOW_EACHWAY']"))
    private WebElement ewChk;

    @FindBy(css = ("input#eachway_lock"))
    private List<WebElement> ewLockedChks;

    @FindBy(css = "#market_settings input[name='race_num']")
    @CacheLookup
    private WebElement raceNumTxt;

    @FindBy(css = "input[name^='position[']")
    private List<WebElement> positionTxts;

    @FindBy(css = "input[type=text][id^=price_][onkeyup^=calculate_selection_percent")
    private List<WebElement> priceTxts;

    @FindBy(css = ("input[type=text][name^='hard_limit[']"))
    private List<WebElement> hardLimitTxts;

    @FindBy(css = ("input[type=text][name^='soft_limit[']"))
    private List<WebElement> softLimitTxts;

    @FindBy(css = ("input[type=text][name^='interim_limit[']"))
    private List<WebElement> interimLimitTxts;

    @FindBy(css = "#bet_types_content_body table.product_options tbody tr")
    private List<WebElement> productRows;

    @FindBy(css = ("div[id=cross_race_exotics_content_body] table[class$='product_options'] tr"))
    private List<WebElement> crossRaceExoticsProductRows;
    private By crossRaceExoticsCheckbox = By.cssSelector("input[type=checkbox][name^='enable_product[']");

    @FindBy(css = "input[type=hidden][name=event_id]")
    private WebElement eventId;

    @FindBy(css = "select[name='add_product_id[]']")
    private List<WebElement> addProductElems;

    @FindBy(css = "input[type=text][name^='position[']")
    public List<WebElement> runnerPositions;

    By runnerScratchCheckboxSelector = By.cssSelector("input[type='checkbox'][name^='market_action[']");

    @FindBy(css = ("input[id=f4_manage_button]"))
    public WebElement statusButton;

    @FindBy(css = ("input[name=refund][src='images/button_scratch.gif']"))
    public WebElement scratchButton;

    @FindBy(css =("input[name=betfair_place_back_limit]"))
    public WebElement placeBackLimit;

    private By addProductBtnCSS = By.cssSelector("input[type=image][name=add_prod]");

    private static Logger log = LoggerFactory.getLogger(MarketsPage.class);

    static Map<List<String>, String> productSettingIDs() {
        return Collections.unmodifiableMap(Stream.of(
                new SimpleEntry<>(Arrays.asList("Betting", "Enabled", "On"), "[2][enabled]"),
                new SimpleEntry<>(Arrays.asList("Betting", "Enabled", "Auto"), "[1][auto]"),
                new SimpleEntry<>(Arrays.asList("Betting", "Display Price", "Win"), "[2][win_display_price]"),
                new SimpleEntry<>(Arrays.asList("Betting", "Display Price", "Place"), "[2][place_display_price]"),
                new SimpleEntry<>(Arrays.asList("Betting", "Display Price", "Fluc"), "[2][display_fluctuations]"),
                new SimpleEntry<>(Arrays.asList("Betting", "Enable Single", "Win"), "[2][win_enabled]"),
                new SimpleEntry<>(Arrays.asList("Betting", "Enable Single", "Place"), "[2][place_enabled]"),
                new SimpleEntry<>(Arrays.asList("Betting", "Enable Single", "EW"), "[2][eachway]"),
                new SimpleEntry<>(Arrays.asList("Betting", "Display Column", "Win"), "[2][display_column_win]"),
                new SimpleEntry<>(Arrays.asList("Betting", "Display Column", "Place"), "[2][display_column_place]"),
                new SimpleEntry<>(Arrays.asList("Betting", "Display Column", "EW"), "[2][display_column_eachway]"),
                new SimpleEntry<>(Arrays.asList("Betting", "Enable Multi", "Win"), "[2][multi_win]"),
                new SimpleEntry<>(Arrays.asList("Betting", "Enable Multi", "Place"), "[2][multi_place]"),
                new SimpleEntry<>(Arrays.asList("Betting", "Enable Multi", "EW"), "[2][multi_eachway]"),
                new SimpleEntry<>(Arrays.asList("Betting", "Disp. Result", "Win"), "[2][display_result_win]"),
                new SimpleEntry<>(Arrays.asList("Betting", "Disp. Result", "Place"), "[2][display_result_place]"),

                new SimpleEntry<>(Arrays.asList("Liability", "Display Price", "Win"), "[1][win_display_price]"),
                new SimpleEntry<>(Arrays.asList("Liability", "Display Price", "Place"), "[1][place_display_price]"),
                new SimpleEntry<>(Arrays.asList("Liability", "Single", "Win"), "[1][win_enabled]"),
                new SimpleEntry<>(Arrays.asList("Liability", "Single", "Place"), "[1][place_enabled]"),
                new SimpleEntry<>(Arrays.asList("Liability", "Base", "Win"), "[1][win_base_display_price]"),
                new SimpleEntry<>(Arrays.asList("Liability", "Final Leg Multi", "Win"), "[1][final_leg_win]"),
                new SimpleEntry<>(Arrays.asList("Liability", "Final Leg Multi", "Place"), "[1][final_leg_place]"),
                new SimpleEntry<>(Arrays.asList("Liability", "Multi", "Win"), "[1][multi_win]"),
                new SimpleEntry<>(Arrays.asList("Liability", "Multi", "Place"), "[1][multi_place]"),
                new SimpleEntry<>(Arrays.asList("Liability", "Betback", "Win"), "[1][betback_win]"),
                new SimpleEntry<>(Arrays.asList("Liability", "Betback", "Place"), "[1][betback_place]"),
                new SimpleEntry<>(Arrays.asList("Liability", "Cash Out", "Win"), "[1][liab_single_win_cashout]"),
                new SimpleEntry<>(Arrays.asList("Liability", "Cash Out", "Place"), "[1][liab_single_place_cashout]"),
                new SimpleEntry<>(Arrays.asList("Liability", "Cash Out", "Multi Win"), "[1][liab_multi_win_cashout]"),
                new SimpleEntry<>(Arrays.asList("Liability", "Cash Out", "Multi Place"), "[1][liab_multi_place_cashout]"),
                new SimpleEntry<>(Arrays.asList("Defaults", "Display", "Web"), "[2][is_default]"),
                new SimpleEntry<>(Arrays.asList("Defaults", "Display", "F2"), "[1][is_default]")
        ).collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue)));
    }

    public void load() {
        driver.switchTo().defaultContent();
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("frame_bottom"));
    }

    private List<List<String>> defaultProductSettings() {
        List<List<String>> settings = new ArrayList<>();
        settings.add(Arrays.asList("Betting", "Enabled", "On"));
        settings.add(Arrays.asList("Betting", "Enabled", "Auto"));
        return settings;
    }

    public void verifyLoaded() {
        wait.until(ExpectedConditions.visibilityOf(header));
        assertThat(header.getText()).as("Markets Page header").isEqualTo("Markets");
    }

    public void enterPrices(List<BigDecimal> prices) {
        List<Integer> sizes = Arrays.asList(positionTxts.size(), priceTxts.size(), prices.size());
        Integer size0 = sizes.get(0);
        assertThat(sizes).as("position elements, price elements and prices counts are the same").allMatch(size0::equals);
        for (int i = 0; i < positionTxts.size(); i++) {
            WebElement pos = positionTxts.get(i);
            WebElement priceTxt = priceTxts.get(i);
            BigDecimal priceVal = prices.get(i);
            pos.sendKeys(String.valueOf(i + 1));
            priceTxt.clear();
            priceTxt.sendKeys(priceVal.toString());
        }
        insertBtn.click();
    }

    public void updateRaceNumber(int num) {
        raceNumTxt.clear();
        raceNumTxt.sendKeys("" + num);
        updateBtn.click();
    }

    public void setHardSoftInterimLimits() {
        for (int i = 0; i < positionTxts.size(); i++) {
            WebElement hardLimitTxt = hardLimitTxts.get(i);
            WebElement softLimitTxt = softLimitTxts.get(i);
            WebElement interimLimitTxt = interimLimitTxts.get(i);
            hardLimitTxt.clear();
            hardLimitTxt.sendKeys("999999");
            softLimitTxt.clear();
            softLimitTxt.sendKeys("998999");
            interimLimitTxt.clear();
            interimLimitTxt.sendKeys("997999");
        }
        updateBtn.click();
    }

    public void showMarketManagement() {
        if (!marketManagementSection.isDisplayed()) {
            showHideMarketManagement.click();
        }
        wait.until(ExpectedConditions.visibilityOf(marketManagementSection));
    }

    public void enableProductSettings(String prodName, List<List<String>> settings) {
        showMarketManagement();
        WebElement tr = enableFindProductRow(prodName);
        assertThat(tr).as("Product %s is not found on Market Page", prodName).isNotNull();
        Storage.add(Storage.KEY.PRODUCT_IDS, findProductID(tr));
        log.info("storing Product ID=" + Storage.getLast(Storage.KEY.PRODUCT_IDS));
        ListUtils.union(defaultProductSettings(), settings).forEach(s -> setOption(tr, s));
        updateBtn.click();
    }

    private WebElement enableFindProductRow(String prodName) {
        Function<String, WebElement> findProdRow = name -> productRows.stream().filter(p -> p.getText().contains(name)).findFirst().orElse(null);
        WebElement tr = findProdRow.apply(prodName);
        if (null == tr) {
            boolean added = addProduct(prodName);
            assertThat(added).as("Added product " + prodName).isTrue();
            showMarketManagement();
            tr = findProdRow.apply(prodName);
        }
        return tr;
    }

    private boolean addProduct(String prodName) {
        for (WebElement elem : addProductElems) {
            Select select = new Select(elem);
            WebElement prodOption = select.getOptions().stream().filter(o -> prodName.equals(o.getText())).findFirst().orElse(null);
            if (null != prodOption) {
                select.selectByValue(prodOption.getAttribute("value"));
                WebElement plusBtn = findParent(elem).findElement(addProductBtnCSS);
                plusBtn.click();
                return true;
            }
        }
        return false;
    }

    public void enableCrossRaceProduct(String prodName) {
        showMarketManagement();
        log.info("Trying to enable Cross Race Product =" + prodName);
        WebElement prodRow = crossRaceExoticsProductRows.stream().filter(tr -> tr.getText().contains(prodName)).findFirst().orElse(null);
        assertThat(prodRow).as("Product %s is not found in Cross Race section on Market Page", prodName).isNotNull();
        WebElement chk = prodRow.findElement(crossRaceExoticsCheckbox);
        assertThat(chk.getAttribute("disabled")).as("Cross Race Product checkbox is disabled").isNull();
        String checked = chk.getAttribute("checked");
        if (null == checked) chk.click();
        assertThat(checked).as("Checked Cross Race Product " + prodName).isEqualTo("checked");
        updateBtn.click();
    }

    private Integer findProductID(WebElement tr) {
        String firstCheckboxName = tr.findElement(By.cssSelector("input[type=hidden]")).getAttribute("name");
        Pattern p = Pattern.compile("products\\[(\\d+)\\].*");
        Matcher m = p.matcher(firstCheckboxName);
        Assertions.assertThat(m.find()).as("Found product ID in UI").isTrue();
        String prodIdText = m.group(1);
        return Integer.parseInt(prodIdText);
    }

    private void setOption(WebElement prodRow, List<String> option) {
        String inputCSS = String.format("input[name$='%s']", Helpers.nonNullGet(productSettingIDs(), option));
        WebElement hiddenChk = prodRow.findElement(By.cssSelector(inputCSS));
        WebElement cell = findParent(hiddenChk);
        String imageFile = cell.findElement(By.tagName("img")).getAttribute("src");
        if (imageFile.endsWith("unselected.png")) {
            scrollTo(cell);
            cell.click();
        }
    }

    public void hideMarketManagement() {
        boolean expanded = marketManagementSection.isDisplayed();
        if (expanded) {
            showHideMarketManagement.click();
        }
    }

    public void showMarketDetails() {
        boolean expanded = marketDetailsSection.isDisplayed();
        if (!expanded) {
            showHideMarketDetails.click();
        }
        wait.until(ExpectedConditions.visibilityOf(marketDetailsSection));
    }

    public void enterMarketDetail(boolean isLive, String betsAllowedWin, String betsAllowedPlace, String placeFraction, String numOfPlaces, boolean isEW) {
        if (isLive) {
            marketStatusLive.click();
        } else {
            marketStatusHeld.click();
        }
        new Select(betsAllowedWinSel).selectByVisibleText(betsAllowedWin);
        new Select(betsAllowedPlaceSel).selectByVisibleText(betsAllowedPlace);
        wait.until(ExpectedConditions.visibilityOf(placeBackLimit));
        scrollTo(placeBackLimit);
        if (ewLockedChks.size() > 0 ) {
            WebElement box = ewLockedChks.get(0);
            if (box.isSelected()) {
                box.click();
            }
        }
        new Select(placeFractionSel).selectByVisibleText(placeFraction);
        wait.until(ExpectedConditions.elementToBeClickable(numOfPlacesSel));
        new Select(numOfPlacesSel).selectByVisibleText(numOfPlaces);
        if (!ewChk.isSelected() && isEW) {
            ewChk.click();
        }
        updateBtn.click();
    }

    public void updateMarketDetail(String newPlaceFraction, String newNumOfPlaces) {
        new Select(placeFractionSel).selectByVisibleText(newPlaceFraction);
        wait.until(ExpectedConditions.elementToBeClickable(numOfPlacesSel));
        new Select(numOfPlacesSel).selectByVisibleText(newNumOfPlaces);
        updateBtn.click();
    }

    public void verifySuccessStatus(String msg) {
        wait.until(ExpectedConditions.visibilityOf(status));
        String statusMsg = status.getText();
        Assertions.assertThat(statusMsg).as("market status").contains("SUCCESS");
        Assertions.assertThat(statusMsg).as("market status message").containsIgnoringCase(msg);
    }

    public void verifyMarketStatus(String expectedStatus) {
        String actualStatus = marketStatus.getAttribute("status");
        AssertionsForClassTypes.assertThat(actualStatus)
                .withFailMessage(String.format("Actual status %s doesn't match with expected status %s", actualStatus, expectedStatus))
                .isEqualTo(expectedStatus);
    }

    public String readEventID() {
        return eventId.getAttribute("value");
    }

    public void scratchRunners(List<String> positions) {
        for (String pos : positions) {
            WebElement scratchPosTextbox = runnerPositions.stream()
                    .filter(chk -> chk.getAttribute("value").equals(pos)).findFirst()
                    .orElse(null);
            Assertions.assertThat(scratchPosTextbox).withFailMessage("Did not find a text box with scratch position=" + pos).isNotNull();
            WebElement containerRow = findParent(findParent(scratchPosTextbox));
            Assertions.assertThat(containerRow.getTagName()).as("Pos container row").isEqualTo("tr");
            WebElement scratchCheckbox = containerRow.findElement(runnerScratchCheckboxSelector);
            scratchCheckbox.click();
            Assertions.assertThat(scratchCheckbox.isSelected()).as("Scratch checkbox is ticked").isTrue();
        }
        wait.until(ExpectedConditions.visibilityOf(statusButton));
        statusButton.click();
        wait.until(ExpectedConditions.visibilityOf(scratchButton));
        scratchButton.click();
        acceptAlert();
    }
}

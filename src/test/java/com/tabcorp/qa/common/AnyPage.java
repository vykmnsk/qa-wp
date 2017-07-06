package com.tabcorp.qa.common;

import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Quotes;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AnyPage {
    public final WebDriver driver;
    public final WebDriverWait wait;
    package static final Logger log = LoggerFactory.getLogger(AnyPage.class);

    public AnyPage() {
        driver = DriverWrapper.getInstance().getDriver();
        wait = DriverWrapper.getInstance().getDriverWait();
    }

    public void doubleClick(WebElement element) {
        Actions actions = new Actions(driver);
        actions.doubleClick(element).perform();
    }

    public List<WebElement> findAll(List<By> locators) {
        List<WebElement> elems = new ArrayList<>();

        for (By loc : locators) {
            elems.addAll(driver.findElements(loc));
        }
        return elems;
    }

    public WebElement findOne(List<By> locators) {
        List<WebElement> elems = findAll(locators);
        Assertions.assertThat(elems.size())
                .withFailMessage("Expected to find one of %s", locators)
                .isEqualTo(1);
        return elems.get(0);
    }

    public WebElement findParent(WebElement elem) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return (WebElement) js.executeScript("return arguments[0].parentNode;", elem);
    }

    public void scrollTo(WebElement elem) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elem);
    }

    public void selectByPartialVisibleText(WebElement dropdown, String text) {
        String xpath = ".//option[contains(., " + Quotes.escape(text) + ")]";
        WebElement option = dropdown.findElement(By.xpath(xpath));
        new Select(dropdown).selectByVisibleText(option.getText());
    }

    public void setScreenSizeToMax() {
        java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();
        driver.manage().window().setSize(new org.openqa.selenium.Dimension(width, height));
    }

    public void acceptAlert() {
        driver.switchTo().alert().accept();
    }

    public void switchToNewWindow(List<String> oldWindows) {
        String newWindow = driver.getWindowHandles().stream()
                .filter(w -> !oldWindows.contains(w))
                .findFirst().orElse(null);
        assertThat(newWindow).isNotNull();
        driver.switchTo().window(newWindow);
        driver.switchTo().defaultContent();
    }

    public void ensureChecked(WebElement box, boolean shouldBeChecked) {
        wait.until(ExpectedConditions.visibilityOf(box));
        if (box.isSelected() != shouldBeChecked) {
            box.click();
        }
        assertThat(box.isSelected())
                .as(String.format("Element %s %s is now checked",
                        box.getTagName(),
                        StringUtils.defaultIfEmpty(box.getAttribute("name"), "")))
                .isEqualTo(shouldBeChecked);
    }
}

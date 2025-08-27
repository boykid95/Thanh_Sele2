package vn.agest.selenide.common;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import lombok.extern.log4j.Log4j;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;

@Log4j
public class ElementHelper {

    public void waitForElementVisible(SelenideElement element, String elementName) {
        element.shouldBe(Condition.visible);
    }

    public void waitForElementClickable(SelenideElement element, String elementName) {
        element.shouldBe(Condition.visible);
        element.shouldBe(Condition.enabled);
    }

    public void clickToElement(SelenideElement element, String elementName) {
        try {
            waitForElementClickable(element, elementName);
            element.scrollIntoCenter();
            highlightElement(element);
            element.click();
        } catch (Exception e) {
            try {
                jsClickToElement(element, elementName);
            } catch (Exception jsException) {
                log.error("JavaScript click also failed for element '" + elementName + "'.", jsException);
                throw jsException;
            }
        }
    }

    public void moveToElement(SelenideElement element, String elementName) {
        try {
            waitForElementVisible(element, elementName);
            Actions actions = new Actions(WebDriverRunner.getWebDriver());
            actions.moveToElement(element).perform();
        } catch (Exception e) {
            log.error("Failed to move to element '" + elementName + "'. " + e.getMessage());
        }
    }

    public void selectDropdownByVisibleText(SelenideElement dropdown, String optionText) {
        try {
            dropdown.shouldBe(Condition.visible, Duration.ofSeconds(10));
            highlightElement(dropdown);
            Select select = new Select(dropdown);
            select.selectByVisibleText(optionText);
            log.info("✅ Selected option '" + optionText + "' from dropdown.");
        } catch (Exception e) {
            log.error("❌ Failed to select option '" + optionText + "' from dropdown.", e);
            throw e;
        }
    }

    // Highlight element (Add a red border) (will be removed)
    public void highlightElement(SelenideElement element) {
        JavascriptExecutor js = (JavascriptExecutor) WebDriverRunner.getWebDriver();
        js.executeScript("arguments[0].style.border='3px solid red'", element);
        Selenide.sleep(500);
        js.executeScript("arguments[0].style.border=''", element);
    }

    public void jsClickToElement(SelenideElement element, String elementName) {
        try {
            log.info("Attempting to click element '" + elementName + "' using JavaScript.");
            JavascriptExecutor executor = (JavascriptExecutor) WebDriverRunner.getWebDriver();
            executor.executeScript("arguments[0].click();", element);
        } catch (Exception e) {
            throw new RuntimeException("JavaScript click failed for element: " + elementName, e);
        }
    }
}

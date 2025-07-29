package vn.agest.selenide.common.utilities.helpers;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.interactions.Actions;
import vn.agest.selenide.common.utilities.other.Log;

public class ElementHelper {

    // Wait for page to load completely
    public void waitToLoadPage() {
        try {
            Selenide.Wait().until(driver ->
                    ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete")
            );
        } catch (TimeoutException e) {
            Log.error("Page load timeout");
            throw new RuntimeException("Page load timeout", e);
        }
    }

    // Wait for element to be visible
    public void waitForElementVisible(SelenideElement element, String elementName) {
        Log.info(String.format("Waiting for element '%s' to be visible...", elementName));
        element.shouldBe(Condition.visible);
    }

    // Wait for element to be clickable
    public void waitForElementClickable(SelenideElement element, String elementName) {
        Log.info(String.format("Waiting for element '%s' to be clickable...", elementName));
        element.shouldBe(Condition.visible);
        element.shouldBe(Condition.enabled);
    }

    // Click to element
    public void clickToElement(SelenideElement element, String elementName) {
        try {
            waitForElementClickable(element, elementName);
            Log.info(String.format("Clicking element '%s'...", elementName));
            element.scrollIntoCenter();
            highlightElement(element);
            element.click();
        } catch (Exception e) {
            Log.error("Failed to click element '" + elementName + "'. " + e.getMessage());
        }
    }

    public void moveToElement(SelenideElement element, String elementName) {
        try {
            waitForElementVisible(element, elementName);
            Log.info(String.format("Moving to element '%s'...", elementName));
            Actions actions = new Actions(WebDriverRunner.getWebDriver());
            actions.moveToElement(element).perform();
        } catch (Exception e) {
            Log.error("Failed to move to element '" + elementName + "'. " + e.getMessage());
        }
    }

    // Highlight element (Add a red border)
    public void highlightElement(SelenideElement element) {
        JavascriptExecutor js = (JavascriptExecutor) WebDriverRunner.getWebDriver();
        js.executeScript("arguments[0].style.border='3px solid red'", element);
        Selenide.sleep(500); // Hold the highlight effect for 0.5 seconds
        js.executeScript("arguments[0].style.border=''", element);  // Remove the highlight
    }
}

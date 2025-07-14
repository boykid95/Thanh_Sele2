package common.helpers;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;

public class ElementHelper {

    public static void waitForElementVisible(SelenideElement element, int timeoutSeconds) {
        element.shouldBe(visible, Duration.ofSeconds(timeoutSeconds));
    }

    public static void waitForElementClickable(SelenideElement element, int timeoutSeconds) {
        element.shouldBe(and("clickable", visible, enabled), Duration.ofSeconds(timeoutSeconds));
    }

    public static void waitForText(SelenideElement element, String expectedText, int timeoutSeconds) {
        element.shouldHave(text(expectedText), Duration.ofSeconds(timeoutSeconds));
    }

    public static void waitForDisappear(SelenideElement element, int timeoutSeconds) {
        element.should(disappear, Duration.ofSeconds(timeoutSeconds));
    }
}
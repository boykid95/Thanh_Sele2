package common.helpers;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

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

    public static void closePopupIfPresent() {
        try {
            SelenideElement popupCloseButton = $("button.pum-close:nth-child(3)");
            if (popupCloseButton.isDisplayed() || popupCloseButton.shouldBe(visible, Duration.ofSeconds(5)).isDisplayed()) {
                popupCloseButton.click();
            }
        } catch (Exception ignored) {
        }
    }
}

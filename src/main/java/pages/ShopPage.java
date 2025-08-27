package pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.*;
import static common.helpers.ElementHelper.*;

public class ShopPage {
    private final SelenideElement pageTitle = $("h1.title");
    private final SelenideElement popupCloseBtn = $(".pum-close");

    public void closePopupIfPresent() {
        if (popupCloseBtn.exists() && popupCloseBtn.isDisplayed()) {
            popupCloseBtn.click();
            waitForDisappear(popupCloseBtn, 5);
        }
    }

    public void verifyPageLoaded() {
        closePopupIfPresent();
        waitForElementVisible(pageTitle, 7);
        waitForText(pageTitle, "Shop", 5);
    }
}

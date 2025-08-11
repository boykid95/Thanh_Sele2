package pages;

import common.helpers.ElementHelper;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class HomePage {
    ElementHelper elementHelper = new ElementHelper();

    public void handlePopupIfVisible() {
        ElementHelper.closePopupIfPresent();
    }

    public void clickShopTab() {
        $("a[href='/shop/']").shouldBe(visible).click();
    }
}

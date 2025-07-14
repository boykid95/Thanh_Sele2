package pages;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class HomePage {
    public void clickShopTab() {
        $("a[href='/shop/']").shouldBe(visible).click();
    }
}
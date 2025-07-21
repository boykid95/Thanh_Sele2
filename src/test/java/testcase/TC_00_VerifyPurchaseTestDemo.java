package testcase;

import base.BaseTest;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.ShopPage;
import static com.codeborne.selenide.Selenide.*;

public class TC_00_VerifyPurchaseTestDemo extends BaseTest {

    @Test
    public void verifyShop() {
        open("/");
        HomePage homePage = new HomePage();
        ShopPage shopPage = new ShopPage();

        homePage.handlePopupIfVisible();
        homePage.clickShopTab();
        shopPage.verifyPageLoaded();
    }
}
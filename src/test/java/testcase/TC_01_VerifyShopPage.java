package testcase;

import base.BaseTest;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.ShopPage;
import static com.codeborne.selenide.Selenide.*;

public class TC_01_VerifyShopPage extends BaseTest {

    @Test
    public void verifyShop() {
        open("/");
        HomePage homePage = new HomePage();
        ShopPage shopPage = new ShopPage();

        homePage.clickShopTab();
        shopPage.verifyPageLoaded();
    }
}
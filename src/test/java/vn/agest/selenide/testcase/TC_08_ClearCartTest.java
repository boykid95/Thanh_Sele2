package vn.agest.selenide.testcase;

import io.qameta.allure.Description;
import lombok.extern.log4j.Log4j;
import org.testng.annotations.Test;
import vn.agest.selenide.pageObjects.CartPage;
import vn.agest.selenide.pageObjects.ShopPage;
import vn.agest.selenide.tests.BaseTest;

@Log4j
public class TC_08_ClearCartTest extends BaseTest {

    @Test(description = "Verify users can clear the cart")
    @Description("User adds items to cart, clears the cart, and verifies the cart is empty")
    public void verifyClearCartFunctionality() {

        homePage.open();
        homePage.navigateToLoginPage();
        loginPage.loginWithValidCredentials();

        ShopPage shopPage = homePage.navigateToShopPage();
        shopPage.addRandomProductsToCart(3);
        CartPage cartPage = shopPage.goToCart();

        softAssert.assertTrue(cartPage.hasProductsInCart(),
                "❌ Cart should contain products before clearing");

        cartPage.removeAllProducts();

        softAssert.assertTrue(cartPage.isCartEmpty(),
                "❌ Cart is not empty after clearing");

        log.info("✅ Verified user can clear the cart successfully.");
        softAssert.assertAll();
    }
}

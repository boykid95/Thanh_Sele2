package vn.agest.selenide.testcase;

import io.qameta.allure.Description;
import lombok.extern.log4j.Log4j;
import org.testng.annotations.Test;

import vn.agest.selenide.pageObjects.CartPage;
import vn.agest.selenide.pageObjects.ShopPage;
import vn.agest.selenide.tests.BaseTest;

@Log4j
public class TC_09_VerifyUpdateProductQuantityTest extends BaseTest {

    @Test(description = "Verify user can update product quantity in Cart via +, -, and direct input")
    @Description("Steps: Add product to cart, update quantity using + button, direct input, and - button. Verify quantity and subtotal updated correctly each time.")
    public void verifyUpdateQuantityInCart() {

        homePage.open();
        homePage.navigateToLoginPage();
        loginPage.loginWithValidCredentials();
        ShopPage shopPage = homePage.navigateToShopPage();
        shopPage.addRandomProductsToCart(1);
        CartPage cartPage = shopPage.goToCart();

        double unitPrice = cartPage.getUnitPrice();
        double initialSubtotal = cartPage.getSubtotalPrice();
        softAssert.assertEquals(initialSubtotal, unitPrice * 1, "❌ Subtotal mismatch at qty=1");

        cartPage.clickPlus();
        softAssert.assertEquals(cartPage.getSubtotalPrice(), unitPrice * 2, "❌ Subtotal mismatch at qty=2");

        cartPage.updateQuantity(4);
        softAssert.assertEquals(cartPage.getSubtotalPrice(), unitPrice * 4, "❌ Subtotal mismatch at qty=4");

        cartPage.clickMinus();
        softAssert.assertEquals(cartPage.getSubtotalPrice(), unitPrice * 3, "❌ Subtotal mismatch at qty=3");

        softAssert.assertAll();
    }
}
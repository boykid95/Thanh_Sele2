package vn.agest.selenide.testcase;

import io.qameta.allure.Description;
import org.testng.annotations.Test;
import vn.agest.selenide.common.Constants;
import vn.agest.selenide.model.Product;
import vn.agest.selenide.pageObjects.*;
import vn.agest.selenide.tests.BaseTest;

import java.util.List;

import static vn.agest.selenide.pageObjects.BasePage.mergeProductList;

public class TC_02_VerifyMultipleItemsPurchaseTest extends BaseTest {

    @Test(description = "Verify users can buy multiple items successfully")
    @Description("Full E2E flow: open homepage, login, navigate to shop, select multiple items, add to cart, verify cart, checkout and confirm order")
    public void verifyUserCanBuyMultipleItemsSuccessfully() {
        homePage.open();
        homePage.navigateToLoginPage();
        loginPage.loginWithValidCredentials();

        ShopPage shopPage = homePage.navigateToShopPage();
        List<Product> selectedProducts = shopPage.getRandomProducts(15);
        shopPage.addProductsToCart(selectedProducts);
        List<Product> mergedSelectedProducts = mergeProductList(selectedProducts);

        CartPage cartPage = shopPage.goToCart();
        List<Product> actualCartItems = cartPage.getCartProductInfo();
        softAssert.assertEquals(actualCartItems, mergedSelectedProducts, "Verify all selected products are in cart.");

        CheckoutPage checkoutPage = cartPage.checkOut();
        softAssert.assertTrue(checkoutPage.isLoaded(), "Checkout page did not load correctly");

        OrderStatusPage orderStatusPage = checkoutPage.placeOrder();
        softAssert.assertTrue(orderStatusPage.isLoaded(), "Order status page did not load correctly");
        softAssert.assertEquals(
                orderStatusPage.getOrderConfirmationMessage(),
                Constants.ORDER_CONFIRMATION,
                "❌ Order confirmation message mismatch"
        );

        List<Product> actualOrderItems = orderStatusPage.getOrderProductInfo();
        softAssert.assertEquals(
                actualOrderItems,
                actualCartItems,
                "❌ Ordered products do not match the cart items"
        );

        softAssert.assertAll();
    }
}

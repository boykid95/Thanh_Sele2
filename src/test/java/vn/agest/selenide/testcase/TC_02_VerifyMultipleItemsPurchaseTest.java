package vn.agest.selenide.testcase;

import io.qameta.allure.Description;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import vn.agest.selenide.common.constants.MsgConstants;
import vn.agest.selenide.model.Product;
import vn.agest.selenide.pageObjects.*;
import vn.agest.selenide.pageObjects.components.MiniCartComponent;
import vn.agest.selenide.tests.BaseTest;

import java.util.List;

public class TC_02_VerifyMultipleItemsPurchaseTest extends BaseTest {

    @Test(description = "Verify users can buy multiple items successfully")
    @Description("Full E2E flow: open homepage, login, navigate to shop, select multiple items, add to cart, verify cart, checkout and confirm order")
    public void verifyUserCanBuyMultipleItemsSuccessfully() {
        SoftAssert softAssert = new SoftAssert();

        // Step 1: Open browser and go to homepage
        HomePage homePage = new HomePage();
        homePage.open();

        // Step 2: Login with valid credentials
        LoginPage loginPage = homePage.navigateToLoginPage();
        loginPage.loginWithValidCredentials();

        // Step 3: Navigate to Shop page
        ShopPage shopPage = homePage.navigateToShopPage();

        // Step 4: Select multiple items and add to cart
        List<Product> selectedProducts = shopPage.getRandomProducts(10);
        shopPage.addProductsToCart(selectedProducts);

        // Step 5: Go to the cart and verify all selected items
        CartPage cartPage = shopPage.goToCart();
        List<Product> actualCartItems = cartPage.getCartProductInfo();
        softAssert.assertEquals(actualCartItems, selectedProducts, "Verify all selected products are in cart.");

        // Step 6: Proceed to checkout and confirm order
        CheckoutPage checkoutPage = cartPage.checkOut();
        softAssert.assertTrue(checkoutPage.isLoaded(), "Checkout page did not load correctly");

        // Step 7.1: Verify order confirmation message
        OrderStatusPage orderStatusPage = checkoutPage.placeOrder();
        softAssert.assertTrue(orderStatusPage.isLoaded(), "Order status page did not load correctly");
        softAssert.assertEquals(
                orderStatusPage.getOrderConfirmationMessage(),
                MsgConstants.ORDER_CONFIRMATION,
                "❌ Order confirmation message mismatch"
        );

        // Step 7.2: Verify purchased item details
        List<Product> actualOrderItems = orderStatusPage.getOrderProductInfo();
        softAssert.assertEquals(
                actualOrderItems,
                actualCartItems,
                "❌ Ordered products do not match the cart items"
        );
    }
}

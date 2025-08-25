package vn.agest.selenide.testcase;

import io.qameta.allure.Description;
import lombok.extern.log4j.Log4j;
import org.testng.annotations.Test;
import vn.agest.selenide.model.Order;
import vn.agest.selenide.pageObjects.*;
import vn.agest.selenide.tests.BaseTest;

@Log4j
public class TC_06_GuestCheckoutTest extends BaseTest {

    @Test(description = "TC_06: Verify users try to buy an item without logging in (As a guest)")
    @Description("This test verifies the end-to-end checkout flow for a user who is not logged in.")
    public void verifyGuestCheckoutSuccessfully() {

        ShopPage shopPage = new ShopPage();
        CartPage cartPage = new CartPage();
        CheckoutPage checkoutPage = new CheckoutPage();

        homePage.navigateToShopPage();
        shopPage.addRandomProductsToCart(1);
        shopPage.goToCart();

        cartPage.checkOut();
        OrderStatusPage orderStatusPage = checkoutPage.placeGuestOrder(
                "Kid", "Tester", "123 Test Street", "Da Nang", "55000",
                "0123456789", "guest." + System.currentTimeMillis() + "@example.com"
        );

        Order placedOrder = orderStatusPage.extractOrderInfo();
        String orderId = placedOrder.getOrderId();

        softAssert.assertNotNull(orderId, "❌ Order ID is null, guest checkout failed.");
        softAssert.assertFalse(orderId.isEmpty(), "❌ Order ID is empty, guest checkout failed.");

        softAssert.assertAll();
    }
}

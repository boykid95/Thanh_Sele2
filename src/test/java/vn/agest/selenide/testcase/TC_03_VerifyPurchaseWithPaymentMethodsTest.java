package vn.agest.selenide.testcase;

import io.qameta.allure.Description;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import vn.agest.selenide.common.Constants;
import vn.agest.selenide.model.Product;
import vn.agest.selenide.pageObjects.CartPage;
import vn.agest.selenide.pageObjects.CheckoutPage;
import vn.agest.selenide.pageObjects.OrderStatusPage;
import vn.agest.selenide.pageObjects.ShopPage;
import vn.agest.selenide.tests.BaseTest;

import java.util.List;

public class TC_03_VerifyPurchaseWithPaymentMethodsTest extends BaseTest {
    @DataProvider(name = "paymentMethods")
    public Object[][] paymentMethods() {
        return new Object[][]{
                {"bank"},
                {"cod"}
        };
    }

    @Test(dataProvider = "paymentMethods", description = "Verify users can buy an item with different payment methods")
    @Description("E2E: Login -> Select product -> Add to cart -> Checkout -> Choose payment method -> Confirm order")
    public void verifyPurchaseWithDifferentPaymentMethods(String paymentMethod) {

        homePage.open();
        homePage.navigateToLoginPage();
        loginPage.loginWithValidCredentials();

        ShopPage shopPage = homePage.navigateToShopPage();
        Product product = shopPage.getRandomProducts(1).get(0);
        shopPage.addProductsToCart(List.of(product));

        CartPage cartPage = shopPage.goToCart();
        List<Product> actualCartItems = cartPage.getCartProductInfo();
        softAssert.assertTrue(actualCartItems.contains(product), "Cart does not contain selected product");

        CheckoutPage checkoutPage = cartPage.checkOut();
        checkoutPage.selectPaymentMethod(paymentMethod);

        OrderStatusPage orderStatusPage = checkoutPage.placeOrder();
        softAssert.assertTrue(orderStatusPage.isLoaded(), "Order status page did not load correctly");
        softAssert.assertEquals(
                orderStatusPage.getOrderConfirmationMessage(),
                Constants.ORDER_CONFIRMATION,
                "❌ Order confirmation message mismatch for method: " + paymentMethod
        );

        softAssert.assertAll();
    }
}

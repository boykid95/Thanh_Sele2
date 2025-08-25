package vn.agest.selenide.testcase;

import io.qameta.allure.Description;
import lombok.extern.log4j.Log4j;
import org.testng.annotations.Test;
import vn.agest.selenide.common.Constants;
import vn.agest.selenide.pageObjects.CartPage;
import vn.agest.selenide.pageObjects.CheckoutPage;
import vn.agest.selenide.pageObjects.ShopPage;
import vn.agest.selenide.tests.BaseTest;

@Log4j
public class TC_07_VerifyMandatoryFieldsErrorTest extends BaseTest {

    @Test(description = "Verify system shows error messages when mandatory fields are left blank at Checkout")
    @Description("At Checkout, user leaves all required fields blank and clicks Place Order. " +
            "System should display error messages for each missing mandatory field.")
    public void verifyMandatoryFieldsErrorMessages() {

        homePage.open();
        ShopPage shopPage = homePage.navigateToShopPage();

        shopPage.addProductsToCart(shopPage.getRandomProducts(1));
        CartPage cartPage = shopPage.goToCart();
        CheckoutPage checkoutPage = cartPage.checkOut();
        checkoutPage.clickPlaceOrder();

        for (String expectedError : Constants.ALL_BILLING_ERRORS) {
            boolean isDisplayed = checkoutPage.isErrorMessageDisplayed(expectedError);
            softAssert.assertTrue(isDisplayed, "❌ Missing error message: " + expectedError);
        }

        softAssert.assertAll();
    }
}

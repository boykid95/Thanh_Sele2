package vn.agest.selenide.testcase;

import io.qameta.allure.Description;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import vn.agest.selenide.enums.ProductCategory;
import vn.agest.selenide.pageObjects.*;
import vn.agest.selenide.pageObjects.components.MiniCartComponent;
import vn.agest.selenide.tests.BaseTest;

public class TC_01_VerifyPurchaseTest extends BaseTest {

    @Test(description = "Verify users can buy an item successfully")
    @Description("Full E2E flow: open homepage, login, navigate to All Departments, select category, add to cart, checkout, and verify order")
    public void verifyUserCanBuyItemSuccessfully() {

        homePage.open();
        loginPage.navigateToLoginPage();
        loginPage.loginWithValidCredentials();
        homePage.navigateToAllDepartments();

        ProductCategoryPage productCategoryPage = homePage.navigateToProductCategory(ProductCategory.ELECTRONIC_COMPONENTS_SUPPLIES);
        productCategoryPage.openCategoryPage();
        softAssert.assertTrue(
                productCategoryPage.verifyItemsDisplayedInLayout("grid"),
                "Items are not displayed in grid layout"
        );

        productCategoryPage.switchView("list");
        softAssert.assertTrue(
                productCategoryPage.verifyItemsDisplayedInLayout("list"),
                "Items are not displayed in list layout"
        );

        productCategoryPage.selectRandomProduct();
        productCategoryPage.clickAddToCartButton();

        MiniCartComponent miniCart = productCategoryPage.moveToMiniCart();
        softAssert.assertTrue(
                miniCart.verifyMiniCartItemMatchesSelectedProduct(productCategoryPage),
                "Mini cart product info mismatch"
        );

        CheckoutPage checkoutPage = miniCart.clickCheckoutButton();
        softAssert.assertTrue(
                checkoutPage.isLoaded(),
                "Checkout page is not loaded successfully"
        );

        softAssert.assertTrue(
                checkoutPage.verifyOrderItemDetails(productCategoryPage),
                "Order item details mismatch on checkout page"
        );

        checkoutPage.captureBillingInfo();

        OrderStatusPage orderStatusPage = checkoutPage.placeOrder();
        softAssert.assertTrue(
                orderStatusPage.isLoaded(),
                "Order Status page is not loaded"
        );

        softAssert.assertTrue(
                orderStatusPage.verifyBillingDetails(checkoutPage),
                "Billing information mismatch"
        );

        softAssert.assertTrue(
                orderStatusPage.verifyOrderItemDetails(productCategoryPage.getSelectedProduct()),
                "Order item details mismatch"
        );

        softAssert.assertAll();
    }
}

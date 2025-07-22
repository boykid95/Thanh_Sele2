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
        SoftAssert softAssert = new SoftAssert();

        // Step 1: Open browser and go to homepage
        HomePage homePage = new HomePage();
        homePage.open();

        // Step 2: Login with valid credentials
        LoginPage loginPage = homePage.navigateToLoginPage();
        loginPage.loginWithValidCredentials();

        // Step 3: Navigate to All departments section
        homePage.navigateToAllDepartments();

        // Step 4: Select 'Electronic Components & Supplies'
        ProductCategoryPage productCategoryPage = homePage.navigateToProductCategory(ProductCategory.ELECTRONIC_COMPONENTS_SUPPLIES);
        productCategoryPage.openCategoryPage(); // mở đúng Category page

        // Step 5: Verify items are displayed as grid
        softAssert.assertTrue(
                productCategoryPage.verifyItemsDisplayedInLayout("grid"),
                "Step 5: Items are not displayed in grid layout"
        );

        // Step 6: Switch view to list
        productCategoryPage.switchView("list");

        // Step 7: Verify items are displayed as list
        softAssert.assertTrue(
                productCategoryPage.verifyItemsDisplayedInLayout("list"),
                "Items are not displayed in list layout"
        );

        // Step 8: Select any item randomly
        productCategoryPage.selectRandomProduct();

        // Step 9: Click 'Add to Cart'
        productCategoryPage.clickAddToCartButton();

        // Step 10: Go to the Cart
        MiniCartComponent miniCart = productCategoryPage.moveToMiniCart();

        // Step 11: Verify item details in mini cart
        softAssert.assertTrue(
                miniCart.verifyMiniCartItemMatchesSelectedProduct(productCategoryPage),
                "Mini cart product info mismatch"
        );

        // Step 12: Click on Checkout
        CheckoutPage checkoutPage = miniCart.clickCheckoutButton();

        // Step 13: Verify Checkout page is loaded
        softAssert.assertTrue(
                checkoutPage.isLoaded(),
                "Checkout page is not loaded successfully"
        );

        // Step 14: Verify item details on Checkout page
        softAssert.assertTrue(
                checkoutPage.verifyOrderItemDetails(productCategoryPage),
                "Order item details mismatch on checkout page"
        );

        // Step 15: Fill the billing details with default payment method
        checkoutPage.captureBillingInfo();

        // Step 16: Click on PLACE ORDER
        OrderStatusPage orderStatusPage = checkoutPage.placeOrder();

        // Step 17: Verify Order Status page is loaded
        softAssert.assertTrue(
                orderStatusPage.isLoaded(),
                "Step 17: Order Status page is not loaded"
        );

        // Step 18.1: Verify Billing Information
        softAssert.assertTrue(
                orderStatusPage.verifyBillingDetails(checkoutPage),
                "Step 18.1: Billing information mismatch"
        );

        // Step 18.2: Verify Order Item Information
        softAssert.assertTrue(
                orderStatusPage.verifyOrderItemDetails(productCategoryPage),
                "Step 18.2: Order item details mismatch"
        );

        // Final step: assert all
        softAssert.assertAll();
    }

}

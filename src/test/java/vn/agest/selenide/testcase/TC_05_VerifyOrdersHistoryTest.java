package vn.agest.selenide.testcase;

import io.qameta.allure.Description;
import lombok.extern.log4j.Log4j;
import org.testng.annotations.Test;
import vn.agest.selenide.model.Order;
import vn.agest.selenide.pageObjects.MyAccountPage;
import vn.agest.selenide.tests.BaseTest;

@Log4j
public class TC_05_VerifyOrdersHistoryTest extends BaseTest {

    @Test(description = "Verify that placed orders appear in My Account → Orders")
    @Description("User places two orders and verifies that both appear correctly in the Order History page")
    public void verifyOrdersInHistory() {

        homePage.open();
        homePage.navigateToLoginPage();
        loginPage.loginWithValidCredentials();

        Order order1 = homePage.placeRandomOrder(5);
        log.info("Order1 placed: " + order1);
        Order order2 = homePage.placeRandomOrder(10);
        log.info("Order2 placed: " + order2);

        MyAccountPage myAccountPage = homePage.navigateToMyAccountPage();
        myAccountPage.goToOrders();

        softAssert.assertTrue(myAccountPage.isOrderVisible(order1.getOrderId()),
                "❌ Error: Order #" + order1.getOrderId() + " do not display in Order History.");
        softAssert.assertTrue(myAccountPage.verifyOrderInHistory(order1),
                "❌ Order1 not correct in Order History");

        softAssert.assertTrue(myAccountPage.isOrderVisible(order2.getOrderId()),
                "❌ Error: Order #" + order2.getOrderId() + " do not display in Order History.");
        softAssert.assertTrue(myAccountPage.verifyOrderInHistory(order2),
                "❌ Order2 not correct in Order History");

        softAssert.assertAll();
    }
}

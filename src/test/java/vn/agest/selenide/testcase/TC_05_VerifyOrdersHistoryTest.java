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
    public void verifyOrdersInHistoryAndTheirDetails() {

        homePage.open();
        homePage.navigateToLoginPage();
        loginPage.loginWithValidCredentials();

        Order order1 = homePage.placeRandomOrder(2);
        Order order2 = homePage.placeRandomOrder(3);

        MyAccountPage myAccountPage = homePage.navigateToMyAccountPage();
        myAccountPage.navigateTo("Orders");

        softAssert.assertTrue(myAccountPage.isOrderVisible(order1.getOrderId()),
                "❌ Error: Order #" + order1.getOrderId() + " does not display in Order History.");
        myAccountPage.clickViewButtonForOrder(order1.getOrderId());
        myAccountPage.verifyOrderDetailPage(order1.getOrderId());

        myAccountPage.navigateTo("Orders");
        softAssert.assertTrue(myAccountPage.isOrderVisible(order2.getOrderId()),
                "❌ Error: Order #" + order2.getOrderId() + " does not display in Order History.");
        myAccountPage.clickViewButtonForOrder(order2.getOrderId());
        myAccountPage.verifyOrderDetailPage(order2.getOrderId());

        softAssert.assertAll();
    }
}

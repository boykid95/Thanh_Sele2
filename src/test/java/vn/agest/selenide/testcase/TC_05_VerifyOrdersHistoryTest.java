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

        // --- PHẦN SETUP: ĐĂNG NHẬP VÀ ĐẶT HÀNG ---
        homePage.open();
        homePage.navigateToLoginPage();
        loginPage.loginWithValidCredentials();

        Order order1 = homePage.placeRandomOrder(2);
        log.info("Order1 placed: " + order1.getOrderId());
        Order order2 = homePage.placeRandomOrder(3);
        log.info("Order2 placed: " + order2.getOrderId());

        MyAccountPage myAccountPage = homePage.navigateToMyAccountPage();
        myAccountPage.goToOrders();
        log.info("✅ Navigated to Order History page.");

        // --- KIỂM TRA CHO ORDER 1 ---
        log.info("▶️ Verifying flow for Order 1: #" + order1.getOrderId());
        softAssert.assertTrue(myAccountPage.isOrderVisible(order1.getOrderId()),
                "❌ Error: Order #" + order1.getOrderId() + " does not display in Order History.");

        myAccountPage.clickViewButtonForOrder(order1.getOrderId());
        myAccountPage.verifyOrderDetailPage(order1.getOrderId());
        log.info("-> Verified details for Order 1 successfully.");

        // *** BƯỚC SỬA LỖI QUAN TRỌNG ***
        // Quay lại trang danh sách Lịch sử đơn hàng để chuẩn bị cho lần kiểm tra tiếp theo.
        log.info("Navigating back to the orders list...");
        myAccountPage.goToOrders(); // Cách tốt nhất là điều hướng lại một cách tường minh.

        // --- KIỂM TRA CHO ORDER 2 ---
        log.info("▶️ Verifying flow for Order 2: #" + order2.getOrderId());
        softAssert.assertTrue(myAccountPage.isOrderVisible(order2.getOrderId()),
                "❌ Error: Order #" + order2.getOrderId() + " does not display in Order History.");

        myAccountPage.clickViewButtonForOrder(order2.getOrderId());
        myAccountPage.verifyOrderDetailPage(order2.getOrderId());
        log.info("-> Verified details for Order 2 successfully.");

        // --- KẾT THÚC ---
        log.info("✅ All verifications are complete.");
        softAssert.assertAll();
    }
}

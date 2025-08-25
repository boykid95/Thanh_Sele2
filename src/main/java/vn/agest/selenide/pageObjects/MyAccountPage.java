package vn.agest.selenide.pageObjects;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j;
import vn.agest.selenide.common.ElementHelper;
import vn.agest.selenide.enums.PageType;
import vn.agest.selenide.model.Order;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.$$x;

@Log4j
public class MyAccountPage extends BasePage {

    private final SelenideElement ordersLink = $x("//nav//a[contains(text(),'Orders')]");
    private final ElementsCollection orderRows = $$x("//table[contains(@class,'orders')]//tbody/tr");
    private final SelenideElement billingSection = $x("//div[contains(@class,'woocommerce-Address-billing')]");

    private final String orderRowLocator = "//tr[contains(., '#%s')]";
    private final String viewButtonInRowLocator = "a.view";
    private final SelenideElement orderDetailTitle = $x("//h1[contains(@class,'entry-title')]");
    private final SelenideElement orderDetailsSection = $x("//section[contains(@class,'woocommerce-order-details')]");
    private final SelenideElement billingAddressSection = $x("//section[contains(@class,'woocommerce-customer-details')]");

    public MyAccountPage() {
        super(new ElementHelper(), PageType.MY_ACCOUNT_PAGE);
    }

    @Step("Navigate to Orders section in My Account")
    public void goToOrders() {
        elementHelper.clickToElement(ordersLink, "Orders Link");
    }

    @Step("Execute full verification flow for Order #{order.getOrderId()}")
    public boolean verifyOrderDetails(Order order) {
        String orderId = order.getOrderId();
        try {
            log.info("▶️ Verifying flow for Order: #" + orderId);
            if (!isOrderVisible(orderId)) {
                return false;
            }
            log.info("-> Verified: Order #" + orderId + " is visible in the list.");

            clickViewButtonForOrder(orderId);
            log.info("-> Action: Clicked 'View' button.");

            verifyOrderDetailPage(orderId);
            log.info("-> Verified: Order Details and Billing Address are correct.");

            return true;
        } catch (Exception e) {
            log.error("‼️ FAILED flow for Order #" + orderId + ". Error: " + e.getMessage());
            return false;
        }
    }

    @Step("Check if order with ID {orderId} is visible in history")
    public boolean isOrderVisible(String orderId) {
        try {
            return getOrderRowById(orderId).is(visible);
        } catch (Exception e) {
            log.error("Verification failed: Could not find any element for Order #" + orderId);
            return false;
        }
    }

    @Step("Click 'View' button for order with ID {orderId}")
    public void clickViewButtonForOrder(String orderId) {
        SelenideElement orderRow = getOrderRowById(orderId);
        // *** ĐÂY LÀ CHỖ THAY ĐỔI ***
        SelenideElement viewButton = orderRow.$(viewButtonInRowLocator);
        elementHelper.clickToElement(viewButton, "View Button for Order #" + orderId);
    }

    @Step("Verify Order Detail Page for order ID {orderId}")
    public void verifyOrderDetailPage(String orderId) {
        orderDetailTitle.shouldHave(text("Order #" + orderId));
        orderDetailsSection.shouldBe(visible);
        billingAddressSection.shouldBe(visible);
    }

    private SelenideElement getOrderRowById(String orderId) {
        return $x(String.format(orderRowLocator, orderId));
    }
}

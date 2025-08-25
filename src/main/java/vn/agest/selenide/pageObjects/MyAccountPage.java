package vn.agest.selenide.pageObjects;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j;
import vn.agest.selenide.common.ElementHelper;
import vn.agest.selenide.enums.PageType;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

@Log4j
public class MyAccountPage extends BasePage {

    private final String navMenuItemLocator = "//nav[contains(@class,'woocommerce-MyAccount-navigation')]//a[contains(.,'%s')]";
    private final SelenideElement ordersTable = $("table.woocommerce-orders-table");
    private final String orderRowLocator = "//tr[contains(., '#%s')]";
    private final String viewButtonInRowLocator = "a.view";

    private final SelenideElement orderDetailsTitle = $x("//h2[contains(@class,'woocommerce-order-details__title')]");
    private final SelenideElement orderDetailNumber = $x("//p/mark[contains(@class,'order-number')]");
    private final SelenideElement orderDetailsSection = $x("//section[contains(@class,'woocommerce-order-details')]");
    private final SelenideElement billingAddressSection = $x("//section[contains(@class,'woocommerce-customer-details')]");

    public MyAccountPage() {
        super(new ElementHelper(), PageType.MY_ACCOUNT_PAGE);
    }

    @Step("Navigate to a sub-menu in My Account page: {menuName}")
    public void navigateTo(String menuName) {
        SelenideElement menuItem = $x(String.format(navMenuItemLocator, menuName));
        elementHelper.clickToElement(menuItem, "Click on " + menuName + " menu item");

        if (menuName.equalsIgnoreCase("Orders")) {
            ordersTable.shouldBe(visible);
            log.info("Order history table is now visible.");
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
        SelenideElement viewButton = orderRow.$(viewButtonInRowLocator);
        elementHelper.clickToElement(viewButton, "View Button for Order #" + orderId);

        orderDetailsTitle.shouldBe(visible);
        log.info("Order details page is now visible.");
    }

    @Step("Verify Order Detail Page for order ID {orderId}")
    public void verifyOrderDetailPage(String orderId) {
        orderDetailNumber.shouldHave(text(orderId));
        orderDetailsSection.shouldBe(visible);
        billingAddressSection.shouldBe(visible);
    }

    private SelenideElement getOrderRowById(String orderId) {
        return $x(String.format(orderRowLocator, orderId));
    }
}

package vn.agest.selenide.pageObjects;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j;
import vn.agest.selenide.common.ElementHelper;
import vn.agest.selenide.enums.PageType;
import vn.agest.selenide.model.Order;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.$$x;

@Log4j
public class MyAccountPage extends BasePage {

    private final SelenideElement ordersLink = $x("//nav//a[contains(text(),'Orders')]");
    private final ElementsCollection orderRows = $$x("//table[contains(@class,'orders')]//tbody/tr");
    private final SelenideElement logoutLink = $x("//a[contains(text(),'Logout')]");
    private final SelenideElement billingSection = $x("//div[contains(@class,'woocommerce-Address-billing')]");

    public MyAccountPage() {
        super(new ElementHelper(), PageType.MY_ACCOUNT_PAGE);
    }

    @Step("Navigate to Orders section in My Account")
    public void goToOrders() {
        elementHelper.clickToElement(ordersLink, "Orders Link");
    }

    @Step("Check if order {order} exists in order history table")
    private boolean isOrderDisplayed(Order order) {
        return orderRows.stream()
                .anyMatch(row -> row.getText().contains(order.getOrderId())
                        && row.getText().contains(String.valueOf((int) order.getTotalAmount()))); // cast tránh format lỗi
    }

    @Step("Check billing info for order {order}")
    private boolean verifyBillingInfo(Order order) {
        return billingSection.getText().contains(order.getBillingFullName())
                && billingSection.getText().contains(order.getBillingStreet())
                && billingSection.getText().contains(order.getBillingCity());
    }

    @Step("Verify order {order} is displayed correctly in Order History")
    public boolean verifyOrderInHistory(Order order) {
        boolean orderFound = isOrderDisplayed(order);
        boolean billingMatch = verifyBillingInfo(order);

        log.info("Verify Order " + order.getOrderId()
                + " → Found: " + orderFound
                + ", Billing match: " + billingMatch);

        return orderFound && billingMatch;
    }
}

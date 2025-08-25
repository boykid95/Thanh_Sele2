package vn.agest.selenide.pageObjects;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j;
import vn.agest.selenide.common.ElementHelper;
import vn.agest.selenide.enums.PageType;
import vn.agest.selenide.model.Order;
import vn.agest.selenide.model.Product;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

@Log4j
public class OrderStatusPage extends BasePage {

    private final SelenideElement orderReceivedTitle = $x("//p[contains(@class,'woocommerce-thankyou-order-received')]");
    private final SelenideElement billingAddress = $x("//address");
    private final SelenideElement confirmationMessage = $x("//p[contains(@class,'woocommerce-notice')]");

    // Single item locators
    private final SelenideElement orderItemName = $x("//td[contains(@class,'product-name')]/a");
    private final SelenideElement orderItemPrice = $x("//td[@class='woocommerce-table__product-total product-total']//bdi");
    private final SelenideElement orderIdElement = $x("//li[contains(@class,'woocommerce-order-overview__order')]/strong");

    // Multiple items locators
    private final ElementsCollection orderItemNames = $$x("//td[contains(@class,'product-name')]/a");
    private final ElementsCollection orderItemPrices = $$x("//td[@class='woocommerce-table__product-total product-total']//bdi");
    private final ElementsCollection orderItemQuantities = $$x("//td[contains(@class,'product-name')]//strong");

    public OrderStatusPage() {
        super(new ElementHelper(),PageType.ORDER_STATUS_PAGE);
    }

    @Step("Verify Order Status page is loaded")
    public boolean isLoaded() {
        return orderReceivedTitle.isDisplayed();
    }

    @Step("Get Order Confirmation Message")
    public String getOrderConfirmationMessage() {
        return confirmationMessage.getText().trim();
    }

    @Step("Verify Billing Information matches Checkout Info")
    public boolean verifyBillingDetails(CheckoutPage checkoutPage) {
        String actualBillingInfo = billingAddress.getText().trim();

        String expectedBillingInfo = checkoutPage.getBillingFullName() + "\n" +
                checkoutPage.getBillingCompany() + "\n" +
                checkoutPage.getBillingStreet() + "\n" +
                checkoutPage.getBillingCity() + "\n" +
                checkoutPage.getBillingCountry() + "\n" +
                checkoutPage.getBillingPhone() + "\n" +
                checkoutPage.getBillingEmail();

        boolean billingMatch = actualBillingInfo.equalsIgnoreCase(expectedBillingInfo);
        if (!billingMatch) {
            log.error("Billing address mismatch!" +
                    "\nExpected:\n" + expectedBillingInfo +
                    "\nActual:\n" + actualBillingInfo);
        }

        return billingMatch;
    }

    @Step("Verify single ordered item matches selected product")
    public boolean verifyOrderItemDetails(Product expectedProduct) {
        String actualItemName = orderItemName.getText().trim();
        double actualItemPrice = parsePrice(orderItemPrice.getText());

        boolean itemNameMatch = actualItemName.equalsIgnoreCase(expectedProduct.getName());
        boolean itemPriceMatch = actualItemPrice == expectedProduct.getPrice();

        if (!itemNameMatch) {
            log.error("Item name mismatch. Expected: " + expectedProduct.getName() + ", Actual: " + actualItemName);
        }
        if (!itemPriceMatch) {
            log.error("Item price mismatch. Expected: " + expectedProduct.getPrice() + ", Actual: " + actualItemPrice);
        }

        return itemNameMatch && itemPriceMatch;
    }

    @Step("Get purchased products info from Order Status Page")
    public List<Product> getOrderProductInfo() {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < orderItemNames.size(); i++) {
            String name = orderItemNames.get(i).getText().trim();
            double price = parsePrice(orderItemPrices.get(i).getText());
            String rawQuantity = orderItemQuantities.get(i).getText().trim();

            int quantity = 1;
            if (rawQuantity.contains("×")) {
                try {
                    quantity = Integer.parseInt(rawQuantity.replace("×", "").trim());
                } catch (NumberFormatException ignored) {
                }
            }
            Product product = new Product(name, price, quantity);
            products.add(product);
            product.logInfo("✅ Ordered product found: ");
        }
        return products;
    }

    @Step("Extract all order information from the page")
    public Order extractOrderInfo() {
        String orderId = orderIdElement.getText().trim();
        Order order = new Order();
        order.setOrderId(orderId);
        return order;
    }
}

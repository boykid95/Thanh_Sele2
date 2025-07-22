package vn.agest.selenide.pageObjects;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import vn.agest.selenide.common.utilities.other.Log;
import vn.agest.selenide.enums.PageType;

import static com.codeborne.selenide.Selenide.$x;

public class OrderStatusPage extends BasePage {

    private final SelenideElement orderReceivedTitle = $x("//p[contains(@class,'woocommerce-thankyou-order-received')]");
    private final SelenideElement billingAddress = $x("//address");
    private final SelenideElement orderItemName = $x("//td[contains(@class,'product-name')]/a");
    private final SelenideElement orderItemPrice = $x("//td[@class='woocommerce-table__product-total product-total']//bdi");

    public OrderStatusPage() {
        super(PageType.ORDER_STATUS_PAGE);
    }

    @Step("Verify Order Status page is loaded")
    public boolean isLoaded() {
        return orderReceivedTitle.isDisplayed();
    }

    @Step("Verify Billing Information matches Checkout Info")
    public boolean verifyBillingDetails(CheckoutPage checkoutPage) {

        String actualBillingInfo = billingAddress.getText().trim();

        // Prepare expected billing address from CheckoutPage
        String expectedBillingInfo = checkoutPage.getBillingFullName() + "\n" +
                checkoutPage.getBillingCompany() + "\n" +
                checkoutPage.getBillingStreet() + "\n" +
                checkoutPage.getBillingCity() + "\n" +
                checkoutPage.getBillingCountry() + "\n" +
                checkoutPage.getBillingPhone() + "\n" +
                checkoutPage.getBillingEmail();

        boolean billingMatch = actualBillingInfo.equalsIgnoreCase(expectedBillingInfo);
        if (!billingMatch) {
            Log.error("Billing address mismatch! " + "\n" + "Expected: " + expectedBillingInfo + "\n" + "Actual: " + actualBillingInfo);
        }

        return billingMatch;
    }

    @Step("Verify Order Item matches Selected Product")
    public boolean verifyOrderItemDetails(ProductCategoryPage productPage) {
        String actualItemName = orderItemName.getText().trim();
        String actualItemPrice = orderItemPrice.getText().trim();

        String expectedItemName = productPage.getSelectedProductName();
        String expectedItemPrice = productPage.getSelectedProductPrice();

        boolean itemNameMatch = actualItemName.toLowerCase().contains(expectedItemName.toLowerCase());
        boolean itemPriceMatch = actualItemPrice.replaceAll("[^0-9.]", "").equals(expectedItemPrice.replaceAll("[^0-9.]", ""));

        Log.info("Order Item Verification - Name Match: " + itemNameMatch + ", Price Match: " + itemPriceMatch);

        if (!itemNameMatch) {
            Log.error("Item name does not match. Expected: " + expectedItemName + ", Actual: " + actualItemName);
        }
        if (!itemPriceMatch) {
            Log.error("Item price does not match. Expected: " + expectedItemPrice + ", Actual: " + actualItemPrice);
        }

        return itemNameMatch && itemPriceMatch;
    }
}

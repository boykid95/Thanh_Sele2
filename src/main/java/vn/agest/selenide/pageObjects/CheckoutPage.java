package vn.agest.selenide.pageObjects;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import vn.agest.selenide.common.DriverUtils;
import vn.agest.selenide.enums.PageType;
import vn.agest.selenide.common.ElementHelper;
import vn.agest.selenide.model.Order;
import vn.agest.selenide.model.Product;

import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

@Log4j
public class CheckoutPage extends BasePage {

    private final ElementHelper elementHelper = new ElementHelper();

    private final SelenideElement billingDetailsSection = $x("//div[@class='woocommerce-billing-fields']");
    private final SelenideElement placeOrderButton = $x("//button[@id='place_order']");
    private final SelenideElement orderItemName = $x("//td[contains(@class,'product-name')]/text()[1]");
    private final SelenideElement orderItemPrice = $x("//td[@class='product-total']/span[@class='woocommerce-Price-amount amount']");

    private final SelenideElement firstNameInput = $x("//input[@id='billing_first_name']");
    private final SelenideElement lastNameInput = $x("//input[@id='billing_last_name']");
    private final SelenideElement companyInput = $x("//input[@id='billing_company']");
    private final SelenideElement streetInput = $x("//input[@id='billing_address_1']");
    private final SelenideElement cityInput = $x("//input[@id='billing_city']");
    private final SelenideElement postcodeInput = $x("//input[@id='billing_postcode']");
    private final SelenideElement countryDropdownDisplay = $x("//span[contains(@id,'billing_country') and contains(@class,'select2-selection__rendered')]");
    private final SelenideElement phoneInput = $x("//input[@id='billing_phone']");
    private final SelenideElement emailInput = $x("//input[@id='billing_email']");
    private final SelenideElement directBankTransferOption = $x("//input[@id='payment_method_bacs']");
    private final SelenideElement cashOnDeliveryOption = $x("//input[@id='payment_method_cod']");
    private final SelenideElement errorList = $("ul.woocommerce-error");

    private final SelenideElement loadingOverlay = $x("//div[@class='blockUI blockOverlay']");

    private String billingFirstName;
    private String billingLastName;
    @Getter
    private String billingCompany;
    @Getter
    private String billingStreet;
    @Getter
    private String billingCity;
    @Getter
    private String billingPostcode;
    @Getter
    private String billingCountry;
    @Getter
    private String billingPhone;
    @Getter
    private String billingEmail;

    public CheckoutPage() {
        super(new ElementHelper(), PageType.CHECKOUT_PAGE);
    }

    @Step("Verify Checkout page is loaded")
    public boolean isLoaded() {
        return billingDetailsSection.isDisplayed();
    }

    @Step("Capture Billing Information from Checkout Page")
    public void captureBillingInfo() {
        billingFirstName = firstNameInput.getValue().trim();
        billingLastName = lastNameInput.getValue().trim();
        billingCompany = companyInput.getValue().trim();
        billingStreet = streetInput.getValue().trim();
        billingCity = cityInput.getValue().trim();
        billingPostcode = postcodeInput.getValue().trim();
        billingCountry = countryDropdownDisplay.getText().trim();
        billingPhone = phoneInput.getValue().trim();
        billingEmail = emailInput.getValue().trim();

        log.info("Captured Billing Info: " + "FirstName=" + billingFirstName + ", LastName=" + billingLastName + ", Company=" + billingCompany + ", Street=" + billingStreet + ", City=" + billingCity + ", Postcode=" + billingPostcode + ", Country=" + billingCountry + ", Phone=" + billingPhone + ", Email=" + billingEmail);
    }

    @Step("Get Billing Full Name")
    public String getBillingFullName() {
        return billingFirstName + " " + billingLastName;
    }

    private final SelenideElement orderItemNameCell = $x("//td[contains(@class,'product-name')]");

    @Step("Verify item details on Checkout page match selected product")
    public boolean verifyOrderItemDetails(Product expectedProduct) { // <-- Thay đổi tham số
        elementHelper.waitForElementVisible(orderItemNameCell, "Order Item Cell");

        // Dùng JavaScript để lấy text con đầu tiên của thẻ <td>
        String actualName = executeJavaScript(
                "return arguments[0].childNodes[0].textContent.trim()",
                orderItemNameCell
        );

        double actualPrice = parsePrice(orderItemPrice.getText());

        // Log chi tiết để dễ debug
        log.info("--- DEBUGGING MISMATCH ---");
        log.info("Expected Name: " + expectedProduct.getName() + " | Actual Name: " + actualName);
        log.info("Expected Price: " + expectedProduct.getPrice() + " | Actual Price: " + actualPrice);
        log.info("--------------------------");

        boolean nameMatch = actualName.equalsIgnoreCase(expectedProduct.getName());
        boolean priceMatch = actualPrice == expectedProduct.getPrice();

        return nameMatch && priceMatch;
    }

    @Step("Click on 'Place Order' button")
    public OrderStatusPage placeOrder() {
        elementHelper.clickToElement(placeOrderButton, "Place Order Button");
        waitForLoadingOverlay();
        Selenide.Wait().until(webDriver -> WebDriverRunner.url().contains("/checkout/order-received/"));
        DriverUtils.waitToLoadPage();
        return new OrderStatusPage();
    }

    @Step("Click on 'Place Order' button without filling form")
    public void clickPlaceOrder() {
        elementHelper.clickToElement(placeOrderButton, "Place Order Button");
        waitForLoadingOverlay();
    }

    @Step("Wait for loading overlay to appear and disappear")
    public void waitForLoadingOverlay() {
        try {
            loadingOverlay.shouldBe(visible, Duration.ofSeconds(5));
        } catch (Exception e) {
            log.info("Loading overlay did not appear immediately, continuing...");
        }
        loadingOverlay.shouldNotBe(visible, Duration.ofSeconds(30));
    }

    @Step("Select payment method: {method}")
    public void selectPaymentMethod(String method) {
        switch (method.toLowerCase()) {
            case "bank":
                elementHelper.clickToElement(directBankTransferOption, "Select Direct Bank Transfer");
                break;
            case "cod":
                elementHelper.clickToElement(cashOnDeliveryOption, "Select Cash on Delivery");
                break;
            default:
                throw new IllegalArgumentException("Unsupported payment method: " + method);
        }
    }

    @Step("Place order and extract order info")
    public Order placeOrderAndExtractInfo(List<Product> products) {
        captureBillingInfo();

        elementHelper.clickToElement(placeOrderButton, "Place Order Button");
        waitForLoadingOverlay();
        Selenide.Wait().until(webDriver -> WebDriverRunner.url().contains("/checkout/order-received/"));
        DriverUtils.waitToLoadPage();

        String orderId = $x("//li[contains(@class,'order')]//strong").getText().trim();
        double totalAmount = BasePage.parsePrice(
                $x("//li[contains(@class,'total')]//strong").getText().trim()
        );

        return new Order(orderId, totalAmount,
                billingFirstName, billingLastName,
                billingStreet, billingCity, billingPostcode,
                billingCountry, billingPhone, billingEmail);
    }

    @Step("Fill all guest billing details and place the order")
    public OrderStatusPage placeGuestOrder(String firstName, String lastName, String address, String city, String postcode, String phone, String email) {
        log.info("Filling in guest billing details...");

        firstNameInput.shouldBe(visible, Duration.ofSeconds(10));

        firstNameInput.setValue(firstName);
        lastNameInput.setValue(lastName);
        streetInput.setValue(address);
        cityInput.setValue(city);
        postcodeInput.setValue(postcode);
        phoneInput.setValue(phone);
        emailInput.setValue(email);

        executeJavaScript("arguments[0].click();", cashOnDeliveryOption);
        return placeOrder();
    }

    @Step("Verify that error message '{message}' is displayed")
    public boolean isErrorMessageDisplayed(String message) {
        try {
            return errorList.shouldBe(visible).has(text(message));
        } catch (Exception e) {
            log.error("❌ Could not find error message: '" + message + "'", e);
            return false;
        }
    }
}

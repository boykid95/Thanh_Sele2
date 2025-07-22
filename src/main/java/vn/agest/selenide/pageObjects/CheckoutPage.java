package vn.agest.selenide.pageObjects;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Step;
import vn.agest.selenide.common.utilities.other.Log;
import vn.agest.selenide.enums.PageType;
import vn.agest.selenide.common.utilities.helpers.ElementHelper;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$x;

public class CheckoutPage extends BasePage {

    private final ElementHelper elementHelper = new ElementHelper();

    private final SelenideElement billingDetailsSection = $x("//div[@class='woocommerce-billing-fields']");
    private final SelenideElement placeOrderButton = $x("//button[@id='place_order']");
    private final SelenideElement orderItemName = $x("//td[@class='product-name']");
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

    private String billingFirstName;
    private String billingLastName;
    private String billingCompany;
    private String billingStreet;
    private String billingCity;
    private String billingPostcode;
    private String billingCountry;
    private String billingPhone;
    private String billingEmail;

    public CheckoutPage() {
        super(PageType.CHECKOUT_PAGE);
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

        Log.info("Captured Billing Info: "
                + "FirstName=" + billingFirstName
                + ", LastName=" + billingLastName
                + ", Company=" + billingCompany
                + ", Street=" + billingStreet
                + ", City=" + billingCity
                + ", Postcode=" + billingPostcode
                + ", Country=" + billingCountry
                + ", Phone=" + billingPhone
                + ", Email=" + billingEmail);
    }

    @Step("Get Billing Full Name")
    public String getBillingFullName() {
        return billingFirstName + " " + billingLastName;
    }

    public String getBillingCompany() {
        return billingCompany;
    }

    public String getBillingStreet() {
        return billingStreet;
    }

    public String getBillingCity() {
        return billingCity;
    }

    public String getBillingPostcode() {
        return billingPostcode;
    }

    public String getBillingCountry() {
        return billingCountry;
    }

    public String getBillingPhone() {
        return billingPhone;
    }

    public String getBillingEmail() {
        return billingEmail;
    }

    @Step("Verify item details on Checkout page match selected product")
    public boolean verifyOrderItemDetails(ProductCategoryPage productCategoryPage) {
        elementHelper.waitForElementVisible(orderItemName, "Order Item Name");
        elementHelper.waitForElementVisible(orderItemPrice, "Order Item Price");

        String actualName = orderItemName.getText().trim();
        String actualPrice = orderItemPrice.getText().trim();

        String expectedName = productCategoryPage.getSelectedProductName();
        String expectedPrice = productCategoryPage.getSelectedProductPrice();

        boolean nameMatch = actualName.toLowerCase().contains(expectedName.toLowerCase());
        boolean priceMatch = actualPrice.replaceAll("[^0-9.]", "").equals(expectedPrice.replaceAll("[^0-9.]", ""));

        Log.info("Checkout Verify - Name Match: " + nameMatch + ", Price Match: " + priceMatch);

        return nameMatch && priceMatch;
    }

    @Step("Click on 'Place Order' button")
    public OrderStatusPage placeOrder() {
        elementHelper.waitForElementClickable(placeOrderButton, "Place Order Button");
        elementHelper.clickToElement(placeOrderButton, "Place Order Button");

        waitForLoadingOverlay();
        Selenide.Wait().until(webDriver -> WebDriverRunner.url().contains("/checkout/order-received/"));
        elementHelper.waitToLoadPage();
        return new OrderStatusPage();
    }

    @Step("Wait for loading overlay to appear and disappear")
    public void waitForLoadingOverlay() {
        SelenideElement loadingOverlay = $x("//div[@class='blockUI blockOverlay']");

        try {
            loadingOverlay.shouldBe(Condition.visible, Duration.ofSeconds(5));
        } catch (Exception e) {
            Log.info("Loading overlay did not appear immediately, continuing...");
        }
        loadingOverlay.shouldNotBe(Condition.visible, Duration.ofSeconds(30));
    }
}

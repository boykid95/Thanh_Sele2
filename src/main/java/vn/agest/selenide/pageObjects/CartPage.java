package vn.agest.selenide.pageObjects;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j;
import vn.agest.selenide.common.DriverUtils;
import vn.agest.selenide.common.ElementHelper;
import vn.agest.selenide.enums.PageType;
import vn.agest.selenide.model.Product;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.*;

@Log4j
public class CartPage extends BasePage {

    public CartPage() {
        super(new ElementHelper(), PageType.CART_PAGE);
    }

    private final ElementsCollection cartItems = $$x("//table[contains(@class,'shop_table')]//tr[contains(@class,'cart_item')]");
    private final ElementsCollection removeButtons = $$x("//a[contains(@class,'remove')]");
    private static final String nameSelector = ".cart-item-details .product-title";
    private static final String priceSelector = "td.product-price .amount";
    private static final String quantitySelector = "td.product-quantity input";

    private final SelenideElement checkOutButton = $x("//a[contains(@class,'checkout-button')]");
    private final SelenideElement emptyCartMessage = $x("//div[contains(@class,'cart-empty')]//h1[contains(text(),'YOUR SHOPPING CART IS EMPTY')]");
    private final SelenideElement removeMessage = $x("//div[contains(@class,'woocommerce-message') and contains(text(),'removed')]");

    private final SelenideElement quantityInput = $("input.qty");
    private final SelenideElement plusButton = $(".plus");
    private final SelenideElement minusButton = $(".minus");
    private final SelenideElement updateCartButton = $("button[name='update_cart']");
    private final SelenideElement unitPrice = $("tr.cart_item td.product-price span.woocommerce-Price-amount");
    private final SelenideElement subtotal = $("td.product-subtotal span.woocommerce-Price-amount");
    private final SelenideElement cartOverlay = $("div.blockUI.blockOverlay");

    @Step("Get cart product information")
    public List<Product> getCartProductInfo() {
        List<Product> products = new ArrayList<>();

        for (SelenideElement item : cartItems) {
            String name = item.find(nameSelector).getText();
            double unitPrice = parsePrice(item.find(priceSelector).getText());
            int quantity = Integer.parseInt(item.find(quantitySelector).getValue());
            double totalPrice = unitPrice * quantity;

            Product product = new Product(name, totalPrice, quantity);
            products.add(product);
            product.logInfo("Cart product");
        }

        return products;
    }

    @Step("Proceed to checkout")
    public CheckoutPage checkOut() {
        elementHelper.clickToElement(checkOutButton, "Click Check Out button");
        return new CheckoutPage();
    }

    @Step("Check if products exist in cart")
    public boolean hasProductsInCart() {
        return cartItems.size() > 0;
    }

    @Step("Remove all products from cart")
    public CartPage removeAllProducts() {
        while (!removeButtons.isEmpty()) {
            elementHelper.clickToElement(removeButtons.first(), "Click remove item from cart");
            removeMessage.shouldBe(Condition.visible);
        }
        return this;
    }

    @Step("Verify that cart is empty")
    public boolean isCartEmpty() {
        return emptyCartMessage.shouldBe(Condition.visible).exists();
    }

    @Step("Get product quantity from cart")
    public int getQuantity() {
        return Integer.parseInt(quantityInput.getValue().trim());
    }

    @Step("Click plus button to increase quantity")
    public void clickPlus() {
        plusButton.click();
        waitForCartUpdate();
    }

    @Step("Click minus button to decrease quantity")
    public void clickMinus() {
        minusButton.click();
        waitForCartUpdate();
    }

    @Step("Update quantity to {qty}")
    public void updateQuantity(int qty) {
        quantityInput.clear();
        quantityInput.setValue(String.valueOf(qty));
        updateCartButton.click();
        waitForCartUpdate();
    }

    @Step("Get unit price")
    public double getUnitPrice() {
        return BasePage.parsePrice(unitPrice.getText());
    }

    @Step("Get subtotal price from cart")
    public double getSubtotalPrice() {
        return BasePage.parsePrice(subtotal.getText());
    }

    @Step("Wait for cart to update (overlay appears then disappears)")
    public void waitForCartUpdate() {
        try {
            cartOverlay.shouldBe(Condition.visible, Duration.ofSeconds(5));
        } catch (Exception e) {
            log.info("⚡ Overlay not detected immediately, skip visible check.");
        }
        cartOverlay.shouldNotBe(Condition.visible, Duration.ofSeconds(10));
        log.info("✅ Cart update finished, overlay gone.");
    }
}

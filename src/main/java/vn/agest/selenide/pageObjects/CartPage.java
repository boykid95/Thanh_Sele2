package vn.agest.selenide.pageObjects;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import vn.agest.selenide.common.ElementHelper;
import vn.agest.selenide.enums.PageType;
import vn.agest.selenide.model.Product;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

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
}

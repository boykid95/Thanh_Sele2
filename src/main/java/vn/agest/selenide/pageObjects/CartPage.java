package vn.agest.selenide.pageObjects;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import vn.agest.selenide.common.utilities.helpers.PriceHelper;
import vn.agest.selenide.enums.PageType;
import vn.agest.selenide.model.Product;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

public class CartPage extends BasePage {

    public CartPage() {
        super(PageType.CART_PAGE);
    }

    private final ElementsCollection cartItems = $$x("//table[contains(@class,'shop_table')]//tr[contains(@class,'cart_item')]");
    private static final String NAME_SELECTOR = ".cart-item-details .product-title";
    private static final String PRICE_SELECTOR = "td.product-price .amount";
    private static final String QUANTITY_SELECTOR = "td.product-quantity input";

    private final SelenideElement checkOutButton = $x("//a[contains(@class,'checkout-button')]");

    @Step("Get cart product information")
    public List<Product> getCartProductInfo() {
        List<Product> products = new ArrayList<>();

        for (SelenideElement item : cartItems) {
            String name = item.find(NAME_SELECTOR).getText();
            double price = PriceHelper.parsePrice(item.find(PRICE_SELECTOR).getText());
            int quantity = Integer.parseInt(item.find(QUANTITY_SELECTOR).getValue());

            Product product = new Product(name, price, quantity);
            products.add(product);
            product.logInfo("Cart product");
        }

        return products;
    }

    @Step("Proceed to checkout")
    public CheckoutPage checkOut() {
        elementHelper.clickToElement(checkOutButton,"Click Check Out button");
        return new CheckoutPage();
    }
}

package vn.agest.selenide.pageObjects.components;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import vn.agest.selenide.common.ElementHelper;
import vn.agest.selenide.pageObjects.CheckoutPage;
import vn.agest.selenide.pageObjects.ProductCategoryPage;

import static com.codeborne.selenide.Selenide.$x;

public class MiniCartComponent {

    private final ElementHelper elementHelper = new ElementHelper();

    private final SelenideElement miniCartContainer = $x("//div[@class='header-wrapper']");
    private final SelenideElement miniCartProductName = miniCartContainer.$x(".//h4[@class='product-title']/a");
    private final SelenideElement miniCartProductPrice = miniCartContainer.$x(".//div[@class='descr-box']//span[@class='woocommerce-Price-amount amount']");
    private final SelenideElement checkoutButton = miniCartContainer.$x(".//a[contains(@class,'checkout')]");

    @Step("Verify Mini Cart product matches selected product")
    public boolean verifyMiniCartItemMatchesSelectedProduct(ProductCategoryPage productPage) {
        elementHelper.waitForElementVisible(miniCartProductName, "Mini Cart Product Name");
        elementHelper.waitForElementVisible(miniCartProductPrice, "Mini Cart Product Price");

        String actualName = miniCartProductName.getText().trim();
        String actualPrice = miniCartProductPrice.getText().trim();
        String expectedName = productPage.getSelectedProductName();
        String expectedPrice = productPage.getSelectedProductPrice();

        boolean nameMatch = actualName.toLowerCase().contains(expectedName.toLowerCase());
        boolean priceMatch = actualPrice.replaceAll("[^0-9.]", "")
                .equals(expectedPrice.replaceAll("[^0-9.]", ""));

        return nameMatch && priceMatch;
    }

    @Step("Click Checkout button from mini cart")
    public CheckoutPage clickCheckoutButton() {
        elementHelper.waitForElementVisible(checkoutButton, "Mini Cart Checkout Button");
        elementHelper.clickToElement(checkoutButton, "Mini Cart Checkout Button");
        return new CheckoutPage();
    }
}

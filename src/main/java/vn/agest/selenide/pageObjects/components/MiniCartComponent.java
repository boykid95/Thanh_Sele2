package vn.agest.selenide.pageObjects.components;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j;
import vn.agest.selenide.common.ElementHelper;
import vn.agest.selenide.model.Product;
import vn.agest.selenide.pageObjects.CheckoutPage;
import vn.agest.selenide.pageObjects.ProductCategoryPage;

import static com.codeborne.selenide.Selenide.$x;
import static vn.agest.selenide.pageObjects.BasePage.parsePrice;

@Log4j
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

        Product expected = productPage.getSelectedProduct();

        String actualName = miniCartProductName.getText().trim();
        double actualPrice = parsePrice(miniCartProductPrice.getText());

        boolean nameMatch = actualName.equalsIgnoreCase(expected.getName());
        boolean priceMatch = actualPrice == expected.getPrice();

        if (!nameMatch) {
            log.error("❌ Mini Cart name mismatch. Expected: " + expected.getName() + ", Actual: " + actualName);
        }
        if (!priceMatch) {
            log.error("❌ Mini Cart price mismatch. Expected: " + expected.getPrice() + ", Actual: " + actualPrice);
        }

        return nameMatch && priceMatch;
    }

    @Step("Click Checkout button from mini cart")
    public CheckoutPage clickCheckoutButton() {
        elementHelper.waitForElementVisible(checkoutButton, "Mini Cart Checkout Button");
        elementHelper.clickToElement(checkoutButton, "Mini Cart Checkout Button");
        return new CheckoutPage();
    }
}

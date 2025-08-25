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

import java.util.*;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

@Log4j
public class ShopPage extends BasePage {

    private final SelenideElement viewCartButton = $x("//div[@class='header-wrapper']//div[contains(@class,'header-cart')]/a[contains(@href,'/cart')]");
    private final SelenideElement adCloseButton = $x("//div[@class='pum-close']");
    private final SelenideElement gridViewButton = $x("//div[contains(@class,'switch-grid')]");
    private final SelenideElement listViewButton = $x("//div[contains(@class,'switch-list')]");
    private final SelenideElement loader = $x("//div[contains(@class,'et-loader') and contains(@class,'product-ajax')]");

    private final ElementsCollection productItems = $$x("//div[contains(@class,'content-product')]");
    private static final String titleSelector = ".product-details .product-title";
    private static final String addToCartSelector = ".product-details .add_to_cart_button";
    private static final String discountedPriceSelector = ".products .price ins .amount";
    private static final String normalPriceSelector = ".products .price .amount";


    public ShopPage() {
        super(new ElementHelper(), PageType.SHOP_PAGE);
        closeAdIfPresent();
    }

    @Step("Close any displayed advertisement in Shop page")
    private void closeAdIfPresent() {
        if (adCloseButton.exists() && adCloseButton.isDisplayed()) {
            elementHelper.clickToElement(adCloseButton, "Click Close Advertisement");
            log.info("Advertisement closed successfully.");
        } else {
            log.info("No advertisement displayed.");
        }
    }

    @Step("Extracting price from product: {product}")
    private String extractPrice(SelenideElement product) {
        SelenideElement discounted = product.$(discountedPriceSelector);
        if (discounted.exists()) {
            return discounted.getText().trim();
        }
        return product.$(normalPriceSelector).getText().trim();
    }

    @Step("Get {count} products to list")
    public List<Product> getRandomProducts(int count) {
        List<SelenideElement> shuffledProducts = productItems.stream().collect(Collectors.toList());
        Collections.shuffle(shuffledProducts);

        return shuffledProducts.stream()
                .limit(count)
                .map(productElement -> {
                    String name = productElement.find(titleSelector).getText();
                    String priceText = extractPrice(productElement);
                    double price = BasePage.parsePrice(priceText);
                    Product product = new Product(name, price, 1);
                    product.logInfo("[INFO] Selected random product: " + name + " - " + price);
                    return product;
                })
                .collect(Collectors.toList());
    }

    @Step("Add {products} to cart")
    public void addProductsToCart(List<Product> products) {
        for (Product product : products) {
            for (SelenideElement item : productItems) {
                String itemName = item.find(titleSelector).getText().trim();
                if (itemName.equals(product.getName())) {
                    elementHelper.clickToElement(item.find(addToCartSelector), "Click Add To Cart button");
                    waitForAddToCartLoaderToDisappear();
                    break;
                }
            }
        }
    }

    @Step("Go to Cart Page")
    public CartPage goToCart() {
        elementHelper.clickToElement(viewCartButton, "Click View Cart button");
        return new CartPage();
    }

    @Step("Switch to {viewType} view")
    public void switchView(String viewType) {
        if (viewType.equalsIgnoreCase("grid")) {
            elementHelper.clickToElement(gridViewButton, "Grid View Button");
        } else if (viewType.equalsIgnoreCase("list")) {
            elementHelper.clickToElement(listViewButton, "List View Button");
        } else {
            throw new IllegalArgumentException("Unsupported view type: " + viewType);
        }

        waitForLoadingComplete();
        DriverUtils.waitToLoadPage();
    }

    @Step("Wait until product list loading completes")
    public void waitForLoadingComplete() {
        if (loader.exists() && loader.isDisplayed()) {
            loader.shouldNotBe(Condition.visible);
        }
    }

    private final SelenideElement sortDropdown  = $x("//select[@name='orderby']");
    private final ElementsCollection productPriceElements =
            $$x("//ul[contains(@class,'products')]/li//span[contains(@class,'price')]/span[contains(@class,'amount')]");

    @Step("Sort items by option: {option}")
    public void sortBy(String option) {
        elementHelper.selectDropdownByVisibleText(sortDropdown, option);
        waitForLoadingComplete();
    }

    @Step("Get all product prices from Shop Page")
    public List<Double> getAllProductPrices() {
        return productPriceElements.stream()
                .map(e -> BasePage.parsePrice(e.getText()))
                .collect(Collectors.toList());
    }

    @Step("Add {count} random products to cart and return them")
    public List<Product> addRandomProductsToCart(int count) {
        List<Product> randomProducts = getRandomProducts(count);
        addProductsToCart(randomProducts);
        return randomProducts;
    }
}

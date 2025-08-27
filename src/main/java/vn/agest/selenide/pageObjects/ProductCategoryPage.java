package vn.agest.selenide.pageObjects;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j;
import vn.agest.selenide.common.ConfigFileReader;
import vn.agest.selenide.common.DriverUtils;
import vn.agest.selenide.common.ElementHelper;
import vn.agest.selenide.enums.ProductCategory;
import vn.agest.selenide.model.Product;

import java.util.Random;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

@Log4j
public class ProductCategoryPage extends BasePage {

    private final ProductCategory category;
    private SelenideElement selectedProductElement;
    private Product selectedProduct;

    private final SelenideElement gridViewButton = $x("//div[contains(@class,'switch-grid')]");
    private final SelenideElement listViewButton = $x("//div[contains(@class,'switch-list')]");
    private final SelenideElement loader = $x("//div[contains(@class,'et-loader') and contains(@class,'product-ajax')]");
    private final ElementsCollection productItems = $$x("//div[contains(@class,'ajax-content')]//div[contains(@class,'content-product')]");

    private final String productDetailPath = ".//div[contains(@class,'product-details')]";
    private final String productTitlePath = ".//h2[contains(@class,'product-title')]/a";
    private final String addToCartPath = ".//div[contains(@class,'product-details')]//a[contains(@class,'add_to_cart_button')]";
    private final String priceContainerSelector = ".price";
    private final String discountedPriceSelector = "ins .amount";
    private final String normalPriceSelector = ".amount";

    public ProductCategoryPage(ProductCategory category) {
        super(new ElementHelper(), null);
        this.category = category;
    }

    @Step("Get selected product")
    public Product getSelectedProduct() {
        return selectedProduct;
    }

    @Step("Open Product Category Page")
    public void openCategoryPage() {
        String baseUrl = ConfigFileReader.getBaseUrl();
        String pageUrl = baseUrl + category.getUrlPath();
        Selenide.open(pageUrl);
        DriverUtils.waitToLoadPage();

        String actualTitle = title();
        String expectedTitle = category.getExpectedTitle();

        if (!actualTitle.equals(expectedTitle)) {
            throw new AssertionError("Product Category Page title mismatch! Expected: " + expectedTitle + ", Actual: " + actualTitle);
        }
    }

    private String getContainerXPathByLayout(String layout) {
        if (layout.equalsIgnoreCase("grid")) {
            return "//div[contains(@class,'products-grid')]//div[@class='ajax-content clearfix']/div";
        } else if (layout.equalsIgnoreCase("list")) {
            return "//div[contains(@class,'products-list')]//div[@class='ajax-content clearfix']/div";
        } else {
            throw new IllegalArgumentException("Unsupported layout: " + layout);
        }
    }

    @Step("Verify items are displayed in {layout} view")
    public boolean verifyItemsDisplayedInLayout(String layout) {
        String containerXPath = getContainerXPathByLayout(layout);
        ElementsCollection items = $$x(containerXPath);
        return !items.isEmpty();
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
            loader.shouldNotBe(visible);
        }
    }

    @Step("Select a random product and return it")
    public Product selectRandomProduct() {
        productItems.first().shouldBe(visible);
        if (productItems.isEmpty()) {
            throw new AssertionError("No products found to select.");
        }
        selectedProductElement = productItems.get(new Random().nextInt(productItems.size()));
        selectedProductElement.scrollIntoView(true);

        SelenideElement productDetails = selectedProductElement.$x(productDetailPath);
        String name = productDetails.$x(productTitlePath).getText().trim();
        String priceString = extractPrice(productDetails);
        double price = BasePage.parsePrice(priceString);

        selectedProduct = new Product(name, price, 1);
        selectedProduct.logInfo("[INFO] Selected Product");
        return selectedProduct;
    }

    @Step("Extracting price from product details")
    private String extractPrice(SelenideElement productDetails) {
        SelenideElement priceContainer = productDetails.$(priceContainerSelector);
        if (priceContainer.$(discountedPriceSelector).exists()) {
            return priceContainer.$(discountedPriceSelector).getText();
        }
        return priceContainer.$(normalPriceSelector).getText();
    }

    @Step("Click 'Add to Cart' button for selected product")
    public void clickAddToCartButton() {
        if (selectedProductElement == null) {
            throw new IllegalStateException("No product selected to add to cart.");
        }
        selectedProductElement.hover();
        SelenideElement addToCartButton = selectedProductElement.$x(addToCartPath);
        elementHelper.clickToElement(addToCartButton, "Add to Cart Button");
        waitForAddToCartLoaderToDisappear();
    }
}

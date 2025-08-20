package vn.agest.selenide.pageObjects;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j;
import vn.agest.selenide.common.ConfigFileReader;
import vn.agest.selenide.common.DriverUtils;
import vn.agest.selenide.common.ElementHelper;
import vn.agest.selenide.enums.PageType;
import vn.agest.selenide.enums.ProductCategory;
import vn.agest.selenide.model.Product;
import vn.agest.selenide.pageObjects.components.MiniCartComponent;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Log4j
public abstract class BasePage {
    protected final ElementHelper elementHelper;
    protected final PageType pageType;

    private final SelenideElement loginButton = $x("//span[contains(@class, 'flex-inline')]//span[contains(text(), 'Log in / Sign u')]");
    private final SelenideElement allDepartmentsMenu = $x("//div[@class='secondary-menu-wrapper']//span[text()='All departments']");
    private final SelenideElement productMenu = $x("//ul[@id='menu-all-departments-1']");
    private final SelenideElement viewCartButton = $x("//div[@class='header-wrapper']//div[contains(@class,'header-cart')]/a[contains(@href,'/cart')]");
    private final SelenideElement backToTopButton = $x("//div[contains(@class, 'back-top')]");
    private final SelenideElement cookieNoticeDialog = $x("//div[@id='cookie-notice']");
    private final SelenideElement popupCloseButton = $("button.pum-close:nth-child(3)");
    private final SelenideElement addToCartLoader = $x("//div[@class='et-loader']");

    private static final String categoryLinkPath = "//div[@class='secondary-menu-wrapper']//a[text()='%s']";

    protected BasePage(ElementHelper elementHelper, PageType pageType) {
        this.elementHelper = elementHelper;
        this.pageType = pageType;
    }

    @Step("Open page with defined URL and verify title")
    public void open() {
        String url = ConfigFileReader.getUrlFromPageType(pageType);
        log.info(String.format("🌐 Opening page [%s] with URL: %s", pageType.name(), url));
        Selenide.open(url);
        DriverUtils.waitToLoadPage();
        closePopupIfPresent();
        verifyPageTitle(pageType);
    }

    @Step("Close popup")
    public void closePopupIfPresent() {
        try {
            if (popupCloseButton.isDisplayed() || popupCloseButton.shouldBe(visible, Duration.ofSeconds(5)).isDisplayed()) {
                elementHelper.clickToElement(popupCloseButton, "Close Popup");
            }
        } catch (Exception ignored) {
        }
    }

    @Step("Verify page title for {pageType}")
    public void verifyPageTitle(PageType pageType) {
        String actualTitle = title().trim();
        String expectedTitle = ConfigFileReader.getTitleFromPageType(pageType).trim();

        log.info(String.format("🔍 Verifying page title for [%s] - Expected: '%s' | Actual: '%s'",
                pageType.name(), expectedTitle, actualTitle));

        if (!actualTitle.equalsIgnoreCase(expectedTitle)) {
            log.error(String.format("❌ Page title mismatch for [%s]! Expected: '%s', Actual: '%s'",
                    pageType.name(), expectedTitle, actualTitle));
            throw new AssertionError(
                    String.format("Page title mismatch! Expected: '%s', Actual: '%s'",
                            expectedTitle, actualTitle)
            );
        }

        log.info(String.format("✅ Page title verified successfully for [%s]", pageType.name()));
    }

    @Step("Navigate to Login Page")
    public void navigateToLoginPage() {
        elementHelper.clickToElement(loginButton, "Login Button");
    }

    @Step("Navigate to All Departments menu")
    public void navigateToAllDepartments() {
        elementHelper.waitForElementVisible(allDepartmentsMenu, "All Departments Menu");
        elementHelper.highlightElement(allDepartmentsMenu);
        allDepartmentsMenu.hover();
        elementHelper.waitForElementVisible(productMenu, "Product Menu");
    }

    @Step("Navigate to product category: {productCategory}")
    public ProductCategoryPage navigateToProductCategory(ProductCategory productCategory) {
        navigateToAllDepartments();
        SelenideElement categoryLink = $x(String.format(categoryLinkPath, productCategory.getDisplayName()));
        elementHelper.waitForElementVisible(categoryLink, productCategory.getDisplayName() + " Link");
        elementHelper.clickToElement(categoryLink, productCategory.getDisplayName() + " Link");
        DriverUtils.waitToLoadPage();

        return new ProductCategoryPage(productCategory);
    }

    @Step("Hover over Cart icon to open MiniCart")
    public MiniCartComponent moveToMiniCart() {
        clickBackToTop();
        elementHelper.moveToElement(viewCartButton, "View Cart Button (Cart Icon)");
        return new MiniCartComponent();
    }

    @Step("Click 'Back to Top' button")
    public void clickBackToTop() {
        dismissCookieNoticeIfPresent();
        elementHelper.waitForElementVisible(backToTopButton, "Back to Top Button");
        elementHelper.clickToElement(backToTopButton, "Back to Top Button");
    }

    @Step("Dismiss Cookie Notice if present")
    public void dismissCookieNoticeIfPresent() {
        if (cookieNoticeDialog.isDisplayed()) {
            log.info("Cookie Notice detected. Hiding it...");
            Selenide.executeJavaScript("arguments[0].style.display='none';", cookieNoticeDialog);
            cookieNoticeDialog.shouldNotBe(Condition.visible);
        }
    }

    @Step("Navigate to Shop page")
    public ShopPage navigateToShopPage() {
        SelenideElement shopLink = $x("//div[@class='header-wrapper']//a[@class='item-link' and contains(@href,'/shop')]");

        elementHelper.waitForElementClickable(shopLink, "Shop Link");
        elementHelper.clickToElement(shopLink, "Shop Link");
        return new ShopPage();
    }

    @Step("Merge product list by name and sum their quantities and prices")
    public static List<Product> mergeProductList(List<Product> products) {
        Map<String, Product> merged = new LinkedHashMap<>();
        for (Product p : products) {
            merged.merge(p.getName(), new Product(p.getName(), p.getPrice(), p.getQuantity()), (oldP, newP) -> {
                oldP.setQuantity(oldP.getQuantity() + newP.getQuantity());
                oldP.setPrice(oldP.getPrice() + newP.getPrice());
                return oldP;
            });
        }
        return new ArrayList<>(merged.values());
    }

    @Step("Wait for the Add to Cart loader icon to appear and disappear")
    public void waitForAddToCartLoaderToDisappear() {
        try {
            addToCartLoader.shouldBe(Condition.visible, Duration.ofSeconds(5));
        } catch (Exception e) {
            log.info("⚡ Add to Cart loader did not appear within 5s, possibly processed instantly.");
        }
        addToCartLoader.shouldNotBe(Condition.visible, Duration.ofSeconds(30));
    }

    @Step("Parse price string: '{priceText}' into double value")
    public static double parsePrice(String priceText) {
        try {
            String cleaned = priceText.replaceAll("[$,]", "").trim();

            return Double.parseDouble(cleaned);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Failed to parse price: " + priceText + " (cleaned: " + priceText + ")", e);
        }
    }
}

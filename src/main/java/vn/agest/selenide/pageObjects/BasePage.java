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
import vn.agest.selenide.model.Order;
import vn.agest.selenide.model.Product;
import vn.agest.selenide.pageObjects.components.MiniCartComponent;

import java.time.Duration;
import java.util.*;

@Log4j
public abstract class BasePage {
    protected final ElementHelper elementHelper;
    protected final PageType pageType;

    private final SelenideElement loginButton = $x("//span[contains(@class, 'flex-inline')]//span[contains(text(), 'Log in / Sign u')]");
    private final SelenideElement productMenu = $x("//ul[@id='menu-all-departments-1']");
    private final SelenideElement viewCartButton = $x("//div[@class='header-wrapper']//div[contains(@class,'header-cart')]/a[contains(@href,'/cart')]");
    private final SelenideElement backToTopButton = $x("//div[contains(@class, 'back-top')]");
    private final SelenideElement cookieNoticeDialog = $x("//div[@id='cookie-notice']");
    private final SelenideElement popupCloseButton = $("button.pum-close:nth-child(3)");
    private final SelenideElement addToCartLoader = $x("//div[@class='et-loader']");
    private final SelenideElement shopLink = $x("//div[@class='header-wrapper']//a[@class='item-link' and contains(@href,'/shop')]");
    private final SelenideElement myAccountLink = $x("//div[contains(@class,'header-account')]//a[contains(@href,'my-account')]");
    private final SelenideElement logOutLink = $x("//a[contains(text(),'Logout')]");
    private final SelenideElement accountLabel = $x("//div[contains(@class,'header-account')]//span[contains(@class,'et-element-label')]");
    private static final String categoryLinkPath = "//div[@class='secondary-menu-wrapper']//a[text()='%s']";
    private final String allDepartmentsMenuLocator = "//div[@class='secondary-menu-wrapper']//span[text()='All departments']";

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
        verifyPageTitle(pageType);
    }

    @Step("Close popup")
    public void closePopupIfPresent() {
        try {
            if (popupCloseButton.isDisplayed() || popupCloseButton.shouldBe(visible, Duration.ofSeconds(5)).isDisplayed()) {
                elementHelper.clickToElement(popupCloseButton, "Close Popup");
                log.info("Popup closed.");
            } else {
                log.info("Popup not visible, skip closing.");
            }
        } catch (Exception e) {
            log.info("No popup active, continue.");
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
        SelenideElement allDepartmentsMenu = $x(allDepartmentsMenuLocator);
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

    @Step("Navigate to Shop page")
    public ShopPage navigateToShopPage() {
        elementHelper.clickToElement(shopLink, "Shop Link");
        return new ShopPage();
    }

    @Step("Navigate to Login Page")
    public MyAccountPage navigateToMyAccountPage() {
        elementHelper.clickToElement(myAccountLink, "My Account Link");
        return new MyAccountPage();
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
            double num = Double.parseDouble(priceText.replaceAll("[^\\d.]", ""));
            String[] numbers = String.format("%s", num).split("\\s+");
            if (numbers.length == 0) {
                throw new RuntimeException("No numeric value found in: " + priceText);
            }

            String cleaned = numbers[numbers.length - 1];
            return Double.parseDouble(cleaned);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Failed to parse price: " + priceText, e);
        }
    }

    @Step("Place random order with {count} products")
    public Order placeRandomOrder(int count) {
        ShopPage shopPage = navigateToShopPage();
        List<Product> products = shopPage.addRandomProductsToCart(count);
        CheckoutPage checkoutPage = shopPage.goToCart().checkOut();
        return checkoutPage.placeOrderAndExtractInfo(products);
    }

    @Step("Logout if user is logged in")
    public void logOut() {
        String currentAccountText = accountLabel.getText().trim();
        if (currentAccountText.equalsIgnoreCase("Log in / Sign up")) {
            log.info("ℹ️ User is not logged in. Skipping logout.");
            return;
        }

        try {
            log.info("🔑 Account detected (" + currentAccountText + "), attempting to logout...");
            elementHelper.clickToElement(accountLabel, "Account Label (" + currentAccountText + ")");
            elementHelper.waitForElementVisible(logOutLink, "Logout Link");
            elementHelper.clickToElement(logOutLink, "Logout Link");

            DriverUtils.waitToLoadPage();
            log.info("✅ User has been logged out.");
        } catch (Exception e) {
            log.error("❌ Failed during logout: " + e.getMessage());
            throw e;
        }
    }

    public static int getRandomNumber(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }
}

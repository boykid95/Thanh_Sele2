package vn.agest.selenide.pageObjects;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import vn.agest.selenide.common.utilities.helpers.ConfigFileReader;
import vn.agest.selenide.common.utilities.helpers.ElementHelper;
import vn.agest.selenide.enums.PageType;
import vn.agest.selenide.enums.ProductCategory;
import vn.agest.selenide.model.Product;
import vn.agest.selenide.pageObjects.components.MiniCartComponent;
import vn.agest.selenide.common.utilities.other.Log;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class BasePage {

    protected final ElementHelper elementHelper = new ElementHelper();
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

    public BasePage(PageType pageType) {
        this.pageType = pageType;
    }

    @Step("Open page with defined URL and verify title")
    public void open() {
        Selenide.open(ConfigFileReader.getUrlFromPageType(pageType));
        elementHelper.waitToLoadPage();
        closePopupIfPresent();

        String actualTitle = title();
        String expectedTitle = ConfigFileReader.getTitleFromPageType(pageType);

        if (!actualTitle.equals(expectedTitle)) {
            throw new AssertionError("Page title mismatch! Expected: " + expectedTitle + ", Actual: " + actualTitle);
        }
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
        elementHelper.waitToLoadPage();

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
            Log.info("Cookie Notice detected. Hiding it...");
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

    public static List<Product> mergeProductList(List<Product> products) {
        Map<String, Product> merged = new LinkedHashMap<>();
        for (Product p : products) {
            merged.merge(
                    p.getName(),
                    new Product(p.getName(), p.getPrice(), p.getQuantity()),
                    (oldP, newP) -> {
                        oldP.setQuantity(oldP.getQuantity() + newP.getQuantity());
                        oldP.setPrice(oldP.getPrice() + newP.getPrice());
                        return oldP;
                    }
            );
        }
        return new ArrayList<>(merged.values());
    }

    public void waitForAddToCartLoaderToDisappear() {
        addToCartLoader.should(Condition.appear);
        addToCartLoader.should(Condition.exist);
    }
}

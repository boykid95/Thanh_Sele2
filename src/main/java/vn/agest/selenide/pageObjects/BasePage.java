package vn.agest.selenide.pageObjects;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import vn.agest.selenide.common.utilities.helpers.ConfigFileReader;
import vn.agest.selenide.common.utilities.helpers.ElementHelper;
import vn.agest.selenide.common.utilities.helpers.PageTitlesFileReader;
import vn.agest.selenide.enums.PageType;
import vn.agest.selenide.enums.ProductCategory;
import vn.agest.selenide.pageObjects.components.MiniCartComponent;
import vn.agest.selenide.common.utilities.other.Log;

import java.time.Duration;

public abstract class BasePage {

    protected final ElementHelper elementHelper = new ElementHelper();
    protected final PageType pageType;

    private final SelenideElement loginButton = $x("//span[contains(@class, 'flex-inline')]//span[contains(text(), 'Log in / Sign u')]");
    private final SelenideElement allDepartmentsMenu = $x("//div[@class='secondary-menu-wrapper']//span[text()='All departments']");
    private final SelenideElement productMenu = $x("//ul[@id='menu-all-departments-1']");
    private final SelenideElement viewCartButton = $x("//div[@class='header-wrapper']//div[contains(@class,'header-cart')]/a[contains(@href,'/cart')]");
    private final SelenideElement backToTopButton = $x("//div[contains(@class, 'back-top')]");
    private final SelenideElement cookieNoticeDialog = $x("//div[@id='cookie-notice']");

    public BasePage(PageType pageType) {
        this.pageType = pageType;
    }

    @Step("Open page with defined URL and verify title")
    public void open() {
        String url = ConfigFileReader.getUrlFromPageType(pageType);
        Selenide.open(url);
        elementHelper.waitToLoadPage();
        closePopupIfPresent();

        String actualTitle = title();
        String expectedTitle = PageTitlesFileReader.getTitleFromPageType(pageType);

        if (!actualTitle.equals(expectedTitle)) {
            throw new AssertionError("Page title mismatch! Expected: " + expectedTitle + ", Actual: " + actualTitle);
        }
    }

    @Step("Close popup")
    public static void closePopupIfPresent() {
        try {
            SelenideElement popupCloseButton = $("button.pum-close:nth-child(3)");
            if (popupCloseButton.isDisplayed() || popupCloseButton.shouldBe(visible, Duration.ofSeconds(5)).isDisplayed()) {
                popupCloseButton.click();
            }
        } catch (Exception ignored) {
        }
    }

    @Step("Navigate to Login Page")
    public LoginPage navigateToLoginPage() {
        elementHelper.clickToElement(loginButton, "Login Button");
        return new LoginPage();
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

        SelenideElement categoryLink = $x("//div[@class='secondary-menu-wrapper']//a[text()='" + productCategory.getDisplayName() + "']");
        elementHelper.waitForElementVisible(categoryLink, productCategory.getDisplayName() + " Link");
        elementHelper.highlightElement(categoryLink);
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
        elementHelper.waitForElementVisible(viewCartButton, "View Cart Button");
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
}

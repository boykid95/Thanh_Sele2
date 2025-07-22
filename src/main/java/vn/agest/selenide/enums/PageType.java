package vn.agest.selenide.enums;

public enum PageType {
    HOME_PAGE("homePageUrl", "homePageTitle"),
    LOGIN_PAGE("loginPageUrl", "loginPageTitle"),
    CART_PAGE("cartPageUrl", "cartPageTitle"),
    PRODUCT_PAGE("productPageUrl", "productPageTitle"),
    CHECKOUT_PAGE("checkoutPageUrl", "checkoutPageTitle"),
    ORDER_STATUS_PAGE("orderStatusPageUrl", "orderStatusPageTitle"),
    SHOP_PAGE("shopPageUrl", "shopPageTitle");

    private final String urlKey;
    private final String titleKey;

    PageType(String urlKey, String titleKey) {
        this.urlKey = urlKey;
        this.titleKey = titleKey;
    }

    public String getUrlKey() {
        return urlKey;
    }

    public String getTitleKey() {
        return titleKey;
    }
}

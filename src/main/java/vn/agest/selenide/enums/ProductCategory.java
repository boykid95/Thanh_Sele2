package vn.agest.selenide.enums;

public enum ProductCategory {

    ELECTRONIC_COMPONENTS_SUPPLIES("Electronic Components & Supplies", "product-category/electronic-components-supplies", "Electronic Components & Supplies – TestArchitect Sample Website"),
    AUTOMOBILES_MOTORCYCLES("Automobiles & Motorcycles", "product-category/automobiles-motorcycles", "Automobiles & Motorcycles – TestArchitect Sample Website");

    private final String displayName;
    private final String urlPath;
    private final String expectedTitle;

    ProductCategory(String displayName, String urlPath, String expectedTitle) {
        this.displayName = displayName;
        this.urlPath = urlPath;
        this.expectedTitle = expectedTitle;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public String getExpectedTitle() {
        return expectedTitle;
    }
}

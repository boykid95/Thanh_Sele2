package vn.agest.selenide.enums;

public enum BrowserType {
    CHROME("chrome"),
    FIREFOX("firefox"),
    EDGE("edge");

    private final String value;

    BrowserType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

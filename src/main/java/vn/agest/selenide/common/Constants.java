package vn.agest.selenide.common;

public class Constants {
    public static final String ORDER_CONFIRMATION = "THANK YOU. YOUR ORDER HAS BEEN RECEIVED.";
    public static final String PRICE_LOW_TO_HIGH = "Sort by price: low to high";
    public static final String PRICE_HIGH_TO_LOW = "Sort by price: high to low";

    // Error messages - Checkout mandatory fields
    public static final String BILLING_FIRST_NAME_REQUIRED = "Billing First name is a required field.";
    public static final String BILLING_LAST_NAME_REQUIRED = "Billing Last name is a required field.";
    public static final String BILLING_STREET_REQUIRED = "Billing Street address is a required field.";
    public static final String BILLING_CITY_REQUIRED = "Billing Town / City is a required field.";
    public static final String BILLING_POSTCODE_REQUIRED = "Billing ZIP Code is a required field.";
    public static final String BILLING_PHONE_REQUIRED = "Billing Phone is a required field.";
    public static final String BILLING_EMAIL_REQUIRED = "Billing Email address is a required field.";

    public static final String[] ALL_BILLING_ERRORS = {
            BILLING_FIRST_NAME_REQUIRED,
            BILLING_LAST_NAME_REQUIRED,
            BILLING_STREET_REQUIRED,
            BILLING_CITY_REQUIRED,
            BILLING_POSTCODE_REQUIRED,
            BILLING_PHONE_REQUIRED,
            BILLING_EMAIL_REQUIRED
    };
}

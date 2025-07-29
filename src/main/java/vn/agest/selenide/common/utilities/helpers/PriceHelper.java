package vn.agest.selenide.common.utilities.helpers;

public class PriceHelper {
    public static double parsePrice(String priceText) {
        if (priceText == null || priceText.isEmpty()) {
            throw new IllegalArgumentException("Price text cannot be null or empty");
        }

        String cleaned = priceText.replaceAll("[^\\d.,]", "").replaceAll(",", "");

        if (cleaned.isEmpty()) {
            throw new IllegalArgumentException("Invalid price format after cleaning: " + priceText);
        }

        try {
            return Double.parseDouble(cleaned);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Failed to parse price: " + priceText, e);
        }
    }
}

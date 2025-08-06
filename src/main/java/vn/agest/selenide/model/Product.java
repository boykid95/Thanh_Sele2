package vn.agest.selenide.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {
    private String name;
    private double price;
    private int quantity;

    public void logInfo(String prefix) {
        logInfo(String.format(prefix + ": %s, Price: %s, Quantity: %s", name, price, quantity));
    }
}

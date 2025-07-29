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
        System.out.println(prefix + ": " + name + ", Price: " + price + ", Quantity: " + quantity);
    }
}

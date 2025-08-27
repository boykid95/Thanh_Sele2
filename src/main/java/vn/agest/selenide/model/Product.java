package vn.agest.selenide.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
public class Product {
    private String name;
    private double price;
    private int quantity;

    public void logInfo(String prefix) {
        System.out.println(String.format(prefix + ": %s, Price: %s, Quantity: %s", name, price, quantity));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Double.compare(product.price, price) == 0 &&
                quantity == product.quantity &&
                name != null && product.name != null &&
                name.equalsIgnoreCase(product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name == null ? null : name.toLowerCase(), price, quantity);
    }
}

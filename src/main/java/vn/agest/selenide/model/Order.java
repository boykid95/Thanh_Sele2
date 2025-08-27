package vn.agest.selenide.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private String orderId;
    private double totalAmount;
    private String billingFirstName;
    private String billingLastName;
    private String billingStreet;
    private String billingCity;
    private String billingPostcode;
    private String billingCountry;
    private String billingPhone;
    private String billingEmail;
}
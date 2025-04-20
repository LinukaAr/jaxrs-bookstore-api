package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private int customerId;
    private List<CartItem> items = new ArrayList<>();

    public Cart() {
    }

    public Cart(int customerId) {
        this.customerId = customerId;
    }

    // Getters and Setters

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }

    public void addItem(CartItem item) {
        this.items.add(item);
    }
}

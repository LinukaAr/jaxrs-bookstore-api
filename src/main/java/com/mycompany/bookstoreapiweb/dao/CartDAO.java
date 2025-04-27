package com.mycompany.bookstoreapiweb.dao;

import com.mycompany.bookstoreapiweb.model.Cart;
import com.mycompany.bookstoreapiweb.model.CartItem;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CartDAO {
    private static final Map<Integer, Cart> carts = new ConcurrentHashMap<>();

    public Cart getCart(int customerId) {
        return carts.get(customerId);
    }

    public void saveCart(Cart cart) {
        carts.put(cart.getCustomerId(), cart);
    }

    public void deleteCart(int customerId) {
        carts.remove(customerId);
    }

    public void addItemToCart(int customerId, CartItem item) {
        Cart cart = carts.computeIfAbsent(customerId, Cart::new);
        cart.addItem(item);
    }

    public void removeItemFromCart(int customerId, int bookId) {
        Cart cart = carts.get(customerId);
        if (cart != null) {
            cart.getItems().removeIf(item -> item.getBookId() == bookId);
        }
    }

    public static Map<Integer, Cart> getAllCarts() {
        return carts;
    }

    public void updateCartItem(int customerId, CartItem updatedItem) {
        Cart cart = carts.get(customerId);
        if (cart != null) {
            // Find the existing item and update its quantity
            cart.getItems().removeIf(item -> item.getBookId() == updatedItem.getBookId());
            cart.addItem(updatedItem);
        }
    }
}
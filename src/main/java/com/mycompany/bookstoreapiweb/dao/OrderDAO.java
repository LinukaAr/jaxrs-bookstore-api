package com.mycompany.bookstoreapiweb.dao;

import com.mycompany.bookstoreapiweb.model.Order;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderDAO {
    private static final Map<Integer, List<Order>> ordersByCustomer = new ConcurrentHashMap<>();
    private static final AtomicInteger orderId = new AtomicInteger(1);

    public Order placeOrder(int customerId, Order order) {
        order.setId(orderId.getAndIncrement());
        ordersByCustomer.computeIfAbsent(customerId, k -> new ArrayList<>()).add(order);
        return order;
    }

    public List<Order> getOrdersByCustomer(int customerId) {
        return ordersByCustomer.getOrDefault(customerId, new ArrayList<>());
    }

    public Order getOrderById(int customerId, int orderId) {
        List<Order> orders = ordersByCustomer.getOrDefault(customerId, new ArrayList<>());
        return orders.stream()
                .filter(order -> order.getId() == orderId)
                .findFirst()
                .orElse(null);
    }
}
package com.mycompany.bookstoreapiweb.dao;

import com.mycompany.bookstoreapiweb.model.Customer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomerDAO {
    private static final Map<Integer, Customer> customers = new ConcurrentHashMap<>();
    private static final AtomicInteger customerId = new AtomicInteger(1);

    public Customer addCustomer(Customer customer) {
        customer.setId(customerId.getAndIncrement());
        customers.put(customer.getId(), customer);
        return customer;
    }

    public Collection<Customer> getAllCustomers() {
        return customers.values();
    }

    public Customer getCustomerById(int id) {
        return customers.get(id);
    }

    public Customer updateCustomer(int id, Customer customer) {
        customer.setId(id);
        customers.put(id, customer);
        return customer;
    }

    public boolean deleteCustomer(int id) {
        return customers.remove(id) != null;
    }

    public boolean customerExists(int id) {
        return customers.containsKey(id);
    }
}
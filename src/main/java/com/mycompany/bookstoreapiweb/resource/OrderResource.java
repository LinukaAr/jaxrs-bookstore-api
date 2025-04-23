package com.mycompany.bookstoreapiweb.resource;

import com.mycompany.bookstoreapiweb.exception.*;
import com.mycompany.bookstoreapiweb.model.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

@Path("/customers/{customerId}/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {
    private static final Map<Integer, List<Order>> ordersByCustomer = new ConcurrentHashMap<>();
    private static final Map<Integer, Cart> carts = CartResource.getCarts();
    private static final Map<Integer, Book> books = BookResource.getBooks();
    private static final AtomicInteger orderId = new AtomicInteger(1);
    private static final Logger logger = Logger.getLogger(OrderResource.class.getName());

    @POST
    public Response placeOrder(@PathParam("customerId") int customerId) {
        Cart cart = carts.get(customerId);
        if (cart == null || cart.getItems().isEmpty()) {
            throw new CartNotFoundException("Cart is empty or not found.");
        }

        double total = 0;
        synchronized (books) {
            for (CartItem item : cart.getItems()) {
                Book book = books.get(item.getBookId());
                if (book == null) throw new BookNotFoundException("Book not found.");
                if (book.getStock() < item.getQuantity()) throw new OutOfStockException("Book out of stock.");
                total += item.getQuantity() * book.getPrice();
                book.setStock(book.getStock() - item.getQuantity());
            }
        }

        Order order = new Order(orderId.getAndIncrement(), customerId, new ArrayList<>(cart.getItems()), total);
        ordersByCustomer.computeIfAbsent(customerId, k -> new ArrayList<>()).add(order);
        cart.getItems().clear(); // Clear cart after order

        logger.info("Order placed for customer ID: " + customerId + ", Order ID: " + order.getId());
        return Response.status(Response.Status.CREATED).entity(order).build();
    }

    @GET
    public List<Order> getOrders(@PathParam("customerId") int customerId) {
        logger.info("Fetching orders for customer ID: " + customerId);
        return ordersByCustomer.getOrDefault(customerId, new ArrayList<>());
    }

    @GET
    @Path("/{orderId}")
    public Order getOrder(@PathParam("customerId") int customerId,
                          @PathParam("orderId") int id) {
        logger.info("Fetching order ID: " + id + " for customer ID: " + customerId);
        List<Order> orders = ordersByCustomer.getOrDefault(customerId, new ArrayList<>());
        return orders.stream()
                .filter(order -> order.getId() == id)
                .findFirst()
                .orElseThrow(() -> new WebApplicationException("Order not found", 404));
    }

}
package com.mycompany.bookstoreapiweb.resource;

import com.mycompany.bookstoreapiweb.dao.OrderDAO;
import com.mycompany.bookstoreapiweb.exception.*;
import com.mycompany.bookstoreapiweb.model.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;
import java.util.logging.Logger;

@Path("/customers/{customerId}/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {
    private static final OrderDAO orderDAO = new OrderDAO();
    private static final Map<Integer, Cart> carts = CartResource.getCarts();
    private static final Map<Integer, Book> books = BookResource.getBooks();
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

        Order order = new Order(0, customerId, new ArrayList<>(cart.getItems()), total);
        order = orderDAO.placeOrder(customerId, order);
        cart.getItems().clear(); // Clear cart after order

        logger.info("Order placed for customer ID: " + customerId + ", Order ID: " + order.getId());
        return Response.status(Response.Status.CREATED).entity(order).build();
    }

    @GET
    public List<Order> getOrders(@PathParam("customerId") int customerId) {
        logger.info("Fetching orders for customer ID: " + customerId);
        return orderDAO.getOrdersByCustomer(customerId);
    }

    @GET
    @Path("/{orderId}")
    public Order getOrder(@PathParam("customerId") int customerId,
                          @PathParam("orderId") int id) {
        logger.info("Fetching order ID: " + id + " for customer ID: " + customerId);
        Order order = orderDAO.getOrderById(customerId, id);
        if (order == null) {
            throw new WebApplicationException("Order not found", 404);
        }
        return order;
    }
}
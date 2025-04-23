package com.mycompany.bookstoreapiweb.resource;

import com.mycompany.bookstoreapiweb.exception.*;
import com.mycompany.bookstoreapiweb.model.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Path("/customers/{customerId}/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource {
    private static final Map<Integer, Cart> carts = new ConcurrentHashMap<>();
    private static final Map<Integer, Book> books = BookResource.getBooks(); // Access book map
    private static final Logger logger = Logger.getLogger(CartResource.class.getName());

    @POST
    @Path("/items")
    public Response addItem(@PathParam("customerId") int customerId, CartItem item) {
        validateCartItem(item);

        Book book = books.get(item.getBookId());
        if (book == null) throw new BookNotFoundException("Book not found.");
        if (book.getStock() < item.getQuantity()) throw new OutOfStockException("Not enough stock.");

        Cart cart = carts.computeIfAbsent(customerId, Cart::new);
        cart.addItem(item);

        // Update stock
        synchronized (book) {
            book.setStock(book.getStock() - item.getQuantity());
        }

        logger.info("Item added to cart for customer ID: " + customerId);
        return Response.status(Response.Status.CREATED).entity(cart).build();
    }

    @GET
    public Cart getCart(@PathParam("customerId") int customerId) {
        Cart cart = carts.get(customerId);
        if (cart == null) throw new CartNotFoundException("Cart not found.");
        logger.info("Fetching cart for customer ID: " + customerId);
        return cart;
    }

    @PUT
    @Path("/items/{bookId}")
    public Cart updateItem(@PathParam("customerId") int customerId,
                           @PathParam("bookId") int bookId,
                           CartItem updatedItem) {
        validateCartItem(updatedItem);

        Cart cart = carts.get(customerId);
        if (cart == null) throw new CartNotFoundException("Cart not found.");

        for (CartItem item : cart.getItems()) {
            if (item.getBookId() == bookId) {
                Book book = books.get(bookId);
                if (book == null) throw new BookNotFoundException("Book not found.");

                // Adjust stock
                synchronized (book) {
                    int stockAdjustment = updatedItem.getQuantity() - item.getQuantity();
                    if (book.getStock() < stockAdjustment) throw new OutOfStockException("Not enough stock.");
                    book.setStock(book.getStock() - stockAdjustment);
                }

                item.setQuantity(updatedItem.getQuantity());
                logger.info("Item updated in cart for customer ID: " + customerId);
                return cart;
            }
        }

        throw new BookNotFoundException("Book not found in cart.");
    }

    @DELETE
    @Path("/items/{bookId}")
    public Cart removeItem(@PathParam("customerId") int customerId,
                           @PathParam("bookId") int bookId) {
        Cart cart = carts.get(customerId);
        if (cart == null) throw new CartNotFoundException("Cart not found.");

        Book book = books.get(bookId);
        if (book == null) throw new BookNotFoundException("Book not found.");

        cart.getItems().removeIf(item -> {
            if (item.getBookId() == bookId) {
                // Restore stock
                synchronized (book) {
                    book.setStock(book.getStock() + item.getQuantity());
                }
                return true;
            }
            return false;
        });

        logger.info("Item removed from cart for customer ID: " + customerId);
        return cart;
    }

    private void validateCartItem(CartItem item) {
        if (item.getBookId() <= 0) {
            throw new InvalidInputException("Invalid book ID.");
        }
        if (item.getQuantity() <= 0) {
            throw new InvalidInputException("Quantity must be greater than 0.");
        }
    }

    public static Map<Integer, Cart> getCarts() {
        return carts;
    }
}
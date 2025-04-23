package com.mycompany.bookstoreapiweb.resource;

import com.mycompany.bookstoreapiweb.exception.*;
import com.mycompany.bookstoreapiweb.model.*;

import javax.ws.rs.*;              
import javax.ws.rs.core.*;        
import java.util.*;

@Path("/customers/{customerId}/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource {
    private static final Map<Integer, Cart> carts = new HashMap<>();
    private static final Map<Integer, Book> books = BookResource.getBooks(); // Access book map

    @POST
    @Path("/items")
    public Response addItem(@PathParam("customerId") int customerId, CartItem item) {
        Book book = books.get(item.getBookId());
        if (book == null) throw new BookNotFoundException("Book not found.");
        if (book.getStock() < item.getQuantity()) throw new OutOfStockException("Not enough stock.");

        Cart cart = carts.computeIfAbsent(customerId, Cart::new);
        cart.addItem(item);
        return Response.status(Response.Status.CREATED).entity(cart).build();
    }

    @GET
    public Cart getCart(@PathParam("customerId") int customerId) {
        Cart cart = carts.get(customerId);
        if (cart == null) throw new CartNotFoundException("Cart not found.");
        return cart;
    }

    @PUT
    @Path("/items/{bookId}")
    public Cart updateItem(@PathParam("customerId") int customerId,
                           @PathParam("bookId") int bookId,
                           CartItem updatedItem) {
        Cart cart = carts.get(customerId);
        if (cart == null) throw new CartNotFoundException("Cart not found.");
        for (CartItem item : cart.getItems()) {
            if (item.getBookId() == bookId) {
                item.setQuantity(updatedItem.getQuantity());
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
        cart.getItems().removeIf(item -> item.getBookId() == bookId);
        return cart;
    }

    public static Map<Integer, Cart> getCarts() {
        return carts;
    }
}

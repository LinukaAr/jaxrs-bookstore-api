package com.mycompany.bookstoreapiweb.resource;

import com.mycompany.bookstoreapiweb.dao.BookDAO;
import com.mycompany.bookstoreapiweb.dao.CartDAO;
import com.mycompany.bookstoreapiweb.exception.*;
import com.mycompany.bookstoreapiweb.model.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;
import java.util.logging.Logger;

@Path("/customers/{customerId}/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource {
    private static final CartDAO cartDAO = new CartDAO();
    private static final Map<Integer, Book> books = BookResource.getBooks(); // Access book map
    private static final Logger logger = Logger.getLogger(CartResource.class.getName());

    private static final Map<Integer, Cart> carts = new HashMap<>();

    public static Map<Integer, Cart> getCarts() {
        return carts;
    }

    @POST
    @Path("/items")
    public Response addItem(@PathParam("customerId") int customerId, CartItem item) {
        validateCartItem(item);

        // Fetch the book dynamically from the database
        Book book = new BookDAO().getBookById(item.getBookId());
        if (book == null) throw new BookNotFoundException("Book not found.");
        if (book.getStock() < item.getQuantity()) throw new OutOfStockException("Not enough stock.");

        cartDAO.addItemToCart(customerId, item);

        // Update stock
        synchronized (book) {
            book.setStock(book.getStock() - item.getQuantity());
        }

        logger.info("Item added to cart for customer ID: " + customerId);
        return Response.status(Response.Status.CREATED).entity(cartDAO.getCart(customerId)).build();
    }

    @GET
    public Cart getCart(@PathParam("customerId") int customerId) {
        Cart cart = cartDAO.getCart(customerId);
        if (cart == null) throw new CartNotFoundException("Cart not found.");
        logger.info("Fetching cart for customer ID: " + customerId);
        return cart;
    }

    @DELETE
    @Path("/items/{bookId}")
    public Cart removeItem(@PathParam("customerId") int customerId,
                           @PathParam("bookId") int bookId) {
        Cart cart = cartDAO.getCart(customerId);
        if (cart == null) throw new CartNotFoundException("Cart not found.");

        Book book = books.get(bookId);
        if (book == null) throw new BookNotFoundException("Book not found.");

        cartDAO.removeItemFromCart(customerId, bookId);

        // Restore stock
        synchronized (book) {
            book.setStock(book.getStock() + cart.getItems().stream()
                    .filter(item -> item.getBookId() == bookId)
                    .mapToInt(CartItem::getQuantity)
                    .sum());
        }

        logger.info("Item removed from cart for customer ID: " + customerId);
        return cartDAO.getCart(customerId);
    }

    @PUT
    @Path("/items/{bookId}")
    public Response updateCartItem(@PathParam("customerId") int customerId,
                                @PathParam("bookId") int bookId,
                                CartItem updatedItem) {
        validateCartItem(updatedItem);

        // Fetch the cart dynamically from CartDAO
        Cart cart = cartDAO.getCart(customerId);
        if (cart == null) {
            throw new CartNotFoundException("Cart not found for customer ID: " + customerId);
        }

        // Fetch the book dynamically from BookDAO
        Book book = new BookDAO().getBookById(bookId);
        if (book == null) {
            throw new BookNotFoundException("Book not found.");
        }

        // Check if the book exists in the cart
        Optional<CartItem> existingItemOpt = cart.getItems().stream()
                .filter(item -> item.getBookId() == bookId)
                .findFirst();

        if (!existingItemOpt.isPresent()) {
            throw new InvalidInputException("Book not found in the cart.");
        }

        CartItem existingItem = existingItemOpt.get();

        // Calculate stock adjustment
        int stockAdjustment = updatedItem.getQuantity() - existingItem.getQuantity();
        if (book.getStock() < stockAdjustment) {
            throw new OutOfStockException("Not enough stock available.");
        }

        // Update the cart item quantity
        existingItem.setQuantity(updatedItem.getQuantity());
        cartDAO.updateCartItem(customerId, existingItem);

        // Update the stock
        synchronized (book) {
            book.setStock(book.getStock() - stockAdjustment);
            new BookDAO().updateBookStock(bookId, book.getStock()); // Persist stock changes
        }

        logger.info("Cart item updated for customer ID: " + customerId + ", book ID: " + bookId);
        return Response.ok(cartDAO.getCart(customerId)).build();
    }

    private void validateCartItem(CartItem item) {
        if (item.getBookId() <= 0) {
            throw new InvalidInputException("Invalid book ID.");
        }
        if (item.getQuantity() <= 0) {
            throw new InvalidInputException("Quantity must be greater than 0.");
        }
    }
}
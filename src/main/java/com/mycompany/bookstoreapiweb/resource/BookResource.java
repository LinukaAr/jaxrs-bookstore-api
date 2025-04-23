package com.mycompany.bookstoreapiweb.resource;

import com.mycompany.bookstoreapiweb.model.Book;
import com.mycompany.bookstoreapiweb.exception.BookNotFoundException;
import com.mycompany.bookstoreapiweb.exception.InvalidInputException;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
    private static final Map<Integer, Book> books = new ConcurrentHashMap<>();
    private static final AtomicInteger bookId = new AtomicInteger(1);
    private static final Logger logger = Logger.getLogger(BookResource.class.getName());

    public static Map<Integer, Book> getBooks() {
        return books;
    }

    @POST
    public Response add(Book book) {
        validateBook(book);
        book.setId(bookId.getAndIncrement());
        books.put(book.getId(), book);
        logger.info("Book added: " + book);
        return Response.status(Response.Status.CREATED).entity(book).build();
    }

    @GET
    public Collection<Book> getAll() {
        logger.info("Fetching all books");
        return books.values();
    }

    @GET
    @Path("/{id}")
    public Book get(@PathParam("id") int id) {
        logger.info("Fetching book with ID: " + id);
        Book book = books.get(id);
        if (book == null) {
            throw new BookNotFoundException("Book ID " + id + " not found");
        }
        return book;
    }

    @PUT
    @Path("/{id}")
    public Book update(@PathParam("id") int id, Book book) {
        validateBook(book);
        logger.info("Updating book with ID: " + id);
        if (!books.containsKey(id)) {
            throw new BookNotFoundException("Book ID " + id + " not found");
        }
        book.setId(id);
        books.put(id, book);
        return book;
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        logger.info("Deleting book with ID: " + id);
        if (books.remove(id) == null) {
            throw new BookNotFoundException("Book ID " + id + " not found");
        }
        return Response.noContent().build();
    }

    // This method is used to validate the book object before adding or updating it.
    private void validateBook(Book book) {
        if (book.getTitle() == null || book.getTitle().isEmpty()) {
            throw new InvalidInputException("Book title cannot be null or empty");
        }
        if (book.getIsbn() == null || book.getIsbn().isEmpty()) {
            throw new InvalidInputException("Book ISBN cannot be null or empty");
        }
        if (book.getPrice() <= 0) {
            throw new InvalidInputException("Book price must be greater than 0");
        }
        if (book.getStock() < 0) {
            throw new InvalidInputException("Book stock cannot be negative");
        }
    }
}
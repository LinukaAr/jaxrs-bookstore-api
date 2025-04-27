package com.mycompany.bookstoreapiweb.resource;

import com.mycompany.bookstoreapiweb.dao.AuthorDAO;
import com.mycompany.bookstoreapiweb.dao.BookDAO;
import com.mycompany.bookstoreapiweb.exception.*;
import com.mycompany.bookstoreapiweb.model.Book;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;
import java.util.logging.Logger;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
    private static final BookDAO bookDAO = new BookDAO();
    private static final AuthorDAO authorDAO = new AuthorDAO(); // Add AuthorDAO for validation
    private static final Logger logger = Logger.getLogger(BookResource.class.getName());

    public static Map<Integer, Book> getBooks() {
        Map<Integer, Book> bookMap = new HashMap<>();
        for (Book book : bookDAO.getAllBooks()) {
            bookMap.put(book.getId(), book);
        }
        return bookMap;
    }
    
    @POST
    public Response add(Book book) {
        validateBook(book);
        validateAuthorId(book.getAuthorId()); // Validate authorId
        Book createdBook = bookDAO.addBook(book);
        logger.info("Book added: " + createdBook);
        return Response.status(Response.Status.CREATED).entity(createdBook).build();
    }

    @GET
    public Collection<Book> getAll() {
        logger.info("Fetching all books");
        return bookDAO.getAllBooks();
    }

    @GET
    @Path("/{id}")
    public Book get(@PathParam("id") int id) {
        logger.info("Fetching book with ID: " + id);
        Book book = bookDAO.getBookById(id);
        if (book == null) {
            throw new BookNotFoundException("Book ID " + id + " not found");
        }
        return book;
    }

    @PUT
    @Path("/{id}")
    public Book update(@PathParam("id") int id, Book book) {
        validateBook(book);
        validateAuthorId(book.getAuthorId()); // Validate authorId
        logger.info("Updating book with ID: " + id);
        if (!bookDAO.bookExists(id)) {
            throw new BookNotFoundException("Book ID " + id + " not found");
        }
        return bookDAO.updateBook(id, book);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        logger.info("Deleting book with ID: " + id);
        if (!bookDAO.deleteBook(id)) {
            throw new BookNotFoundException("Book ID " + id + " not found");
        }
        return Response.noContent().build();
    }

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
        if (book.getPublicationYear() > Calendar.getInstance().get(Calendar.YEAR)) {
            throw new InvalidInputException("Publication year cannot be in the future.");
        }
    }

    private void validateAuthorId(int authorId) {
        if (!authorDAO.authorExists(authorId)) {
            throw new AuthorNotFoundException("Author with ID " + authorId + " does not exist.");
        }
    }
}
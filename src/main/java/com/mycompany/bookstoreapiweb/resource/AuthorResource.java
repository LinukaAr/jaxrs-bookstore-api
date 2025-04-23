package com.mycompany.bookstoreapiweb.resource;

import com.mycompany.bookstoreapiweb.dao.AuthorDAO;
import com.mycompany.bookstoreapiweb.exception.*;
import com.mycompany.bookstoreapiweb.model.Author;
import com.mycompany.bookstoreapiweb.model.Book;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {
    private static final AuthorDAO authorDAO = new AuthorDAO();
    private static final Map<Integer, Book> books = new ConcurrentHashMap<>();
    private static final Logger logger = Logger.getLogger(AuthorResource.class.getName());

    @POST
    public Response add(Author author) {
        validateAuthor(author);
        Author createdAuthor = authorDAO.addAuthor(author);
        logger.info("Author added: " + createdAuthor);
        return Response.status(Response.Status.CREATED).entity(createdAuthor).build();
    }

    @GET
    public Collection<Author> getAll() {
        logger.info("Fetching all authors");
        return authorDAO.getAllAuthors();
    }

    @GET
    @Path("/{id}")
    public Author get(@PathParam("id") int id) {
        logger.info("Fetching author with ID: " + id);
        Author author = authorDAO.getAuthorById(id);
        if (author == null) {
            throw new AuthorNotFoundException("Author ID " + id + " not found");
        }
        return author;
    }

    @PUT
    @Path("/{id}")
    public Author update(@PathParam("id") int id, Author author) {
        validateAuthor(author);
        logger.info("Updating author with ID: " + id);
        if (!authorDAO.authorExists(id)) {
            throw new AuthorNotFoundException("Author ID " + id + " not found");
        }
        return authorDAO.updateAuthor(id, author);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        logger.info("Deleting author with ID: " + id);
        if (!authorDAO.deleteAuthor(id)) {
            throw new AuthorNotFoundException("Author ID " + id + " not found");
        }
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/books")
    public List<Book> getBooksByAuthor(@PathParam("id") int id) {
        logger.info("Fetching books for author with ID: " + id);
        List<Book> booksByAuthor = books.values().stream()
            .filter(book -> book.getAuthorId() == id)
            .toList();

        if (booksByAuthor.isEmpty()) {
            throw new AuthorNotFoundException("No books found for Author ID " + id);
        }
        return booksByAuthor;
    }

    private void validateAuthor(Author author) {
        if (author.getFName() == null || author.getFName().isEmpty() || author.getLName() == null || author.getLName().isEmpty()) {
            throw new InvalidInputException("Author name cannot be null or empty");
        }
    }
}
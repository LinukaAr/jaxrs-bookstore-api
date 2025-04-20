package org.example.resource;

import org.example.model.Author;
import org.example.exception.AuthorNotFoundException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.example.model.Book;

import java.util.*;

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {
    private static final Map<Integer, Author> authors = new HashMap<>();
    private static final Map<Integer, Book> books = new HashMap<>();
    private static int authorId = 1;

    @POST
    public Response add(Author author) {
        author.setId(authorId++);
        authors.put(author.getId(), author);
        return Response.status(Response.Status.CREATED).entity(author).build();
    }

    @GET
    public Collection<Author> getAll() {
        return authors.values();
    }

    @GET
    @Path("/{id}")
    public Author get(@PathParam("id") int id) {
        Author a = authors.get(id);
        if (a == null) throw new AuthorNotFoundException("Author ID " + id + " not found");
        return a;
    }

    @PUT
    @Path("/{id}")
    public Author update(@PathParam("id") int id, Author author) {
        if (!authors.containsKey(id)) throw new AuthorNotFoundException("Author ID " + id + " not found");
        author.setId(id);
        authors.put(id, author);
        return author;
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        if (authors.remove(id) == null) throw new AuthorNotFoundException("Author ID " + id + " not found");
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/books")
    public List<Book> getBooksByAuthor(@PathParam("id") int id) {
        List<Book> booksByAuthor = books.values().stream()
            .filter(book -> book.getAuthorId() == id) // Use 'id' instead of 'authorId'
            .toList();

        if (booksByAuthor.isEmpty()) {
            throw new AuthorNotFoundException("No books found for Author ID " + id); // Use 'id' here as well
        }
        return booksByAuthor;
    }
}

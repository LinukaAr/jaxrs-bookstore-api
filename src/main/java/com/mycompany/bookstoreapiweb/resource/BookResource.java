package com.mycompany.bookstoreapiweb.resource;

import com.mycompany.bookstoreapiweb.model.Book;
import com.mycompany.bookstoreapiweb.exception.BookNotFoundException;

import javax.ws.rs.*;              
import javax.ws.rs.core.*;          

import java.util.*;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
    private static final Map<Integer, Book> books = new HashMap<>();
    private static int bookId = 1;

    public static Map<Integer, Book> getBooks() {
        return books;
    }

    @POST
    public Response add(Book book) {
        book.setId(bookId++);
        books.put(book.getId(), book);
        return Response.status(Response.Status.CREATED).entity(book).build();
    }

    @GET
    public Collection<Book> getAll() {
        return books.values();
    }

    @GET
    @Path("/{id}")
    public Book get(@PathParam("id") int id) {
        Book b = books.get(id);
        if (b == null) throw new BookNotFoundException("Book ID " + id + " not found");
        return b;
    }

    @PUT
    @Path("/{id}")
    public Book update(@PathParam("id") int id, Book book) {
        if (!books.containsKey(id)) throw new BookNotFoundException("Book ID " + id + " not found");
        book.setId(id);
        books.put(id, book);
        return book;
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        if (books.remove(id) == null) throw new BookNotFoundException("Book ID " + id + " not found");
        return Response.noContent().build();
    }
}

package com.mycompany.bookstoreapiweb.exception.mapper;

import com.mycompany.bookstoreapiweb.exception.BookNotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class BookNotFoundMapper implements ExceptionMapper<BookNotFoundException> {
    @Override
    public Response toResponse(BookNotFoundException ex) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", "Book Not Found", "message", ex.getMessage()))
                .build();
    }
}

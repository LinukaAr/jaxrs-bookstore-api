package org.example.exception.mapper;

import org.example.exception.BookNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
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

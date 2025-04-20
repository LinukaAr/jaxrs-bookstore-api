package org.example.exception.mapper;

import org.example.exception.AuthorNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class AuthorNotFoundMapper implements ExceptionMapper<AuthorNotFoundException> {
    @Override
    public Response toResponse(AuthorNotFoundException ex) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", "Author Not Found", "message", ex.getMessage()))
                .build();
    }
}

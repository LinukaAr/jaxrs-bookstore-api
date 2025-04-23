package com.mycompany.bookstoreapiweb.exception.mapper;

import com.mycompany.bookstoreapiweb.exception.AuthorNotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
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

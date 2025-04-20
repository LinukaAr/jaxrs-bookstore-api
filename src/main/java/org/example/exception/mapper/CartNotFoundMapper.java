package org.example.exception.mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.example.exception.CartNotFoundException;

import java.util.Map;

@Provider
public class CartNotFoundMapper implements ExceptionMapper<CartNotFoundException> {
    @Override
    public Response toResponse(CartNotFoundException ex) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", "Cart Not Found", "message", ex.getMessage()))
                .build();
    }
}

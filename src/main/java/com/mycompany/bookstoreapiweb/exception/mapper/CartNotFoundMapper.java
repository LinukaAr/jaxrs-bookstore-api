package com.mycompany.bookstoreapiweb.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import com.mycompany.bookstoreapiweb.exception.CartNotFoundException;

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

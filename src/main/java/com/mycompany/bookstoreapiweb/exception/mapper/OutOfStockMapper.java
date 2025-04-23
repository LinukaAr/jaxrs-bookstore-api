package com.mycompany.bookstoreapiweb.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import com.mycompany.bookstoreapiweb.exception.OutOfStockException;

import java.util.Map;

@Provider
public class OutOfStockMapper implements ExceptionMapper<OutOfStockException> {
    @Override
    public Response toResponse(OutOfStockException ex) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", "Out of Stock", "message", ex.getMessage()))
                .build();
    }
}

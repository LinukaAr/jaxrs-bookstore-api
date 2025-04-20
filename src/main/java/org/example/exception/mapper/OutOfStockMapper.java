package org.example.exception.mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.example.exception.OutOfStockException;

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

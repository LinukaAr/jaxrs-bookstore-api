package org.example.exception.mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.example.exception.InvalidInputException;

import java.util.Map;

@Provider
public class InvalidInputMapper implements ExceptionMapper<InvalidInputException> {
    @Override
    public Response toResponse(InvalidInputException ex) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", "Invalid Input", "message", ex.getMessage()))
                .build();
    }
}

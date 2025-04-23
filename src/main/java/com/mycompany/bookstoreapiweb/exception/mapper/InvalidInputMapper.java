package com.mycompany.bookstoreapiweb.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import com.mycompany.bookstoreapiweb.exception.InvalidInputException;

import java.util.Map;

@Provider
public class InvalidInputMapper implements ExceptionMapper<InvalidInputException> {
    @Override
    public Response toResponse(InvalidInputException ex) {
        return Response.status(Response.Status.BAD_REQUEST) // Changed to 400
                .entity(Map.of("error", "Invalid Input", "message", ex.getMessage()))
                .build();
    }
}
package org.example.exception.mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.example.exception.CustomerNotFoundException;

import java.util.Map;

@Provider
public class CustomerNotFoundMapper implements ExceptionMapper<CustomerNotFoundException> {
    @Override
    public Response toResponse(CustomerNotFoundException ex) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", "Customer Not Found", "message", ex.getMessage()))
                .build();
    }
}

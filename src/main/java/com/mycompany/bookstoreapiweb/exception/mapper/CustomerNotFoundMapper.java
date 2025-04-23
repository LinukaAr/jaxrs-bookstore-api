package com.mycompany.bookstoreapiweb.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import com.mycompany.bookstoreapiweb.exception.CustomerNotFoundException;

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

package org.example.resource;

import org.example.exception.CustomerNotFoundException;
import org.example.model.Customer;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.*;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {
    private static final Map<Integer, Customer> customers = new HashMap<>();
    private static int customerId = 1;

    @POST
    public Response addCustomer(Customer customer) {
        customer.setId(customerId++);
        customers.put(customer.getId(), customer);
        return Response.status(Response.Status.CREATED).entity(customer).build();
    }

    @GET
    public Collection<Customer> getAllCustomers() {
        return customers.values();
    }

    @GET
    @Path("/{id}")
    public Customer getCustomer(@PathParam("id") int id) {
        Customer customer = customers.get(id);
        if (customer == null) throw new CustomerNotFoundException("Customer ID " + id + " not found.");
        return customer;
    }

    @PUT
    @Path("/{id}")
    public Customer updateCustomer(@PathParam("id") int id, Customer customer) {
        if (!customers.containsKey(id)) throw new CustomerNotFoundException("Customer ID " + id + " not found.");
        customer.setId(id);
        customers.put(id, customer);
        return customer;
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") int id) {
        if (customers.remove(id) == null) throw new CustomerNotFoundException("Customer ID " + id + " not found.");
        return Response.noContent().build();
    }
}

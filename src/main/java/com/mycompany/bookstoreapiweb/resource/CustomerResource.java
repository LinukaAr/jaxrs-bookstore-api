package com.mycompany.bookstoreapiweb.resource;

import com.mycompany.bookstoreapiweb.exception.CustomerNotFoundException;
import com.mycompany.bookstoreapiweb.model.Customer;

import javax.ws.rs.*;              
import javax.ws.rs.core.*;        
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

package com.mycompany.bookstoreapiweb.resource;

import com.mycompany.bookstoreapiweb.exception.CustomerNotFoundException;
import com.mycompany.bookstoreapiweb.exception.InvalidInputException;
import com.mycompany.bookstoreapiweb.model.Customer;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {
    private static final Map<Integer, Customer> customers = new ConcurrentHashMap<>();
    private static final AtomicInteger customerId = new AtomicInteger(1);
    private static final Logger logger = Logger.getLogger(CustomerResource.class.getName());

    @POST
    public Response addCustomer(Customer customer) {
        validateCustomer(customer);
        customer.setId(customerId.getAndIncrement());
        customers.put(customer.getId(), customer);
        logger.info("Customer added: " + customer);
        return Response.status(Response.Status.CREATED).entity(customer).build();
    }

    @GET
    public Collection<Customer> getAllCustomers() {
        logger.info("Fetching all customers");
        return customers.values();
    }

    @GET
    @Path("/{id}")
    public Customer getCustomer(@PathParam("id") int id) {
        logger.info("Fetching customer with ID: " + id);
        Customer customer = customers.get(id);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer ID " + id + " not found.");
        }
        return customer;
    }

    @PUT
    @Path("/{id}")
    public Customer updateCustomer(@PathParam("id") int id, Customer customer) {
        validateCustomer(customer);
        logger.info("Updating customer with ID: " + id);
        if (!customers.containsKey(id)) {
            throw new CustomerNotFoundException("Customer ID " + id + " not found.");
        }
        customer.setId(id);
        customers.put(id, customer);
        return customer;
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") int id) {
        logger.info("Deleting customer with ID: " + id);
        if (customers.remove(id) == null) {
            throw new CustomerNotFoundException("Customer ID " + id + " not found.");
        }
        return Response.noContent().build();
    }

    private void validateCustomer(Customer customer) {
        if (customer.getFName() == null || customer.getFName().isEmpty() || customer.getLName() == null || customer.getLName().isEmpty()) {
            throw new InvalidInputException("Customer first name and last name cannot be null or empty");
        }
        if (customer.getEmail() == null || customer.getEmail().isEmpty()) {
            throw new InvalidInputException("Customer email cannot be null or empty");
        }
    }
}
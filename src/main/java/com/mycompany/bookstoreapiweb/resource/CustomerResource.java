package com.mycompany.bookstoreapiweb.resource;

import com.mycompany.bookstoreapiweb.dao.CustomerDAO;
import com.mycompany.bookstoreapiweb.exception.*;
import com.mycompany.bookstoreapiweb.model.Customer;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;
import java.util.logging.Logger;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {
    private static final CustomerDAO customerDAO = new CustomerDAO();
    private static final Logger logger = Logger.getLogger(CustomerResource.class.getName());

    @POST
    public Response addCustomer(Customer customer) {
        validateCustomer(customer);
        Customer createdCustomer = customerDAO.addCustomer(customer);
        logger.info("Customer added: " + createdCustomer);
        return Response.status(Response.Status.CREATED).entity(createdCustomer).build();
    }

    @GET
    public Collection<Customer> getAllCustomers() {
        logger.info("Fetching all customers");
        return customerDAO.getAllCustomers();
    }

    @GET
    @Path("/{id}")
    public Customer getCustomer(@PathParam("id") int id) {
        logger.info("Fetching customer with ID: " + id);
        Customer customer = customerDAO.getCustomerById(id);
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
        if (!customerDAO.customerExists(id)) {
            throw new CustomerNotFoundException("Customer ID " + id + " not found.");
        }
        return customerDAO.updateCustomer(id, customer);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") int id) {
        logger.info("Deleting customer with ID: " + id);
        if (!customerDAO.deleteCustomer(id)) {
            throw new CustomerNotFoundException("Customer ID " + id + " not found.");
        }
        return Response.noContent().build();
    }

    private void validateCustomer(Customer customer) {
        if (customer.getFName() == null || customer.getFName().isEmpty() || 
            customer.getLName() == null || customer.getLName().isEmpty()) {
            throw new InvalidInputException("Customer first name and last name cannot be null or empty");
        }
        if (customer.getEmail() == null || customer.getEmail().isEmpty()) {
            throw new InvalidInputException("Customer email cannot be null or empty");
        }
        if (!customer.getEmail().matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new InvalidInputException("Customer email format is invalid");
        }
    }
}
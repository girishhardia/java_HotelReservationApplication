package service;

import model.Customer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Service class for managing customers.
 */
public class CustomerService {
    private static final CustomerService INSTANCE = new CustomerService();
    
    private final Map<String, Customer> customers = new HashMap<>();

    private CustomerService() {}

    public static CustomerService getInstance() {
        return INSTANCE;
    }

    public void addCustomer(String email, String firstName, String lastName) {
        Customer customer = new Customer(firstName, lastName, email);
        customers.put(email, customer);
    }

    public Customer getCustomer(String customerEmail) {
        return customers.get(customerEmail);
    }

    public Collection<Customer> getAllCustomers() {
        return customers.values();
    }
}

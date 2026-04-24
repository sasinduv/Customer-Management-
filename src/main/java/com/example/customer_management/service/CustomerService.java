package com.example.customer_management.service;

import com.example.customer_management.entity.Customer;
import java.util.List;

public interface CustomerService {

        Customer saveCustomer(Customer customer);
        List<Customer> getAllCustomers();
    
}

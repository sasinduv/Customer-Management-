package com.example.customer_management.impl;

import com.example.customer_management.entity.Customer;
import com.example.customer_management.repository.CustomerRepository;
import com.example.customer_management.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    if (customerRepository.existsByNic(requestDto.getNic())) {
    throw new RuntimeException("NIC already exists");
}
    
}

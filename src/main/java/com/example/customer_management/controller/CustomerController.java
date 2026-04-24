package com.example.customer_management.controller;

import com.example.customer_management.entity.Customer;
import com.example.customer_management.repository.CustomerRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/customers")
@CrossOrigin("*")
public class CustomerController {
    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping("/test")
    public String test() {
        return "Backend working!";
    }

    @PostMapping
    public Customer save(@RequestBody Customer customer) {
        return customerRepository.save(customer);
    }

    @GetMapping
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }
    
}

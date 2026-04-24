package com.example.customer_management.controller;

import com.example.customer_management.entity.Customer;
import com.example.customer_management.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.validation.Valid;
@RestController
@RequestMapping("/api/customers")
@CrossOrigin("*")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping
    public Customer save(@RequestBody @Valid Customer customer) {
        return service.saveCustomer(customer);
    }

    @GetMapping
    public List<Customer> getAll() {
        return service.getAllCustomers();
    }

    @GetMapping("/test")
    public String test() {
        return "Service layer working!";
    }
}

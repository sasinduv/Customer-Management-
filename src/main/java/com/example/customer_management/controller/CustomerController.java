package com.example.customer_management.controller;

import com.example.customer_management.dto.CustomerRequestDto;
import com.example.customer_management.dto.CustomerResponseDto;
import com.example.customer_management.dto.BulkUploadResultDto;
import com.example.customer_management.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import javax.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin("*")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public CustomerResponseDto createCustomer(
            @Valid @RequestBody CustomerRequestDto requestDto) {
        return customerService.createCustomer(requestDto);
    }

    @PutMapping("/{id}")
    public CustomerResponseDto updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequestDto requestDto) {
        return customerService.updateCustomer(id, requestDto);
    }

    @GetMapping("/{id}")
    public CustomerResponseDto getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    @GetMapping
    public List<CustomerResponseDto> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @PostMapping("/bulk-upload")
    public BulkUploadResultDto bulkUpload(
        @RequestParam("file") MultipartFile file) {
        return customerService.bulkUploadCustomers(file);
    }
}
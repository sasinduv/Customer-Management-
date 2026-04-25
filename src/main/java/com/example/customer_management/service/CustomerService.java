package com.example.customer_management.service;

import com.example.customer_management.dto.CustomerRequestDto;
import com.example.customer_management.dto.CustomerResponseDto;

import java.util.List;

public interface CustomerService {

    CustomerResponseDto createCustomer(CustomerRequestDto requestDto);

    CustomerResponseDto getCustomerById(Long id);

    CustomerResponseDto updateCustomer(Long id, CustomerRequestDto requestDto);

    List<CustomerResponseDto> getAllCustomers();

    String bulkUploadCustomers();
}

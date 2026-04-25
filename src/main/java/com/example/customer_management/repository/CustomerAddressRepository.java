package com.example.customer_management.repository;

import com.example.customer_management.entity.CustomerAddress;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Long> {
    List<CustomerAddress> findByCustomerId(Long customerId);
    
}

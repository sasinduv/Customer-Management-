package com.example.customer_management.repository;

import com.example.customer_management.entity.CustomerMobile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerMobileRepository extends JpaRepository<CustomerMobile, Long> {
    List<CustomerMobile> findByCustomerId(Long customerId);
    
    void deleteByCustomerId(Long customerId);
}

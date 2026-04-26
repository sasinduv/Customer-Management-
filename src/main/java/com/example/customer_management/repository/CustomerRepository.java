package com.example.customer_management.repository;

import com.example.customer_management.entity.Customer;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT c FROM Customer c WHERE c.email = :email")
    Optional<Customer> findByEmail(@Param("email") String email);

    @Query("SELECT c FROM Customer c WHERE c.name LIKE %:name%")
    List<Customer> findByNameContaining(@Param("name") String name);

    boolean existsByNic(String nic);

    Optional<Customer> findByNic(String nic);
}    


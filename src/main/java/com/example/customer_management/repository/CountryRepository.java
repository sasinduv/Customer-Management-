package com.example.customer_management.repository;

import com.example.customer_management.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long>{
    
}

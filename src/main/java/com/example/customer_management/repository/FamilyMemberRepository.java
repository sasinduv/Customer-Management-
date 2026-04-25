package com.example.customer_management.repository;

import com.example.customer_management.entity.FamilyMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FamilyMemberRepository extends JpaRepository<FamilyMember, Long> {
    List<FamilyMember> findByCustomerId(Long customerId);
    
}

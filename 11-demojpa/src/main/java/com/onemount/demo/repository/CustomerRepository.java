package com.onemount.demo.repository;

import java.util.Optional;

import com.onemount.demo.model.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByJob(String job);
}

package com.ttcs.ttcs.repository;

import com.ttcs.ttcs.enity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUsernameAndPassword(String username,String password);
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}

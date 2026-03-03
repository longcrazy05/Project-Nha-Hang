package com.ttcs.ttcs.repository;

import com.ttcs.ttcs.enity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}

package com.ttcs.ttcs.repository;

import com.ttcs.ttcs.enity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    public boolean existsByEmail(String email);
    public boolean existsByUsername(String username);
    public boolean existsByPhone(String phone);

    Optional<Employee>
    findByUsernameAndPassword(String username,String password);
}

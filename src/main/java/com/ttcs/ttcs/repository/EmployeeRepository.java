package com.ttcs.ttcs.repository;

import com.ttcs.ttcs.enity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}

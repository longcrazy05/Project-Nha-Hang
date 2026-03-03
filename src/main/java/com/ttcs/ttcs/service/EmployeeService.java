package com.ttcs.ttcs.service;

import com.ttcs.ttcs.enity.Employee;
import com.ttcs.ttcs.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    // list
    public List<Employee> findAllEmp(){
        return employeeRepository.findAll();
    }
    // find by id
    public Employee findById(Long id){
        return employeeRepository.findById(id).orElse(null);
    }
    // delete
    public void delete(Long id){
        employeeRepository.deleteById(id);
    }
    // save
    public Employee saveEmployee(Employee employee){
        return employeeRepository.save(employee);
    }

}

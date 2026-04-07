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

    public boolean existsByEmail(String email){
        return employeeRepository.existsByEmail(email);
    }
    boolean existsByUsername(String username){
        return employeeRepository.existsByUsername(username);
    }
    public  boolean existsByPhone(String phone){
        return employeeRepository.existsByPhone(phone);
    }
    public String isDuplicateForCreate(Employee emp) {
        if(existsByPhone(emp.getPhone())) return "phone";
        if(existsByEmail(emp.getEmail())) return "email";
        if(existsByUsername(emp.getUsername())) return "username";
        return "none";
//        return existsByEmail(emp.getEmail())
//                || existsByPhone(emp.getPhone())
//                || existsByUsername(emp.getUsername());
    }

    public String isDuplicateForUpdate(Employee emp) {
        Employee old = findById(emp.getId());
        if(!old.getPhone().equals(emp.getPhone()) && existsByPhone(emp.getPhone())) return "phone";
        if(!old.getEmail().equals(emp.getEmail()) && existsByEmail(emp.getEmail())) return "email";
        if(!old.getUsername().equals(emp.getUsername()) && existsByUsername(emp.getUsername())) return "username";
        return "none";
//        return (!old.getEmail().equals(emp.getEmail()) && existsByEmail(emp.getEmail()))
//                || (!old.getPhone().equals(emp.getPhone()) && existsByPhone(emp.getPhone()))
//                || (!old.getUsername().equals(emp.getUsername()) && existsByUsername(emp.getUsername()));
    }

    public Employee login(String username,String password){

        return employeeRepository
                .findByUsernameAndPassword(username,password)
                .orElse(null);

    }
}

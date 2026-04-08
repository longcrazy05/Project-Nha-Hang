package com.ttcs.ttcs.controller.admin;

import com.ttcs.ttcs.enity.Employee;
import com.ttcs.ttcs.service.EmployeeService;
import com.ttcs.ttcs.service.SocketService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("admin/employees")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final SocketService socketService;
    public EmployeeController(EmployeeService employeeService,
                              SocketService socketService) {
        this.employeeService = employeeService;
        this.socketService = socketService;
    }

    @GetMapping
    public String listEmployee(Model model){
        model.addAttribute("employees", employeeService.findAllEmp());
        return "admin/employee/list";
    }
    @GetMapping("/create")
    public String showCreateForm(Model model){
        model.addAttribute("emp", new Employee());
        return "admin/employee/form";
    }
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model){
        model.addAttribute("emp", employeeService.findById(id));
        return "admin/employee/form";
    }
    @PostMapping("/save")
    public String saveEmp(@ModelAttribute Employee emp, Model model){
        String isDuplicate;
        if(emp.getId() == null){
            isDuplicate = employeeService.isDuplicateForCreate(emp);
        }
        else {
            isDuplicate = employeeService.isDuplicateForUpdate(emp);
        }
        if (!isDuplicate.equals("none")) {
            model.addAttribute("emp", emp);
            model.addAttribute("error", isDuplicate+" đã tồn tại!");
            return "admin/employee/form";
        }
        employeeService.saveEmployee(emp);
        socketService.notifyEmployeeChanged();
        return "redirect:/admin/employees";
    }
    @PostMapping("/delete/{id}")
    public String deleteEmp(@PathVariable Long id){
        employeeService.delete(id);
        socketService.notifyEmployeeChanged();
        return "redirect:/admin/employees";
    }
}

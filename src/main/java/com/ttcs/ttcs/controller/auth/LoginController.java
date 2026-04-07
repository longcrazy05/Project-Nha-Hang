package com.ttcs.ttcs.controller.auth;

import com.ttcs.ttcs.enity.Customer;
import com.ttcs.ttcs.enity.Employee;
import com.ttcs.ttcs.service.CustomerService;
import com.ttcs.ttcs.service.EmployeeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    private final CustomerService customerService;
    private final EmployeeService employeeService;

    public LoginController(
            CustomerService customerService,
            EmployeeService employeeService
    ){
        this.customerService = customerService;
        this.employeeService = employeeService;
    }


    @GetMapping("/login")
    public String login(){
        return "auth/login";
    }


    @PostMapping("/login")
    public String doLogin(
            String username,
            String password,
            HttpSession session,
            Model model
    ){

// check customer
        Customer c =
                customerService.login(username,password);

        if(c != null){
            session.setAttribute("customer", c);
            return "redirect:/customer/home/"+c.getId();
        }


// check employee
        Employee e =
                employeeService.login(username,password);

        if(e != null){

            session.setAttribute("employee", e);

            if(e.getDepartment().equals("quan ly")){
                return "redirect:/admin/dashboard";
            }

            return "redirect:/staff/orders";
        }

        model.addAttribute("error","Sai tài khoản hoặc mật khẩu");
        model.addAttribute("username", username);
        return "auth/login";

    }
    @GetMapping("/logout")
    public String logout(HttpSession session){

        session.invalidate();

        return "redirect:/customer/";
    }

    @GetMapping("/register")
    public String register(){
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerSave(
            Customer customer,
            Model model,
            RedirectAttributes redirect
    ){
        model.addAttribute("customer", customer);
        if(customerService.existsUsername(customer.getUsername())){
            model.addAttribute("usernameError","Username đã tồn tại");
            return "auth/register";
        }

        if(customerService.existsEmail(customer.getEmail())){
            model.addAttribute("emailError","Email đã tồn tại");
            return "auth/register";
        }

        if(customerService.existsPhone(customer.getPhone())){
            model.addAttribute("phoneError","Phone đã tồn tại");
            return "auth/register";
        }


        customerService.save(customer);
        redirect.addFlashAttribute(
                "success",
                "Đăng ký thành công!"
        );
        return "redirect:/login";
    }
}

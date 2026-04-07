package com.ttcs.ttcs.config;

import com.ttcs.ttcs.enity.Customer;
import com.ttcs.ttcs.enity.Employee;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {

        HttpSession session = request.getSession();

        Customer customer =
                (Customer) session.getAttribute("customer");

        Employee employee =
                (Employee) session.getAttribute("employee");

        String uri = request.getRequestURI();

        if(uri.startsWith("/customer") && customer == null){
            response.sendRedirect("/login");
            return false;
        }

        if(uri.startsWith("/staff") && employee == null){
            response.sendRedirect("/login");
            return false;
        }

        if(uri.startsWith("/admin") &&
                (employee == null || !"quan ly".equals(employee.getDepartment()))){
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }
}
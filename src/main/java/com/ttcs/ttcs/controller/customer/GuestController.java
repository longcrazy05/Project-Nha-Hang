package com.ttcs.ttcs.controller.customer;

import com.ttcs.ttcs.service.CustomerService;
import com.ttcs.ttcs.service.ReservationService;
import com.ttcs.ttcs.service.SocketService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GuestController {

    private final CustomerService customerService;

    public GuestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    //home
    @GetMapping("/home")
    public String publicHome(Model model){
        model.addAttribute("foods", customerService.foodList());

        return "public/home";
    }
}
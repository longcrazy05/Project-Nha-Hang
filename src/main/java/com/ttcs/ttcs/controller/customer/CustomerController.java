package com.ttcs.ttcs.controller.customer;

import com.ttcs.ttcs.dto.CartItem;
import com.ttcs.ttcs.enity.Customer;
import com.ttcs.ttcs.enity.OrderItem;
import com.ttcs.ttcs.enity.Reservation;
import com.ttcs.ttcs.service.CustomerService;
import com.ttcs.ttcs.service.ReservationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final ReservationService reservationService;
    public CustomerController(CustomerService customerService,
                              ReservationService reservationService) {
        this.customerService = customerService;
        this.reservationService = reservationService;
    }

    //home
    @GetMapping("/")
    public String publicHome(Model model){
        model.addAttribute("foods", customerService.foodList());

        return "public/home";
    }
    // Trang thông tin customer
    @GetMapping("/home/{id}")
    public String dashboard(@PathVariable Long id, Model model) {
        model.addAttribute("customer", customerService.findById(id));
        model.addAttribute("orders", customerService.foodOrderList(id));
        model.addAttribute("reservations", customerService.reservationList(id));
        model.addAttribute("tables", customerService.availableTable());
        model.addAttribute("foods", customerService.foodList());
        return "customer/home";
    }

    // Xem chi tiết order
    @GetMapping("/order/{orderId}")
    public String orderDetail(@PathVariable Long orderId, Model model) {
        model.addAttribute("items", customerService.orderItemList(orderId));
        return "customer/order-detail";
    }

    // Xem menu (foods)
    @GetMapping("/foods")
    public String foodList(Model model) {
        model.addAttribute("foods", customerService.foodList());
        return "customer/foods";
    }
    // Xem ds đặt trước
    @GetMapping("/reservations/{id}")
    public String reservations(Model model, @PathVariable Long id ){
        model.addAttribute("customer", customerService.findById(id));
        model.addAttribute("reservations", customerService.reservationList(id));
        return "customer/reservation";
    }

    @GetMapping("/profile/{id}")
    public String profile(Model model, @PathVariable Long id){
        model.addAttribute("customer", customerService.findById(id));
        return "customer/profile";
    }
    @PostMapping("/profile/update")
    public String saveProfile(@ModelAttribute Customer customer){
        customerService.save(customer);
        return "redirect:/customer/profile/"+customer.getId();
    }
    @PostMapping("/reservation/save")
    public String saveReservation(
            Long customerId,
            Integer guestCount,
            LocalDateTime time
    ){

        reservationService.createReservation(
                customerId,
                guestCount,
                time
        );

        return "redirect:/customer/reservations/" + customerId;
    }

    @GetMapping("/reservation/cancel/{id}")
    public String cancelReservation(@PathVariable Long id){

        Reservation r = reservationService.findById(id);

        r.setStatus("CANCELLED");

        reservationService.save(r);

        return "redirect:/customer/reservations/" + r.getCustomer().getId();
    }
    @GetMapping("/orders/{id}")
    public String orders(@PathVariable Long id, Model model){

        model.addAttribute(
                "orders",
                customerService.foodOrderList(id)
        );

        model.addAttribute(
                "totalSpent",
                customerService.totalSpent(id)
        );

        model.addAttribute(
                "weekLabels",
                customerService.weekLabels(id)
        );

        model.addAttribute(
                "weekValues",
                customerService.weekValues(id)
        );
        model.addAttribute("customer", customerService.findById(id));
        return "customer/orders";
    }
    @GetMapping("/orders/detail/{id}")
    @ResponseBody
    public List<OrderItem> detail(@PathVariable Long id){
        return customerService.orderItemList(id);
    }

    @PostMapping("/order/create/{customerId}")
    @ResponseBody
    public String createOrder(
            @PathVariable Long customerId,
            @RequestBody List<CartItem> cart
    ){
        customerService.createFromCart(customerId, cart);
        return "ok";
    }
}
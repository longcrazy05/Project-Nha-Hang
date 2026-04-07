package com.ttcs.ttcs.controller.staff;

import com.ttcs.ttcs.enity.Customer;
import com.ttcs.ttcs.enity.FoodOrder;
import com.ttcs.ttcs.enity.RestaurantTable;
import com.ttcs.ttcs.service.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/staff/orders")
public class FoodOrderController {
    private final FoodOrderService foodOrderService;
    private final EmployeeService employeeService;
    private final RestaurantTableService tableService;
    private final ReservationService reservationService;
//    private final CustomerService customerService;

    public FoodOrderController(FoodOrderService foodOrderService, EmployeeService employeeService
                                , RestaurantTableService tableService,
                               ReservationService reservationService){
        this.foodOrderService = foodOrderService;
        this.employeeService = employeeService;
        this.tableService = tableService;
        this.reservationService = reservationService;
//        this.customerService = customerService;
    }

    @GetMapping()
    public String listOrders(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate date,
            Model model
            ){
        if (date == null) date = LocalDate.now();
        model.addAttribute("orders", foodOrderService.getOrdersByDate(date));
        model.addAttribute("selectedDate", date);

        return "staff/food_order/list";
    }
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model){
        model.addAttribute("items", foodOrderService.findById(id));
        return "staff/food_order/detail";
    }
    @GetMapping("/create")
    public String createOrder(Model model){
        model.addAttribute("order", new FoodOrder());
        model.addAttribute("employees", employeeService.findAllEmp());
        model.addAttribute("tables", tableService.findAvailableTable());
        model.addAttribute("todayReservations", reservationService.todayReservation());
        return "staff/food_order/form";
    }
    @GetMapping("/edit/{id}")
    public String editOrder(@PathVariable Long id,@RequestParam(required = false) String date,
                            Model model){
        model.addAttribute("order", foodOrderService.findById(id));
        model.addAttribute("employees", employeeService.findAllEmp());
        model.addAttribute("tables", tableService.findAvailableTable());

//        System.out.println("edit "+date);
        model.addAttribute("selectedDate", date);
        return "staff/food_order/form";
    }
    @PostMapping("/save")
    public String save(@ModelAttribute FoodOrder foodOrder,
        @RequestParam String action, @RequestParam(required = false) String redirectDate,
                       @RequestParam(required = false) Long tableId,
                       @RequestParam(required = false) Long reservationId){
        if("create".equals(action)){
            if (tableId == 0) foodOrder.setOrderType("TAKE AWAY");
            else foodOrder.setOrderType("DINE IN");
            if(foodOrder.getCustomer()==null || foodOrder.getCustomer().getId()==null){
                Customer c = new Customer();
                c.setId(1L);
                foodOrder.setCustomer(c);
            }
            foodOrderService.createOrder(foodOrder, reservationId, tableId);
        }
        if("paid".equals(action)){
            foodOrderService.markAsPaid(foodOrder.getId());
        }
        if("save".equals(action)){
            foodOrderService.save(foodOrder);
        }
//        System.out.println("save: "+redirectDate);
        if (redirectDate != null && !redirectDate.isBlank()) {
            return "redirect:/staff/orders?date=" + redirectDate;
        }

        return "redirect:/staff/orders";
    }
}

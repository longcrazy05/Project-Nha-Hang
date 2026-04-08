package com.ttcs.ttcs.controller.staff;

import com.ttcs.ttcs.enity.Customer;
import com.ttcs.ttcs.enity.FoodOrder;
import com.ttcs.ttcs.enity.Reservation;
import com.ttcs.ttcs.enity.RestaurantTable;
import com.ttcs.ttcs.repository.CustomerRepository;
import com.ttcs.ttcs.service.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/staff/orders")
public class FoodOrderController {
    private final FoodOrderService foodOrderService;
    private final EmployeeService employeeService;
    private final RestaurantTableService tableService;
    private final ReservationService reservationService;
    private final CustomerRepository customerRepository;
    private final SocketService socketService;

    public FoodOrderController(FoodOrderService foodOrderService, EmployeeService employeeService
                                , RestaurantTableService tableService,
                               ReservationService reservationService,
                               CustomerRepository customerRepository,
                               SocketService socketService){
        this.foodOrderService = foodOrderService;
        this.employeeService = employeeService;
        this.tableService = tableService;
        this.reservationService = reservationService;
        this.customerRepository = customerRepository;
        this.socketService = socketService;
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
        FoodOrder order = new FoodOrder();
        order.setCustomer(new Customer());
        model.addAttribute("order", order);
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
                       BindingResult result,
                       Model model,
                       @RequestParam String action,
                       @RequestParam(required = false) String redirectDate,
                       @RequestParam(required = false) Long tableId,
                       @RequestParam(required = false) Long reservationId) {

        if ("create".equals(action)) {

            if (tableId == 0) foodOrder.setOrderType("TAKE AWAY");
            else foodOrder.setOrderType("DINE IN");

            // đảm bảo không null
            if (foodOrder.getCustomer() == null) {
                foodOrder.setCustomer(new Customer());
            }

            Long customerId = foodOrder.getCustomer().getId();

            // ✅ không nhập → default = 1
            if (customerId == null) {
                foodOrder.getCustomer().setId(1L);
            } else {
                boolean exists = customerRepository.existsById(customerId);

                // ❌ không tồn tại → trả về form + message
                if (!exists) {
                    model.addAttribute("order", foodOrder);
                    model.addAttribute("customerError", "Customer không tồn tại");
                    model.addAttribute("tables", tableService.findAvailableTable());
                    model.addAttribute("todayReservations", reservationService.todayReservation());

                    return "staff/food_order/form";
                }
            }

            foodOrderService.createOrder(foodOrder, reservationId, tableId);
            socketService.notifyOrderChanged(); // realtime
            socketService.notifyTableChanged();
        }

        if ("paid".equals(action)) {
            foodOrderService.markAsPaid(foodOrder.getId(), foodOrder.getPaymentMethod());
        }

        if ("save".equals(action)) {
            foodOrderService.save(foodOrder);
        }

        if (redirectDate != null && !redirectDate.isBlank()) {
            return "redirect:/staff/orders?date=" + redirectDate;
        }

        return "redirect:/staff/orders";
    }
    @PostMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id,
                              @RequestParam(required = false) String date) {

        FoodOrder order = foodOrderService.findById(id);

        if (order.getPaidAt() != null) {
            return "redirect:/staff/orders?error=paid";
        }

        Reservation r = order.getReservation();

        // trả bàn trước
        if (r != null && r.getTable() != null) {
            RestaurantTable table = r.getTable();
            table.setAvailable(true);
            tableService.saveTable(table);
        }

        // xóa order trước
        foodOrderService.delete(id);

        // sau đó mới xóa reservation
        if (r != null) {
            reservationService.delete(r);
        }
        socketService.notifyOrderChanged(); // realtime
        socketService.notifyTableChanged();
        return "redirect:/staff/orders" + (date != null ? "?date=" + date : "");
    }
}

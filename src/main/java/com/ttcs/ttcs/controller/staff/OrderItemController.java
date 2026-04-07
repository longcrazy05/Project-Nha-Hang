package com.ttcs.ttcs.controller.staff;

import com.ttcs.ttcs.service.FoodOrderService;
import com.ttcs.ttcs.service.FoodService;
import com.ttcs.ttcs.service.OrderItemService;
import org.apache.juli.logging.Log;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;

@Controller
@RequestMapping("/staff/order-items")
public class OrderItemController {
    private final OrderItemService orderItemService;
    private final FoodOrderService foodOrderService;
    private final FoodService foodService;

    public OrderItemController(OrderItemService orderItemService, FoodOrderService foodOrderService, FoodService foodService) {
        this.orderItemService = orderItemService;
        this.foodOrderService = foodOrderService;
        this.foodService = foodService;
    }
    @GetMapping("/{orderId}")
    public String detail(@PathVariable Long orderId,
                         @RequestParam(required = false) String date,
                         Model model){
        model.addAttribute("items", orderItemService.getItemsByOrderId(orderId));
        model.addAttribute("foods", foodService.findAll());
        model.addAttribute("orderId", orderId);
        model.addAttribute("total", orderItemService.total(orderId));
        model.addAttribute("selectedDate", date);
        model.addAttribute("order", foodOrderService.findById(orderId));
        return "/staff/food_order/detail";
    }

    // thêm món
    @PostMapping("/add")
    public String addItem(@RequestParam Long orderId,
                          @RequestParam Long foodId,
                          @RequestParam Integer quantity){

        orderItemService.addItem(orderId, foodId, quantity);

        return "redirect:/staff/order-items/" + orderId;
    }

    // xóa món
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         @RequestParam Long orderId,
                         @RequestParam(required = false) String date){

        orderItemService.deleteItem(id);

        return "redirect:/staff/order-items/" + orderId;
    }

    // bắt đầu nấu
    @GetMapping("/start/{id}")
    public String start(@PathVariable Long id,
                        @RequestParam Long orderId){

        orderItemService.startCooking(id);

        return "redirect:/staff/order-items/" + orderId;
    }

    // nấu xong
    @GetMapping("/finish/{id}")
    public String finish(@PathVariable Long id,
                         @RequestParam Long orderId){

        orderItemService.finishCooking(id);

        return "redirect:/staff/order-items/" + orderId;
    }

    // phục vụ
    @GetMapping("/serve/{id}")
    public String serve(@PathVariable Long id,
                        @RequestParam Long orderId){

        orderItemService.serve(id);

        return "redirect:/staff/order-items/" + orderId;
    }
    @GetMapping("/waiting")
    public String wait(Model model){
        model.addAttribute("waits", orderItemService.waitingFood());
        return "staff/food_order/food_prepared";
    }
    // xóa món
    @GetMapping("/waiting/delete/{id}")
    public String deleteW(@PathVariable Long id){
        orderItemService.deleteItem(id);
        return "redirect:/staff/order-items/waiting";
    }

    // bắt đầu nấu
    @GetMapping("/waiting/start/{id}")
    public String startW(@PathVariable Long id){
        orderItemService.startCooking(id);
        return "redirect:/staff/order-items/waiting";
    }

    // nấu xong
    @GetMapping("/waiting/finish/{id}")
    public String finishW(@PathVariable Long id){
        orderItemService.finishCooking(id);

        return "redirect:/staff/order-items/waiting";
    }

    // phục vụ
    @GetMapping("/waiting/serve/{id}")
    public String serveW(@PathVariable Long id){
        orderItemService.serve(id);
        return "redirect:/staff/order-items/waiting";
    }
}

package com.ttcs.ttcs.controller.admin;

import com.ttcs.ttcs.service.FoodOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.text.DateFormatter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final FoodOrderService foodOrderService;

    public DashboardController(FoodOrderService foodOrderService) {
        this.foodOrderService = foodOrderService;
    }

    @GetMapping()
    public String dashboard(
            @RequestParam(required = false) Integer year,
            Model model) {

        if (year == null) {
            year = LocalDate.now().getYear();
        }

        Long todayRevenue = foodOrderService.getTodayRevenue();
        Long todayQuantityOrder = foodOrderService.getTodayQuantityOrder();

        Long monthRevenue = foodOrderService.getMonthRevenue();
        Long monthQuantityOder = foodOrderService.getMonthQuantityOrder();

        List<Object[]> raw = foodOrderService.getRevenueByMonth(year);

        List<String> labels = new ArrayList<>();
        List<Long> values = new ArrayList<>();

        for (Object[] row : raw) {
            labels.add("Month " + row[0]);
            values.add((Long) row[1]);
        }
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        model.addAttribute("todayRevenue", todayRevenue);
        model.addAttribute("todayQuantityOrder", todayQuantityOrder);
        model.addAttribute("monthRevenue", monthRevenue);
        model.addAttribute("monthQuantityOder", monthQuantityOder);
        model.addAttribute("labels", labels);
        model.addAttribute("values", values);
        model.addAttribute("selectedYear", year);
        model.addAttribute("today", LocalDate.now().format(f));
        // danh sách năm để chọn
        int yearNow = LocalDate.now().getYear();
        List<Integer> years = new ArrayList<>();
        for(int i=yearNow; i >= yearNow-3; i--){
            years.add(i);
        }
        model.addAttribute("years", years);

       return "admin/dashboard/dashboard";
    }
}


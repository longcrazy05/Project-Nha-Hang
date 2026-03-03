package com.ttcs.ttcs.controller.admin;

import com.ttcs.ttcs.enity.RestaurantTable;
import com.ttcs.ttcs.service.RestaurantTableService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tables")
public class RestaurantTableController {
    public final RestaurantTableService tableService;

    public RestaurantTableController(RestaurantTableService tableService) {
        this.tableService = tableService;
    }
    @GetMapping
    public String listTable(Model model){
        model.addAttribute("tables", tableService.findAllTable());
        return "admin/restaurant_table/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model){
        model.addAttribute("table", new RestaurantTable());
        return "admin/restaurant_table/form";
    }
    @GetMapping("edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model){
        model.addAttribute("table", tableService.findById(id));
        return "admin/restaurant_table/form";
    }
    @PostMapping("/save")
    public String saveTable(@ModelAttribute RestaurantTable table){
        tableService.saveTable(table);
        return "redirect:/tables";
    }
    @GetMapping("toggle/{id}")
    public String toggleAvailable(@PathVariable Long id){
        RestaurantTable table = tableService.findById(id);
        tableService.updateAvailable(id, !table.isAvailable());
        return "redirect:/tables";
    }
}

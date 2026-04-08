package com.ttcs.ttcs.controller.admin;

import com.ttcs.ttcs.enity.RestaurantTable;
import com.ttcs.ttcs.service.RestaurantTableService;
import com.ttcs.ttcs.service.SocketService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("admin/tables")
public class RestaurantTableController {
    public final RestaurantTableService tableService;
    public  final SocketService socketService;
    public RestaurantTableController(RestaurantTableService tableService,
                                     SocketService socketService) {
        this.tableService = tableService;
        this.socketService = socketService;
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
        socketService.notifyTableChanged();
        return "redirect:/admin/tables";
    }
    @GetMapping("toggle/{id}")
    public String toggleAvailable(@PathVariable Long id){
        RestaurantTable table = tableService.findById(id);
        tableService.updateAvailable(id, !table.isAvailable());
        socketService.notifyTableChanged();
        return "redirect:/admin/tables";
    }
}

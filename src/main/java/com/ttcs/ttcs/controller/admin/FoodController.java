package com.ttcs.ttcs.controller.admin;

import com.ttcs.ttcs.enity.Food;
import com.ttcs.ttcs.service.FoodService;
import com.ttcs.ttcs.service.SocketService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("admin/foods")
public class FoodController {
    private final FoodService foodService;
    private final SocketService socketService;
    public FoodController(FoodService foodService,
                          SocketService socketService) {
        this.foodService = foodService;
        this.socketService = socketService;
    }

    @GetMapping
    public String listFood(Model model){
        model.addAttribute("foods", foodService.findAll());
        return "admin/food/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model){
        model.addAttribute("food", new Food());
        return "admin/food/form";
    }

    @PostMapping("/save")
    public String saveFood(@ModelAttribute Food food,
                           @RequestParam(value = "image", required = false) MultipartFile file)
            throws IOException {

        if (file != null && !file.isEmpty()) {
            String fileName = foodService.saveFile(file);
            if (fileName != null) {
                food.setImage_url(fileName);
            }
        }

        foodService.save(food);
        socketService.notifyFoodChanged();
        return "redirect:/admin/foods";
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model){
        model.addAttribute("food", foodService.findById(id));
        return "admin/food/form";
    }

    @GetMapping("/toggle/{id}")
    public String toggleAvailable(@PathVariable Long id){
        Food food = foodService.findById(id);
        foodService.updateAvailable(id, !food.isAvailable());
        socketService.notifyFoodChanged();
        return "redirect:/admin/foods";
    }

}

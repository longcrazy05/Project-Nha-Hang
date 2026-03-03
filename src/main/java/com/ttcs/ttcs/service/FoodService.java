package com.ttcs.ttcs.service;

import com.ttcs.ttcs.enity.Food;
import com.ttcs.ttcs.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class FoodService {
    private final FoodRepository foodRepository;

    public FoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    //lay all mon an
    public List<Food> findAll(){
        return foodRepository.findAll();
    }
    //Lay mon theo id
    public Food findById(Long id){
        return foodRepository.findById(id).orElse(null);
    }

    public Food save(Food food){
        return foodRepository.save(food);
    }
    public void deleteById(Long id){
        foodRepository.deleteById(id);
    }

    public void updateAvailable(Long id, boolean available){
        Food food = findById(id);
        food.setAvailable(available);
        foodRepository.save(food);
    }
    @Value("${upload.path}")
    private String uploadPath;

    public String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String originalName = file.getOriginalFilename();

        if (originalName == null || originalName.isBlank()) {
            return null;
        }

        // tạo tên file mới để tránh trùng
        String fileName = System.currentTimeMillis() + "_" + originalName;

        Path uploadDir = Paths.get(uploadPath);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        Path path = uploadDir.resolve(fileName);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }


}

package com.ttcs.ttcs.service;

import com.ttcs.ttcs.enity.RestaurantTable;
import com.ttcs.ttcs.repository.RestaurantTableRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantTableService {
    private final RestaurantTableRepository tableRepository;

    public RestaurantTableService(RestaurantTableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    // list of table
    public List<RestaurantTable> findAllTable(){
        return tableRepository.findAll();
    }
    // list of available table
    public List<RestaurantTable> findAvailableTable(){return tableRepository.findByAvailableTrue();}
    // find by id
    public RestaurantTable findById(Long id){
        return tableRepository.findById(id).orElse(null);
    }
    // save
    public void saveTable(RestaurantTable restaurantTable){
        tableRepository.save(restaurantTable);
    }
    public void deleteById(Long id){
        tableRepository.deleteById(id);
    }
    public void updateAvailable(Long id, boolean available){
        RestaurantTable table = findById(id);
        table.setAvailable(available);
        saveTable(table);
    }
}

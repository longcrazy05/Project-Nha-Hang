package com.ttcs.ttcs.repository;

import com.ttcs.ttcs.enity.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {
    List<RestaurantTable> findByAvailableTrue();
}

package com.ttcs.ttcs.repository;

import com.ttcs.ttcs.enity.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {
    List<RestaurantTable> findByAvailableTrue();
    // Tổng số bàn
    @Query("SELECT COUNT(t) FROM RestaurantTable t")
    Long totalTables();

    // Số bàn đang sử dụng (available = false)
    @Query("SELECT COUNT(t) FROM RestaurantTable t WHERE t.available = false")
    Long usingTables();
}

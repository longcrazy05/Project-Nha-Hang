package com.ttcs.ttcs.repository;

import com.ttcs.ttcs.enity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
//    @Query("""
//            select o from OrderItem o
//            where order_id = :id
//            """)
//    List<OrderItem> findOrderById(@Param("id") Long id);
    List<OrderItem> findByFoodOrderId(Long orderId);
    Optional<OrderItem> findByFoodOrderIdAndFoodId(Long orderId, Long food_id);
}

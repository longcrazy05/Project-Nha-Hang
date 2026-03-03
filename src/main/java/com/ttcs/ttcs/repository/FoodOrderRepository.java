package com.ttcs.ttcs.repository;

import com.ttcs.ttcs.enity.FoodOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FoodOrderRepository extends JpaRepository<FoodOrder, Long> {
    // doanh thu ngay
    @Query("""
            select coalesce(sum(f.totalAmount), 0)
            from FoodOrder f
            where date(f.paidAt) = current_date
            """)
    Long getTodayRevenue();
    // so luong don trong ngay
    @Query("""
            select count(*)
            from FoodOrder f
            where date(f.paidAt) = current_date
            """)
    Long getTodayQuantityOrder();

    // doanh thu thang
    @Query("""
        SELECT COALESCE(SUM(f.totalAmount),0)
        FROM FoodOrder f
        WHERE YEAR(f.paidAt) = YEAR(CURRENT_DATE)
          AND MONTH(f.paidAt) = MONTH(CURRENT_DATE)
    """)
    Long getMonthRevenue();
    // so luong don trong thang
    @Query("""
        SELECT count(*)
        FROM FoodOrder f
        WHERE YEAR(f.paidAt) = YEAR(CURRENT_DATE)
          AND MONTH(f.paidAt) = MONTH(CURRENT_DATE)
            """)
    Long getMonthQuantityOrder();

    // doanh thu cac thang trong nam
    @Query("""
        SELECT MONTH(f.paidAt), SUM(f.totalAmount)
        FROM FoodOrder f
        WHERE YEAR(f.paidAt) = :year
        GROUP BY MONTH(f.paidAt)
        ORDER BY MONTH(f.paidAt)
    """)
    List<Object[]> getRevenueByMonth(@Param("year") int year);

    // ds don hang trong ngay
    @Query("""
            select f from FoodOrder f
            where date(f.openedAt) = :date
            """)
    List<FoodOrder> findByDate(LocalDate date);
}

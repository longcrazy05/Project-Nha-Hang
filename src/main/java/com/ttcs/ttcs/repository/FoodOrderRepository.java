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
        SELECT MONTH(f.paidAt), COALESCE(SUM(f.totalAmount),0), COUNT(f)
        FROM FoodOrder f
        WHERE YEAR(f.paidAt) = :year
        GROUP BY MONTH(f.paidAt)
        ORDER BY MONTH(f.paidAt)
    """)
    List<Object[]> getRevenueAndOrderByMonth(@Param("year") int year);

    // ds don hang trong ngay
    @Query("""
            select f from FoodOrder f
            where date(f.openedAt) = :date
            """)
    List<FoodOrder> findByDate(LocalDate date);

    // tong so don trong nam
    @Query("SELECT COUNT(f) FROM FoodOrder f WHERE YEAR(f.paidAt) = :year")
    Long countOrdersByYear(int year);
    // doanh thu năm
    @Query("SELECT coalesce(sum(f.totalAmount), 0) FROM FoodOrder f WHERE YEAR(f.paidAt) = :year")
    Long getYearRevenue(int year);

    // ds order theo khach hang
//    @Query("""
//            select * from FoodOrder f
//            where f.customerId = :id
//            """)
    List<FoodOrder> findByCustomerId(Long id);
    List<FoodOrder> findByCustomerIdOrderByOpenedAtDesc(Long id);
    @Query("""
        SELECT COALESCE(SUM(o.totalAmount),0)
        FROM FoodOrder o
        WHERE o.customer.id = :id
        """)
    Long totalSpent(Long id);
    //
    @Query(value = """
    SELECT 
        d.day_name,
        COALESCE(SUM(o.total_amount), 0) AS total
    FROM (
        SELECT 'Monday' AS day_name UNION
        SELECT 'Tuesday' UNION
        SELECT 'Wednesday' UNION
        SELECT 'Thursday' UNION
        SELECT 'Friday' UNION
        SELECT 'Saturday' UNION
        SELECT 'Sunday'
    ) d
    LEFT JOIN food_order o 
        ON DAYNAME(o.paid_at) = d.day_name
        AND o.customer_id = :id
        AND o.paid_at >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)
    GROUP BY d.day_name
    ORDER BY FIELD(
        d.day_name,
        'Monday','Tuesday','Wednesday',
        'Thursday','Friday','Saturday','Sunday'
    )
""", nativeQuery = true)
    List<Object[]> weekStats(@Param("id") Long id);

    @Query(value = """
    SELECT 
        m.month,
        COALESCE(SUM(o.total_amount), 0) AS total
    FROM (
        SELECT 1 AS month UNION SELECT 2 UNION SELECT 3 UNION
        SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION
        SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION
        SELECT 10 UNION SELECT 11 UNION SELECT 12
    ) m
    LEFT JOIN food_order o
        ON MONTH(o.paid_at) = m.month
        AND YEAR(o.paid_at) = YEAR(CURDATE())
        AND o.customer_id = :id
    GROUP BY m.month
    ORDER BY m.month
""", nativeQuery = true)
    List<Object[]> monthStats(@Param("id") Long id);
}

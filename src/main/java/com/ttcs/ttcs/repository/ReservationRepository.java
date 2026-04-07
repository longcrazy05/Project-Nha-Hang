package com.ttcs.ttcs.repository;

import com.ttcs.ttcs.enity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("""
            select r from Reservation r
            where date(r.reservationTime) = current_date
            and r.status = "booked"
            """)
    List<Reservation> findTodayReservation();

    List<Reservation> findByCustomerId(Long id);
    List<Reservation>
    findByCustomerIdOrderByReservationTimeDesc(Long id);
}

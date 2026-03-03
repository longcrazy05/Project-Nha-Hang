package com.ttcs.ttcs.service;

import com.ttcs.ttcs.enity.Customer;
import com.ttcs.ttcs.enity.FoodOrder;
import com.ttcs.ttcs.enity.Reservation;
import com.ttcs.ttcs.enity.RestaurantTable;
import com.ttcs.ttcs.repository.CustomerRepository;
import com.ttcs.ttcs.repository.FoodOrderRepository;
import com.ttcs.ttcs.repository.ReservationRepository;
import com.ttcs.ttcs.repository.RestaurantTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FoodOrderService {
    private final FoodOrderRepository foodOrderRepository;
    private final ReservationRepository reservationRepository;
    private final RestaurantTableRepository tableRepository;
    private final CustomerRepository customerRepository;

    public FoodOrderService(FoodOrderRepository foodOrderRepository, ReservationRepository reservationRepository, RestaurantTableRepository tableRepository,
                            CustomerRepository customerRepository) {
        this.foodOrderRepository = foodOrderRepository;
        this.reservationRepository = reservationRepository;
        this.tableRepository = tableRepository;
        this.customerRepository = customerRepository;
    }

    public Long getTodayRevenue(){
        return foodOrderRepository.getTodayRevenue();
    }

    public Long getTodayQuantityOrder(){
        return foodOrderRepository.getTodayQuantityOrder();
    }

    public Long getMonthRevenue(){
        return foodOrderRepository.getMonthRevenue();
    }

    public Long getMonthQuantityOrder(){
        return foodOrderRepository.getMonthQuantityOrder();
    }

    public List<Object[]> getRevenueByMonth(int year) {
        return foodOrderRepository.getRevenueByMonth(year);
    }

    // today list
    public List<FoodOrder> todayOrders(){
        return foodOrderRepository.findByDate(LocalDate.now());
    }
    //
    public List<FoodOrder> getOrdersByDate(LocalDate date){
        return foodOrderRepository.findByDate(date);
    }
    // all list
    public List<FoodOrder> findAllFoodOrder(){
        return foodOrderRepository.findAll();
    }
    // find by id
    public FoodOrder findById(Long id){
        return foodOrderRepository.findById(id).orElse(null);
    }
    // delete
    public void delete(Long id){
        foodOrderRepository.deleteById(id);
    }
    // reservation ID
    public Long reservationId(Long id){
        return findById(id).getReservation().getId();
    }
    // save
    public FoodOrder save(FoodOrder foodOrder){
        return foodOrderRepository.save(foodOrder);
    }

    // create
    @Transactional
    public void createOrder(FoodOrder order,
                            Long reservationId,
                            Long tableId) {

        Reservation reservation;
        order.setTotalAmount(0L);
        if(tableId == 0){
            order.setOrderType("TAKE AWAY");
        }
        else {
            order.setOrderType("DINE IN");
        }
        // ===== CASE 1: Có reservation sẵn =====
        if (reservationId != null) {

            reservation = reservationRepository
                    .findById(reservationId)
                    .orElseThrow();

            // Nếu reservation chưa có bàn
            if (reservation.getTable() == null && tableId != null && tableId != 0) {

                RestaurantTable table = tableRepository
                        .findById(tableId)
                        .orElseThrow();

                table.setAvailable(false);
                reservation.setTable(table);
            }

            reservation.setStatus("IN_USE");
        }

        // ===== CASE 2: New reservation =====
        else {

            reservation = new Reservation();

            Customer guest = customerRepository
                    .findById(order.getCustomer().getId())
                    .orElseThrow();

            reservation.setCustomer(guest);
            reservation.setReservationTime(LocalDateTime.now());
            reservation.setStatus("IN_USE");

            if (tableId != null && tableId != 0) {

                RestaurantTable table = tableRepository
                        .findById(tableId)
                        .orElseThrow();

                table.setAvailable(false);
                reservation.setTable(table);
            }

            reservationRepository.save(reservation);
        }

        order.setReservation(reservation);
        order.setOpenedAt(LocalDateTime.now());

        foodOrderRepository.save(order);
    }

    @Transactional
    public void markAsPaid(Long orderId){

        FoodOrder order = foodOrderRepository.findById(orderId).orElseThrow();

        order.setPaidAt(LocalDateTime.now());

        Reservation reservation = order.getReservation();

        if (reservation != null && reservation.getTable() != null) {

            RestaurantTable table = reservation.getTable();
            table.setAvailable(true);

            reservation.setStatus("DONE");
        }
    }
}

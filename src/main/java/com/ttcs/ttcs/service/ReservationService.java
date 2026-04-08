package com.ttcs.ttcs.service;

import com.ttcs.ttcs.enity.Customer;
import com.ttcs.ttcs.enity.Reservation;
import com.ttcs.ttcs.repository.CustomerRepository;
import com.ttcs.ttcs.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final CustomerRepository customerRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              CustomerRepository customerRepository) {
        this.reservationRepository = reservationRepository;
        this.customerRepository = customerRepository;
    }
    // list reservation
    public List<Reservation> findAllReservation(){
        return reservationRepository.findAll();
    }
    // findById
    public Reservation findById(Long id){
        return reservationRepository.findById(id).orElse(null);
    }
    // today list
    public List<Reservation> todayReservation(){
        return reservationRepository.findTodayReservation();
    }
    //save
    public void save(Reservation r){
        reservationRepository.save(r);
    }
    //delete
    public void delete(Reservation reservation){
        reservationRepository.delete(reservation);
    }
    // ds theo khach hang
    public List<Reservation> CustomerReservationList(Long id){
        return reservationRepository.findByCustomerIdOrderByReservationTimeDesc(id);
    }

    //create reservation
    public void createReservation(Long customerId, Integer guestCount, LocalDateTime time){
        Reservation r = new Reservation();
        Customer c = customerRepository.findById(customerId).orElse(null);
        r.setCustomer(c);
        r.setGuestCount(guestCount);
        r.setReservationType("BOOK");
        r.setReservationTime(time);
        r.setStatus("BOOKED");
        reservationRepository.save(r);
    }

    @Transactional
    public void autoCancel(){

        List<Reservation> list =
                reservationRepository.findAll();

        for(Reservation r : list){
            String status = r.getStatus();
            if(
                    "BOOKED".equals(status)
                            && r.getReservationTime().isBefore(LocalDateTime.now())
            ){
                r.setStatus("CANCELLED");
            }

        }

    }
}

package com.ttcs.ttcs.service;

import com.ttcs.ttcs.enity.Reservation;
import com.ttcs.ttcs.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
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

}

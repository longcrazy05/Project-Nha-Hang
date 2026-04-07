package com.ttcs.ttcs.controller.staff;

import com.ttcs.ttcs.repository.ReservationRepository;
import com.ttcs.ttcs.service.ReservationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.PublicKey;

@Controller
@RequestMapping("/staff/reservation")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public String listReservation(Model model){
        model.addAttribute("reservations", reservationService.todayReservation());
        return "staff/reservation/list";
    }
}

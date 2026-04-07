package com.ttcs.ttcs.service;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class SchedulerService {

    private final ReservationService reservationService;

    public SchedulerService(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Scheduled(fixedRate = 60000)
    public void autoCancelTask(){
        reservationService.autoCancel();
    }

}

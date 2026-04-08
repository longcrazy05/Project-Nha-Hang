package com.ttcs.ttcs.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class SocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public SocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyOrderChanged() {
        messagingTemplate.convertAndSend("/topic/orders", "update");
    }

    // realtime tables
    public void notifyTableChanged() {
        messagingTemplate.convertAndSend("/topic/tables", "update");
    }
    // online pay
    public void notifyRevenueChanged() {
        messagingTemplate.convertAndSend("/topic/revenue", "update");
    }
    // employee
    public void notifyEmployeeChanged() {
        messagingTemplate.convertAndSend("/topic/employee", "update");
    }
    public void notifyFoodChanged() {
        messagingTemplate.convertAndSend("/topic/food", "update");
    }
}
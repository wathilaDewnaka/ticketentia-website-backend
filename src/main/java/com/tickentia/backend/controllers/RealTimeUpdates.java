package com.tickentia.backend.controllers;

import com.tickentia.backend.dto.SocketDTO;
import com.tickentia.backend.entities.TicketPools;
import com.tickentia.backend.service.vendor.VendorService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Controller
public class RealTimeUpdates {
    private final VendorService vendorService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public RealTimeUpdates(VendorService vendorService, SimpMessagingTemplate simpMessagingTemplate) {
        this.vendorService = vendorService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/dashboard")
    public void getTicketPool(@Payload SocketDTO socketDTO) {
        TicketPools ticketPools = vendorService.getTicketPoolBySession(socketDTO.getSessionId());

        if (ticketPools == null) {
            throw new RuntimeException("No ticket pools found for sessionId: " + socketDTO.getSessionId());
        }

        simpMessagingTemplate.convertAndSend("/topic/updates/" + socketDTO.getSessionId(), ticketPools);
    }
}

package com.tickentia.backend.controllers;

import com.tickentia.backend.dto.InitializerRequest;
import com.tickentia.backend.service.ticketing.TicketingService;
import com.tickentia.backend.service.ticketing.TicketingServiceImplementation;
import com.tickentia.backend.service.vendor.VendorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendor/session")
@CrossOrigin(origins = "http://localhost:4200")
public class Vendor {
    private final VendorService vendorService;
    private final TicketingService ticketingService;

    public Vendor(VendorService vendorService, TicketingService ticketingService) {
        this.vendorService = vendorService;
        this.ticketingService = ticketingService;
    }

    @PostMapping("/start")
    public ResponseEntity<String> startSession(@ModelAttribute InitializerRequest initializerRequest) {
        try {
            boolean status = ticketingService.configureSession(initializerRequest);

            if (status) {
                return ResponseEntity.ok("Session created successfully");
            } else {
                return ResponseEntity.status(500).body("Failed to create session");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while creating the session");
        }
    }

    @GetMapping("/stop/{id}")
    public ResponseEntity<?> stopSession(@PathVariable long id) {
        boolean status = ticketingService.stopSession(id);
        return ResponseEntity.ok().body("Session stopped successfully.");
    }

    @GetMapping("/all-sessions/{vendorId}")
    public ResponseEntity<?> sessionsByVendorId(@PathVariable long vendorId){
        return ResponseEntity.ok(vendorService.showAllActiveSessions(vendorId));
    }

    @GetMapping("/all-sessions/inactive/{vendorId}")
    public ResponseEntity<?> inactiveSessionsByVendorId(@PathVariable long vendorId){
        return ResponseEntity.ok(vendorService.showAllInactiveSessions(vendorId));
    }

    @GetMapping("/all-sessions/session/{sessionId}")
    public ResponseEntity<?> getSessionsById(@PathVariable long sessionId){
        return ResponseEntity.ok(vendorService.getEvent(sessionId));
    }

    @GetMapping("/ticket-pool/{sessionId}")
    public ResponseEntity<?> getTicketPool(@PathVariable long sessionId){
        return ResponseEntity.ok(ticketingService.getTicketPool(sessionId));
    }
}

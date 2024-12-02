package com.tickentia.backend.controllers;

import com.tickentia.backend.dto.SuccessResponse;
import com.tickentia.backend.dto.TicketPurchaseRequest;
import com.tickentia.backend.service.customer.CustomerService;
import com.tickentia.backend.service.ticketing.TicketingService;
import com.tickentia.backend.service.ticketing.TicketingServiceImplementation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin(origins = "http://localhost:4200")
public class Customer {
    private final CustomerService customerService;
    private final TicketingService ticketingService;

    public Customer(CustomerService customerService, TicketingService ticketingService) {
        this.customerService = customerService;
        this.ticketingService = ticketingService;
    }

    @PostMapping("/events/purchase-ticket")
    public ResponseEntity<?> purchaseTicket(@RequestBody TicketPurchaseRequest ticketPurchaseRequest){
        boolean status = ticketingService.purchaseTickets(ticketPurchaseRequest);
        if (status){
            SuccessResponse successResponse = new SuccessResponse(true, "Tickets purchased successfully !");
            return ResponseEntity.ok(successResponse);
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    @GetMapping("/events")
    public ResponseEntity<?> getEvents(){
        return ResponseEntity.ok(customerService.getAllRegularEvents());
    }

    @GetMapping("/events/vip")
    public ResponseEntity<?> getAllEvents(){
        return ResponseEntity.ok(customerService.getAllEvents());
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<?> getEvent(@PathVariable long id){
        return ResponseEntity.ok(customerService.getEvent(id));
    }

    @GetMapping("/history/{customerId}")
    public ResponseEntity<?> bookingHistory(@PathVariable long customerId){
        return ResponseEntity.ok(customerService.bookingHistory(customerId));
    }
}

package com.tickentia.backend.controllers;

import com.tickentia.backend.dto.DeleteAccount;
import com.tickentia.backend.dto.SignUpRequest;
import com.tickentia.backend.dto.UpdatePassword;
import com.tickentia.backend.service.admin.AdminService;
import com.tickentia.backend.service.ticketing.TicketingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/admin")
public class Admin {
    private final AdminService adminService;
    private final TicketingService ticketingService;

    public Admin(AdminService adminService, TicketingService ticketingService) {
        this.adminService = adminService;
        this.ticketingService = ticketingService;
    }

    @GetMapping("/vendors")
    public ResponseEntity<?> getVendors(){
        return ResponseEntity.ok(adminService.listVendors());
    }

    @GetMapping("/customers")
    public ResponseEntity<?> getCustomers(){
        return ResponseEntity.ok(adminService.listCustomers());
    }

    @PostMapping("/delete-account")
    public ResponseEntity<?> removeAccount(@RequestBody DeleteAccount deleteAccount){
        if (Objects.equals(deleteAccount.getUserType(), "VENDOR")){
            boolean status = adminService.deleteVendor(deleteAccount.getEmail());
            return ResponseEntity.ok("Vendor deleted !");
        } else if (Objects.equals(deleteAccount.getUserType(), "CUSTOMER")){
            boolean status = adminService.deleteCustomer(deleteAccount.getEmail());
            return ResponseEntity.ok("Customer deleted !");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete user");
    }

    @PostMapping("/update")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePassword updatePassword){
        boolean success = adminService.updatePassword(updatePassword);
        if (success){
            return ResponseEntity.ok("Updated the password");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update password");
    }

    @PostMapping("/add-user")
    public ResponseEntity<?> addUser(@RequestBody SignUpRequest signUpRequest){
        boolean success = adminService.addUser(signUpRequest);
        if (success){
            return ResponseEntity.ok("Added a user");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add user");
    }

    @GetMapping("/session/stop/{sessionId}")
    public ResponseEntity<?> stopSession(@PathVariable long sessionId){
        boolean status = ticketingService.stopSession(sessionId);
        if (status){
            return ResponseEntity.ok("Ticket pool stopped");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add stop ticket pool");
    }

    @GetMapping("/events")
    public ResponseEntity<?> getEvents(){
        return ResponseEntity.ok(adminService.getAllEvents());
    }

    @GetMapping("/ticket-pools/{sessionId}")
    public ResponseEntity<?> getTicketPool(@PathVariable long sessionId){
        return ResponseEntity.ok(ticketingService.getTicketPool(sessionId));
    }
}

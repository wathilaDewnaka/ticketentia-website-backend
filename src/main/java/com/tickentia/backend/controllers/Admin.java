package com.tickentia.backend.controllers;

import com.tickentia.backend.dto.ManageRequest;
import com.tickentia.backend.service.admin.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/admin")
public class Admin {
    private final AdminService adminService;

    public Admin(AdminService adminService) {
        this.adminService = adminService;
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
    public ResponseEntity<?> removeAccount(@RequestBody ManageRequest manageRequest){
        if (Objects.equals(manageRequest.getUserType(), "VENDOR")){
            adminService.deleteVendor(manageRequest.getEmail());
        } else if (Objects.equals(manageRequest.getUserType(), "CUSTOMER")){
            adminService.deleteCustomer(manageRequest.getEmail());
        }
        return ResponseEntity.ok("");
    }

    @GetMapping("/events")
    public ResponseEntity<?> getEvents(){
        return ResponseEntity.ok(adminService.getAllEvents());
    }

    public void removeAccount(){

    }

    public void listAccount(){

    }
}

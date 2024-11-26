package com.tickentia.backend.controllers;

import com.tickentia.backend.dto.AuthenticationResponse;
import com.tickentia.backend.dto.LoginRequest;
import com.tickentia.backend.dto.SignUpRequest;
import com.tickentia.backend.entities.AdminDetails;
import com.tickentia.backend.entities.Customers;
import com.tickentia.backend.entities.Vendors;
import com.tickentia.backend.others.JWTService;
import com.tickentia.backend.respositary.AdminRepository;
import com.tickentia.backend.respositary.CustomerRepository;
import com.tickentia.backend.respositary.VendorRepository;
import com.tickentia.backend.service.auth.AuthorizationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class Authorization {
    private final AuthorizationService authorizationService;
    private final AuthenticationManager authenticationManager;
    private final CustomerRepository customerRepository;
    private final VendorRepository vendorRepository;
    private final AdminRepository adminRepository;
    private final JWTService jwtService;

    public Authorization(AuthorizationService authorizationService, AuthenticationManager authenticationManager, CustomerRepository customerRepository, VendorRepository vendorRepository, AdminRepository adminRepository, JWTService jwtService) {
        this.authorizationService = authorizationService;
        this.authenticationManager = authenticationManager;
        this.customerRepository = customerRepository;
        this.vendorRepository = vendorRepository;
        this.adminRepository = adminRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest){
        if (Objects.equals(signUpRequest.getUserType(), "CUSTOMER")){
            if (authorizationService.isCustomerExist(signUpRequest.getEmail())){
                return new ResponseEntity<>("User already exists", HttpStatus.NOT_ACCEPTABLE);
            }
            Customers createdCustomer = authorizationService.registerCustomer(signUpRequest);
            return ResponseEntity.ok(createdCustomer);
        } else {
            if (authorizationService.isVendorExist(signUpRequest.getEmail())){
                return new ResponseEntity<>("User already exists", HttpStatus.NOT_ACCEPTABLE);
            }
            Vendors createdVendor = authorizationService.registerVendor(signUpRequest);
            return ResponseEntity.ok(createdVendor);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            if (Objects.equals(loginRequest.getUserType(), "CUSTOMER")) {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("customer." + loginRequest.getEmail(), loginRequest.getPassword()));

                Optional<Customers> customers = customerRepository.findByEmail(loginRequest.getEmail());

                if (customers.isPresent()) {
                    Customers customer = customers.get();
                    String jwtToken = jwtService.generateToken(customer);
                    String role = (customer.getCustomerType().equals("CUSTOMER")) ? "CUSTOMER" : "VIP-CUSTOMER";

                    AuthenticationResponse authenticationResponse = new AuthenticationResponse(jwtToken, customer.getName(), role, customer.getCustomerId());
                    return ResponseEntity.ok(authenticationResponse);
                } else {
                    return ResponseEntity.status(404).body("User not found");
                }
            } else if (Objects.equals(loginRequest.getUserType(), "VENDOR")) {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("vendor." + loginRequest.getEmail(), loginRequest.getPassword()));
                Optional<Vendors> vendors = vendorRepository.findByEmail(loginRequest.getEmail());

                if (vendors.isPresent()) {
                    Vendors vendor = vendors.get();
                    String jwtToken = jwtService.generateToken(vendor);

                    AuthenticationResponse authenticationResponse = new AuthenticationResponse(jwtToken, vendor.getName(), "VENDOR", vendor.getVendorId());
                    return ResponseEntity.ok(authenticationResponse);
                } else {
                    return ResponseEntity.status(404).body("User not found");
                }
            } else {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("admin." + loginRequest.getEmail(), loginRequest.getPassword()));

                Optional<AdminDetails> adminDetails = adminRepository.findByEmail(loginRequest.getEmail());
                if (adminDetails.isPresent()){
                    AdminDetails admin = adminDetails.get();
                    String jwtToken = jwtService.generateToken(admin);

                    AuthenticationResponse authenticationResponse = new AuthenticationResponse(jwtToken, admin.getName(), "ADMIN", admin.getAdminId());
                    return ResponseEntity.ok(authenticationResponse);
                } else {
                    return ResponseEntity.status(404).body("User not found");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(403).body("Invalid credentials");
        }
    }
}

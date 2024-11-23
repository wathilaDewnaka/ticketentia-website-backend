package com.tickentia.backend.service.auth;

import com.tickentia.backend.controllers.Vendor;
import com.tickentia.backend.dto.LoginRequest;
import com.tickentia.backend.dto.SignUpRequest;
import com.tickentia.backend.entities.Customers;
import com.tickentia.backend.entities.Vendors;
import com.tickentia.backend.respositary.CustomerRepository;
import com.tickentia.backend.respositary.VendorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorizationServiceImplementation implements AuthorizationService {
    private final CustomerRepository customerRepository;
    private final VendorRepository vendorRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthorizationServiceImplementation(CustomerRepository customerRepository, VendorRepository vendorRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.vendorRepository = vendorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Vendors registerVendor(SignUpRequest signUpRequest) {
        Vendors vendor = new Vendors(signUpRequest.getFirstName() + " " + signUpRequest.getLastName(), signUpRequest.getEmail(), passwordEncoder.encode(signUpRequest.getPassword()), signUpRequest.getCountry(), signUpRequest.getAddress(), signUpRequest.getTelephone(), signUpRequest.getBrOrNICNumber());
        return vendorRepository.save(vendor);
    }

    @Override
    public Customers registerCustomer(SignUpRequest signUpRequest) {
        Customers customer = new Customers(signUpRequest.getFirstName() + " " + signUpRequest.getLastName(), signUpRequest.getEmail(), passwordEncoder.encode(signUpRequest.getPassword()), signUpRequest.getTelephone(), signUpRequest.getAddress(), signUpRequest.getCountry(), signUpRequest.getBrOrNICNumber(), "CUSTOMER", 0);
        return customerRepository.save(customer);
    }

    @Override
    public boolean isCustomerExist(String email) {
        Optional<Customers> customers = customerRepository.findByEmail(email);
        if (customers.isPresent()){
            return true;
        }
        return false;
    }

    @Override
    public boolean isVendorExist(String email) {
        Optional<Vendors> vendors = vendorRepository.findByEmail(email);
        if (vendors.isPresent()){
            return true;
        }
        return false;
    }
}

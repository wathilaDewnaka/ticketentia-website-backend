package com.tickentia.backend.service.auth;

import com.tickentia.backend.controllers.Vendor;
import com.tickentia.backend.dto.LoginRequest;
import com.tickentia.backend.dto.SignUpRequest;
import com.tickentia.backend.entities.AdminDetails;
import com.tickentia.backend.entities.Customers;
import com.tickentia.backend.entities.Vendors;
import com.tickentia.backend.enums.UserType;
import com.tickentia.backend.respositary.AdminRepository;
import com.tickentia.backend.respositary.CustomerRepository;
import com.tickentia.backend.respositary.VendorRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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
        Vendors vendor = new Vendors(capitalize(signUpRequest.getFirstName() + " " + signUpRequest.getLastName()), signUpRequest.getEmail().toLowerCase().strip(), passwordEncoder.encode(signUpRequest.getPassword().strip()), capitalize(signUpRequest.getCountry()), capitalize(signUpRequest.getAddress()), signUpRequest.getTelephone(), signUpRequest.getBrOrNICNumber());
        return vendorRepository.save(vendor);
    }

    @Override
    public Customers registerCustomer(SignUpRequest signUpRequest) {
        Customers customer = new Customers(capitalize(signUpRequest.getFirstName() + " " + signUpRequest.getLastName()), signUpRequest.getEmail().toLowerCase().strip(), passwordEncoder.encode(signUpRequest.getPassword().strip()), signUpRequest.getTelephone(), capitalize(signUpRequest.getAddress()), capitalize(signUpRequest.getCountry()), signUpRequest.getBrOrNICNumber(), UserType.CUSTOMER.name(), 0);
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

    private String capitalize(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        String[] words = input.split(" "); // Split the input by spaces
        String result = ""; // Initialize an empty string to hold the final result

        for (String word : words) {
            if (!word.isEmpty()) {
                // Capitalize the first letter and add the rest of the word in lowercase
                result += Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase() + " ";
            }
        }

        return result.trim(); // Remove any extra trailing space
    }
}

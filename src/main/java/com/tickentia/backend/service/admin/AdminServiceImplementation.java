package com.tickentia.backend.service.admin;

import com.tickentia.backend.entities.AdminDetails;
import com.tickentia.backend.entities.Customers;
import com.tickentia.backend.entities.Sessions;
import com.tickentia.backend.entities.Vendors;
import com.tickentia.backend.respositary.AdminRepository;
import com.tickentia.backend.respositary.CustomerRepository;
import com.tickentia.backend.respositary.SessionsRepository;
import com.tickentia.backend.respositary.VendorRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImplementation implements AdminService{
    private final VendorRepository vendorRepository;
    private final CustomerRepository customerRepository;
    private final AdminRepository adminRepository;
    private final SessionsRepository sessionsRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminServiceImplementation(VendorRepository vendorRepository, CustomerRepository customerRepository, AdminRepository adminRepository, SessionsRepository sessionsRepository, PasswordEncoder passwordEncoder) {
        this.vendorRepository = vendorRepository;
        this.customerRepository = customerRepository;
        this.adminRepository = adminRepository;
        this.sessionsRepository = sessionsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void createAdminAccount(){
        Optional<AdminDetails> adminDetails = adminRepository.findByEmail("admin@ticketentia.com");
        if (adminDetails.isEmpty()){
            AdminDetails admin = new AdminDetails("Admin", "admin@ticketentia.com", passwordEncoder.encode("Admin@Ticketentia-Main-123"));
            adminRepository.save(admin);
        }
    }

    @Override
    public boolean deleteVendor(String email) {
        Optional<Vendors> vendors = vendorRepository.findByEmail(email);
        if (vendors.isPresent()) {
            vendorRepository.deleteByEmail(email);
            return true;
        }

        return false;
    }

    @Override
    public boolean deleteCustomer(String email) {
        Optional<Customers> customers = customerRepository.findByEmail(email);
        if (customers.isPresent()){
            customerRepository.deleteByEmail(email);
            return true;
        }
        return false;
    }

    @Override
    public List<Vendors> listVendors() {
        return vendorRepository.findAll();
    }

    @Override
    public List<Customers> listCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public List<Sessions> getAllEvents() {
        return sessionsRepository.findAll();
    }
}

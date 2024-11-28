package com.tickentia.backend.service.admin;

import com.tickentia.backend.dto.DeleteAccount;
import com.tickentia.backend.dto.SignUpRequest;
import com.tickentia.backend.dto.UpdatePassword;
import com.tickentia.backend.entities.AdminDetails;
import com.tickentia.backend.entities.Customers;
import com.tickentia.backend.entities.Sessions;
import com.tickentia.backend.entities.Vendors;
import com.tickentia.backend.enums.UserType;
import com.tickentia.backend.respositary.AdminRepository;
import com.tickentia.backend.respositary.CustomerRepository;
import com.tickentia.backend.respositary.SessionsRepository;
import com.tickentia.backend.respositary.VendorRepository;
import jakarta.annotation.PostConstruct;
import org.apache.catalina.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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
    public boolean deleteAccount(DeleteAccount deleteAccount) {
        if (Objects.equals(deleteAccount.getUserType(), UserType.VENDOR.name())){
            Optional<Vendors> vendor = vendorRepository.findByEmail(deleteAccount.getEmail());

            if (vendor.isPresent()){
                List<Sessions> sessions = sessionsRepository.findByVendorId(vendor.get().getVendorId());
                for (Sessions session: sessions){

                }

                vendorRepository.delete(vendor.get());
                return true;
            }
        }
        Optional<Customers> customers = customerRepository.findByEmail(deleteAccount.getEmail());

        if (customers.isPresent()){
            customerRepository.delete(customers.get());
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
        return sessionsRepository.findByIsActiveTrue();
    }

    @Override
    public boolean updatePassword(UpdatePassword updatePassword) {
        if (Objects.equals(updatePassword.getUserType(), UserType.VENDOR.name())){
            Optional<Vendors> optionalVendor = vendorRepository.findByEmail(updatePassword.getEmail());

            if (optionalVendor.isPresent()){
                Vendors vendor = optionalVendor.get();
                vendor.setPassword(passwordEncoder.encode(updatePassword.getPassword()));
                vendorRepository.save(vendor);
                return true;
            }
        }

        Optional<Customers> optionalCustomer = customerRepository.findByEmail(updatePassword.getEmail());

        if (optionalCustomer.isPresent()){
            Customers customer = optionalCustomer.get();
            customer.setPassword(passwordEncoder.encode(updatePassword.getPassword()));
            customerRepository.save(customer);
            return true;
        }
        return false;
    }

    @Override
    public boolean addUser(SignUpRequest signUpRequest) {
        if (signUpRequest.getUserType().equals(UserType.VENDOR.name())){
            Vendors vendor = new Vendors(signUpRequest.getFirstName() + " " + signUpRequest.getLastName(), signUpRequest.getEmail(), passwordEncoder.encode(signUpRequest.getPassword()), signUpRequest.getCountry(), signUpRequest.getAddress(), signUpRequest.getTelephone(), signUpRequest.getBrOrNICNumber());
            vendorRepository.save(vendor);
            return true;

        } else if(signUpRequest.getUserType().equals(UserType.CUSTOMER.name())){
            Customers customer = new Customers(signUpRequest.getFirstName() + " " + signUpRequest.getLastName(), signUpRequest.getEmail(), passwordEncoder.encode(signUpRequest.getPassword()), signUpRequest.getTelephone(), signUpRequest.getAddress(), signUpRequest.getCountry(), signUpRequest.getBrOrNICNumber(), "CUSTOMER", 0);
            customerRepository.save(customer);
            return true;

        }

        return false;
    }
}

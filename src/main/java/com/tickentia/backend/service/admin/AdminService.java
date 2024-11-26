package com.tickentia.backend.service.admin;

import com.tickentia.backend.dto.SignUpRequest;
import com.tickentia.backend.dto.UpdatePassword;
import com.tickentia.backend.entities.Customers;
import com.tickentia.backend.entities.Sessions;
import com.tickentia.backend.entities.Vendors;

import java.util.List;

public interface AdminService {
    boolean deleteVendor(String email);

    boolean deleteCustomer(String email);

    List<Vendors> listVendors();

    List<Customers> listCustomers();

    List<Sessions> getAllEvents();

    boolean updatePassword(UpdatePassword updatePassword);

    boolean addUser(SignUpRequest signUpRequest);

}

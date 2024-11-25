package com.tickentia.backend.service.auth;

import com.tickentia.backend.controllers.Vendor;
import com.tickentia.backend.dto.LoginRequest;
import com.tickentia.backend.dto.SignUpRequest;
import com.tickentia.backend.entities.Customers;
import com.tickentia.backend.entities.Vendors;

import java.util.List;

public interface AuthorizationService {
    Vendors registerVendor(SignUpRequest signUpRequest);

    Customers registerCustomer(SignUpRequest signUpRequest);

    boolean isCustomerExist(String email);

    boolean isVendorExist(String email);
}

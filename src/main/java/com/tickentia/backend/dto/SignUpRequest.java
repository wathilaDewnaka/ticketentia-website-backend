package com.tickentia.backend.dto;

public class SignUpRequest extends LoginRequest{
    private String firstName;
    private String lastName;
    private String address;
    private String country;
    private String brOrNICNumber;
    private String telephone;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getCountry() {
        return country;
    }

    public String getBrOrNICNumber() {
        return brOrNICNumber;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setBrOrNICNumber(String brOrNICNumber) {
        this.brOrNICNumber = brOrNICNumber;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}

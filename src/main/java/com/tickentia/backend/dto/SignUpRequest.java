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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBrOrNICNumber() {
        return brOrNICNumber;
    }

    public void setBrOrNICNumber(String brOrNICNumber) {
        this.brOrNICNumber = brOrNICNumber;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public String toString() {
        return "SignUpRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", country='" + country + '\'' +
                ", brOrNICNumber='" + brOrNICNumber + '\'' +
                ", telephone='" + telephone + '\'' +
                '}';
    }
}

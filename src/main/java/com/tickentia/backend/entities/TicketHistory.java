package com.tickentia.backend.entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TicketHistory")
public class TicketHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long recordId;

    private long customerId;
    private int quantity;
    private String eventName;
    private String eventTime;
    private String seatNumbers;
    private String eventDate;
    private Date purchasedDate;
    private double discount;
    private double ticketPrice;
    private double totalPrice;
    private String eventVenue;
    private String returnedImage;

    @Lob
    private byte[] eventImage;

    public TicketHistory(){ }

    public TicketHistory(long customerId, int quantity, String eventName, String eventTime, String seatNumbers, String eventDate, Date purchasedDate, double discount, double ticketPrice, double totalPrice, String eventVenue, byte[] eventImage) {
        this.customerId = customerId;
        this.quantity = quantity;
        this.eventName = eventName;
        this.eventTime = eventTime;
        this.seatNumbers = seatNumbers;
        this.eventDate = eventDate;
        this.purchasedDate = purchasedDate;
        this.discount = discount;
        this.ticketPrice = ticketPrice;
        this.totalPrice = totalPrice;
        this.eventVenue = eventVenue;
        this.eventImage = eventImage;
    }

    public long getRecordId() {
        return recordId;
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getSeatNumbers() {
        return seatNumbers;
    }

    public void setSeatNumbers(String seatNumbers) {
        this.seatNumbers = seatNumbers;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public Date getPurchasedDate() {
        return purchasedDate;
    }

    public void setPurchasedDate(Date purchasedDate) {
        this.purchasedDate = purchasedDate;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getEventVenue() {
        return eventVenue;
    }

    public void setEventVenue(String eventVenue) {
        this.eventVenue = eventVenue;
    }

    public String getReturnedImage() {
        return returnedImage;
    }

    public void setReturnedImage(String returnedImage) {
        this.returnedImage = returnedImage;
    }

    public byte[] getEventImage() {
        return eventImage;
    }

    public void setEventImage(byte[] eventImage) {
        this.eventImage = eventImage;
    }
}

package com.tickentia.backend.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "Sessions")
public class Sessions {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long sessionId;

    private long vendorId;
    private String vendorName;
    private String vendorEmail;
    private String eventName;
    private String eventVenue;
    private String eventCategory;
    private String eventTime;
    private String eventDescription;
    private String eventType;
    private String returnedImage;
    private String eventDate;
    private Date postedDate;
    private double ticketPrice;
    private double vipDiscount;
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;
    private boolean isActive;

    @Lob
    private byte[] eventImage;
    
    public Sessions() { }

    public Sessions(String vendorName, long vendorId, String vendorEmail, String eventName, String eventVenue, String eventCategory, String eventTime, String eventDescription, String eventType, String eventDate, Date postedDate, double ticketPrice, double vipDiscount, int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity, byte[] eventImage) {
        this.vendorName = vendorName;
        this.vendorId = vendorId;
        this.vendorEmail = vendorEmail;
        this.eventName = eventName;
        this.eventVenue = eventVenue;
        this.eventCategory = eventCategory;
        this.eventTime = eventTime;
        this.eventDescription = eventDescription;
        this.eventType = eventType;
        this.eventDate = eventDate;
        this.postedDate = postedDate;
        this.ticketPrice = ticketPrice;
        this.vipDiscount = vipDiscount;
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketCapacity = maxTicketCapacity;
        this.eventImage = eventImage;
        this.isActive = true;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public long getVendorId() {
        return vendorId;
    }

    public void setVendorId(long vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorEmail() {
        return vendorEmail;
    }

    public void setVendorEmail(String vendorEmail) {
        this.vendorEmail = vendorEmail;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventVenue() {
        return eventVenue;
    }

    public void setEventVenue(String eventVenue) {
        this.eventVenue = eventVenue;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        System.out.println(maxTicketCapacity);
        System.out.println(totalTickets);
        if (maxTicketCapacity > totalTickets){
            this.maxTicketCapacity = maxTicketCapacity;
//            throw new IllegalArgumentException("Pool ticket count greater than total tickets");
        }
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public double getVipDiscount() {
        return vipDiscount;
    }

    public void setVipDiscount(double vipDiscount) {
        this.vipDiscount = vipDiscount;
    }

    public byte[] getEventImage() {
        return eventImage;
    }

    public void setEventImage(byte[] eventImage) {
        this.eventImage = eventImage;
    }

    public String getReturnedImage() {
        return returnedImage;
    }

    public void setReturnedImage(String returnedImage) {
        this.returnedImage = returnedImage;
    }

}

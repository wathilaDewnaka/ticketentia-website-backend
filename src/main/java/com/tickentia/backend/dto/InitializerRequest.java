package com.tickentia.backend.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public class InitializerRequest {
    private long vendorId;

    private MultipartFile eventImage;
    private String eventName;
    private String eventVenue;
    private String eventCategory;
    private String eventTime;
    private String eventDescription;
    private String eventType;
    private String eventDate;
    private double ticketPrice;
    private double vipDiscount;
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;

    public void setVendorId(long vendorId) {
        this.vendorId = vendorId;
    }

    public void setEventImage(MultipartFile eventImage) {
        this.eventImage = eventImage;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setEventVenue(String eventVenue) {
        this.eventVenue = eventVenue;
    }

    public void setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public void setVipDiscount(double vipDiscount) {
        this.vipDiscount = vipDiscount;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public long getVendorId() {
        return vendorId;
    }

    public MultipartFile getEventImage() {
        return eventImage;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventVenue() {
        return eventVenue;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public String getEventTime() {
        return eventTime;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public String getEventType() {
        return eventType;
    }

    public String getEventDate() {
        return eventDate;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public double getVipDiscount() {
        return vipDiscount;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    @Override
    public String toString() {
        return "InitializerRequest{" +
                "vendorId=" + vendorId +
                ", eventImage=" + eventImage +
                ", eventName='" + eventName + '\'' +
                ", eventVenue='" + eventVenue + '\'' +
                ", eventCategory='" + eventCategory + '\'' +
                ", eventTime='" + eventTime + '\'' +
                ", eventDescription='" + eventDescription + '\'' +
                ", eventType='" + eventType + '\'' +
                ", eventDate='" + eventDate + '\'' +
                ", ticketPrice=" + ticketPrice +
                ", vipDiscount=" + vipDiscount +
                ", totalTickets=" + totalTickets +
                ", ticketReleaseRate=" + ticketReleaseRate +
                ", customerRetrievalRate=" + customerRetrievalRate +
                ", maxTicketCapacity=" + maxTicketCapacity +
                '}';
    }
}

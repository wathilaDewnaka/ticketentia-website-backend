package com.tickentia.backend.dto;

public class Ticket {
    private int seatNo;
    private String eventName;
    private double eventPrice;

    public Ticket(int seatNo, String eventName, double eventPrice) {
        this.seatNo = seatNo;
        this.eventName = eventName;
        this.eventPrice = eventPrice;
    }

    public int getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(int seatNo) {
        this.seatNo = seatNo;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public double getEventPrice() {
        return eventPrice;
    }

    public void setEventPrice(double eventPrice) {
        this.eventPrice = eventPrice;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "seatNo=" + seatNo +
                ", eventName='" + eventName + '\'' +
                ", eventPrice=" + eventPrice +
                '}';
    }
}

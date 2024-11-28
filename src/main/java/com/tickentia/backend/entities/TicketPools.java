package com.tickentia.backend.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "TicketPools")
public class TicketPools {
    @Id
    private long sessionId;

    private int currentTicketsInPool;
    private int maxTicketCapacity;
    private int releaseTicketCount;
    private int totalTickets;
    private boolean isActive;

    public TicketPools(){

    }

    public TicketPools(long sessionId, int currentTicketsInPool, int maxTicketCapacity, int releaseTicketCount, int totalTickets) {
        this.sessionId = sessionId;
        this.currentTicketsInPool = currentTicketsInPool;
        this.maxTicketCapacity = maxTicketCapacity;
        this.releaseTicketCount = releaseTicketCount;
        this.totalTickets = totalTickets;
        this.isActive = true;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public int getCurrentTicketsInPool() {
        return currentTicketsInPool;
    }

    public void setCurrentTicketsInPool(int currentTicketsInPool) {
        this.currentTicketsInPool = currentTicketsInPool;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public int getReleaseTicketCount() {
        return releaseTicketCount;
    }

    public void setReleaseTicketCount(int releaseTicketCount) {
        this.releaseTicketCount = releaseTicketCount;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "TicketPools{" +
                "sessionId=" + sessionId +
                ", currentTicketsInPool=" + currentTicketsInPool +
                ", maxTicketCapacity=" + maxTicketCapacity +
                ", releaseTicketCount=" + releaseTicketCount +
                ", totalTickets=" + totalTickets +
                ", isActive=" + isActive +
                '}';
    }
}

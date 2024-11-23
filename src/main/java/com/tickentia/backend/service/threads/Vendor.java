package com.tickentia.backend.service.threads;

import com.tickentia.backend.dto.Ticket;
import com.tickentia.backend.entities.Sessions;
import com.tickentia.backend.service.ticketpool.TicketPool;

public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final Sessions sessions;
    private int releaseCount;

    public Vendor(TicketPool ticketPool, Sessions sessions, int releaseCount) {
        this.ticketPool = ticketPool;
        this.sessions = sessions;
        this.releaseCount = releaseCount;
    }

    public Vendor(TicketPool ticketPool, Sessions sessions) {
        this.ticketPool = ticketPool;
        this.sessions = sessions;
    }

    @Override
    public void run() {
        /* Start from 1 and run until the total tickets are finished
         If previous seat number is there in DB (Where stopped previously) extract the seat number and continue from there
         If it's stopped at 50, then it should start with 51 likewise and run until total tickets are over */
        int ticketsReleased = releaseCount > 0 ? releaseCount + 1 : 1;


        while (ticketsReleased <= sessions.getTotalTickets()) {
            try {
                Ticket ticket = new Ticket(ticketsReleased, sessions.getEventName(), sessions.getTicketPrice());
                ticketPool.addTicket(ticket);

                Thread.sleep(sessions.getTicketReleaseRate());
                ticketsReleased++;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}

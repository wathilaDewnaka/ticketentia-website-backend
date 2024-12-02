package com.tickentia.backend.service.ticketpool;

import com.tickentia.backend.dto.Ticket;
import com.tickentia.backend.entities.Sessions;
import com.tickentia.backend.entities.TicketPools;
import com.tickentia.backend.respositary.TicketPoolsRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

public class TicketPool {
    private List<Ticket> tickets;
    private int maxCapacity;
    private boolean isActive;
    private long sessionId;
    private SimpMessagingTemplate simpMessagingTemplate;
    private TicketPoolsRepository ticketPoolsRepository;

    public TicketPool(long sessionId, int maxCapacity, TicketPoolsRepository ticketPoolsRepository, SimpMessagingTemplate simpMessagingTemplate) {
        this.maxCapacity = maxCapacity;
        this.sessionId = sessionId;
        this.ticketPoolsRepository = ticketPoolsRepository;
        this.tickets = new Vector<>();
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.isActive = true;
    }

    public TicketPool(Sessions sessions, TicketPools ticketPools, TicketPoolsRepository ticketPoolsRepository, SimpMessagingTemplate simpMessagingTemplate){
        this.maxCapacity = sessions.getMaxTicketCapacity();
        this.sessionId = sessions.getSessionId();
        this.ticketPoolsRepository = ticketPoolsRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.tickets = new Vector<>();
        this.isActive = true;

        int lastSoldSeatNumber = ticketPools.getReleaseTicketCount() - ticketPools.getCurrentTicketsInPool(); // 470

        for (int i = 1; i <= ticketPools.getCurrentTicketsInPool() ; i++) {
            lastSoldSeatNumber++;
            Ticket ticket = new Ticket(lastSoldSeatNumber, sessions.getEventName(),sessions.getTicketPrice());
            tickets.add(ticket);
        }
    }

    public synchronized void addTicket(Ticket ticket) throws InterruptedException {
        while (tickets.size() == maxCapacity) {
            if (!isActive) {
                System.out.println("Pool is inactive. Stopping addTicket.");
                throw new InterruptedException("Pool is inactive");
            }

            wait();
        }

        if (!isActive) {
            System.out.println("Pool is inactive. Stopping addTicket.");
            throw new InterruptedException("Pool is inactive");
        }

        Optional<TicketPools> ticketPools = ticketPoolsRepository.findById(sessionId);
        if (ticketPools.isPresent()) {
            TicketPools ticketPool = ticketPools.get();
            ticketPool.setCurrentTicketsInPool(ticketPool.getCurrentTicketsInPool() + 1);
            ticketPool.setReleaseTicketCount(ticketPool.getReleaseTicketCount() + 1);
            ticketPool.setTotalTickets(ticketPool.getTotalTickets() - 1);
            ticketPoolsRepository.save(ticketPool);

            simpMessagingTemplate.convertAndSend("/topic/updates/" + sessionId, ticketPool);
        }

        tickets.add(ticket);
        System.out.println("Ticket added by " + Thread.currentThread().getName() + ". Total tickets: " + tickets.size());
        notifyAll();
    }

    public synchronized Ticket removeTicket() throws InterruptedException {
        while (tickets.isEmpty()) {
            if (!isActive) {
                System.out.println("Pool is inactive. Stopping removeTicket.");
                throw new InterruptedException("Pool is inactive");
            }
            wait();
        }

        if (!isActive) {
            System.out.println("Pool is inactive. Stopping removeTicket.");
            throw new InterruptedException("Pool is inactive");
        }

        Optional<TicketPools> ticketPools = ticketPoolsRepository.findById(sessionId);
        if (ticketPools.isPresent()) {
            TicketPools ticketPool = ticketPools.get();
            ticketPool.setCurrentTicketsInPool(ticketPool.getCurrentTicketsInPool() - 1);
            ticketPoolsRepository.save(ticketPool);

            simpMessagingTemplate.convertAndSend("/topic/updates/" + sessionId, ticketPool);
        }

        Ticket ticket = tickets.removeFirst();
        notifyAll();
        System.out.println("Ticket removed by " + Thread.currentThread().getName() + ". Total tickets: " + tickets.size());
        return ticket;

    }

    public synchronized void setActive(boolean active) {
        this.isActive = active;

        if (!this.isActive) {
            notifyAll();
            System.out.println("Ticket pool has been set to inactive. All threads will stop.");
        }
    }
}

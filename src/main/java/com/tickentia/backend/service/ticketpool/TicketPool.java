package com.tickentia.backend.service.ticketpool;

// Importing necessary classes for managing tickets, sessions, and messaging
import com.tickentia.backend.dto.Ticket;
import com.tickentia.backend.entities.Sessions;
import com.tickentia.backend.entities.TicketPools;
import com.tickentia.backend.respositary.SessionsRepository;
import com.tickentia.backend.respositary.TicketPoolsRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;
import java.util.Optional;
import java.util.Vector;

public class TicketPool {
    private List<Ticket> tickets; // A thread-safe list to manage tickets in the pool
    private int maxCapacity; // Maximum capacity of the ticket pool
    private long sessionId; // ID of the session associated with this ticket pool
    private SimpMessagingTemplate simpMessagingTemplate; // For sending real-time updates via WebSocket
    private TicketPoolsRepository ticketPoolsRepository; // Repository for accessing ticket pool data
    private SessionsRepository sessionsRepository; // Repository for accessing session data
    private volatile boolean isActive; // Indicates if the ticket pool is active

    // Constructor: Initializes the ticket pool with sessionId and max capacity
    public TicketPool(long sessionId, int maxCapacity, TicketPoolsRepository ticketPoolsRepository, SessionsRepository sessionsRepository, SimpMessagingTemplate simpMessagingTemplate) {
        this.maxCapacity = maxCapacity;
        this.sessionId = sessionId;
        this.ticketPoolsRepository = ticketPoolsRepository;
        this.sessionsRepository = sessionsRepository;
        this.tickets = new Vector<>(); // Using Vector for thread-safe operations
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.isActive = true;
    }

    // Constructor: Initializes ticket pool using session and ticket pool objects
    public TicketPool(Sessions sessions, TicketPools ticketPools, TicketPoolsRepository ticketPoolsRepository, SessionsRepository sessionsRepository, SimpMessagingTemplate simpMessagingTemplate) {
        this.maxCapacity = sessions.getMaxTicketCapacity();
        this.sessionId = sessions.getSessionId();
        this.ticketPoolsRepository = ticketPoolsRepository;
        this.sessionsRepository = sessionsRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.tickets = new Vector<>();
        this.isActive = true;

        // Calculate starting seat number and populate the pool with tickets
        int lastSoldSeatNumber = ticketPools.getReleaseTicketCount() - ticketPools.getCurrentTicketsInPool();
        for (int i = 1; i <= ticketPools.getCurrentTicketsInPool(); i++) {
            lastSoldSeatNumber++;
            Ticket ticket = new Ticket(lastSoldSeatNumber, sessions.getEventName(), sessions.getTicketPrice());
            tickets.add(ticket);
        }
    }

    // Adds a ticket to the pool in a thread-safe manner
    public synchronized void addTicket(Ticket ticket) throws InterruptedException {
        // Wait if the pool is full or inactive
        while (tickets.size() == maxCapacity) {
            if (!isActive) {
                System.out.println("Pool is inactive. Stopping addTicket.");
                throw new InterruptedException("Pool is inactive");
            }
            wait();
        }

        // Check for inactivity again after wait
        if (!isActive) {
            System.out.println("Pool is inactive. Stopping addTicket.");
            throw new InterruptedException("Pool is inactive");
        }

        // Update ticket pool data in the repository
        Optional<TicketPools> ticketPools = ticketPoolsRepository.findById(sessionId);
        if (ticketPools.isPresent()) {
            TicketPools ticketPool = ticketPools.get();
            ticketPool.setCurrentTicketsInPool(ticketPool.getCurrentTicketsInPool() + 1);
            ticketPool.setReleaseTicketCount(ticketPool.getReleaseTicketCount() + 1);
            ticketPool.setTotalTickets(ticketPool.getTotalTickets() - 1);
            ticketPoolsRepository.save(ticketPool);

            // Send WebSocket update
            simpMessagingTemplate.convertAndSend("/topic/updates/" + sessionId, ticketPool);
        }

        // Add the ticket to the pool and notify waiting threads
        tickets.add(ticket);
        System.out.println("Ticket added by " + Thread.currentThread().getName() + " for event id " + sessionId + " Total tickets: " + tickets.size());
        notifyAll();
    }

    // Removes a ticket from the pool in a thread-safe manner
    public synchronized Ticket removeTicket() throws InterruptedException {
        Optional<Sessions> sessions = sessionsRepository.findById(sessionId);
        Optional<TicketPools> ticketPools = ticketPoolsRepository.findById(sessionId);

        // Wait if the pool is empty or inactive
        while (tickets.isEmpty()) {
            if (!isActive) {
                System.out.println("Pool is inactive. Stopping removeTicket.");
                throw new InterruptedException("Pool is inactive");
            }
            if (ticketPools.isPresent() && ticketPools.get().getTotalTickets() == 0 && sessions.isPresent()) {
                // Deactivate session and pool when all tickets are sold
                ticketPools.get().setActive(false);
                sessions.get().setActive(false);
                sessionsRepository.save(sessions.get());
                ticketPoolsRepository.save(ticketPools.get());

                throw new InterruptedException("Pool is inactive");
            }
            wait();
        }

        // Check for inactivity again after wait
        if (!isActive) {
            System.out.println("Pool is inactive. Stopping removeTicket.");
            throw new InterruptedException("Pool is inactive");
        }

        // Update ticket pool data in the repository
        if (ticketPools.isPresent()) {
            TicketPools ticketPool = ticketPools.get();
            ticketPool.setCurrentTicketsInPool(ticketPool.getCurrentTicketsInPool() - 1);
            ticketPoolsRepository.save(ticketPool);

            // Send WebSocket update
            simpMessagingTemplate.convertAndSend("/topic/updates/" + sessionId, ticketPool);
        }

        // Remove a ticket from the pool and notify waiting threads
        Ticket ticket = tickets.remove(0);
        notifyAll();
        System.out.println("Ticket removed by " + Thread.currentThread().getName() + " event id is " + sessionId + ". Total tickets: " + tickets.size());
        return ticket;
    }

    // Sets the activity status of the ticket pool and notifies waiting threads
    public synchronized void setActive(boolean active) {
        this.isActive = active;

        // Notify all threads if the pool becomes inactive
        if (!this.isActive) {
            notifyAll();
            System.out.println("Ticket pool has been set to inactive. All threads will stop.");
        }
    }
}

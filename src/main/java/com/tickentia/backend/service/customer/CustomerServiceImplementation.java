package com.tickentia.backend.service.customer;

import com.tickentia.backend.entities.Sessions;
import com.tickentia.backend.entities.TicketHistory;
import com.tickentia.backend.enums.EventType;
import com.tickentia.backend.respositary.SessionsRepository;
import com.tickentia.backend.respositary.TicketHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImplementation implements CustomerService {
    private final SessionsRepository sessionsRepository;
    private final TicketHistoryRepository ticketHistoryRepository;

    public CustomerServiceImplementation(SessionsRepository sessionsRepository, TicketHistoryRepository ticketHistoryRepository) {
        this.sessionsRepository = sessionsRepository;
        this.ticketHistoryRepository = ticketHistoryRepository;
    }

    @Override
    public List<Sessions> getAllEvents() {
        List<Sessions> sessions = sessionsRepository.findByIsActiveTrue();

        return sessions.stream().map(session -> {
            if (session.getEventImage() != null) {
                // Encode the byte array to a Base64 string
                String base64Image = Base64.getEncoder().encodeToString(session.getEventImage());
                session.setReturnedImage(base64Image);  // Set the encoded image
            }
            return session;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Sessions> getAllRegularEvents() {
        List<Sessions> sessions = sessionsRepository.findByIsActiveTrueAndEventTypeEquals(EventType.REGULAR.name());

        return sessions.stream().map(session -> {
            if (session.getEventImage() != null) {
                // Encode the byte array to a Base64 string
                String base64Image = Base64.getEncoder().encodeToString(session.getEventImage());
                session.setReturnedImage(base64Image);  // Set the encoded image
            }
            return session;
        }).collect(Collectors.toList());
    }

    @Override
    public Sessions getEvent(long sessionId) {
        Optional<Sessions> session = sessionsRepository.findById(sessionId);

        Sessions sessionCurrent = session.get();
        if (!sessionCurrent.isActive()){
            return null;
        } else if (sessionCurrent.getEventImage() != null) {
            sessionCurrent.setReturnedImage(Base64.getEncoder().encodeToString(sessionCurrent.getEventImage()));
        }

        return sessionCurrent;
    }

    @Override
    public List<TicketHistory> bookingHistory(long customerId) {
        List<TicketHistory> ticketHistories = ticketHistoryRepository.findAllByCustomerId(customerId);
        return ticketHistories.stream().map(session -> {
            if (session.getEventImage() != null) {
                // Encode the byte array to a Base64 string
                String base64Image = Base64.getEncoder().encodeToString(session.getEventImage());
                session.setReturnedImage(base64Image);  // Set the encoded image
            }
            return session;
        }).collect(Collectors.toList());
    }
}

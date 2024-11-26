package com.tickentia.backend.service.ticketing;

import com.tickentia.backend.dto.InitializerRequest;
import com.tickentia.backend.dto.TicketPurchaseRequest;
import com.tickentia.backend.entities.TicketPools;

import java.io.IOException;

public interface TicketingService {
    boolean configureSession(InitializerRequest initializerRequest) throws IOException;

    boolean purchaseTickets(TicketPurchaseRequest ticketPurchaseRequest);

    TicketPools getTicketPool(long sessionId);

    boolean stopSession(long sessionId);
}

package com.tickentia.backend.service.ticketing;

import com.tickentia.backend.dto.InitializerRequest;
import com.tickentia.backend.dto.TicketPurchaseRequest;

import java.io.IOException;

public interface TicketingService {
    boolean configureSession(InitializerRequest initializerRequest) throws IOException;

    boolean purchaseTickets(TicketPurchaseRequest ticketPurchaseRequest);

    boolean stopSession(long sessionId);
}

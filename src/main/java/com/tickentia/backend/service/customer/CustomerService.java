package com.tickentia.backend.service.customer;

import com.tickentia.backend.entities.Sessions;
import com.tickentia.backend.entities.TicketHistory;

import java.util.List;

public interface CustomerService {
    List<Sessions> getAllEvents();

    List<Sessions> getAllRegularEvents();

    Sessions getEvent(long sessionId);

    List<TicketHistory> bookingHistory(long id);
}

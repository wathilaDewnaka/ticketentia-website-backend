package com.tickentia.backend.service.vendor;

import com.tickentia.backend.dto.InitializerRequest;
import com.tickentia.backend.entities.Sessions;
import com.tickentia.backend.entities.TicketPools;
//import com.tickentia.backend.entities.TicketPools;

import java.util.List;

public interface VendorService {
    List<Sessions> showAllActiveSessions(long vendorId);

    List<Sessions> showAllInactiveSessions(long vendorId);

    Sessions getEvent(long sessionId);

    TicketPools getTicketPoolBySession(long sessionId);

}

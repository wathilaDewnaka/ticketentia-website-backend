package com.tickentia.backend.service.vendor;

import com.tickentia.backend.entities.Sessions;
import com.tickentia.backend.entities.TicketPools;
import com.tickentia.backend.respositary.SessionsRepository;
import com.tickentia.backend.respositary.TicketPoolsRepository;
import com.tickentia.backend.respositary.VendorRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VendorServiceImplementation implements VendorService {
    private final SessionsRepository sessionsRepository;
    private final VendorRepository vendorRepository;
    private final TicketPoolsRepository ticketPoolsRepository;

    public VendorServiceImplementation(SessionsRepository sessionsRepository, VendorRepository vendorRepository, TicketPoolsRepository ticketPoolsRepository) {
        this.sessionsRepository = sessionsRepository;
        this.vendorRepository = vendorRepository;
        this.ticketPoolsRepository = ticketPoolsRepository;
    }

    @Override
    public List<Sessions> showAllActiveSessions(long vendorId) {
        return sessionsRepository.findByVendorIdAndIsActiveTrue(vendorId);
    }

    @Override
    public List<Sessions> showAllInactiveSessions(long vendorId) {
        return sessionsRepository.findByVendorIdAndIsActiveFalse(vendorId);
    }

    @Override
    public Sessions getEvent(long sessionId) {
        Optional<Sessions> session = sessionsRepository.findById(sessionId);

        Sessions sessionCurrent = session.get();
        if(sessionCurrent.getEventImage() != null){
            sessionCurrent.setReturnedImage(Base64.getEncoder().encodeToString(sessionCurrent.getEventImage()));
        }

        return sessionCurrent;
    }

    @Override
    public TicketPools getTicketPoolBySession(long sessionId) {
        Optional<TicketPools> ticketPools = ticketPoolsRepository.findById(sessionId);

        if (ticketPools.isPresent()){
            return ticketPools.get();
        }

        return null;
    }
}

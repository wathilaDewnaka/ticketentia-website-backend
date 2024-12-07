package com.tickentia.backend.service.ticketing;

import com.tickentia.backend.dto.InitializerRequest;
import com.tickentia.backend.dto.SuccessResponse;
import com.tickentia.backend.dto.TicketPurchaseRequest;
import com.tickentia.backend.entities.Customers;
import com.tickentia.backend.entities.Sessions;
import com.tickentia.backend.entities.TicketPools;
import com.tickentia.backend.entities.Vendors;
import com.tickentia.backend.enums.EventType;
import com.tickentia.backend.enums.UserType;
import com.tickentia.backend.respositary.*;
import com.tickentia.backend.service.threads.Customer;
import com.tickentia.backend.service.threads.Vendor;
import com.tickentia.backend.service.ticketpool.TicketPool;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TicketingServiceImplementation implements TicketingService {
    private final VendorRepository vendorRepository;
    private final SessionsRepository sessionsRepository;
    private final CustomerRepository customerRepository;
    private final TicketHistoryRepository ticketHistoryRepository;
    private final TicketPoolsRepository ticketPoolsRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final Map<Long, TicketPool> ticketPoolHashMap;

    public TicketingServiceImplementation(VendorRepository vendorRepository, SessionsRepository sessionsRepository, CustomerRepository customerRepository, TicketHistoryRepository ticketHistoryRepository, TicketPoolsRepository ticketPoolsRepository, SimpMessagingTemplate simpMessagingTemplate) {
        this.vendorRepository = vendorRepository;
        this.sessionsRepository = sessionsRepository;
        this.customerRepository = customerRepository;
        this.ticketHistoryRepository = ticketHistoryRepository;
        this.ticketPoolsRepository = ticketPoolsRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.ticketPoolHashMap = new ConcurrentHashMap<>();
    }

    @PostConstruct
    public void createTicketPoolsByDB(){
        List<TicketPools> ticketPools = ticketPoolsRepository.findAllByIsActiveTrue();

        for (TicketPools ticketPool : ticketPools) {
            Optional<Sessions> currentSession = sessionsRepository.findById(ticketPool.getSessionId());
            if (currentSession.isPresent()) {
                TicketPool ticketPoolObj = new TicketPool(currentSession.get(), ticketPool, ticketPoolsRepository, simpMessagingTemplate);
                ticketPoolHashMap.put(currentSession.get().getSessionId(), ticketPoolObj);

                Runnable vendor = new Vendor(ticketPoolObj, currentSession.get(), ticketPool.getReleaseTicketCount());
                Thread thread = new Thread(vendor, "Vendor ID - " + currentSession.get().getVendorId());
                thread.start();
            }
        }
    }

    @Override
    public boolean configureSession(InitializerRequest initializerRequest) throws IOException {
        Optional<Vendors> vendorDetails = vendorRepository.findById(initializerRequest.getVendorId());
        System.out.println(initializerRequest);

        if (vendorDetails.isPresent()){
            Vendors currentVendor = vendorDetails.get();

            byte[] imageData = (initializerRequest.getEventImage() != null) ? initializerRequest.getEventImage().getBytes() : null;

            Sessions newSession = new Sessions(currentVendor.getName(), currentVendor.getVendorId(), currentVendor.getEmail(), capitalize(initializerRequest.getEventName()), capitalize(initializerRequest.getEventVenue()), initializerRequest.getEventCategory().toUpperCase(), initializerRequest.getEventTime(), capitalize(initializerRequest.getEventDescription()), initializerRequest.getEventType().toUpperCase(), initializerRequest.getEventDate(), new Date(), initializerRequest.getTicketPrice(), initializerRequest.getVipDiscount(), initializerRequest.getTotalTickets(), initializerRequest.getTicketReleaseRate(), initializerRequest.getCustomerRetrievalRate(), initializerRequest.getMaxTicketCapacity(), imageData);
            sessionsRepository.save(newSession);

            // Configuring the entity of ticket pool for that event
            TicketPools newTicketPool = new TicketPools(newSession.getSessionId(), 0, newSession.getMaxTicketCapacity(), 0, newSession.getTotalTickets());
            ticketPoolsRepository.save(newTicketPool); // Saving the records in the table

            // Creating the shared ticket pool object
            TicketPool ticketPool = new TicketPool(newSession.getSessionId(), initializerRequest.getMaxTicketCapacity(), ticketPoolsRepository, simpMessagingTemplate);
            ticketPoolHashMap.put(newSession.getSessionId(), ticketPool);

            Runnable vendor = new Vendor(ticketPool, newSession);
            Thread vendorThread = new Thread(vendor, "Vendor ID - " + currentVendor.getVendorId());
            vendorThread.start();
            return true;

        }

        return false;
    }

    @Override
    public SuccessResponse purchaseTickets(TicketPurchaseRequest ticketPurchaseRequest) {
        Optional<Sessions> sessions = sessionsRepository.findById(ticketPurchaseRequest.getSessionId());
        Optional<Customers> customers = customerRepository.findById(ticketPurchaseRequest.getCustomerId());

        if (sessions.isPresent() && customers.isPresent()) {
            Sessions session = sessions.get();

            if (EventType.VIP.name().equals(session.getEventType()) && !UserType.VIP_CUSTOMER.name().equals(customers.get().getCustomerType())) {
                return null;
            }

            TicketPool ticketPool = ticketPoolHashMap.get(ticketPurchaseRequest.getSessionId());

            Runnable customer = new Customer(customerRepository, ticketHistoryRepository, session, ticketPool, ticketPurchaseRequest);
            Thread customerThread = new Thread(customer, "Customer ID - " + customers.get().getCustomerId());

            SuccessResponse successResponse = new SuccessResponse(true, "Tickets purchased successfully !");
            if (customers.get().getCustomerType().equals(UserType.VIP_CUSTOMER.name())){
                successResponse.setUserRole(UserType.VIP_CUSTOMER.name());
            }

            if (UserType.VIP_CUSTOMER.name().equals(customers.get().getCustomerType())){
                customerThread.setPriority(Thread.MAX_PRIORITY);
            }

            customerThread.start();
            return successResponse;
        }

        return null;
    }

    @Override
    public TicketPools getTicketPool(long sessionId) {
        Optional<TicketPools> ticketPools = ticketPoolsRepository.findById(sessionId);
        if (ticketPools.isPresent()){
            return ticketPools.get();
        }

        return null;
    }


    @Override
    public boolean stopSession(long sessionId) {
        Optional<Sessions> sessions = sessionsRepository.findById(sessionId);
        Optional<TicketPools> ticketPools = ticketPoolsRepository.findById(sessionId);

        if (sessions.isPresent() && ticketPools.isPresent()) {
            Sessions session = sessions.get();
            TicketPools ticketPool = ticketPools.get();

            session.setActive(false);
            ticketPool.setActive(false);

            sessionsRepository.save(session);
            ticketPoolsRepository.save(ticketPool);

            TicketPool ticketPoolShared = ticketPoolHashMap.get(sessionId);
            ticketPoolShared.setActive(false); // Terminate all the threads using this shared ticket pool

            return true;
        }

        return false;
    }

    private static String capitalize(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        String[] words = input.split(" "); // Split the input by spaces
        String result = ""; // Initialize an empty string to hold the final result

        for (String word : words) {
            if (!word.isEmpty()) {
                // Capitalize the first letter and add the rest of the word in lowercase
                result += Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase() + " ";
            }
        }

        return result.trim(); // Remove any extra trailing space
    }
}

package com.tickentia.backend.service.threads;

import com.tickentia.backend.dto.Ticket;
import com.tickentia.backend.dto.TicketPurchaseRequest;
import com.tickentia.backend.entities.Customers;
import com.tickentia.backend.entities.Sessions;
import com.tickentia.backend.entities.TicketHistory;
import com.tickentia.backend.enums.UserType;
import com.tickentia.backend.respositary.CustomerRepository;
import com.tickentia.backend.respositary.TicketHistoryRepository;
import com.tickentia.backend.service.ticketpool.TicketPool;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Customer implements Runnable{
    private final CustomerRepository customerRepository;
    private final TicketHistoryRepository ticketHistoryRepository;
    private final Sessions session;
    private final TicketPurchaseRequest ticketPurchaseRequest;
    private final TicketPool ticketPool;
    private final int customerRetrievalRate;
    private final List<Ticket> purchasedTickets;

    public Customer(CustomerRepository customerRepository, TicketHistoryRepository ticketHistoryRepository, Sessions session, TicketPool ticketPool, TicketPurchaseRequest ticketPurchaseRequest) {
        this.customerRepository = customerRepository;
        this.ticketHistoryRepository = ticketHistoryRepository;
        this.session = session;
        this.ticketPool = ticketPool;
        this.ticketPurchaseRequest = ticketPurchaseRequest;
        this.customerRetrievalRate = session.getCustomerRetrievalRate();
        this.purchasedTickets = new LinkedList<>();
    }

    @Override
    public void run() {
        Optional<Customers> optionalCustomer = customerRepository.findById(ticketPurchaseRequest.getCustomerId());
        double totalPurchases = 0;

        if (optionalCustomer.isPresent()){
            Customers customer = optionalCustomer.get();

            for (int i = 1; i <= ticketPurchaseRequest.getQuantity(); i++) {
                try {
                    Ticket ticket = ticketPool.removeTicket();
                    totalPurchases += ticket.getEventPrice();

                    purchasedTickets.add(ticket);

                    Thread.sleep(customerRetrievalRate * 1000L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            updateValues(customer, totalPurchases);

        }
    }

    private void updateValues(Customers customer, double totalPurchase){
        if (!purchasedTickets.isEmpty()) {
            String seatNumbers = "";
            for (Ticket ti : purchasedTickets){
                seatNumbers += "Seat - " + ti.getSeatNo() + ", ";
            }

            // Update customer type if eligible for VIP status
            if (customer.getTotalPurchases() >= 50000 && UserType.CUSTOMER.name().equals(customer.getCustomerType())) {
                customer.setCustomerType(UserType.VIP_CUSTOMER.name());
            }
            seatNumbers = seatNumbers.substring(0, seatNumbers.length() - 2);

            // Calculate discount and update customer purchases
            double discount = (customer.getTotalPurchases() >= 50000) ? session.getVipDiscount() : 0;
            double discountedAmount = totalPurchase * discount / 100;
            double finalAmount = totalPurchase - discountedAmount;

            TicketHistory ticketHistory = new TicketHistory(
                    ticketPurchaseRequest.getCustomerId(),
                    purchasedTickets.size(),
                    session.getEventName(),
                    session.getEventTime(),
                    seatNumbers,
                    session.getEventDate(),
                    new Date(),
                    discount,
                    session.getTicketPrice(),
                    finalAmount,
                    session.getEventVenue(),
                    session.getEventImage()
            );
            // Update customer total purchases
            customer.setTotalPurchases(customer.getTotalPurchases() + finalAmount);

            // Save ticket history and customer
            ticketHistoryRepository.save(ticketHistory);
            customerRepository.save(customer);
        }
    }
}

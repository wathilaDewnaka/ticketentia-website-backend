package com.tickentia.backend.respositary;

import com.tickentia.backend.entities.TicketHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketHistoryRepository extends JpaRepository<TicketHistory, Long> {
    List<TicketHistory> findAllByCustomerId(long customerId);
}

package com.tickentia.backend.respositary;

import com.tickentia.backend.entities.TicketPools;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketPoolsRepository extends JpaRepository<TicketPools, Long> {
    List<TicketPools> findAllByIsActiveTrue();
}

package com.tickentia.backend.respositary;

import com.tickentia.backend.entities.Customers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customers, Long> {
    Optional<Customers> findByEmail(String email);

    void deleteByEmail(String email);
}

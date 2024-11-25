package com.tickentia.backend.respositary;

import com.tickentia.backend.entities.AdminDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<AdminDetails, Long> {
    Optional<AdminDetails> findByEmail(String email);

}

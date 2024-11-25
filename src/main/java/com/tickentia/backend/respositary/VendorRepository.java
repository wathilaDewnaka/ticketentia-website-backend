package com.tickentia.backend.respositary;

import com.tickentia.backend.entities.Vendors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendors, Long> {
    Optional<Vendors> findByEmail(String username);

    void deleteByEmail(String email);
}

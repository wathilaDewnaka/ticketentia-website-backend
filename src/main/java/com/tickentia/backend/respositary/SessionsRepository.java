package com.tickentia.backend.respositary;

import com.tickentia.backend.entities.Sessions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionsRepository extends JpaRepository<Sessions, Long> {
    List<Sessions> findByIsActiveTrue();

    List<Sessions> findByIsActiveTrueAndEventTypeEquals(String eventType);

    List<Sessions> findByVendorIdAndIsActiveTrue(long vendorId);

    List<Sessions> findByVendorIdAndIsActiveFalse(long vendorId);

    List<Sessions> findByVendorId(long vendorId);
}

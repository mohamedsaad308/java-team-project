package com.edfaaly.backend.repository;

import com.edfaaly.backend.model.Trip;
import com.edfaaly.backend.model.User;
import com.edfaaly.backend.model.enums.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Long> {
    Optional<Trip> findByDriverAndStatus(User driver, TripStatus status);
    List<Trip> findByDriverOrderByStartedAtDesc(User driver);
}

package com.edfaaly.backend.repository;

import com.edfaaly.backend.model.Transaction;
import com.edfaaly.backend.model.Trip;
import com.edfaaly.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByRiderOrderByCreatedAtDesc(User rider);
    List<Transaction> findByDriverOrderByCreatedAtDesc(User driver);
    List<Transaction> findByTripOrderByCreatedAtDesc(Trip trip);
    java.util.Optional<Transaction> findByClientRequestId(String clientRequestId);
}

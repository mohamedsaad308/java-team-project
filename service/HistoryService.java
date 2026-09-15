package com.edfaaly.backend.service;

import com.edfaaly.backend.dto.TransactionResponse;
import com.edfaaly.backend.model.Transaction;
import com.edfaaly.backend.model.User;
import com.edfaaly.backend.model.enums.UserType;
import com.edfaaly.backend.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final TransactionRepository transactionRepository;

    public List<TransactionResponse> getRiderHistory(User rider) {
        return transactionRepository.findByRiderOrderByCreatedAtDesc(rider).stream()
                .map(tx -> toResponse(tx, UserType.RIDER))
                .toList();
    }

    public List<TransactionResponse> getDriverHistory(User driver) {
        return transactionRepository.findByDriverOrderByCreatedAtDesc(driver).stream()
                .map(tx -> toResponse(tx, UserType.DRIVER))
                .toList();
    }

    private TransactionResponse toResponse(Transaction tx, UserType viewerType) {
        String counterpartyName = viewerType == UserType.RIDER
                ? tx.getDriver().getFullName()
                : tx.getRider().getFullName();

        return new TransactionResponse(
                tx.getId(),
                counterpartyName,
                tx.getAmount(),
                tx.getStatus().name(),
                tx.getPaymentMethod().name(),
                tx.getCreatedAt(),
                tx.getTrip() != null ? tx.getTrip().getId() : null
        );
    }
}

package com.edfaaly.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TransactionResponse {
    private Long transactionId;
    private String counterpartyName; // اسم السائق (للراكب) أو اسم الراكب (للسائق)
    private Double amount;
    private String status;
    private String paymentMethod;
    private LocalDateTime dateTime;
    private Long tripId;
}

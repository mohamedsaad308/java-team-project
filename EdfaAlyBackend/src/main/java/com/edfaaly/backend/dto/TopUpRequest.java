package com.edfaaly.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TopUpRequest {
    @NotNull
    @Positive
    private Double amount;
}

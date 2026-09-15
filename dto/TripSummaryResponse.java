package com.edfaaly.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TripSummaryResponse {
    private Long tripId;
    private String status;
    private String fromLocation;
    private String toLocation;
    private Double totalCollected;
    private Integer ridersCount;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
}

package com.edfaaly.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DriverSummaryResponse {
    private Long driverId;
    private String fullName;
    private String driverShortCode;
    private Double ratingAvg;
    private Long activeTripId; // null لو مفيش مشوار نشط حاليًا
}

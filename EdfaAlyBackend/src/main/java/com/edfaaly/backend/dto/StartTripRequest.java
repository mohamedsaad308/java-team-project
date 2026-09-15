package com.edfaaly.backend.dto;

import lombok.Data;

@Data
public class StartTripRequest {
    private String fromLocation; // اختياري - مثلاً "رمسيس"
    private String toLocation;   // اختياري - مثلاً "السلام"
}

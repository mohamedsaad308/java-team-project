package com.edfaaly.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Long userId;
    private String userType;
    private String driverShortCode; // موجود فقط للسائق
}

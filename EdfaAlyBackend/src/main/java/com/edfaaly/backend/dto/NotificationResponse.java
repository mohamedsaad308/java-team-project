package com.edfaaly.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NotificationResponse {
    private Long id;
    private String title;
    private String body;
    private Boolean isRead;
    private LocalDateTime createdAt;
}

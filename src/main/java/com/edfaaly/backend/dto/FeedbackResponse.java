package com.edfaaly.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class FeedbackResponse {
    private String riderName;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}

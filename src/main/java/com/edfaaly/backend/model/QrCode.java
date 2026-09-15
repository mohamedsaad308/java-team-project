package com.edfaaly.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "qr_codes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QrCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = false)
    private User driver;

    @Column(nullable = false, unique = true)
    private String qrValue;

    private Boolean isActive = true;

    private LocalDateTime createdAt = LocalDateTime.now();
}

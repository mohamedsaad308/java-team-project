package com.edfaaly.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "feedback")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "transaction_id", nullable = false, unique = true)
    private Transaction transaction;

    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = false)
    private User driver;

    @ManyToOne
    @JoinColumn(name = "rider_id", nullable = false)
    private User rider;

    @Column(nullable = false)
    private Integer rating; // 1 - 5

    @Column(length = 500)
    private String comment;

    private LocalDateTime createdAt = LocalDateTime.now();
}

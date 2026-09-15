package com.edfaaly.backend.model;

import com.edfaaly.backend.model.enums.TripStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "trips")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = false)
    private User driver;

    /** مثلاً: "رمسيس" - اختياري */
    private String fromLocation;

    /** مثلاً: "السلام" - اختياري */
    private String toLocation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TripStatus status = TripStatus.ACTIVE;

    /**
     * إجمالي المبلغ المُجمّع خلال المشوار - بيتحدّث تلقائيًا مع كل عملية دفع ناجحة.
     */
    @Column(nullable = false)
    private Double totalCollected = 0.0;

    /** عدد الركاب اللي دفعوا خلال المشوار ده */
    private Integer ridersCount = 0;

    private LocalDateTime startedAt = LocalDateTime.now();

    private LocalDateTime endedAt;
}

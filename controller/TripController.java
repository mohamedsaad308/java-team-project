package com.edfaaly.backend.controller;

import com.edfaaly.backend.dto.StartTripRequest;
import com.edfaaly.backend.dto.TripSummaryResponse;
import com.edfaaly.backend.security.CurrentUserProvider;
import com.edfaaly.backend.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;
    private final CurrentUserProvider currentUserProvider;

    /** السائق يبدأ مشوار جديد - يقفل تلقائيًا أي مشوار نشط سابق */
    @PostMapping("/start")
    public TripSummaryResponse startTrip(@RequestBody(required = false) StartTripRequest request) {
        StartTripRequest req = request != null ? request : new StartTripRequest();
        return tripService.startTrip(currentUserProvider.get(), req);
    }

    /** إجمالي المشوار الحالي - يُستخدم لتحديث الشاشة لحظة بلحظة */
    @GetMapping("/active")
    public TripSummaryResponse getActiveTrip() {
        return tripService.getActiveTrip(currentUserProvider.get());
    }

    /** إنهاء المشوار - المبلغ يكون بالفعل في المحفظة أول بأول مع كل عملية */
    @PostMapping("/end")
    public TripSummaryResponse endTrip() {
        return tripService.endTrip(currentUserProvider.get());
    }
}

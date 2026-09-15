package com.edfaaly.backend.service;

import com.edfaaly.backend.dto.StartTripRequest;
import com.edfaaly.backend.dto.TripSummaryResponse;
import com.edfaaly.backend.model.Trip;
import com.edfaaly.backend.model.User;
import com.edfaaly.backend.model.enums.TripStatus;
import com.edfaaly.backend.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;

    /**
     * نافذة السماح: لو راكب دفع خلال المدة دي بعد ما السائق قفل المشوار (نسيان، تأخير شبكة...الخ)
     * العملية بتتحسب لسه على نفس المشوار المقفول بدل ما تتحسب "مشوار مستقل" أو تروح من غير مشوار خالص.
     */
    private static final long LATE_PAYMENT_GRACE_MINUTES = 10;

    @Transactional
    public TripSummaryResponse startTrip(User driver, StartTripRequest request) {
        // لو فيه مشوار نشط بالفعل، اقفله تلقائيًا الأول (مفيش أكتر من مشوار نشط في نفس الوقت)
        tripRepository.findByDriverAndStatus(driver, TripStatus.ACTIVE)
                .ifPresent(this::closeTripInternal);

        Trip trip = new Trip();
        trip.setDriver(driver);
        trip.setFromLocation(request.getFromLocation());
        trip.setToLocation(request.getToLocation());
        trip.setStatus(TripStatus.ACTIVE);
        trip.setTotalCollected(0.0);
        trip.setRidersCount(0);
        trip.setStartedAt(LocalDateTime.now());

        return toSummary(tripRepository.save(trip));
    }

    public TripSummaryResponse getActiveTrip(User driver) {
        Trip trip = tripRepository.findByDriverAndStatus(driver, TripStatus.ACTIVE)
                .orElseThrow(() -> new IllegalStateException("لا يوجد مشوار نشط حاليًا"));
        return toSummary(trip);
    }

    public Trip getActiveTripEntity(User driver) {
        return tripRepository.findByDriverAndStatus(driver, TripStatus.ACTIVE).orElse(null);
    }

    /**
     * بيرجع المشوار اللي لازم تتحسب عليه عملية دفع جديدة:
     * 1) المشوار النشط لو موجود
     * 2) أو آخر مشوار اتقفل من أقل من {@link #LATE_PAYMENT_GRACE_MINUTES} دقايق (عملية متأخرة)
     * 3) أو null (العملية تتسجل من غير ربط بمشوار)
     */
    public Trip getTripForNewPayment(User driver, boolean[] outIsLateAddition) {
        Trip active = getActiveTripEntity(driver);
        if (active != null) {
            outIsLateAddition[0] = false;
            return active;
        }

        List<Trip> recent = tripRepository.findByDriverOrderByStartedAtDesc(driver);
        if (!recent.isEmpty()) {
            Trip lastTrip = recent.get(0);
            if (lastTrip.getStatus() == TripStatus.CLOSED && lastTrip.getEndedAt() != null
                    && lastTrip.getEndedAt().isAfter(LocalDateTime.now().minusMinutes(LATE_PAYMENT_GRACE_MINUTES))) {
                outIsLateAddition[0] = true;
                return lastTrip;
            }
        }

        outIsLateAddition[0] = false;
        return null;
    }

    @Transactional
    public TripSummaryResponse endTrip(User driver) {
        Trip trip = tripRepository.findByDriverAndStatus(driver, TripStatus.ACTIVE)
                .orElseThrow(() -> new IllegalStateException("لا يوجد مشوار نشط لإنهائه"));
        closeTripInternal(trip);
        return toSummary(trip);
    }

    private void closeTripInternal(Trip trip) {
        trip.setStatus(TripStatus.CLOSED);
        trip.setEndedAt(LocalDateTime.now());
        tripRepository.save(trip);
        // ملحوظة: تحويل المبلغ فعليًا للمحفظة بيحصل أول بأول مع كل عملية دفع ناجحة
        // (شوف PaymentService) - مش وقت قفل المشوار، عشان الرصيد يكون متاح للسائق فورًا.
    }

    @Transactional
    public void addToTrip(Trip trip, double amount) {
        trip.setTotalCollected(trip.getTotalCollected() + amount);
        trip.setRidersCount(trip.getRidersCount() + 1);
        tripRepository.save(trip);
    }

    private TripSummaryResponse toSummary(Trip trip) {
        return new TripSummaryResponse(
                trip.getId(),
                trip.getStatus().name(),
                trip.getFromLocation(),
                trip.getToLocation(),
                trip.getTotalCollected(),
                trip.getRidersCount(),
                trip.getStartedAt(),
                trip.getEndedAt()
        );
    }
}

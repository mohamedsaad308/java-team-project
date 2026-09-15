package com.edfaaly.backend.service;

import com.edfaaly.backend.dto.DriverSummaryResponse;
import com.edfaaly.backend.dto.ResolveDriverRequest;
import com.edfaaly.backend.model.Trip;
import com.edfaaly.backend.model.User;
import com.edfaaly.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverResolutionService {

    private final QrCodeService qrCodeService;
    private final UserRepository userRepository;
    private final TripService tripService;

    public DriverSummaryResponse resolve(ResolveDriverRequest request) {
        User driver;

        if (request.getQrValue() != null && !request.getQrValue().isBlank()) {
            driver = qrCodeService.resolveDriverByQr(request.getQrValue());
        } else if (request.getManualCode() != null && !request.getManualCode().isBlank()) {
            String code = request.getManualCode().trim().toUpperCase();
            driver = userRepository.findByDriverShortCode(code)
                    .or(() -> userRepository.findByLicensePlate(code))
                    .orElseThrow(() -> new IllegalArgumentException("لم يتم العثور على سائق بهذا الكود"));
        } else {
            throw new IllegalArgumentException("لازم تحدد qrValue أو manualCode");
        }

        Trip activeTrip = tripService.getActiveTripEntity(driver);

        return new DriverSummaryResponse(
                driver.getId(),
                driver.getFullName(),
                driver.getDriverShortCode(),
                driver.getRatingAvg(),
                activeTrip != null ? activeTrip.getId() : null
        );
    }
}

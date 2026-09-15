package com.edfaaly.backend.controller;

import com.edfaaly.backend.model.QrCode;
import com.edfaaly.backend.security.CurrentUserProvider;
import com.edfaaly.backend.service.QrCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/driver")
@RequiredArgsConstructor
public class DriverController {

    private final QrCodeService qrCodeService;
    private final CurrentUserProvider currentUserProvider;

    @GetMapping("/qrcode")
    public Map<String, Object> getQrCode() {
        QrCode qr = qrCodeService.getOrCreateActiveCode(currentUserProvider.get());
        return Map.of("qrValue", qr.getQrValue(), "driverShortCode", currentUserProvider.get().getDriverShortCode());
    }

    @PostMapping("/qrcode/regenerate")
    public Map<String, Object> regenerateQrCode() {
        QrCode qr = qrCodeService.regenerate(currentUserProvider.get());
        return Map.of("qrValue", qr.getQrValue());
    }

    @GetMapping("/profile")
    public Map<String, Object> getProfile() {
        var driver = currentUserProvider.get();
        return Map.of(
                "fullName", driver.getFullName(),
                "driverShortCode", driver.getDriverShortCode() != null ? driver.getDriverShortCode() : "",
                "ratingAvg", driver.getRatingAvg() != null ? driver.getRatingAvg() : 0.0,
                "ratingCount", driver.getRatingCount() != null ? driver.getRatingCount() : 0
        );
    }
}

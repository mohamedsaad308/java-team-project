package com.edfaaly.backend.service;

import com.edfaaly.backend.model.QrCode;
import com.edfaaly.backend.model.User;
import com.edfaaly.backend.repository.QrCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QrCodeService {

    private final QrCodeRepository qrCodeRepository;

    public QrCode getOrCreateActiveCode(User driver) {
        return qrCodeRepository.findByDriverAndIsActiveTrue(driver)
                .orElseGet(() -> createNewCode(driver));
    }

    @Transactional
    public QrCode regenerate(User driver) {
        qrCodeRepository.findByDriverAndIsActiveTrue(driver).ifPresent(old -> {
            old.setIsActive(false);
            qrCodeRepository.save(old);
        });
        return createNewCode(driver);
    }

    private QrCode createNewCode(User driver) {
        QrCode qr = new QrCode();
        qr.setDriver(driver);
        qr.setQrValue(UUID.randomUUID().toString()); // قيمة طويلة وآمنة تُرمّز داخل صورة QR فقط
        qr.setIsActive(true);
        return qrCodeRepository.save(qr);
    }

    public User resolveDriverByQr(String qrValue) {
        return qrCodeRepository.findByQrValueAndIsActiveTrue(qrValue)
                .map(QrCode::getDriver)
                .orElseThrow(() -> new IllegalArgumentException("كود QR غير صالح"));
    }
}

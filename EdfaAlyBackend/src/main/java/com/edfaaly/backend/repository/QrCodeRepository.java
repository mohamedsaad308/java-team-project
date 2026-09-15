package com.edfaaly.backend.repository;

import com.edfaaly.backend.model.QrCode;
import com.edfaaly.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QrCodeRepository extends JpaRepository<QrCode, Long> {
    Optional<QrCode> findByQrValueAndIsActiveTrue(String qrValue);
    Optional<QrCode> findByDriverAndIsActiveTrue(User driver);
}

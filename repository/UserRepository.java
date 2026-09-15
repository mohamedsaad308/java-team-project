package com.edfaaly.backend.repository;

import com.edfaaly.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByDriverShortCode(String driverShortCode);
    Optional<User> findByLicensePlate(String licensePlate);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByDriverShortCode(String driverShortCode);
}

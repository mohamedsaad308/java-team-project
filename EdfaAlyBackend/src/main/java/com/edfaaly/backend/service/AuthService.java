package com.edfaaly.backend.service;

import com.edfaaly.backend.dto.AuthResponse;
import com.edfaaly.backend.dto.LoginRequest;
import com.edfaaly.backend.dto.RegisterRequest;
import com.edfaaly.backend.model.User;
import com.edfaaly.backend.model.Wallet;
import com.edfaaly.backend.model.enums.UserType;
import com.edfaaly.backend.repository.UserRepository;
import com.edfaaly.backend.repository.WalletRepository;
import com.edfaaly.backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new IllegalArgumentException("رقم الموبايل مسجل بالفعل");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setUserType(request.getUserType());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setCardToken(request.getCardToken());

        if (request.getUserType() == UserType.DRIVER) {
            String plate = normalizePlate(request.getLicensePlate());
            if (plate == null || plate.isBlank()) {
                throw new IllegalArgumentException("رقم لوحة العربية إجباري لتسجيل السائق");
            }
            if (userRepository.findByLicensePlate(plate).isPresent()) {
                throw new IllegalArgumentException("رقم اللوحة ده مسجل بالفعل لسائق تاني");
            }
            user.setLicensePlate(plate);
            // الكود اللي هيكتبه الراكب يدويًا هو نفسه رقم اللوحة - فريد طبيعيًا، مفيش داعي لتوليد كود عشوائي
            user.setDriverShortCode(plate);
        }

        User saved = userRepository.save(user);

        Wallet wallet = new Wallet();
        wallet.setUser(saved);
        wallet.setBalance(0.0);
        walletRepository.save(wallet);

        String token = jwtUtil.generateToken(saved.getId(), saved.getUserType().name());
        return new AuthResponse(token, saved.getId(), saved.getUserType().name(), saved.getDriverShortCode());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new IllegalArgumentException("بيانات الدخول غير صحيحة"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("بيانات الدخول غير صحيحة");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUserType().name());
        return new AuthResponse(token, user.getId(), user.getUserType().name(), user.getDriverShortCode());
    }

    /**
     * تطبيع رقم اللوحة: إزالة المسافات الزيادة وتوحيد حالة الحروف (كابيتال)
     * عشان "أ ب ج 1234" و"ابج1234" ماتتحسبش كأرقام مختلفة وقت البحث اليدوي.
     */
    private String normalizePlate(String plate) {
        if (plate == null) return null;
        return plate.trim().replaceAll("\\s+", "").toUpperCase();
    }
}

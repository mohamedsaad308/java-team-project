package com.edfaaly.backend.config;

import com.edfaaly.backend.dto.RegisterRequest;
import com.edfaaly.backend.model.enums.UserType;
import com.edfaaly.backend.repository.UserRepository;
import com.edfaaly.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * بيانات تجريبية جاهزة عشان تختبر الـ APIs على طول من غير ما تسجّل يدويًا.
 * بتشتغل مرة واحدة بس (لو قاعدة البيانات فاضية)، وبتتقفل بالكامل بضبط
 * app.demo.seed-data=false في application.properties (مثلاً في بيئة الإنتاج).
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DemoDataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AuthService authService;

    @Value("${app.demo.seed-data:true}")
    private boolean seedData;

    private static final String DRIVER_PHONE = "01000000001";
    private static final String RIDER_PHONE = "01000000002";
    private static final String DEMO_PASSWORD = "demo1234";

    @Override
    public void run(String... args) {
        if (!seedData || userRepository.count() > 0) {
            return;
        }

        RegisterRequest driver = new RegisterRequest();
        driver.setFullName("Demo Driver");
        driver.setPhoneNumber(DRIVER_PHONE);
        driver.setPassword(DEMO_PASSWORD);
        driver.setUserType(UserType.DRIVER);
        driver.setLicensePlate("DEMO123");
        authService.register(driver);

        RegisterRequest rider = new RegisterRequest();
        rider.setFullName("Demo Rider");
        rider.setPhoneNumber(RIDER_PHONE);
        rider.setPassword(DEMO_PASSWORD);
        rider.setUserType(UserType.RIDER);
        authService.register(rider);

        log.info("======================================================");
        log.info(" Demo accounts seeded (password for both: {})", DEMO_PASSWORD);
        log.info("   DRIVER -> phoneNumber: {}  (licensePlate/manualCode: DEMO123)", DRIVER_PHONE);
        log.info("   RIDER  -> phoneNumber: {}", RIDER_PHONE);
        log.info(" Login via POST /api/auth/login to get a JWT for each,");
        log.info(" then use the 'Authorize' button in /swagger-ui.html.");
        log.info("======================================================");
    }
}

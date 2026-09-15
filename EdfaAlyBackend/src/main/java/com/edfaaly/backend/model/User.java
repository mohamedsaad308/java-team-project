package com.edfaaly.backend.model;

import com.edfaaly.backend.model.enums.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType userType;

    @Column(nullable = false)
    private String passwordHash;

    /**
     * لا يُخزَّن رقم الفيزا كامل أبدًا هنا — ده Token بيرجع من بوابة الدفع (Payment Gateway)
     * بعد ربط الكارت، عشان نلتزم بمعايير أمان الدفع (PCI-DSS) ونتجنب المسؤولية القانونية.
     * اختياري في المرحلة الحالية لأن الدفع بيتم بالمحفظة الداخلية/الخارجية بس - مفيش كارت مطلوب.
     */
    private String cardToken;

    /**
     * كود السائق المستخدم في الدفع اليدوي بديل مسح QR.
     * قرار: بنستخدم رقم لوحة العربية نفسه كـ"كود قصير" — لأن أرقام/حروف اللوحات فريدة
     * على مستوى النظام كله بطبيعتها (مفيش لوحتين بنفس الرقم)، فمفيش داعي لتوليد كود عشوائي منفصل.
     * لسه محتفظين بالعمود باسم driverShortCode عشان نفس الـ API القديم (Android/الموقع) يفضل شغال من غير تغيير.
     */
    @Column(unique = true)
    private String driverShortCode;

    /**
     * رقم لوحة العربية - إجباري وفريد لكل سائق (مصدر driverShortCode).
     * null بالنسبة للراكب.
     */
    @Column(unique = true)
    private String licensePlate;

    private Double ratingAvg = 0.0;

    private Integer ratingCount = 0;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}

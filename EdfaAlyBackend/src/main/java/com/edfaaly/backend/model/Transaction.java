package com.edfaaly.backend.model;

import com.edfaaly.backend.model.enums.PaymentInputType;
import com.edfaaly.backend.model.enums.PaymentMethod;
import com.edfaaly.backend.model.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rider_id", nullable = false)
    private User rider;

    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = false)
    private User driver;

    /**
     * المشوار اللي اتحسبت عليه العملية دي. ممكن يكون null لو حصلت العملية من غير مشوار مفتوح
     * (مثلاً في مرحلة الاختبار)، لكن الأفضل إجباريًا لاحقًا.
     */
    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @Column(nullable = false)
    private Double amount;

    /**
     * قيمة فريدة يبعتها تطبيق الموبايل مع كل محاولة دفع (مثلاً UUID بيتولد مرة واحدة قبل الإرسال).
     * لو الشبكة ضعيفة والتطبيق أعاد إرسال نفس الطلب، بنستخدمها عشان منحسبش العملية مرتين.
     */
    @Column(unique = true)
    private String clientRequestId;

    /**
     * true لو العملية اتحصلت بعد ما السائق قفل المشوار بفترة قصيرة (نافذة سماح)
     * وانضافت لآخر مشوار مقفول بدل ما تتحسب مشوار مستقل أو من غير مشوار خالص.
     */
    @Column(nullable = false)
    private Boolean lateAddition = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentInputType inputType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status = TransactionStatus.PENDING;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}

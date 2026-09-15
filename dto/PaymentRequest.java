package com.edfaaly.backend.dto;

import com.edfaaly.backend.model.enums.PaymentInputType;
import com.edfaaly.backend.model.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PaymentRequest {

    @NotNull
    private Long driverId;

    @NotNull
    @Positive
    private Double amount;

    @NotNull
    private PaymentMethod paymentMethod; // INSTAPAY أو WALLET

    @NotNull
    private PaymentInputType inputType; // QR_SCAN أو MANUAL_ID

    /**
     * قيمة فريدة يولّدها تطبيق الموبايل (UUID) مرة واحدة قبل إرسال الطلب.
     * لو الشبكة قطعت والتطبيق أعاد الإرسال بنفس القيمة، السيرفر هيتجاهل التكرار
     * ويرجّع نتيجة العملية الأصلية بدل ما يخصم/يحوّل مرة تانية.
     */
    @NotNull
    private String clientRequestId;
}

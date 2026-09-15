package com.edfaaly.backend.dto;

import com.edfaaly.backend.model.enums.UserType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank
    private String fullName;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String password;

    @NotNull
    private UserType userType; // RIDER أو DRIVER

    /** اختياري في المرحلة الحالية (محفظة داخلية + محفظة إلكترونية خارجية) - هيبقى إجباري لما InstaPay يتفعّل فعليًا */
    private String cardToken;

    /** إجباري لو userType = DRIVER (بيتحقق منه في AuthService) - ده الكود اللي هيستخدمه الراكب في الدفع اليدوي */
    private String licensePlate;
}

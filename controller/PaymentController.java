package com.edfaaly.backend.controller;

import com.edfaaly.backend.dto.DriverSummaryResponse;
import com.edfaaly.backend.dto.PaymentRequest;
import com.edfaaly.backend.dto.ResolveDriverRequest;
import com.edfaaly.backend.dto.TransactionResponse;
import com.edfaaly.backend.security.CurrentUserProvider;
import com.edfaaly.backend.service.DriverResolutionService;
import com.edfaaly.backend.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final DriverResolutionService driverResolutionService;
    private final PaymentService paymentService;
    private final CurrentUserProvider currentUserProvider;

    /**
     * الخطوة الأولى في شاشة الدفع: الراكب يبعت إما qrValue (بعد مسح الكود)
     * أو manualCode (كود السائق القصير أو رقم اللوحة اللي كتبه يدويًا).
     * الرد بيرجع بيانات السائق عشان تتعرض في شاشة تأكيد المبلغ.
     */
    @PostMapping("/resolve-driver")
    public DriverSummaryResponse resolveDriver(@RequestBody ResolveDriverRequest request) {
        return driverResolutionService.resolve(request);
    }

    /** الخطوة الثانية: تنفيذ الدفع الفعلي بعد تأكيد المبلغ وطريقة الدفع */
    @PostMapping("/pay")
    public TransactionResponse pay(@Valid @RequestBody PaymentRequest request) {
        return paymentService.pay(currentUserProvider.get(), request);
    }
}

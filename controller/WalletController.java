package com.edfaaly.backend.controller;

import com.edfaaly.backend.dto.TopUpRequest;
import com.edfaaly.backend.model.Wallet;
import com.edfaaly.backend.security.CurrentUserProvider;
import com.edfaaly.backend.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;
    private final CurrentUserProvider currentUserProvider;

    @GetMapping("/balance")
    public Map<String, Object> getBalance() {
        Wallet wallet = walletService.getWallet(currentUserProvider.get());
        return Map.of("balance", wallet.getBalance());
    }

    /**
     * دي شحن وهمي (Mock Top-Up) لغرض التطوير والديمو بس، عشان تقدر تجرب الدفع
     * بمحفظة (WALLET) وهي فاضية. مفيش أي بوابة دفع حقيقية وراها.
     * لازم تتشال أو تتحمى (مثلاً admin-only) قبل أي بيئة إنتاج فعلية —
     * الشحن الحقيقي هيتم عن طريق بوابة دفع/InstaPay وقت ما يتكامل فعليًا.
     */
    @PostMapping("/dev-topup")
    public Map<String, Object> devTopUp(@Valid @RequestBody TopUpRequest request) {
        walletService.credit(currentUserProvider.get(), request.getAmount());
        Wallet wallet = walletService.getWallet(currentUserProvider.get());
        return Map.of("balance", wallet.getBalance());
    }

    // ملحوظة: عمليات "سحب الرصيد" و"تحويل لـ InstaPay" محتاجة تكامل فعلي
    // مع بوابة الدفع/البنك الشريك، فمش هتتضاف هنا كـ endpoint وهمي — هتتبنى وقت ربط InstaPay الحقيقي.
}

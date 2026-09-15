package com.edfaaly.backend.service;

import com.edfaaly.backend.model.User;
import com.edfaaly.backend.model.Wallet;
import com.edfaaly.backend.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    public Wallet getWallet(User user) {
        return walletRepository.findByUser(user)
                .orElseThrow(() -> new IllegalStateException("المحفظة غير موجودة لهذا المستخدم"));
    }

    @Transactional
    public void credit(User user, double amount) {
        Wallet wallet = getWallet(user);
        wallet.setBalance(wallet.getBalance() + amount);
        wallet.setUpdatedAt(LocalDateTime.now());
        walletRepository.save(wallet);
    }

    @Transactional
    public void debit(User user, double amount) {
        Wallet wallet = getWallet(user);
        if (wallet.getBalance() < amount) {
            throw new IllegalStateException("الرصيد غير كافٍ في المحفظة");
        }
        wallet.setBalance(wallet.getBalance() - amount);
        wallet.setUpdatedAt(LocalDateTime.now());
        walletRepository.save(wallet);
    }
}

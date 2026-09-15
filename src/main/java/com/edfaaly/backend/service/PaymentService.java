package com.edfaaly.backend.service;

import com.edfaaly.backend.dto.PaymentRequest;
import com.edfaaly.backend.dto.TransactionResponse;
import com.edfaaly.backend.model.Trip;
import com.edfaaly.backend.model.User;
import com.edfaaly.backend.model.Transaction;
import com.edfaaly.backend.model.enums.PaymentMethod;
import com.edfaaly.backend.model.enums.TransactionStatus;
import com.edfaaly.backend.repository.TransactionRepository;
import com.edfaaly.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final WalletService walletService;
    private final TripService tripService;
    private final NotificationService notificationService;

    @Transactional
    public TransactionResponse pay(User rider, PaymentRequest request) {

        // منع التكرار: لو الطلب ده اتبعت قبل كدا فعلاً (إعادة محاولة بسبب ضعف الشبكة)، رجّع نفس النتيجة القديمة
        var existing = transactionRepository.findByClientRequestId(request.getClientRequestId());
        if (existing.isPresent()) {
            return toResponse(existing.get());
        }

        User driver = userRepository.findById(request.getDriverId())
                .orElseThrow(() -> new IllegalArgumentException("السائق غير موجود"));

        Transaction tx = new Transaction();
        tx.setRider(rider);
        tx.setDriver(driver);
        tx.setAmount(request.getAmount());
        tx.setPaymentMethod(request.getPaymentMethod());
        tx.setInputType(request.getInputType());
        tx.setClientRequestId(request.getClientRequestId());
        tx.setStatus(TransactionStatus.PENDING);

        // المشوار النشط، أو آخر مشوار مقفول حديثًا ضمن نافذة السماح (عملية متأخرة)، أو من غير مشوار خالص
        boolean[] isLate = new boolean[1];
        Trip trip = tripService.getTripForNewPayment(driver, isLate);
        tx.setTrip(trip);
        tx.setLateAddition(isLate[0]);

        try {
            if (request.getPaymentMethod() == PaymentMethod.WALLET) {
                walletService.debit(rider, request.getAmount());
            } else {
                // InstaPay/بوابة دفع خارجية: لسه مؤجل - المرحلة الأولى محفظة داخلية فقط (حسب القرار الحالي)
            }

            // المبلغ بيتحول تلقائيًا لمحفظة السائق فورًا مع كل عملية ناجحة
            walletService.credit(driver, request.getAmount());

            if (trip != null) {
                tripService.addToTrip(trip, request.getAmount());
            }

            tx.setStatus(TransactionStatus.SUCCESS);

        } catch (Exception e) {
            tx.setStatus(TransactionStatus.FAILED);
            transactionRepository.save(tx);
            throw e;
        }

        Transaction saved = transactionRepository.save(tx);

        notificationService.send(rider, "تم الدفع بنجاح",
                "تم دفع " + request.getAmount() + " جنيه لـ " + driver.getFullName());
        notificationService.send(driver, "استلمت مبلغ جديد",
                "استلمت " + request.getAmount() + " جنيه من " + rider.getFullName());

        return toResponse(saved);
    }

    private TransactionResponse toResponse(Transaction saved) {
        return new TransactionResponse(
                saved.getId(),
                saved.getDriver().getFullName(),
                saved.getAmount(),
                saved.getStatus().name(),
                saved.getPaymentMethod().name(),
                saved.getCreatedAt(),
                saved.getTrip() != null ? saved.getTrip().getId() : null
        );
    }
}

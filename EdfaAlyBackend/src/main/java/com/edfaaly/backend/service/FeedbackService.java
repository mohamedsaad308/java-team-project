package com.edfaaly.backend.service;

import com.edfaaly.backend.dto.FeedbackRequest;
import com.edfaaly.backend.dto.FeedbackResponse;
import com.edfaaly.backend.model.Feedback;
import com.edfaaly.backend.model.Transaction;
import com.edfaaly.backend.model.User;
import com.edfaaly.backend.repository.FeedbackRepository;
import com.edfaaly.backend.repository.TransactionRepository;
import com.edfaaly.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Transactional
    public void submit(User rider, FeedbackRequest request) {
        Transaction tx = transactionRepository.findById(request.getTransactionId())
                .orElseThrow(() -> new IllegalArgumentException("العملية غير موجودة"));

        if (!tx.getRider().getId().equals(rider.getId())) {
            throw new IllegalArgumentException("لا يمكنك تقييم عملية لا تخصك");
        }

        Feedback feedback = new Feedback();
        feedback.setTransaction(tx);
        feedback.setDriver(tx.getDriver());
        feedback.setRider(rider);
        feedback.setRating(request.getRating());
        feedback.setComment(request.getComment());
        feedbackRepository.save(feedback);

        updateDriverAverage(tx.getDriver(), request.getRating());
    }

    public List<FeedbackResponse> getForDriver(User driver) {
        return feedbackRepository.findByDriverOrderByCreatedAtDesc(driver).stream()
                .map(f -> new FeedbackResponse(f.getRider().getFullName(), f.getRating(), f.getComment(), f.getCreatedAt()))
                .toList();
    }

    private void updateDriverAverage(User driver, int newRating) {
        int oldCount = driver.getRatingCount() != null ? driver.getRatingCount() : 0;
        double oldAvg = driver.getRatingAvg() != null ? driver.getRatingAvg() : 0.0;

        double newAvg = ((oldAvg * oldCount) + newRating) / (oldCount + 1);

        driver.setRatingAvg(newAvg);
        driver.setRatingCount(oldCount + 1);
        userRepository.save(driver);
    }
}

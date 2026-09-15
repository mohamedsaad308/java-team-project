package com.edfaaly.backend.controller;

import com.edfaaly.backend.dto.FeedbackRequest;
import com.edfaaly.backend.dto.FeedbackResponse;
import com.edfaaly.backend.security.CurrentUserProvider;
import com.edfaaly.backend.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final CurrentUserProvider currentUserProvider;

    @PostMapping
    public void submit(@Valid @RequestBody FeedbackRequest request) {
        feedbackService.submit(currentUserProvider.get(), request);
    }

    /** تقييمات السائق الحالي المسجّل دخوله */
    @GetMapping("/driver")
    public List<FeedbackResponse> myRatings() {
        return feedbackService.getForDriver(currentUserProvider.get());
    }
}

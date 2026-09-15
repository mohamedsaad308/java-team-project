package com.edfaaly.backend.controller;

import com.edfaaly.backend.dto.NotificationResponse;
import com.edfaaly.backend.security.CurrentUserProvider;
import com.edfaaly.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final CurrentUserProvider currentUserProvider;

    @GetMapping
    public List<NotificationResponse> getMyNotifications() {
        return notificationService.getForUser(currentUserProvider.get());
    }
}

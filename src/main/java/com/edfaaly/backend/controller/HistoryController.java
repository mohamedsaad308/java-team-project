package com.edfaaly.backend.controller;

import com.edfaaly.backend.dto.TransactionResponse;
import com.edfaaly.backend.security.CurrentUserProvider;
import com.edfaaly.backend.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;
    private final CurrentUserProvider currentUserProvider;

    @GetMapping("/rider")
    public List<TransactionResponse> riderHistory() {
        return historyService.getRiderHistory(currentUserProvider.get());
    }

    @GetMapping("/driver")
    public List<TransactionResponse> driverHistory() {
        return historyService.getDriverHistory(currentUserProvider.get());
    }
}

package com.edfaaly.backend.repository;

import com.edfaaly.backend.model.Notification;
import com.edfaaly.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByCreatedAtDesc(User user);
}

package com.edfaaly.backend.repository;

import com.edfaaly.backend.model.Feedback;
import com.edfaaly.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByDriverOrderByCreatedAtDesc(User driver);
}

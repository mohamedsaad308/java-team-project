package com.edfaaly.backend.service;

import com.edfaaly.backend.dto.NotificationResponse;
import com.edfaaly.backend.model.Notification;
import com.edfaaly.backend.model.User;
import com.edfaaly.backend.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    /**
     * ملحوظة: الحفظ في قاعدة البيانات هنا بيمثل "الإشعار" كسجل.
     * الإرسال الفعلي كـ Push Notification لازم يتم عبر Firebase Cloud Messaging (FCM)
     * من نفس المكان اللي بيتنادى فيه send() - مش موجود في هذا الهيكل المبدئي.
     */
    public void send(User user, String title, String body) {
        Notification n = new Notification();
        n.setUser(user);
        n.setTitle(title);
        n.setBody(body);
        notificationRepository.save(n);
        // TODO: استدعاء FCM هنا لإرسال Push فعلي للجهاز
    }

    public List<NotificationResponse> getForUser(User user) {
        return notificationRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(n -> new NotificationResponse(n.getId(), n.getTitle(), n.getBody(), n.getIsRead(), n.getCreatedAt()))
                .toList();
    }
}

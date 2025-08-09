package com.rec_notification.repository;

import com.rec_notification.entity.EmailAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailAttachmentRepository extends JpaRepository<EmailAttachment, Long> {
    List<EmailAttachment> findByNotificationId(Long notificationId);
}
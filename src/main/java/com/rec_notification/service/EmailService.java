package com.rec_notification.service;


import com.rec_notification.dto.*;
import com.rec_notification.entity.*;
import com.rec_notification.enums.*;
import com.rec_notification.repository.*;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final NotificationRepository notificationRepository;
    private final EmailAttachmentRepository attachmentRepository;

    @Value("${notification.from-email}")
    private String fromEmail;

    @Value("${notification.from-name}")
    private String fromName;

    public EmailService(JavaMailSender mailSender,
                        NotificationRepository notificationRepository,
                        EmailAttachmentRepository attachmentRepository) {
        this.mailSender = mailSender;
        this.notificationRepository = notificationRepository;
        this.attachmentRepository = attachmentRepository;
    }

    @Async
    public CompletableFuture<NotificationResponse> sendEmail(EmailNotificationRequest request) {
        log.info("Sending email to: {}", request.getRecipient());

        Notification notification = createNotification(request);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, fromName);
            helper.setTo(request.getRecipient());
            helper.setSubject(request.getSubject());
            helper.setText(request.getContent(), true);

            // Procesar archivos adjuntos si existen
            if (request.getAttachments() != null && !request.getAttachments().isEmpty()) {
                processAttachments(request.getAttachments(), helper, notification);
                notification.setType(NotificationType.EMAIL_WITH_ATTACHMENT);
            }

            mailSender.send(message);

            notification.setStatus(NotificationStatus.SENT);
            notification.setSentAt(LocalDateTime.now());

            log.info("Email sent successfully to: {}", request.getRecipient());

        } catch (Exception e) {
            log.error("Failed to send email to: {}", request.getRecipient(), e);
            notification.setStatus(NotificationStatus.FAILED);
            notification.setErrorMessage(e.getMessage());
        }

        notification = notificationRepository.save(notification);
        return CompletableFuture.completedFuture(mapToResponse(notification));
    }

    private void processAttachments(List<MultipartFile> files, MimeMessageHelper helper, Notification notification) throws Exception {
        List<EmailAttachment> attachments = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                // Validar tamaño y tipo de archivo
                validateFile(file);

                // Agregar al correo
                helper.addAttachment(file.getOriginalFilename(), file);

                // Guardar en base de datos
                EmailAttachment attachment = EmailAttachment.builder()
                        .fileName(file.getOriginalFilename())
                        .contentType(file.getContentType())
                        .data(file.getBytes())
                        .notification(notification)
                        .build();

                attachments.add(attachment);
            }
        }

        if (!attachments.isEmpty()) {
            attachmentRepository.saveAll(attachments);
        }
    }

    private void validateFile(MultipartFile file) throws Exception {
        // Validar tamaño (máximo 5MB por archivo)
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("File size exceeds 5MB limit: " + file.getOriginalFilename());
        }

        // Validar tipos de archivo permitidos
        String contentType = file.getContentType();
        List<String> allowedTypes = Arrays.asList(
                "application/pdf",
                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "application/vnd.ms-excel",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "image/jpeg",
                "image/png",
                "text/plain"
        );

        if (!allowedTypes.contains(contentType)) {
            throw new IllegalArgumentException("File type not allowed: " + contentType);
        }
    }

    private Notification createNotification(EmailNotificationRequest request) {
        return Notification.builder()
                .recipient(request.getRecipient())
                .subject(request.getSubject())
                .content(request.getContent())
                .type(NotificationType.EMAIL)
                .status(NotificationStatus.PROCESSING)
                .build();
    }

    private NotificationResponse mapToResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .recipient(notification.getRecipient())
                .subject(notification.getSubject())
                .type(notification.getType())
                .status(notification.getStatus())
                .createdAt(notification.getCreatedAt())
                .sentAt(notification.getSentAt())
                .errorMessage(notification.getErrorMessage())
                .build();
    }
}
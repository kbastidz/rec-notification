package com.rec_notification.controller;

import com.rec_notification.dto.*;
import com.rec_notification.enums.*;
import com.rec_notification.service.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/notifications")
@Validated
@Slf4j
public class NotificationController {

    private final EmailService emailService;
    private final PushNotificationService pushNotificationService;
    private final NotificationService notificationService;

    public NotificationController(EmailService emailService,
                                  PushNotificationService pushNotificationService,
                                  NotificationService notificationService) {
        this.emailService = emailService;
        this.pushNotificationService = pushNotificationService;
        this.notificationService = notificationService;
    }

    @PostMapping("/email")
    public ResponseEntity<NotificationResponse> sendEmail(@Valid @RequestBody EmailNotificationRequest request) {
        try {
            CompletableFuture<NotificationResponse> future = emailService.sendEmail(request);
            NotificationResponse response = future.get(30, TimeUnit.SECONDS);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error sending email", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(NotificationResponse.builder()
                            .status(NotificationStatus.FAILED)
                            .errorMessage(e.getMessage())
                            .build());
        }
    }

    @PostMapping("/email/with-attachments")
    public ResponseEntity<NotificationResponse> sendEmailWithAttachments(
            @RequestParam("recipient") @Email String recipient,
            @RequestParam("subject") String subject,
            @RequestParam("content") String content,
            @RequestParam("attachments") List<MultipartFile> attachments) {

        EmailNotificationRequest request = EmailNotificationRequest.builder()
                .recipient(recipient)
                .subject(subject)
                .content(content)
                .attachments(attachments)
                .build();

        try {
            CompletableFuture<NotificationResponse> future = emailService.sendEmail(request);
            NotificationResponse response = future.get(30, TimeUnit.SECONDS);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error sending email with attachments", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(NotificationResponse.builder()
                            .status(NotificationStatus.FAILED)
                            .errorMessage(e.getMessage())
                            .build());
        }
    }

    /*@PostMapping("/push")
    public ResponseEntity<NotificationResponse> sendPushNotification(@Valid @RequestBody PushNotificationRequest request) {
        try {
            CompletableFuture<NotificationResponse> future = pushNotificationService.sendPushNotification(request);
            NotificationResponse response = future.get(30, TimeUnit.SECONDS);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error sending push notification", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(NotificationResponse.builder()
                            .status(NotificationStatus.FAILED)
                            .errorMessage(e.getMessage())
                            .build());
        }
    }

    @PostMapping("/fcm/register")
    public ResponseEntity<String> registerFcmToken(@Valid @RequestBody FcmTokenRequest request) {
        try {
            pushNotificationService.registerFcmToken(request);
            return ResponseEntity.ok("FCM token registered successfully");
        } catch (Exception e) {
            log.error("Error registering FCM token", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error registering FCM token: " + e.getMessage());
        }
    }

    @DeleteMapping("/fcm/unregister/{userId}")
    public ResponseEntity<String> unregisterFcmToken(@PathVariable String userId) {
        try {
            pushNotificationService.unregisterFcmToken(userId);
            return ResponseEntity.ok("FCM tokens unregistered successfully");
        } catch (Exception e) {
            log.error("Error unregistering FCM tokens", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error unregistering FCM tokens: " + e.getMessage());
        }
    }*/

    @GetMapping("/history/{recipient}")
    public ResponseEntity<List<NotificationResponse>> getNotificationHistory(@PathVariable String recipient) {
        List<NotificationResponse> notifications = notificationService.getNotificationsByRecipient(recipient);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByStatus(@PathVariable NotificationStatus status) {
        List<NotificationResponse> notifications = notificationService.getNotificationsByStatus(status);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getNotificationById(@PathVariable Long id) {
        try {
            NotificationResponse notification = notificationService.getNotificationById(id);
            return ResponseEntity.ok(notification);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<NotificationResponse> notifications = notificationService.getNotificationsByDateRange(startDate, endDate);
        return ResponseEntity.ok(notifications);
    }
}

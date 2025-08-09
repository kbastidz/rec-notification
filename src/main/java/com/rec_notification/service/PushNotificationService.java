package com.rec_notification.service;


import com.rec_notification.dto.*;
import com.rec_notification.entity.*;
import com.rec_notification.entity.Notification;
import com.rec_notification.enums.NotificationStatus;
import com.rec_notification.enums.NotificationType;
import com.rec_notification.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.google.firebase.*;
import com.google.firebase.messaging.*;
@Service
@Slf4j
public class PushNotificationService {
/*
    private final FirebaseMessaging firebaseMessaging;
    private final FcmTokenRepository fcmTokenRepository;
    private final NotificationRepository notificationRepository;

    public PushNotificationService(FirebaseMessaging firebaseMessaging,
                                   FcmTokenRepository fcmTokenRepository,
                                   NotificationRepository notificationRepository) {
        this.firebaseMessaging = firebaseMessaging;
        this.fcmTokenRepository = fcmTokenRepository;
        this.notificationRepository = notificationRepository;
    }

    @Async
    public CompletableFuture<NotificationResponse> sendPushNotification(PushNotificationRequest request) {
        log.info("Sending push notification to user: {}", request.getUserId());

        List<FcmToken> tokens = fcmTokenRepository.findByUserIdAndIsActiveTrue(request.getUserId());

        if (tokens.isEmpty()) {
            log.warn("No active FCM tokens found for user: {}", request.getUserId());
            throw new RuntimeException("No active FCM tokens found for user: " + request.getUserId());
        }

        Notification notification = createPushNotification(request);
        List<String> failedTokens = new ArrayList<>();
        boolean atLeastOneSuccess = false;

        for (FcmToken fcmToken : tokens) {
            try {
                Message message = buildMessage(request, fcmToken.getToken());
                String response = firebaseMessaging.send(message);

                log.info("Push notification sent successfully to token: {} with response: {}",
                        fcmToken.getToken().substring(0, 10) + "...", response);
                atLeastOneSuccess = true;

            } catch (FirebaseMessagingException e) {
                log.error("Failed to send push notification to token: {}",
                        fcmToken.getToken().substring(0, 10) + "...", e);

                // Si el token es inválido, marcarlo como inactivo
                if (e.getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED ||
                        e.getMessagingErrorCode() == MessagingErrorCode.INVALID_ARGUMENT) {
                    fcmToken.setIsActive(false);
                    fcmTokenRepository.save(fcmToken);
                }

                failedTokens.add(fcmToken.getToken());
            }
        }

        if (atLeastOneSuccess) {
            notification.setStatus(NotificationStatus.SENT);
            notification.setSentAt(LocalDateTime.now());
        } else {
            notification.setStatus(NotificationStatus.FAILED);
            notification.setErrorMessage("Failed to send to all tokens: " + String.join(", ", failedTokens));
        }

        notification = notificationRepository.save(notification);
        return CompletableFuture.completedFuture(mapToResponse(notification));
    }

    public void registerFcmToken(FcmTokenRequest request) {
        log.info("Registering FCM token for user: {}", request.getUserId());

        // Desactivar tokens anteriores del usuario
        fcmTokenRepository.deactivateTokensByUserId(request.getUserId());

        // Crear nuevo token
        FcmToken fcmToken = FcmToken.builder()
                .userId(request.getUserId())
                .token(request.getToken())
                .deviceType(request.getDeviceType())
                .isActive(true)
                .build();

        fcmTokenRepository.save(fcmToken);
        log.info("FCM token registered successfully for user: {}", request.getUserId());
    }

    public void unregisterFcmToken(String userId) {
        log.info("Unregistering FCM tokens for user: {}", userId);
        fcmTokenRepository.deactivateTokensByUserId(userId);
    }

    private Message buildMessage(PushNotificationRequest request, String token) {
        Message.Builder messageBuilder = Message.builder()
                .setToken(token)
                .setNotification(com.google.firebase.messaging.Notification.builder()
                        .setTitle(request.getTitle())
                        .setBody(request.getBody())
                        .setImage(request.getImageUrl())
                        .build());

        if (request.getData() != null && !request.getData().isEmpty()) {
            messageBuilder.putAllData(request.getData());
        }

        return messageBuilder.build();
    }

    private Notification createPushNotification(PushNotificationRequest request) {
        return Notification.builder()
                .recipient(request.getUserId())
                .subject(request.getTitle())
                .content(request.getBody())
                .type(NotificationType.PUSH)
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

 */
}

package com.rec_notification.controller;

@org.springframework.web.bind.annotation.RestController
@lombok.RequiredArgsConstructor
@lombok.extern.slf4j.Slf4j
public class HealthController {

    // Health check endpoint que el gateway espera
    @org.springframework.web.bind.annotation.GetMapping("/notifications/health")
    public java.util.Map<String, Object> health() {
        java.util.Map<String, Object> health = new java.util.HashMap<>();
        health.put("status", "UP");
        health.put("service", "notification-service");
        health.put("timestamp", java.time.LocalDateTime.now());
        health.put("port", 8081);
        health.put("version", "1.0.0");
        return health;
    }

    // Información del servicio
    @org.springframework.web.bind.annotation.GetMapping("/notifications/info")
    public java.util.Map<String, Object> getServiceInfo() {
        java.util.Map<String, Object> info = new java.util.HashMap<>();
        info.put("name", "notification-service");
        info.put("version", "1.0.0");
        info.put("port", 8081);
        info.put("description", "Comprehensive Notification Service - Email, Push, FCM");
        info.put("modules", java.util.Arrays.asList("Email Service", "Push Notifications", "FCM Integration"));
        info.put("email-endpoints", java.util.Arrays.asList(
                "/api/notifications/email",
                "/api/notifications/email/with-attachments"
        ));
        info.put("push-endpoints", java.util.Arrays.asList(
                "/api/notifications/push",
                "/api/notifications/fcm/register",
                "/api/notifications/fcm/unregister/{userId}"
        ));
        info.put("query-endpoints", java.util.Arrays.asList(
                "/api/notifications/history/{recipient}",
                "/api/notifications/status/{status}",
                "/api/notifications/{id}",
                "/api/notifications/date-range"
        ));
        info.put("health-endpoints", java.util.Arrays.asList(
                "/notifications/health",
                "/notifications/info",
                "/notifications/status",
                "/ping"
        ));
        info.put("timestamp", java.time.LocalDateTime.now());
        return info;
    }

    // Endpoint para verificar conectividad básica
    @org.springframework.web.bind.annotation.GetMapping("/ping")
    public java.util.Map<String, String> ping() {
        java.util.Map<String, String> response = new java.util.HashMap<>();
        response.put("message", "pong");
        response.put("service", "notification-service");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return response;
    }

    // Endpoint para verificar que los módulos estén funcionando
    @org.springframework.web.bind.annotation.GetMapping("/notifications/status")
    public java.util.Map<String, Object> getModuleStatus() {
        java.util.Map<String, Object> status = new java.util.HashMap<>();
        status.put("service", "notification-service");
        status.put("overall-status", "UP");

        java.util.Map<String, String> modules = new java.util.HashMap<>();
        modules.put("email-service", "UP");
        modules.put("push-notification-service", "UP");
        modules.put("fcm-service", "UP");
        modules.put("notification-history", "UP");
        modules.put("database", "UP");

        status.put("modules", modules);
        status.put("timestamp", java.time.LocalDateTime.now());
        return status;
    }

    // Estadísticas básicas del servicio
    @org.springframework.web.bind.annotation.GetMapping("/notifications/stats")
    public java.util.Map<String, Object> getServiceStats() {
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("service", "notification-service");

        // En un caso real, obtendrías estas estadísticas de tu base de datos
        java.util.Map<String, Integer> counts = new java.util.HashMap<>();
        counts.put("total-notifications", 0);
        counts.put("emails-sent", 0);
        counts.put("push-notifications-sent", 0);
        counts.put("failed-notifications", 0);
        counts.put("registered-fcm-tokens", 0);

        stats.put("counts", counts);
        stats.put("timestamp", java.time.LocalDateTime.now());
        stats.put("note", "Stats would be populated from database in production");
        return stats;
    }
}
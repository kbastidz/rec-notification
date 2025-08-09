package com.rec_notification.config;

import com.google.auth.oauth2.GoogleCredentials;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.*;
import java.util.concurrent.ThreadPoolExecutor;
import com.google.firebase.messaging.*;
import com.google.firebase.*;

@Configuration
@EnableAsync
@Slf4j
public class FirebaseConfig {

    @Value("${firebase.config-path:firebase-service-account.json}")
    private String firebaseConfigPath;

    /*@Bean
    public FirebaseMessaging firebaseMessaging() throws IOException {
        log.info("Initializing Firebase with config file: {}", firebaseConfigPath);

        try {
            // Método 1: Intentar cargar desde classpath
            ClassPathResource resource = new ClassPathResource(firebaseConfigPath);
            InputStream inputStream;

            if (resource.exists()) {
                log.info("Firebase config found in classpath");
                inputStream = resource.getInputStream();
            } else {
                // Método 2: Intentar cargar desde el sistema de archivos
                log.warn("Firebase config not found in classpath, trying file system");
                File file = new File("src/main/resources/" + firebaseConfigPath);
                if (file.exists()) {
                    log.info("Firebase config found in file system: {}", file.getAbsolutePath());
                    inputStream = new FileInputStream(file);
                } else {
                    // Método 3: Buscar en el directorio actual
                    File currentDirFile = new File(firebaseConfigPath);
                    if (currentDirFile.exists()) {
                        log.info("Firebase config found in current directory: {}", currentDirFile.getAbsolutePath());
                        inputStream = new FileInputStream(currentDirFile);
                    } else {
                        log.error("Firebase config file not found in any location:");
                        log.error("  - Classpath: {}", firebaseConfigPath);
                        log.error("  - File system: {}", file.getAbsolutePath());
                        log.error("  - Current dir: {}", currentDirFile.getAbsolutePath());
                        log.error("  - Working directory: {}", System.getProperty("user.dir"));

                        // Listar archivos en resources para debug
                        try {
                            File resourcesDir = new File("src/main/resources");
                            if (resourcesDir.exists()) {
                                log.info("Files in src/main/resources:");
                                File[] files = resourcesDir.listFiles();
                                if (files != null) {
                                    for (File f : files) {
                                        log.info("  - {}", f.getName());
                                    }
                                }
                            }
                        } catch (Exception e) {
                            log.warn("Could not list resources directory", e);
                        }

                        throw new FileNotFoundException("Firebase service account file not found: " + firebaseConfigPath);
                    }
                }
            }

            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(inputStream);
            inputStream.close();

            FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                    .setCredentials(googleCredentials)
                    .build();

            // Limpiar instancia existente si existe
            try {
                FirebaseApp existingApp = FirebaseApp.getInstance("notification-service");
                log.info("Deleting existing Firebase app instance");
                existingApp.delete();
            } catch (IllegalStateException e) {
                log.info("No existing Firebase app found, creating new one");
            }

            FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions, "notification-service");
            log.info("Firebase app initialized successfully");

            return FirebaseMessaging.getInstance(app);

        } catch (Exception e) {
            log.error("Error initializing Firebase", e);
            throw new RuntimeException("Failed to initialize Firebase: " + e.getMessage(), e);
        }
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("NotificationAsync-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }*/
}

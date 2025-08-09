package com.rec_notification.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "email_attachments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String contentType;

    @Lob
    private byte[] data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id")
    private Notification notification;
}
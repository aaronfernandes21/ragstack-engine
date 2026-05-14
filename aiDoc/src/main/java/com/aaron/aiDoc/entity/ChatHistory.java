package com.aaron.aiDoc.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_history", schema = "ai")
@Getter
@Setter
public class ChatHistory {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long chatId;

    private String userId;

    private Long docId;

    @Column(columnDefinition = "TEXT")
    private String question;

    @Column(columnDefinition = "TEXT")
    private String answer;

    private LocalDateTime createdAt = LocalDateTime.now();
}

package com.aaron.aiDoc.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
@Setter
@Getter
@Entity
@Table(name="docJob", schema = "jobs")
public class DocumentJob {

    @Id
    @GeneratedValue
    private Long id;
    private String userId;
    private String fileName;
    @Enumerated(EnumType.STRING)
    private JobStatus status;

    private String errorMessage;

    private LocalDateTime createdAt = LocalDateTime.now();


}

package com.aaron.aiDoc.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name="pdf_chunks" , schema="ai")
public class PdfChunks {
    @Id
    @GeneratedValue
    private Long id;
    private Long documentId;


    private String userId;
    private String fileName;
    @Column(columnDefinition = "text")
    private String content;

    @Column(columnDefinition = "vector(768)") // depends on ollama model
    private float[] embedding;

    private LocalDateTime createdAt = LocalDateTime.now();

}

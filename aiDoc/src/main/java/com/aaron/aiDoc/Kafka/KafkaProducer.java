package com.aaron.aiDoc.Kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
@Service
public class KafkaProducer {

    @Autowired
    KafkaTemplate<String, DocumentEvent> kafkaTemplate;

    public void sendDocumentForProcessing(MultipartFile file, Long docJobId) throws IOException {
        byte[] fileBytes = file.getBytes();
        kafkaTemplate.send("document-topic", new DocumentEvent(docJobId, fileBytes));
    }
}

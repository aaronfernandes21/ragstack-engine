package com.aaron.aiDoc.controller;

import com.aaron.aiDoc.entity.DocumentJob;
import com.aaron.aiDoc.service.DocumentService;
import com.aaron.aiDoc.Kafka.KafkaProducer;
import com.aaron.aiDoc.service.FunctionalityService;
import com.aaron.aiDoc.service.QueryService;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class ServiceController {

    @Autowired
    DocumentService documentService;

    @Autowired
    QueryService queryService;

    @Autowired
    FunctionalityService  functionalityService;

    private final KafkaProducer kafkaProducer;

    public ServiceController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @GetMapping("/hi")
    public String hi(){
        return "hi";

    }


    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam MultipartFile file){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

//        String username = auth.getName();
        String userId = auth.getDetails().toString();
        DocumentJob docJob = documentService.createJob(file,userId);
        Long docJobId = docJob.getId();

        try {
            kafkaProducer.sendDocumentForProcessing(file, docJobId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("File uploaded. Processing started with docId:"+docJobId);
    }

    @PostMapping(value = "/ask/{docId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<Flux<String>> askQuestion(
            @RequestBody Map<String, String> body, @PathVariable Long docId){

        String question = body.get("question");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

//        System.out.println("AUTH OBJECT: " + auth);
//        System.out.println("AUTH NAME: " + auth.getName());
//        System.out.println("AUTHORITIES: " + auth.getAuthorities());

        String userId = auth.getDetails().toString();
        Flux<String> response = queryService.ask(question, userId, docId);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/getDocDetails")
    public ResponseEntity<List<Map<Long, String>>> getDocDetails(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getDetails().toString();
        List<Map<Long, String>> docIds = functionalityService.getDocDetails(userId);
        if (docIds == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(docIds);


    }

}
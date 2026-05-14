package com.aaron.aiDoc.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class EmbeddingService {

    @Value("${embedding.api.url}")
    private String apiUrl;

    @Value("${embedding.model}")
    private String model;
    private final RestTemplate restTemplate = new RestTemplate();

    public List<List<Float>> embed(List<String> chunks) {

        List<List<Float>> embeddings = new ArrayList<>();

        for (String chunk : chunks) {

            Map<String, Object> body = new HashMap<>();
            body.put("model", model);
            body.put("prompt", chunk);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request =
                    new HttpEntity<>(body, headers);

            ResponseEntity<Map> response =
                    restTemplate.postForEntity(apiUrl, request, Map.class);

            List<Double> vector = (List<Double>) response.getBody().get("embedding");

            List<Float> floatVector = vector.stream()
                    .map(Double::floatValue)
                    .toList();

            embeddings.add(floatVector);
        }

        return embeddings;
    }
    public List<Float> embed1(String chunks) {

        List<Float> embeddings = new ArrayList<>();


            Map<String, Object> body = new HashMap<>();
            body.put("model", model);
            body.put("prompt", chunks);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request =
                    new HttpEntity<>(body, headers);

            ResponseEntity<Map> response =
                    restTemplate.postForEntity(apiUrl, request, Map.class);

            List<Double> vector = (List<Double>) response.getBody().get("embedding");

            return vector.stream()
                    .map(Double::floatValue)
                    .toList();


        }



}

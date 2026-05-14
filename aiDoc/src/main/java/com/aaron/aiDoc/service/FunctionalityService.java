package com.aaron.aiDoc.service;

import com.aaron.aiDoc.entity.DocumentJob;
import com.aaron.aiDoc.repository.DocumentRepo;
import com.aaron.aiDoc.repository.VectorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FunctionalityService {
    @Autowired
    DocumentRepo documentRepo;

    public List<Map<Long, String>> getDocDetails(String userId) {
        List<Map<Long, String>> docDetails = new ArrayList<>();
        List<DocumentJob> docJobs = documentRepo.findAllByUserId(userId);
        int n = docJobs.size();
        if(n==0){
            return null;
        }
        for (int i=0; i<n; i++) {
            DocumentJob docJob = docJobs.get(i);
            Map<Long, String> docDetail = new HashMap<>();
            docDetail.put(docJob.getId(),docJob.getFileName());
            docDetails.add(docDetail);
        }

        return docDetails;
    }
}


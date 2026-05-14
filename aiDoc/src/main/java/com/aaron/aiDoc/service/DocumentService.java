package com.aaron.aiDoc.service;

import com.aaron.aiDoc.entity.DocumentJob;
import com.aaron.aiDoc.entity.JobStatus;
import com.aaron.aiDoc.repository.DocumentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
public class DocumentService {

    @Autowired
    DocumentRepo documentRepo;
    public DocumentJob createJob(MultipartFile file, String userId) {
        DocumentJob docJob = new DocumentJob();
        docJob.setFileName(
                file.getOriginalFilename()
        );
        docJob.setUserId(userId);
        docJob.setStatus(JobStatus.UPLOADED);
        return documentRepo.save(docJob);


    }
}

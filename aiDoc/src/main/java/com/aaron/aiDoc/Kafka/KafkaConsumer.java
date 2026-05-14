package com.aaron.aiDoc.Kafka;

import com.aaron.aiDoc.entity.DocumentJob;
import com.aaron.aiDoc.entity.JobStatus;
import com.aaron.aiDoc.entity.PdfChunks;
import com.aaron.aiDoc.repository.DocumentRepo;
import com.aaron.aiDoc.repository.VectorRepo;
import com.aaron.aiDoc.service.EmbeddingService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class KafkaConsumer {
    @Autowired
    DocumentRepo documentRepo;

    @Autowired
    VectorRepo vectorRepo;

    @Autowired
    EmbeddingService embeddingService;

    @KafkaListener(topics = "document-topic")
    public void process (DocumentEvent event){
        Long docJobId= event.getDocJobId();
        //Integer docJobIdInt = Integer.valueOf(Math.toIntExact(docJobId));

        //Integer docJobIdInt = Integer.parseInt(docJobId);
        Optional<DocumentJob> job0 = documentRepo.findById(docJobId);
        if(job0.isPresent()){
            DocumentJob job = job0.get();
            try{
                job.setStatus(JobStatus.PROCESSING);
                byte[] fileBytes = event.getFileBytes();
                String fileByteText = extractText(fileBytes);
                List<String> chunk = chunkText(500, fileByteText);
                List<List<Float>> embeddings = embeddingService.embed(chunk);
                saveChunks(docJobId, job, chunk, embeddings);
                job.setStatus(JobStatus.SUCCESSFUL);
            }
            catch(Exception e){
                job.setStatus(JobStatus.FAILED);
                job.setErrorMessage(String.valueOf(e));
            }

            documentRepo.save(job);


        }
        else{
            System.out.println("Document Job Not Found");
        }

    }

    private String extractText(byte[] fileBytes) throws IOException {
        try (PDDocument document = PDDocument.load(fileBytes)) {

            PDFTextStripper stripper = new PDFTextStripper();

            stripper.setSortByPosition(true);

            return stripper.getText(document);
        }
    }
    private List<String> chunkText(int chunkSize, String text) {
        List<String> chunks = new ArrayList<>();

        for (int i = 0; i < text.length(); i += chunkSize) {
            chunks.add(text.substring(i, Math.min(text.length(), i + chunkSize)));
        }

        return chunks;
    }

    private void saveChunks(
            Long jobId,
            DocumentJob job,
            List<String> chunks,
            List<List<Float>> embeddings) {

        List<PdfChunks> batch = new ArrayList<>();

        for (int i = 0; i < chunks.size(); i++) {

            PdfChunks chunk = new PdfChunks();

            chunk.setDocumentId(jobId);
            chunk.setUserId(job.getUserId());
            chunk.setFileName(job.getFileName());
            chunk.setContent(chunks.get(i));


            List<Float> embeddingList = embeddings.get(i);
            float[] embeddingArray = new float[embeddingList.size()];

            for (int j = 0; j < embeddingList.size(); j++) {
                embeddingArray[j] = embeddingList.get(j);
            }

            chunk.setEmbedding(embeddingArray);

            batch.add(chunk);
        }

        vectorRepo.saveAll(batch);
    }
}

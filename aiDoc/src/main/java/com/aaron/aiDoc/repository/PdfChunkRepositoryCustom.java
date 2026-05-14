package com.aaron.aiDoc.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PdfChunkRepositoryCustom {

    @PersistenceContext
    private EntityManager em;
    @SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection"})
    public List<String> generateChunk(String queryEmbedding, String userId, long docId, int limit){

        String sql = "SELECT content FROM ai.pdf_chunks " +
                "WHERE user_id = :userId AND document_id = :docId " +
                "ORDER BY l2_distance(embedding, CAST(:embedding AS vector)) " +
                "LIMIT :limit";

        return em.createNativeQuery(sql)
                .setParameter("embedding", queryEmbedding)
                .setParameter("userId", userId)
                .setParameter("limit", limit)
                .setParameter("docId", docId)
                .getResultList();
    }
}

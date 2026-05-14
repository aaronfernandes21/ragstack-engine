package com.aaron.aiDoc.repository;

import com.aaron.aiDoc.entity.ChatHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatHistoryRepo extends JpaRepository<ChatHistory, Long> {


    List<ChatHistory> findTop5ByUserIdAndDocIdOrderByCreatedAtDesc(
            String userId,
            Long docId
    );

    long countByUserIdAndDocId(
            String userId,
            Long docId
    );

    Optional<ChatHistory>
    findFirstByUserIdAndDocIdOrderByCreatedAtAsc(
            String userId,
            Long docId
    );
}

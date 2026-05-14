package com.aaron.aiDoc.repository;

import com.aaron.aiDoc.entity.DocumentJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Repository
public interface DocumentRepo extends JpaRepository<DocumentJob, Long> {

    List<DocumentJob> findAllByUserId(String userId);
}

package com.aaron.aiDoc.repository;

import com.aaron.aiDoc.entity.PdfChunks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VectorRepo extends JpaRepository<PdfChunks, Long> {

}

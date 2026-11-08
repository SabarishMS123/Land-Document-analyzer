package com.landanalyzer.repository;

import com.landanalyzer.entity.QnaMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QnaMessageRepository extends JpaRepository<QnaMessage, Long> {
    List<QnaMessage> findByDocumentIdOrderByAskedAtAsc(Long documentId);
}

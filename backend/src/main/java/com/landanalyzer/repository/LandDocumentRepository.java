package com.landanalyzer.repository;

import com.landanalyzer.entity.LandDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LandDocumentRepository extends JpaRepository<LandDocument, Long> {

    List<LandDocument> findByRiskLevel(LandDocument.RiskLevel riskLevel);

    List<LandDocument> findByStatus(LandDocument.ProcessingStatus status);

    List<LandDocument> findByDocumentTypeIgnoreCase(String documentType);
}

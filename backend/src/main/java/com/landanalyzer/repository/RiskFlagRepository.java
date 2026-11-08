package com.landanalyzer.repository;

import com.landanalyzer.entity.RiskFlag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RiskFlagRepository extends JpaRepository<RiskFlag, Long> {
    List<RiskFlag> findByDocumentId(Long documentId);
}

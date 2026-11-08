package com.landanalyzer.controller;

import com.landanalyzer.entity.LandDocument;
import com.landanalyzer.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard")

public class DashboardController {

    private final DocumentService documentService;
    public DashboardController(DocumentService documentService) {
        this.documentService = documentService;
    }
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> stats() {
        List<LandDocument> all = documentService.getAll();

        Map<String, Long> byRisk = all.stream()
                .filter(d -> d.getRiskLevel() != null)
                .collect(Collectors.groupingBy(d -> d.getRiskLevel().name(), Collectors.counting()));

        Map<String, Long> byStatus = all.stream()
                .filter(d -> d.getStatus() != null)
                .collect(Collectors.groupingBy(d -> d.getStatus().name(), Collectors.counting()));

        Map<String, Long> byType = all.stream()
                .filter(d -> d.getDocumentType() != null && !d.getDocumentType().isBlank())
                .collect(Collectors.groupingBy(LandDocument::getDocumentType, Collectors.counting()));

        double avgRiskScore = all.stream()
                .filter(d -> d.getRiskScore() != null)
                .mapToInt(LandDocument::getRiskScore)
                .average()
                .orElse(0.0);

        return ResponseEntity.ok(Map.of(
                "totalDocuments", all.size(),
                "byRiskLevel", byRisk,
                "byStatus", byStatus,
                "byDocumentType", byType,
                "averageRiskScore", Math.round(avgRiskScore * 10.0) / 10.0
        ));
    }
}

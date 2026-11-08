package com.landanalyzer.dto;

import com.landanalyzer.entity.LandDocument;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DocumentSummaryDto(
        Long id,
        String fileName,
        String documentType,
        String surveyNumber,
        String propertyAddress,
        String granterName,
        String granteeName,
        String executionDate,
        String registrationNumber,
        BigDecimal considerationAmount,
        String status,
        String riskLevel,
        Integer riskScore,
        String riskSummary,
        LocalDateTime uploadedAt,
        LocalDateTime analyzedAt
) {
    public static DocumentSummaryDto from(LandDocument d) {
        return new DocumentSummaryDto(
                d.getId(),
                d.getFileName(),
                d.getDocumentType(),
                d.getSurveyNumber(),
                d.getPropertyAddress(),
                d.getGranterName(),
                d.getGranteeName(),
                d.getExecutionDate(),
                d.getRegistrationNumber(),
                d.getConsiderationAmount(),
                d.getStatus() != null ? d.getStatus().name() : null,
                d.getRiskLevel() != null ? d.getRiskLevel().name() : null,
                d.getRiskScore(),
                d.getRiskSummary(),
                d.getUploadedAt(),
                d.getAnalyzedAt()
        );
    }
}

package com.landanalyzer.dto;

/**
 * Structured schema the LLM is prompted to return (as JSON) after reading
 * raw OCR/extracted deed text. Mirrors the analytical fields persisted on
 * LandDocument.
 */
public record DocumentExtractionResult(
        String documentType,
        String surveyNumber,
        String propertyAddress,
        String granterName,
        String granteeName,
        String executionDate,
        String registrationNumber,
        String registrationDate,
        String subRegistrarOffice,
        String considerationAmount,
        String extentOfLand,
        String boundaries,
        String encumbranceNotes
) {}

package com.landanalyzer.dto;

public record RiskFlagDto(
        String category,
        String severity,
        String description,
        String recommendation
) {}

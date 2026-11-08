package com.landanalyzer.dto;

import java.util.List;

public record RiskAuditResult(
        Integer riskScore,
        String riskLevel,
        String summary,
        List<RiskFlagDto> flags
) {}

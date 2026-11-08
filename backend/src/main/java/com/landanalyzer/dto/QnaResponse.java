package com.landanalyzer.dto;

public record QnaResponse(
        String question,
        String answer,
        String groundingExcerpt
) {}

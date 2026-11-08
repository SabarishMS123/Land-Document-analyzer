package com.landanalyzer.dto;

import jakarta.validation.constraints.NotBlank;

public record QnaRequest(
        @NotBlank(message = "question must not be blank")
        String question
) {}

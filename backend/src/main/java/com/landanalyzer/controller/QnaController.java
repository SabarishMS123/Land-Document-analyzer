package com.landanalyzer.controller;

import com.landanalyzer.dto.QnaRequest;
import com.landanalyzer.dto.QnaResponse;
import com.landanalyzer.entity.QnaMessage;
import com.landanalyzer.service.QnaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents/{documentId}/qna")

public class QnaController {

    private final QnaService qnaService;

    @PostMapping
    public ResponseEntity<QnaResponse> ask(@PathVariable Long documentId,
                                            @Valid @RequestBody QnaRequest request) {
        QnaMessage message = qnaService.ask(documentId, request.question());
        return ResponseEntity.ok(new QnaResponse(message.getQuestion(), message.getAnswer(), message.getGroundingExcerpt()));
    }
    public QnaController(QnaService qnaService) {
        this.qnaService = qnaService;
    }
    @GetMapping
    public ResponseEntity<List<QnaMessage>> history(@PathVariable Long documentId) {
        return ResponseEntity.ok(qnaService.history(documentId));
    }
}

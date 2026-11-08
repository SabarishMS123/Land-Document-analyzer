package com.landanalyzer.controller;

import com.landanalyzer.dto.DocumentSummaryDto;
import com.landanalyzer.entity.LandDocument;
import com.landanalyzer.entity.RiskFlag;
import com.landanalyzer.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/documents")

public class DocumentController {

    private final DocumentService documentService;

    /** Upload a deed PDF; runs full extraction + risk audit synchronously. */
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<DocumentSummaryDto> upload(@RequestParam("file") MultipartFile file) {
        LandDocument saved = documentService.ingestAndAnalyze(file);
        return ResponseEntity.ok(DocumentSummaryDto.from(saved));
    }
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }
    /** Dashboard index: list all processed documents. */
    @GetMapping
    public ResponseEntity<List<DocumentSummaryDto>> listAll() {
        List<DocumentSummaryDto> docs = documentService.getAll().stream()
                .map(DocumentSummaryDto::from)
                .toList();
        return ResponseEntity.ok(docs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentSummaryDto> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(DocumentSummaryDto.from(documentService.getById(id)));
    }

    @GetMapping("/{id}/risk-flags")
    public ResponseEntity<List<RiskFlag>> getRiskFlags(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.getRiskFlags(id));
    }

    @GetMapping("/{id}/raw-text")
    public ResponseEntity<String> getRawText(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.getRawText(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        documentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

//package com.landanalyzer.service;
//
//import com.landanalyzer.dto.DocumentExtractionResult;
//import com.landanalyzer.dto.RiskAuditResult;
//import com.landanalyzer.entity.LandDocument;
//import com.landanalyzer.entity.RiskFlag;
//import com.landanalyzer.exception.DocumentNotFoundException;
//import com.landanalyzer.repository.LandDocumentRepository;
//import com.landanalyzer.repository.RiskFlagRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class DocumentService {
//
//    private final PdfExtractionService pdfExtractionService;
//    private final GroqAiService groqAiService;
//    private final LandDocumentRepository documentRepository;
//    private final RiskFlagRepository riskFlagRepository;
//
//    /**
//     * Full ingestion pipeline: PDF -> raw text -> LLM structured extraction
//     * -> persistence -> automated risk audit. Runs synchronously so the
//     * caller gets a fully analyzed document in one round trip; swap for an
//     * async/queued pipeline for higher-throughput production use.
//     */
//    @Transactional
//    public LandDocument ingestAndAnalyze(MultipartFile file) {
//        String rawText = pdfExtractionService.extractText(file);
//
//        LandDocument doc = LandDocument.builder()
//                .fileName(file.getOriginalFilename())
//                .rawExtractedText(rawText)
//                .status(LandDocument.ProcessingStatus.EXTRACTING)
//                .build();
//        doc = documentRepository.save(doc);
//
//        try {
//            DocumentExtractionResult extraction = groqAiService.extractStructuredData(rawText);
//            applyExtraction(doc, extraction);
//            doc.setStatus(LandDocument.ProcessingStatus.EXTRACTED);
//            doc = documentRepository.save(doc);
//
//            doc.setStatus(LandDocument.ProcessingStatus.ANALYZING);
//            RiskAuditResult audit = groqAiService.runRiskAudit(rawText, extraction);
//            applyRiskAudit(doc, audit);
//            doc.setStatus(LandDocument.ProcessingStatus.ANALYZED);
//            doc.setAnalyzedAt(LocalDateTime.now());
//
//        } catch (Exception e) {
//            log.error("Analysis pipeline failed for document {}", doc.getId(), e);
//            doc.setStatus(LandDocument.ProcessingStatus.FAILED);
//        }
//
//        return documentRepository.save(doc);
//    }
//
//    private void applyExtraction(LandDocument doc, DocumentExtractionResult r) {
//        doc.setDocumentType(nullSafe(r.documentType()));
//        doc.setSurveyNumber(nullSafe(r.surveyNumber()));
//        doc.setPropertyAddress(nullSafe(r.propertyAddress()));
//        doc.setGranterName(nullSafe(r.granterName()));
//        doc.setGranteeName(nullSafe(r.granteeName()));
//        doc.setExecutionDate(nullSafe(r.executionDate()));
//        doc.setRegistrationNumber(nullSafe(r.registrationNumber()));
//        doc.setRegistrationDate(nullSafe(r.registrationDate()));
//        doc.setSubRegistrarOffice(nullSafe(r.subRegistrarOffice()));
//        doc.setExtentOfLand(nullSafe(r.extentOfLand()));
//        doc.setBoundaries(nullSafe(r.boundaries()));
//        doc.setEncumbranceNotes(nullSafe(r.encumbranceNotes()));
//        doc.setConsiderationAmount(parseAmount(r.considerationAmount()));
//    }
//
//    private void applyRiskAudit(LandDocument doc, RiskAuditResult audit) {
//        doc.setRiskScore(audit.riskScore());
//        doc.setRiskSummary(audit.summary());
//        try {
//            doc.setRiskLevel(LandDocument.RiskLevel.valueOf(audit.riskLevel().toUpperCase()));
//        } catch (Exception e) {
//            doc.setRiskLevel(LandDocument.RiskLevel.UNKNOWN);
//        }
//
//        if (audit.flags() != null) {
//            List<RiskFlag> flags = audit.flags().stream()
//                    .map(f -> RiskFlag.builder()
//                            .document(doc)
//                            .category(f.category())
//                            .severity(parseSeverity(f.severity()))
//                            .description(f.description())
//                            .recommendation(f.recommendation())
//                            .build())
//                    .toList();
//            riskFlagRepository.saveAll(flags);
//        }
//    }
//
//    private RiskFlag.Severity parseSeverity(String s) {
//        try {
//            return RiskFlag.Severity.valueOf(s.toUpperCase());
//        } catch (Exception e) {
//            return RiskFlag.Severity.MEDIUM;
//        }
//    }
//
//    private BigDecimal parseAmount(String raw) {
//        if (raw == null || raw.isBlank()) return null;
//        try {
//            String cleaned = raw.replaceAll("[^0-9.]", "");
//            return cleaned.isBlank() ? null : new BigDecimal(cleaned);
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    private String nullSafe(String s) {
//        return s == null ? "" : s;
//    }
//
//    public LandDocument getById(Long id) {
//        return documentRepository.findById(id)
//                .orElseThrow(() -> new DocumentNotFoundException(id));
//    }
//
//    public List<LandDocument> getAll() {
//        return documentRepository.findAll();
//    }
//
//    public List<RiskFlag> getRiskFlags(Long documentId) {
//        return riskFlagRepository.findByDocumentId(documentId);
//    }
//
//    public String getRawText(Long documentId) {
//        return getById(documentId).getRawExtractedText();
//    }
//
//    public void delete(Long id) {
//        if (!documentRepository.existsById(id)) {
//            throw new DocumentNotFoundException(id);
//        }
//        documentRepository.deleteById(id);
//    }
//}
package com.landanalyzer.service;

import com.landanalyzer.dto.DocumentExtractionResult;
import com.landanalyzer.dto.RiskAuditResult;
import com.landanalyzer.entity.LandDocument;
import com.landanalyzer.entity.RiskFlag;
import com.landanalyzer.exception.DocumentNotFoundException;
import com.landanalyzer.repository.LandDocumentRepository;
import com.landanalyzer.repository.RiskFlagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
// Remove @RequiredArgsConstructor and @Slf4j
public class DocumentService {

    private static final Logger log = LoggerFactory.getLogger(DocumentService.class);

    private final PdfExtractionService pdfExtractionService;
    private final GroqAiService groqAiService;
    private final LandDocumentRepository documentRepository;
    private final RiskFlagRepository riskFlagRepository;

    // Manual constructor
    public DocumentService(PdfExtractionService pdfExtractionService,
                           GroqAiService groqAiService,
                           LandDocumentRepository documentRepository,
                           RiskFlagRepository riskFlagRepository) {
        this.pdfExtractionService = pdfExtractionService;
        this.groqAiService = groqAiService;
        this.documentRepository = documentRepository;
        this.riskFlagRepository = riskFlagRepository;
    }

    /**
     * Full ingestion pipeline: PDF -> raw text -> LLM structured extraction
     * -> persistence -> automated risk audit. Runs synchronously so the
     * caller gets a fully analyzed document in one round trip; swap for an
     * async/queued pipeline for higher-throughput production use.
     */
    @Transactional
    public LandDocument ingestAndAnalyze(MultipartFile file) {
        String rawText = pdfExtractionService.extractText(file);

        LandDocument doc = new LandDocument();
        doc.setFileName(file.getOriginalFilename());
        doc.setRawExtractedText(rawText);
        doc.setStatus(LandDocument.ProcessingStatus.EXTRACTING);
        doc = documentRepository.save(doc);

        try {
            DocumentExtractionResult extraction = groqAiService.extractStructuredData(rawText);
            applyExtraction(doc, extraction);
            doc.setStatus(LandDocument.ProcessingStatus.EXTRACTED);
            doc = documentRepository.save(doc);

            doc.setStatus(LandDocument.ProcessingStatus.ANALYZING);
            RiskAuditResult audit = groqAiService.runRiskAudit(rawText, extraction);
            applyRiskAudit(doc, audit);
            doc.setStatus(LandDocument.ProcessingStatus.ANALYZED);
            doc.setAnalyzedAt(LocalDateTime.now());

        } catch (Exception e) {
            log.error("Analysis pipeline failed for document {}", doc.getId(), e);
            doc.setStatus(LandDocument.ProcessingStatus.FAILED);
        }

        return documentRepository.save(doc);
    }

    private void applyExtraction(LandDocument doc, DocumentExtractionResult r) {
        doc.setDocumentType(nullSafe(r.documentType()));
        doc.setSurveyNumber(nullSafe(r.surveyNumber()));
        doc.setPropertyAddress(nullSafe(r.propertyAddress()));
        doc.setGranterName(nullSafe(r.granterName()));
        doc.setGranteeName(nullSafe(r.granteeName()));
        doc.setExecutionDate(nullSafe(r.executionDate()));
        doc.setRegistrationNumber(nullSafe(r.registrationNumber()));
        doc.setRegistrationDate(nullSafe(r.registrationDate()));
        doc.setSubRegistrarOffice(nullSafe(r.subRegistrarOffice()));
        doc.setExtentOfLand(nullSafe(r.extentOfLand()));
        doc.setBoundaries(nullSafe(r.boundaries()));
        doc.setEncumbranceNotes(nullSafe(r.encumbranceNotes()));
        doc.setConsiderationAmount(parseAmount(r.considerationAmount()));
    }

    private void applyRiskAudit(LandDocument doc, RiskAuditResult audit) {
        doc.setRiskScore(audit.riskScore());
        doc.setRiskSummary(audit.summary());
        try {
            doc.setRiskLevel(LandDocument.RiskLevel.valueOf(audit.riskLevel().toUpperCase()));
        } catch (Exception e) {
            doc.setRiskLevel(LandDocument.RiskLevel.UNKNOWN);
        }

        if (audit.flags() != null) {
            List<RiskFlag> flags = audit.flags().stream()
                    .map(f -> {
                        RiskFlag flag = new RiskFlag();
                        flag.setDocument(doc);
                        flag.setCategory(f.category());
                        flag.setSeverity(parseSeverity(f.severity()));
                        flag.setDescription(f.description());
                        flag.setRecommendation(f.recommendation());
                        return flag;
                    })
                    .toList();
            riskFlagRepository.saveAll(flags);
        }
    }

    private RiskFlag.Severity parseSeverity(String s) {
        try {
            return RiskFlag.Severity.valueOf(s.toUpperCase());
        } catch (Exception e) {
            return RiskFlag.Severity.MEDIUM;
        }
    }

    private BigDecimal parseAmount(String raw) {
        if (raw == null || raw.isBlank()) return null;
        try {
            String cleaned = raw.replaceAll("[^0-9.]", "");
            return cleaned.isBlank() ? null : new BigDecimal(cleaned);
        } catch (Exception e) {
            return null;
        }
    }

    private String nullSafe(String s) {
        return s == null ? "" : s;
    }

    public LandDocument getById(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException(id));
    }

    public List<LandDocument> getAll() {
        return documentRepository.findAll();
    }

    public List<RiskFlag> getRiskFlags(Long documentId) {
        return riskFlagRepository.findByDocumentId(documentId);
    }

    public String getRawText(Long documentId) {
        return getById(documentId).getRawExtractedText();
    }

    public void delete(Long id) {
        if (!documentRepository.existsById(id)) {
            throw new DocumentNotFoundException(id);
        }
        documentRepository.deleteById(id);
    }
}
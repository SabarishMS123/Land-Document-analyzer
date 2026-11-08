package com.landanalyzer.service;

import com.landanalyzer.exception.DocumentProcessingException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Extracts raw text from unstructured deed PDFs so it can be handed to the
 * LLM for structured-schema extraction. Deed scans that are pure images
 * (no text layer) will return an empty/near-empty string here — in a full
 * production build this is the seam where an OCR fallback (e.g. Tesseract)
 * would be plugged in.
 */
@Service
public class PdfExtractionService {

    private static final Logger log = LoggerFactory.getLogger(PdfExtractionService.class);

    public String extractText(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new DocumentProcessingException("Uploaded file is empty");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.equals("application/pdf")) {
            throw new DocumentProcessingException("Only PDF files are supported, got: " + contentType);
        }

        try (PDDocument document = Loader.loadPDF(file.getBytes())) {
            if (document.isEncrypted()) {
                throw new DocumentProcessingException("PDF is password protected: " + file.getOriginalFilename());
            }
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            String text = stripper.getText(document);

            if (text == null || text.isBlank()) {
                log.warn("No extractable text layer found in {} — document may be a scanned image", file.getOriginalFilename());
            }
            return text == null ? "" : text.trim();
        } catch (IOException e) {
            throw new DocumentProcessingException("Failed to parse PDF: " + file.getOriginalFilename(), e);
        }
    }
}
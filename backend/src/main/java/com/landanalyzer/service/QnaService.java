//package com.landanalyzer.service;
//
//import com.landanalyzer.entity.LandDocument;
//import com.landanalyzer.entity.QnaMessage;
//import com.landanalyzer.exception.DocumentProcessingException;
//import com.landanalyzer.repository.QnaMessageRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class QnaService {
//
//    private final GroqAiService groqAiService;
//    private final DocumentService documentService;
//    private final QnaMessageRepository qnaMessageRepository;
//
//    public QnaMessage ask(Long documentId, String question) {
//        LandDocument doc = documentService.getById(documentId);
//
//        if (doc.getRawExtractedText() == null || doc.getRawExtractedText().isBlank()) {
//            throw new DocumentProcessingException(
//                    "No extracted text is available for this document — it may not have a text layer.");
//        }
//
//        String answer = groqAiService.answerQuestion(doc.getRawExtractedText(), question);
//
//        QnaMessage message = QnaMessage.builder()
//                .document(doc)
//                .question(question)
//                .answer(answer)
//                .build();
//
//        return qnaMessageRepository.save(message);
//    }
//
//    public List<QnaMessage> history(Long documentId) {
//        // ensure document exists, else throws DocumentNotFoundException
//        documentService.getById(documentId);
//        return qnaMessageRepository.findByDocumentIdOrderByAskedAtAsc(documentId);
//    }
//}

package com.landanalyzer.service;

import com.landanalyzer.entity.LandDocument;
import com.landanalyzer.entity.QnaMessage;
import com.landanalyzer.exception.DocumentProcessingException;
import com.landanalyzer.repository.QnaMessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QnaService {

    private final GroqAiService groqAiService;
    private final DocumentService documentService;
    private final QnaMessageRepository qnaMessageRepository;

    public QnaService(GroqAiService groqAiService,
                      DocumentService documentService,
                      QnaMessageRepository qnaMessageRepository) {
        this.groqAiService = groqAiService;
        this.documentService = documentService;
        this.qnaMessageRepository = qnaMessageRepository;
    }

    public QnaMessage ask(Long documentId, String question) {
        LandDocument doc = documentService.getById(documentId);

        if (doc.getRawExtractedText() == null || doc.getRawExtractedText().isBlank()) {
            throw new DocumentProcessingException(
                    "No extracted text is available for this document — it may not have a text layer.");
        }

        String answer = groqAiService.answerQuestion(doc.getRawExtractedText(), question);

        QnaMessage message = new QnaMessage();
        message.setDocument(doc);
        message.setQuestion(question);
        message.setAnswer(answer);

        return qnaMessageRepository.save(message);
    }

    public List<QnaMessage> history(Long documentId) {
        documentService.getById(documentId);
        return qnaMessageRepository.findByDocumentIdOrderByAskedAtAsc(documentId);
    }
}

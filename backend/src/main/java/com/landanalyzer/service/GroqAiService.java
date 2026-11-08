//package com.landanalyzer.service;
//
//import com.landanalyzer.dto.DocumentExtractionResult;
//import com.landanalyzer.dto.RiskAuditResult;
//import com.landanalyzer.exception.DocumentProcessingException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.chat.messages.SystemMessage;
//import org.springframework.ai.chat.messages.UserMessage;
//import org.springframework.ai.chat.prompt.Prompt;
//import org.springframework.ai.converter.BeanOutputConverter;
//import org.springframework.stereotype.Service;
//
///**
// * Wraps all LLM calls to Groq (Llama 3.3 70B) through Spring AI's
// * OpenAI-compatible ChatClient (Groq exposes an OpenAI-compatible API at
// * https://api.groq.com/openai/v1). Every prompt is designed to keep the
// * model grounded strictly in the supplied deed text — no outside knowledge.
// */
//@Service
//public class GroqAiService {
//
//    private static final Logger log = LoggerFactory.getLogger(GroqAiService.class);
//
//    private final ChatClient chatClient;
//
//    // Cap how much raw deed text we push into the context window per call.
//    private static final int MAX_CONTEXT_CHARS = 12000;
//
//    public GroqAiService(ChatClient.Builder chatClientBuilder) {
//        this.chatClient = chatClientBuilder.build();
//    }
//
//    private String truncate(String text) {
//        if (text == null) return "";
//        return text.length() > MAX_CONTEXT_CHARS ? text.substring(0, MAX_CONTEXT_CHARS) : text;
//    }
//
//    /**
//     * Converts unstructured deed text into the structured analytical schema.
//     */
//    public DocumentExtractionResult extractStructuredData(String rawText) {
//        BeanOutputConverter<DocumentExtractionResult> converter =
//                new BeanOutputConverter<>(DocumentExtractionResult.class);
//
//        String systemPrompt = """
//                You are a legal document analysis assistant specializing in Indian land/property deeds
//                (sale deeds, gift deeds, mortgage deeds, lease deeds, partition deeds).
//                Extract ONLY information explicitly present in the supplied document text.
//                If a field is not present, return an empty string for it — never guess or fabricate values.
//                %s
//                """.formatted(converter.getFormat());
//
//        try {
//            String response = chatClient.prompt(new Prompt(
//                            new SystemMessage(systemPrompt),
//                            new UserMessage("DEED TEXT:\n\n" + truncate(rawText))
//                    ))
//                    .call()
//                    .content();
//
//            return converter.convert(response);
//        } catch (Exception e) {
//            log.error("Groq extraction call failed", e);
//            throw new DocumentProcessingException("AI extraction failed: " + e.getMessage(), e);
//        }
//    }
//
//    /**
//     * Runs a rule-grounded risk audit over the extracted structured data plus raw text,
//     * returning a score, level, narrative summary, and itemized risk flags.
//     */
//    public RiskAuditResult runRiskAudit(String rawText, DocumentExtractionResult extraction) {
//        BeanOutputConverter<RiskAuditResult> converter =
//                new BeanOutputConverter<>(RiskAuditResult.class);
//
//        String systemPrompt = """
//                You are a property title risk auditor. Analyze the deed for common Indian land-title risk
//                indicators, including but not limited to:
//                - Gaps or breaks in the chain of title
//                - Missing or inconsistent registration/sub-registrar details
//                - Encumbrances, liens, or pending mortgage references
//                - Boundary description conflicts or vague/ambiguous extent of land
//                - Stamp duty or consideration amount inconsistencies
//                - Missing witness, notarization, or execution details
//                - Power-of-attorney or succession irregularities
//
//                Score overall risk from 0 (no risk) to 100 (severe risk).
//                riskLevel must be one of: LOW, MEDIUM, HIGH.
//                Base every flag strictly on evidence in the text — do not invent facts.
//                %s
//                """.formatted(converter.getFormat());
//
//        String userContent = """
//                EXTRACTED FIELDS:
//                %s
//
//                RAW DEED TEXT:
//                %s
//                """.formatted(extraction, truncate(rawText));
//
//        try {
//            String response = chatClient.prompt(new Prompt(
//                            new SystemMessage(systemPrompt),
//                            new UserMessage(userContent)
//                    ))
//                    .call()
//                    .content();
//
//            return converter.convert(response);
//        } catch (Exception e) {
//            log.error("Groq risk audit call failed", e);
//            throw new DocumentProcessingException("AI risk audit failed: " + e.getMessage(), e);
//        }
//    }
//
//    /**
//     * Context-grounded natural language Q&A. The model is instructed to answer
//     * ONLY from the provided deed text and to explicitly say when the answer
//     * isn't present, to avoid hallucinated legal claims.
//     */
//    public String answerQuestion(String rawText, String question) {
//        String systemPrompt = """
//                You are a document Q&A assistant. Answer the user's question using ONLY the deed text provided
//                below. If the answer cannot be determined from the text, respond exactly:
//                "The document does not contain enough information to answer this question."
//                Keep answers concise and cite the specific clause or detail you relied on where possible.
//
//                DEED TEXT:
//                %s
//                """.formatted(truncate(rawText));
//
//        try {
//            return chatClient.prompt(new Prompt(
//                            new SystemMessage(systemPrompt),
//                            new UserMessage(question)
//                    ))
//                    .call()
//                    .content();
//        } catch (Exception e) {
//            log.error("Groq Q&A call failed", e);
//            throw new DocumentProcessingException("AI Q&A failed: " + e.getMessage(), e);
//        }
//    }
//}

package com.landanalyzer.service;

import com.landanalyzer.dto.DocumentExtractionResult;
import com.landanalyzer.dto.RiskAuditResult;
import com.landanalyzer.exception.DocumentProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroqAiService {

    private static final Logger log = LoggerFactory.getLogger(GroqAiService.class);

    private final ChatClient chatClient;
    private static final int MAX_CONTEXT_CHARS = 12000;

    public GroqAiService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    private String truncate(String text) {
        if (text == null) return "";
        return text.length() > MAX_CONTEXT_CHARS ? text.substring(0, MAX_CONTEXT_CHARS) : text;
    }

    public DocumentExtractionResult extractStructuredData(String rawText) {
        BeanOutputConverter<DocumentExtractionResult> converter =
                new BeanOutputConverter<>(DocumentExtractionResult.class);

        String systemPrompt = """
                You are a legal document analysis assistant specializing in Indian land/property deeds
                (sale deeds, gift deeds, mortgage deeds, lease deeds, partition deeds).
                Extract ONLY information explicitly present in the supplied document text.
                If a field is not present, return an empty string for it — never guess or fabricate values.
                %s
                """.formatted(converter.getFormat());

        try {
            // ✅ FIX: Use List.of() with messages
            List<Message> messages = List.of(
                    new SystemMessage(systemPrompt),
                    new UserMessage("DEED TEXT:\n\n" + truncate(rawText))
            );
            
            String response = chatClient.prompt(new Prompt(messages))
                    .call()
                    .content();

            return converter.convert(response);
        } catch (Exception e) {
            log.error("Groq extraction call failed", e);
            throw new DocumentProcessingException("AI extraction failed: " + e.getMessage(), e);
        }
    }

    public RiskAuditResult runRiskAudit(String rawText, DocumentExtractionResult extraction) {
        BeanOutputConverter<RiskAuditResult> converter =
                new BeanOutputConverter<>(RiskAuditResult.class);

        String systemPrompt = """
                You are a property title risk auditor. Analyze the deed for common Indian land-title risk
                indicators, including but not limited to:
                - Gaps or breaks in the chain of title
                - Missing or inconsistent registration/sub-registrar details
                - Encumbrances, liens, or pending mortgage references
                - Boundary description conflicts or vague/ambiguous extent of land
                - Stamp duty or consideration amount inconsistencies
                - Missing witness, notarization, or execution details
                - Power-of-attorney or succession irregularities

                Score overall risk from 0 (no risk) to 100 (severe risk).
                riskLevel must be one of: LOW, MEDIUM, HIGH.
                Base every flag strictly on evidence in the text — do not invent facts.
                %s
                """.formatted(converter.getFormat());

        String userContent = """
                EXTRACTED FIELDS:
                %s

                RAW DEED TEXT:
                %s
                """.formatted(extraction, truncate(rawText));

        try {
            // ✅ FIX: Use List.of() with messages
            List<Message> messages = List.of(
                    new SystemMessage(systemPrompt),
                    new UserMessage(userContent)
            );
            
            String response = chatClient.prompt(new Prompt(messages))
                    .call()
                    .content();

            return converter.convert(response);
        } catch (Exception e) {
            log.error("Groq risk audit call failed", e);
            throw new DocumentProcessingException("AI risk audit failed: " + e.getMessage(), e);
        }
    }

    public String answerQuestion(String rawText, String question) {
        String systemPrompt = """
                You are a document Q&A assistant. Answer the user's question using ONLY the deed text provided
                below. If the answer cannot be determined from the text, respond exactly:
                "The document does not contain enough information to answer this question."
                Keep answers concise and cite the specific clause or detail you relied on where possible.

                DEED TEXT:
                %s
                """.formatted(truncate(rawText));

        try {
            // ✅ FIX: Use List.of() with messages
            List<Message> messages = List.of(
                    new SystemMessage(systemPrompt),
                    new UserMessage(question)
            );
            
            return chatClient.prompt(new Prompt(messages))
                    .call()
                    .content();
        } catch (Exception e) {
            log.error("Groq Q&A call failed", e);
            throw new DocumentProcessingException("AI Q&A failed: " + e.getMessage(), e);
        }
    }
}
//package com.landanalyzer.entity;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "qna_messages")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class QnaMessage {
//
//    public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}
//
//	public LandDocument getDocument() {
//		return document;
//	}
//
//	public void setDocument(LandDocument document) {
//		this.document = document;
//	}
//
//	public String getQuestion() {
//		return question;
//	}
//
//	public void setQuestion(String question) {
//		this.question = question;
//	}
//
//	public String getAnswer() {
//		return answer;
//	}
//
//	public void setAnswer(String answer) {
//		this.answer = answer;
//	}
//
//	public String getGroundingExcerpt() {
//		return groundingExcerpt;
//	}
//
//	public void setGroundingExcerpt(String groundingExcerpt) {
//		this.groundingExcerpt = groundingExcerpt;
//	}
//
//	public LocalDateTime getAskedAt() {
//		return askedAt;
//	}
//
//	public void setAskedAt(LocalDateTime askedAt) {
//		this.askedAt = askedAt;
//	}
//
//	@Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "document_id", nullable = false)
//    @JsonIgnore
//    private LandDocument document;
//
//    @Column(columnDefinition = "TEXT", nullable = false)
//    private String question;
//
//    @Column(columnDefinition = "TEXT")
//    private String answer;
//
//    @Column(columnDefinition = "TEXT")
//    private String groundingExcerpt;
//
//    @Column(nullable = false, updatable = false)
//    private LocalDateTime askedAt;
//
//    @PrePersist
//    protected void onCreate() {
//        this.askedAt = LocalDateTime.now();
//    }
//}
package com.landanalyzer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "qna_messages")
public class QnaMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    @JsonIgnore
    private LandDocument document;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String question;

    @Column(columnDefinition = "TEXT")
    private String answer;

    @Column(columnDefinition = "TEXT")
    private String groundingExcerpt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime askedAt;

    public QnaMessage() {}

    @PrePersist
    protected void onCreate() {
        this.askedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LandDocument getDocument() { return document; }
    public void setDocument(LandDocument document) { this.document = document; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }

    public String getGroundingExcerpt() { return groundingExcerpt; }
    public void setGroundingExcerpt(String groundingExcerpt) { this.groundingExcerpt = groundingExcerpt; }

    public LocalDateTime getAskedAt() { return askedAt; }
    public void setAskedAt(LocalDateTime askedAt) { this.askedAt = askedAt; }
}
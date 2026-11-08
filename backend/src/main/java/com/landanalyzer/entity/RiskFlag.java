//package com.landanalyzer.entity;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Table(name = "risk_flags")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class RiskFlag {
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
//	public String getCategory() {
//		return category;
//	}
//
//	public void setCategory(String category) {
//		this.category = category;
//	}
//
//	public Severity getSeverity() {
//		return severity;
//	}
//
//	public void setSeverity(Severity severity) {
//		this.severity = severity;
//	}
//
//	public String getDescription() {
//		return description;
//	}
//
//	public void setDescription(String description) {
//		this.description = description;
//	}
//
//	public String getRecommendation() {
//		return recommendation;
//	}
//
//	public void setRecommendation(String recommendation) {
//		this.recommendation = recommendation;
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
//    @Column(nullable = false)
//    private String category; // e.g. TITLE_CHAIN_GAP, ENCUMBRANCE, STAMP_DUTY_MISMATCH, BOUNDARY_CONFLICT
//
//    @Enumerated(EnumType.STRING)
//    private Severity severity;
//
//    @Column(columnDefinition = "TEXT")
//    private String description;
//
//    @Column(columnDefinition = "TEXT")
//    private String recommendation;
//
//    public enum Severity {
//        LOW, MEDIUM, HIGH, CRITICAL
//    }
//}
package com.landanalyzer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "risk_flags")
public class RiskFlag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    @JsonIgnore
    private LandDocument document;

    @Column(nullable = false)
    private String category;

    @Enumerated(EnumType.STRING)
    private Severity severity;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String recommendation;

    public enum Severity {
        LOW, MEDIUM, HIGH, CRITICAL
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LandDocument getDocument() { return document; }
    public void setDocument(LandDocument document) { this.document = document; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Severity getSeverity() { return severity; }
    public void setSeverity(Severity severity) { this.severity = severity; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getRecommendation() { return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }
}
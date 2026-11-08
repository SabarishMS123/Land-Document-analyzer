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
//@Table(name = "land_documents")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class LandDocument {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false)
//    private String fileName;
//
//    public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}
//
//	public String getFileName() {
//		return fileName;
//	}
//
//	public void setFileName(String fileName) {
//		this.fileName = fileName;
//	}
//
//	public String getDocumentType() {
//		return documentType;
//	}
//
//	public void setDocumentType(String documentType) {
//		this.documentType = documentType;
//	}
//
//	public String getRawExtractedText() {
//		return rawExtractedText;
//	}
//
//	public void setRawExtractedText(String rawExtractedText) {
//		this.rawExtractedText = rawExtractedText;
//	}
//
//	public String getSurveyNumber() {
//		return surveyNumber;
//	}
//
//	public void setSurveyNumber(String surveyNumber) {
//		this.surveyNumber = surveyNumber;
//	}
//
//	public String getPropertyAddress() {
//		return propertyAddress;
//	}
//
//	public void setPropertyAddress(String propertyAddress) {
//		this.propertyAddress = propertyAddress;
//	}
//
//	public String getGranterName() {
//		return granterName;
//	}
//
//	public void setGranterName(String granterName) {
//		this.granterName = granterName;
//	}
//
//	public String getGranteeName() {
//		return granteeName;
//	}
//
//	public void setGranteeName(String granteeName) {
//		this.granteeName = granteeName;
//	}
//
//	public String getExecutionDate() {
//		return executionDate;
//	}
//
//	public void setExecutionDate(String executionDate) {
//		this.executionDate = executionDate;
//	}
//
//	public String getRegistrationNumber() {
//		return registrationNumber;
//	}
//
//	public void setRegistrationNumber(String registrationNumber) {
//		this.registrationNumber = registrationNumber;
//	}
//
//	public String getRegistrationDate() {
//		return registrationDate;
//	}
//
//	public void setRegistrationDate(String registrationDate) {
//		this.registrationDate = registrationDate;
//	}
//
//	public String getSubRegistrarOffice() {
//		return subRegistrarOffice;
//	}
//
//	public void setSubRegistrarOffice(String subRegistrarOffice) {
//		this.subRegistrarOffice = subRegistrarOffice;
//	}
//
//	public java.math.BigDecimal getConsiderationAmount() {
//		return considerationAmount;
//	}
//
//	public void setConsiderationAmount(java.math.BigDecimal considerationAmount) {
//		this.considerationAmount = considerationAmount;
//	}
//
//	public String getExtentOfLand() {
//		return extentOfLand;
//	}
//
//	public void setExtentOfLand(String extentOfLand) {
//		this.extentOfLand = extentOfLand;
//	}
//
//	public String getBoundaries() {
//		return boundaries;
//	}
//
//	public void setBoundaries(String boundaries) {
//		this.boundaries = boundaries;
//	}
//
//	public String getEncumbranceNotes() {
//		return encumbranceNotes;
//	}
//
//	public void setEncumbranceNotes(String encumbranceNotes) {
//		this.encumbranceNotes = encumbranceNotes;
//	}
//
//	public ProcessingStatus getStatus() {
//		return status;
//	}
//
//	public void setStatus(ProcessingStatus status) {
//		this.status = status;
//	}
//
//	public RiskLevel getRiskLevel() {
//		return riskLevel;
//	}
//
//	public void setRiskLevel(RiskLevel riskLevel) {
//		this.riskLevel = riskLevel;
//	}
//
//	public Integer getRiskScore() {
//		return riskScore;
//	}
//
//	public void setRiskScore(Integer riskScore) {
//		this.riskScore = riskScore;
//	}
//
//	public String getRiskSummary() {
//		return riskSummary;
//	}
//
//	public void setRiskSummary(String riskSummary) {
//		this.riskSummary = riskSummary;
//	}
//
//	public LocalDateTime getUploadedAt() {
//		return uploadedAt;
//	}
//
//	public void setUploadedAt(LocalDateTime uploadedAt) {
//		this.uploadedAt = uploadedAt;
//	}
//
//	public LocalDateTime getAnalyzedAt() {
//		return analyzedAt;
//	}
//
//	public void setAnalyzedAt(LocalDateTime analyzedAt) {
//		this.analyzedAt = analyzedAt;
//	}
//
//	@Column(nullable = false)
//    private String documentType; // SALE_DEED, GIFT_DEED, MORTGAGE_DEED, LEASE_DEED, PARTITION_DEED
//
//    @Lob
//    @JsonIgnore
//    @Column(columnDefinition = "TEXT")
//    private String rawExtractedText;
//
//    // --- Structured analytical schema extracted by the LLM ---
//    private String surveyNumber;
//    private String propertyAddress;
//
//    @Column(columnDefinition = "TEXT")
//    private String granterName;
//
//    @Column(columnDefinition = "TEXT")
//    private String granteeName;
//
//    private String executionDate;
//    private String registrationNumber;
//    private String registrationDate;
//    private String subRegistrarOffice;
//
//    @Column(precision = 15, scale = 2)
//    private java.math.BigDecimal considerationAmount;
//
//    @Column(columnDefinition = "TEXT")
//    private String extentOfLand;
//
//    @Column(columnDefinition = "TEXT")
//    private String boundaries;
//
//    @Column(columnDefinition = "TEXT")
//    private String encumbranceNotes;
//
//    @Enumerated(EnumType.STRING)
//    private ProcessingStatus status;
//
//    @Enumerated(EnumType.STRING)
//    private RiskLevel riskLevel;
//
//    private Integer riskScore;
//
//    @Column(columnDefinition = "TEXT")
//    private String riskSummary;
//
//    @Column(nullable = false, updatable = false)
//    private LocalDateTime uploadedAt;
//
//    private LocalDateTime analyzedAt;
//
//    @PrePersist
//    protected void onCreate() {
//        this.uploadedAt = LocalDateTime.now();
//        if (this.status == null) {
//            this.status = ProcessingStatus.UPLOADED;
//        }
//    }
//
//    public enum ProcessingStatus {
//        UPLOADED, EXTRACTING, EXTRACTED, ANALYZING, ANALYZED, FAILED
//    }
//
//    public enum RiskLevel {
//        LOW, MEDIUM, HIGH, UNKNOWN
//    }
//}
package com.landanalyzer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "land_documents")
public class LandDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = true)
    private String documentType;

    @Lob
    @JsonIgnore
    @Column(columnDefinition = "TEXT")
    private String rawExtractedText;

    private String surveyNumber;
    private String propertyAddress;

    @Column(columnDefinition = "TEXT")
    private String granterName;

    @Column(columnDefinition = "TEXT")
    private String granteeName;

    private String executionDate;
    private String registrationNumber;
    private String registrationDate;
    private String subRegistrarOffice;

    @Column(precision = 15, scale = 2)
    private BigDecimal considerationAmount;

    @Column(columnDefinition = "TEXT")
    private String extentOfLand;

    @Column(columnDefinition = "TEXT")
    private String boundaries;

    @Column(columnDefinition = "TEXT")
    private String encumbranceNotes;

    @Enumerated(EnumType.STRING)
    private ProcessingStatus status;

    @Enumerated(EnumType.STRING)
    private RiskLevel riskLevel;

    private Integer riskScore;

    @Column(columnDefinition = "TEXT")
    private String riskSummary;

    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadedAt;

    private LocalDateTime analyzedAt;

    public LandDocument() {}

    @PrePersist
    protected void onCreate() {
        this.uploadedAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = ProcessingStatus.UPLOADED;
        }
    }

    public enum ProcessingStatus {
        UPLOADED, EXTRACTING, EXTRACTED, ANALYZING, ANALYZED, FAILED
    }

    public enum RiskLevel {
        LOW, MEDIUM, HIGH, UNKNOWN
    }

    // Getters and Setters - ALL of them!
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getDocumentType() { return documentType; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }

    public String getRawExtractedText() { return rawExtractedText; }
    public void setRawExtractedText(String rawExtractedText) { this.rawExtractedText = rawExtractedText; }

    public String getSurveyNumber() { return surveyNumber; }
    public void setSurveyNumber(String surveyNumber) { this.surveyNumber = surveyNumber; }

    public String getPropertyAddress() { return propertyAddress; }
    public void setPropertyAddress(String propertyAddress) { this.propertyAddress = propertyAddress; }

    public String getGranterName() { return granterName; }
    public void setGranterName(String granterName) { this.granterName = granterName; }

    public String getGranteeName() { return granteeName; }
    public void setGranteeName(String granteeName) { this.granteeName = granteeName; }

    public String getExecutionDate() { return executionDate; }
    public void setExecutionDate(String executionDate) { this.executionDate = executionDate; }

    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }

    public String getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(String registrationDate) { this.registrationDate = registrationDate; }

    public String getSubRegistrarOffice() { return subRegistrarOffice; }
    public void setSubRegistrarOffice(String subRegistrarOffice) { this.subRegistrarOffice = subRegistrarOffice; }

    public BigDecimal getConsiderationAmount() { return considerationAmount; }
    public void setConsiderationAmount(BigDecimal considerationAmount) { this.considerationAmount = considerationAmount; }

    public String getExtentOfLand() { return extentOfLand; }
    public void setExtentOfLand(String extentOfLand) { this.extentOfLand = extentOfLand; }

    public String getBoundaries() { return boundaries; }
    public void setBoundaries(String boundaries) { this.boundaries = boundaries; }

    public String getEncumbranceNotes() { return encumbranceNotes; }
    public void setEncumbranceNotes(String encumbranceNotes) { this.encumbranceNotes = encumbranceNotes; }

    public ProcessingStatus getStatus() { return status; }
    public void setStatus(ProcessingStatus status) { this.status = status; }

    public RiskLevel getRiskLevel() { return riskLevel; }
    public void setRiskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; }

    public Integer getRiskScore() { return riskScore; }
    public void setRiskScore(Integer riskScore) { this.riskScore = riskScore; }

    public String getRiskSummary() { return riskSummary; }
    public void setRiskSummary(String riskSummary) { this.riskSummary = riskSummary; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }

    public LocalDateTime getAnalyzedAt() { return analyzedAt; }
    public void setAnalyzedAt(LocalDateTime analyzedAt) { this.analyzedAt = analyzedAt; }
}

# 🏛️ AI Land Document Analyzer

<div align="center">

![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2%2B-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![React](https://img.shields.io/badge/React-18%2B-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15%2B-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Spring AI](https://img.shields.io/badge/Spring_AI-Groq_/_Llama_3.3-FF6F00?style=for-the-badge&logo=openai&logoColor=white)

**Automated risk auditing, document indexing, and grounded Q&A across your ingested deed archive.**

[Explore the Features](#-key-features) • [Architecture](#-system-architecture) • [Getting Started](#-getting-started) • [API Endpoints](#-api-reference)

</div>

---

## 📖 Overview

The **AI Land Document Analyzer** is a full-stack application designed to modernize the way land and property deeds are processed. By leveraging the power of Large Language Models (LLMs) via **Spring AI** and **Groq**, this tool transforms unstructured PDF deeds into structured, searchable, and risk-audited digital records. 

The UI features a custom **"Cadastral Ledger"** design system—a deep ink navy interface that surfaces documents as parchment cards, with risk levels rendered as wax-seal stamps.

---

## 📸 Screenshots

<div align="center">
  <img src="./images/main-dashboard.png" width="800" alt="Main Dashboard" />
  <p><em>Main Dashboard: Real-time stats and Risk Distribution</em></p>

  <br/>

  <img src="./images/deeds-upload.png" width="800" alt="Deeds Upload" />
  <p><em>Deeds Upload: Drag-and-drop PDF ingestion pipeline</em></p>

  <br/>

  <img src="./images/document-ledger.png" width="800" alt="Document Ledger Board" />
  <p><em>Document Ledger: Searchable index of all processed deeds</em></p>
</div>

---

## 🚀 Key Features & Deep Dive

### 1. 📄 Intelligent Ingestion Pipeline
When a PDF is uploaded, the Spring Boot backend extracts raw text using **Apache PDFBox**. This text is then passed to the **Llama 3.3 70B** model (hosted on Groq) to extract a highly structured schema (Survey Number, Grantee, Execution Date, Boundaries, etc.).
* *Note: The pipeline handles both text-based PDFs and pure image scans (requires an OCR fallback in production).*

### 2. ⚖️ Automated Risk Audit (Title Risk)
Using the extracted schema and the raw text, the LLM acts as a property title auditor. It looks for red flags such as:
* Gaps in the chain of title.
* Encumbrances and pending mortgages.
* Boundary description conflicts.
* Stamp duty inconsistencies.
The model outputs a **Risk Score (0-100)**, a severity level (LOW, MEDIUM, HIGH), and a list of actionable flags with recommendations.

### 3. 💬 Grounded Q&A (RAG-lite)
Users can ask natural language questions about a specific deed. The backend fetches the raw text and forces the LLM to answer **ONLY** using the provided text. If the answer isn't in the document, the AI strictly responds: *"The document does not contain enough information to answer this question."* This prevents legal hallucinations.

---

## 🏗️ System Architecture

The application follows a standard decoupled Client-Server architecture, integrating with an external AI provider.

```mermaid
graph TD
    subgraph Frontend [React Frontend]
        UI[React App]
        Router[React Router]
        Charts[Recharts]
        API_Client[Axios API Client]
        UI --> Router
        UI --> Charts
        UI --> API_Client
    end

    subgraph Backend [Spring Boot Backend]
        Controllers[REST Controllers]
        Services[Business Services]
        Repos[JPA Repositories]
        PDF[PDFBox Extractor]
        
        Controllers --> Services
        Services --> Repos
        Services --> PDF
    end

    subgraph External [External Services]
        Groq[Groq API / Llama 3.3 70B]
        DB[(PostgreSQL Database)]
    end

    API_Client -- HTTP/REST --> Controllers
    Services -- SQL --> DB
    Services -- Prompting --> Groq

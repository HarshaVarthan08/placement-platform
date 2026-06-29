# Sprint 7 – Milestone 2 Completion Report

## AI Placement Platform

**Sprint:** 7 – AI Interview Preparation System

**Milestone:** 2 – Personalized Question Engine

**Status:** ✅ Completed & Fully Validated

**Completion Date:** 29 June 2026

---

# Objective

The objective of Milestone 2 was to build an intelligent personalized interview question generation engine using Gemini AI while minimizing API usage through versioning, caching, and lazy regeneration.

---

# Features Implemented

### Candidate Context Builder

Implemented a dedicated CandidateContextService that constructs interview context entirely from existing platform data.

Context includes:

* Student Profile
* Skills
* Resume Analysis
* Projects
* Internship
* Target Role
* Target Companies

The uploaded resume PDF is never reprocessed during question generation.

---

### Interview Profile Versioning

Activated InterviewProfile.

Implemented automatic profile version management.

Profile version increments only when interview-relevant information changes:

* Resume replacement
* Skills
* Projects
* Internship
* Target role

---

### Personalized Question Generation

Implemented QuestionPoolGenerationService using Gemini AI.

Generated:

* Exactly 50 personalized interview questions

Each question includes:

* Question
* Ideal Answer
* Key Points
* Topic
* Category
* Difficulty
* Display Order

---

### AI Response Validation

Implemented strict validation ensuring:

* Valid JSON
* Exactly 50 questions
* Correct category distribution
* No duplicate questions
* Valid display order
* Question quality validation
* Retry once on malformed AI responses

---

### Question Pool Lifecycle

Implemented complete lifecycle:

ACTIVE

↓

OUTDATED

↓

ARCHIVED

Old pools are preserved while new pools become active.

---

### Lazy Regeneration

Implemented API optimization strategy.

Profile updates only mark the pool as OUTDATED.

Gemini is invoked only when regeneration is explicitly required.

Learning Mode can continue using an OUTDATED personalized pool.

---

### REST APIs

Implemented:

GET

```text
/api/interview/question-pool/status
```

POST

```text
/api/interview/question-pool/generate
```

---

### Cost Optimization

Successfully implemented the intended API strategy:

* First generation → 1 Gemini API call
* Cached generation → 0 API calls
* Resume updates → 0 API calls
* Skill updates → 0 API calls
* Learning Mode → 0 API calls
* Regeneration only when required

---

### Testing

Implemented comprehensive unit tests covering:

* Candidate context generation
* Version management
* Pool generation
* Pool persistence
* JSON validation
* Duplicate detection
* Display order validation
* Lazy regeneration
* Safe archiving
* Cache reuse

Total project tests:

* 48 Tests Passed
* 0 Failures
* 0 Errors

---

# Manual Validation

Successfully validated:

* Candidate context creation
* Gemini integration
* Personalized question generation
* Database persistence
* Pool status endpoint
* Cache reuse
* Profile version increment
* OUTDATED detection
* Safe regeneration
* Pool archiving
* Active pool reuse
* API cost optimization

---

# Design Decisions

The following architectural decisions were finalized:

* Resume text is reused from existing analysis.
* Candidate context is assembled entirely from database entities.
* Gemini generation is isolated behind dedicated services.
* Personalized pools are version-controlled.
* Previous pools remain archived.
* Regeneration occurs lazily.
* Active pools are reused whenever possible.

---

# Deliverables

* Candidate Context Engine
* Gemini Question Generation Engine
* Version Management
* Question Pool Lifecycle
* Lazy Regeneration
* Cache Strategy
* REST APIs
* Unit Tests
* Complete Manual Validation

---

# Result

Sprint 7 Milestone 2 successfully delivers a production-ready Personalized Question Engine capable of generating high-quality AI interview questions while maintaining extremely low Gemini API usage through intelligent caching and version management.

This milestone serves as the foundation for the Interview Runtime Engine implemented in Milestone 3.

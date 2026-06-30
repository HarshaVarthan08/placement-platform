# Sprint 7 – Milestone 3 Completion Report

# AI Placement Platform

**Sprint:** 7 – AI Interview Preparation System

**Milestone:** 3 – Interview Runtime Engine

**Status:** ✅ Completed & Fully Validated

**Completion Date:** 29 June 2026

---

# Objective

The objective of Milestone 3 was to transform the generated interview question pools into a complete interview execution engine. This milestone focused on interview session management, intelligent question selection, answer recording, progress tracking, session completion, and runtime validation while maintaining zero Gemini API usage during interview execution.

---

# Features Implemented

## Interview Runtime Engine

Implemented a complete interview execution workflow supporting both Learning Mode and Mock Interview Mode.

Workflow:

* Interview availability validation
* Active session validation
* Question selection
* Session creation
* Frozen interview generation
* Question navigation
* Answer recording
* Progress tracking
* Interview completion

---

## InterviewSessionQuestion

Introduced the `InterviewSessionQuestion` entity to freeze interview questions at the moment an interview begins.

Each session question stores:

* Immutable question snapshot
* Question source
* Display order
* Current status
* Answer details
* Timing information

This guarantees that running interviews remain unaffected even if the Global Question Bank or Personalized Question Pool changes.

---

## Intelligent Question Selection

### Learning Mode

Implemented configurable question selection using:

* 60% Global Questions
* 40% Personalized Questions

Selection priority:

1. Never Practiced
2. Least Recently Practiced
3. Least Frequently Practiced
4. Random Tie Break

Question exposure is tracked automatically.

---

### Mock Interview

Implemented 100% Personalized Question selection.

Questions are presented in interview-oriented order:

* Project Questions
* Technical Questions
* Scenario Questions
* Behavioral Questions
* HR Questions

---

## Session Management

Implemented:

* Active interview detection
* Frozen interview sessions
* Current question tracking
* Question navigation
* Interview completion
* Cooldown activation

Added `currentQuestionIndex` to support efficient runtime navigation.

---

## Answer Management

Implemented support for:

* TEXT answers
* AUDIO answer metadata

Current milestone stores:

* Text Answer
* Audio Path (reserved)
* Answer Type
* Answer Time
* Time Taken

Audio transcription and AI evaluation are deferred to Milestone 4.

---

## Validation Rules

Implemented runtime validation including:

* Only one active interview per student
* Cannot submit empty answers
* Cannot submit invalid answer type combinations
* Prevent double answer submission
* Cannot navigate before answering the current question
* Cannot complete interviews containing unanswered questions

All validation failures return HTTP 400 responses.

---

## Global Question Seeder

Implemented a production-grade `GlobalQuestionSeeder`.

Features:

* Runs automatically during application startup
* Executes only when `global_questions` is empty
* Prevents duplicate inserts
* Seeds 60 professional interview questions

Topics include:

* Java Core
* Spring Boot
* REST APIs
* Databases
* System Design
* Data Structures & Algorithms
* Behavioral Questions

Each question contains:

* Question Text
* Ideal Answer
* Key Points
* Topic
* Difficulty
* Applicable Roles
* Skill Tags

---

## REST APIs

Implemented:

* GET `/api/interview/availability`
* POST `/api/interview/start`
* GET `/api/interview/current-question`
* POST `/api/interview/answer`
* POST `/api/interview/next`
* GET `/api/interview/progress`
* POST `/api/interview/complete`

---

## Configuration

Externalized runtime configuration into `application.properties`.

Implemented configurable:

* Learning Question Count
* Mock Question Count
* Learning Ratio
* Mock Ratio
* Cooldown Duration

No interview logic is hardcoded.

---

# Testing

## Automated Testing

Successfully executed:

* 60 Unit & Integration Tests
* 0 Failures
* 0 Errors

Coverage includes:

* Session lifecycle
* Question selection
* Progress tracking
* Answer submission
* Validation rules
* Controller endpoints
* Seeder initialization
* Runtime execution

---

## Manual Validation

Completed comprehensive Postman and database validation.

Verified:

* Interview availability
* Learning interview creation
* Mock interview creation
* Question freezing
* 60/40 Learning ratio
* Global question seeding
* Current question retrieval
* Answer submission
* Progress tracking
* Navigation
* Double submission prevention
* Completion validation
* Cooldown behavior
* Database persistence
* Exposure tracking

Verified that no Gemini API calls occur during interview execution.

---

# Design Decisions

The following architectural decisions were finalized:

* Interview questions are frozen into `InterviewSessionQuestion`.
* Immutable snapshots preserve interview consistency.
* Backend calculates response time.
* Runtime uses database-only operations.
* Global questions are automatically seeded.
* Personalized pools remain reusable.
* Interview execution performs zero AI calls.
* Validation is enforced server-side.

---

# Deliverables

Completed:

* Interview Runtime Engine
* Frozen Session Architecture
* Intelligent Question Selection
* Question Exposure Tracking
* Answer Recording
* Navigation Engine
* Progress Tracking
* Runtime Validation
* Global Question Seeder
* REST APIs
* Automated Tests
* Manual Validation

---

# Result

Sprint 7 Milestone 3 successfully delivers a complete interview execution engine capable of conducting realistic Learning and Mock interviews while maintaining interview integrity, minimizing API usage, and preparing the platform for AI-based answer evaluation in Milestone 4.

The architecture established in Milestone 3 provides a stable foundation for integrating AI scoring, personalized feedback, speech-to-text processing, and interview analytics in the final milestone of Sprint 7.

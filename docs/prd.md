# Product Requirements Document (PRD)

## Project Name

AI Placement Platform (Temporary Name)

## Product Vision

An AI-powered platform that helps engineering students improve placement readiness through resume analysis, interview practice, company-specific preparation, and personalized guidance.

---

## Problem Statement

Students preparing for placements often face:

* Lack of resume feedback
* Limited interview practice
* Difficulty understanding company hiring processes
* No centralized preparation platform
* Unclear career roadmaps

---

## Target Users

### Primary Users

* Engineering Students
* Final Year Students
* Fresh Graduates

### Secondary Users

* Placement Coordinators
* Training Institutes

---

## MVP Objectives

The MVP should allow students to:

1. Create and manage profiles
2. Upload resumes and receive AI feedback
3. Practice mock interviews
4. Prepare for target companies
5. Track placement readiness

---

## MVP Modules

### Authentication

Features:

* Register
* Login
* Logout
* JWT Authentication

### Student Profile

Features:

* Personal Information
* Skills
* Target Role
* Target Companies

### Resume Analyzer

Features:

* Resume Upload
* ATS Score
* Strengths Analysis
* Weakness Analysis
* Improvement Suggestions

### Interview Simulator

Features:

* Company Selection
* Role Selection
* Difficulty Selection
* Question Generation
* Answer Evaluation
* Score Report

### Company Hub

Features:

* Company Profiles
* Hiring Process
* Interview Questions
* Preparation Tips

---

## Non-Functional Requirements

### Security

* JWT Authentication
* BCrypt Password Encryption

### Performance

* Page Load < 3 Seconds
* API Response < 5 Seconds

### Scalability

* Support 1000+ Users

### Availability

* 99% Uptime Target

---

## Technology Stack

Frontend:

* React
* Material UI

Backend:

* Spring Boot
* Spring Security

Database:

* MySQL

AI:

* Gemini API

Deployment:

* Vercel
* AWS EC2
* Docker

---

## Future Roadmap

Phase 2:

* AI Career Mentor
* Job Matching
* RAG Knowledge Base
* College Admin Dashboard
* Placement Analytics

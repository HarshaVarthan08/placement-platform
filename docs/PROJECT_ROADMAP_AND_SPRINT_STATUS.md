# PROJECT ROADMAP AND SPRINT STATUS

## Project Overview

Project Name:
AI Placement Platform

Founder:
Harsha Varthan

Current Version:
v0.1.0

Current Phase:
MVP Development

Goal:
Build an AI-powered placement preparation platform that helps engineering students improve placement readiness through:

* Resume Analysis
* Interview Preparation
* Company-Specific Preparation
* Placement Readiness Tracking
* AI-Powered Feedback

---

# Technology Stack

## Frontend

* React 18
* Material UI
* Axios
* React Router DOM

## Backend

* Java 17
* Spring Boot 3
* Spring Security 6
* JWT Authentication
* Maven

## Database

* MySQL

## AI

* Gemini API

## Deployment

Frontend:

* Vercel

Backend:

* AWS EC2

Containerization:

* Docker

---

# Development Workflow

## Roles

ChatGPT:

* Product Architect
* Technical Reviewer
* Sprint Planner

Claude:

* Senior Developer
* Large Module Generation
* Architecture-Aware Coding

Antigravity:

* Project Builder
* File Creation
* Refactoring
* Implementation

---

# Sprint Status

## Sprint 1 — Authentication Module

Status:
COMPLETED ✅

### Objective

Provide secure user authentication and authorization.

### Deliverables

* User Entity
* Role Enum
* User Repository
* DTOs
* Auth Service
* JWT Service
* JWT Filter
* Security Configuration
* Auth Controller
* Health Controller
* Global Exception Handler

### Features Completed

* User Registration
* User Login
* Password Encryption (BCrypt)
* JWT Generation
* JWT Validation
* Role Support
* Account Status Support
* Protected API Foundation

### APIs Completed

POST /api/auth/register

POST /api/auth/login

GET /api/health

### Validation Completed

* Backend Compiles Successfully
* MySQL Connectivity Verified
* Registration Tested
* Login Tested
* JWT Generation Verified
* Health Endpoint Verified

### Git Milestone

Commit:

Sprint 1 Authentication Module Completed

Tag:

sprint-1-completed

---

# Sprint 2 — Student Profile Module

Status:
NEXT

### Objective

Allow authenticated students to view and manage their placement profile.

### Features

* View Profile
* Update Profile
* JWT Protected Access

### Fields

* Name
* Email
* College
* Degree
* Branch
* CGPA
* Graduation Year
* Target Role

### APIs

GET /api/profile/me

PUT /api/profile/me

### Success Criteria

* User can view own profile
* User can update own profile
* Data persists in database
* JWT authentication enforced

---

# Sprint 3 — Skills & Target Companies Module

Status:
PLANNED

### Objective

Allow users to manage skills and target companies.

### Features

* Add Skill
* Remove Skill
* List Skills
* Add Target Company
* Remove Target Company

### APIs

GET /api/skills

POST /api/skills

DELETE /api/skills/{id}

GET /api/companies

POST /api/companies

DELETE /api/companies/{id}

### Success Criteria

* Skills linked to user
* Target companies linked to user

---

# Sprint 4 — Dashboard Module

Status:
PLANNED

### Objective

Provide placement readiness overview.

### Features

* Profile Completion %
* ATS Score Placeholder
* Interview Score Placeholder
* Skills Count
* Target Companies Count

### APIs

GET /api/dashboard

### Success Criteria

* Dashboard loads user metrics
* Responsive UI

---

# Sprint 5 — Resume Analyzer Module

Status:
PLANNED

### Objective

Analyze resumes using Gemini AI.

### Features

* Resume Upload
* ATS Score Generation
* Resume Feedback
* Improvement Suggestions

### APIs

POST /api/resume/upload

GET /api/resume/{id}

### AI Integration

Gemini API

### Success Criteria

* Resume uploaded successfully
* AI feedback generated
* ATS score displayed

---

# Sprint 6 — Company Hub Module

Status:
PLANNED

### Objective

Provide company-specific preparation resources.

### Initial Companies

* Cognizant
* TCS
* Infosys
* Accenture
* EY
* Wipro

### Features

* Hiring Process
* Technical Topics
* HR Questions
* Preparation Tips

### APIs

GET /api/companies

GET /api/companies/{id}

---

# Sprint 7 — Interview Simulator Module

Status:
PLANNED

### Objective

Conduct AI-powered mock interviews.

### Features

* Company Selection
* Role Selection
* Difficulty Selection
* AI Question Generation
* AI Evaluation

### AI Integration

Gemini API

### APIs

POST /api/interview/start

POST /api/interview/answer

GET /api/interview/report/{id}

### Success Criteria

* Questions generated
* Answers evaluated
* Final report generated

---

# Sprint 8 — Frontend Integration

Status:
PLANNED

### Objective

Connect React frontend to backend APIs.

### Features

* Authentication UI
* Profile UI
* Dashboard UI
* Resume UI
* Interview UI

### Success Criteria

* End-to-end user flow works

---

# Sprint 9 — Deployment

Status:
PLANNED

### Objective

Deploy MVP publicly.

### Frontend

Vercel

### Backend

AWS EC2

### Database

MySQL

### Containerization

Docker

### Success Criteria

* Public URL available
* Secure environment variables
* Production configuration

---

# MVP Completion Criteria

The MVP is considered complete when:

* Authentication works
* Profile management works
* Resume analysis works
* Company preparation works
* Interview simulator works
* Frontend is integrated
* Platform is deployed

---

# Current Focus

Current Sprint:

Sprint 2 — Student Profile Module

Immediate Goal:

Create implementation plan for profile management before generating code.

No code should be generated for Sprint 2 until the implementation plan has been reviewed and approved.

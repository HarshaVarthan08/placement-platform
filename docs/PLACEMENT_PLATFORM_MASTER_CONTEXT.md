# PLACEMENT PLATFORM MASTER CONTEXT

## Project Overview

Project Name:
AI Placement Platform (Temporary Name)

Project Type:
AI-Powered Placement Preparation Platform

Current Status:
Phase 1 MVP Development

Founder:
Harsha Varthan

---

# Product Vision

Build a platform that helps engineering students improve placement readiness through:

* Resume Analysis
* Interview Preparation
* Company-Specific Preparation
* Skill Tracking
* Placement Readiness Monitoring

The platform should act as an AI-powered placement assistant that guides students throughout their placement journey.

---

# Target Audience

## Primary Users

* Engineering Students
* Final Year Students
* Fresh Graduates

## Secondary Users

* Placement Coordinators
* Training Institutes

## Future Users

* Recruiters
* College Administrators

---

# Technology Stack

## Frontend

* React 18
* Material UI
* Axios
* React Router DOM

## Backend

* Java 17
* Spring Boot
* Spring Security
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

## Future Integrations

* AWS S3
* Redis
* RAG Pipeline
* Vector Database

---

# Design System

Style:
LinkedIn + LeetCode Inspired

Characteristics:

* Professional
* Minimal
* Modern
* Data Driven
* Responsive

## Colors

Primary:
#2563EB

Secondary:
#0F172A

Success:
#16A34A

Warning:
#F59E0B

Danger:
#DC2626

Background:
#F8FAFC

## Typography

Font:
Inter

---

# MVP Modules

## Module 1

Authentication

## Module 2

Student Profile

## Module 3

Resume Analyzer

## Module 4

Interview Simulator

## Module 5

Company Hub

---

# Authentication Requirements

## Features

* User Registration
* User Login
* User Logout
* JWT Authentication
* Password Encryption

## Security

* BCrypt Password Encoder
* JWT Access Tokens
* Stateless Authentication
* Spring Security

## User Roles

Default Role:

STUDENT

Future Roles:

ADMIN

PLACEMENT_COORDINATOR

## User Status

Field:
is_active

Default Value:
true

Purpose:

* Enable User Accounts
* Disable User Accounts
* Future User Management

---

# User Entity Requirements

The users table must contain:

* id
* name
* email
* password
* role
* is_active
* college
* degree
* branch
* cgpa
* graduation_year
* target_role
* created_at
* updated_at

Defaults:

role = STUDENT

is_active = true

---

# Student Profile Requirements

## Profile Information

* Name
* Email
* College
* Degree
* Branch
* CGPA
* Graduation Year

## Career Information

* Skills
* Target Role
* Target Companies

## Future Enhancements

* Certifications
* Achievements
* Projects
* Internship History

---

# Resume Analyzer Requirements

## Features

* Resume Upload
* ATS Score Generation
* Resume Feedback
* Improvement Suggestions

## Supported Formats

* PDF
* DOCX

## AI Provider

Gemini API

## Expected Output

ATS Score

Strengths

Weaknesses

Recommendations

## Future Enhancements

* Resume Version History
* Resume Comparison
* ATS Optimization Suggestions

---

# Interview Simulator Requirements

## Features

* Company Selection
* Role Selection
* Difficulty Selection
* AI Question Generation
* AI Evaluation

## Difficulty Levels

* Easy
* Medium
* Hard

## Evaluation Metrics

* Communication
* Confidence
* Technical Accuracy
* Problem Solving
* Overall Score

## Expected Output

Question Feedback

Question Score

Final Report

Improvement Areas

Recommended Topics

---

# Company Hub Requirements

## Features

Company Information

Hiring Process

Technical Topics

HR Questions

Preparation Tips

Interview Experiences

## Initial Companies

* Cognizant
* TCS
* Infosys
* Accenture
* EY
* Wipro

## Future Companies

* Capgemini
* Deloitte
* HCL
* Zoho
* Amazon
* Microsoft

---

# Database Standards

## Database

MySQL

## Design Rules

* Normalized Schema
* Proper Foreign Keys
* Timestamp Tracking
* Soft Extensibility

## Naming Convention

snake_case

Examples:

created_at

updated_at

target_role

---

# Backend Development Standards

## Architecture

Controller

Service

Repository

Entity

DTO

Mapper

Security

Exception

Config

AI

Util

## Coding Standards

* Constructor Injection
* DTO Pattern
* Clean Architecture
* RESTful APIs
* Validation Annotations
* Global Exception Handler

## Security Standards

* JWT Authentication
* BCrypt Password Encoding
* Role-Based Authorization Ready

---

# Frontend Development Standards

## Architecture

Pages

Components

Layouts

Services

Hooks

Context

Utils

Assets

## Rules

* Functional Components Only
* React Hooks
* Material UI Components
* Responsive Design
* Reusable Components

## Shared Components

* Sidebar
* Navbar
* ScoreCard
* ProgressCard
* CompanyCard
* ResumeCard
* InterviewCard

---

# AI Development Standards

## Provider

Gemini API

## Rules

* Return Structured JSON
* Avoid Unstructured Responses
* Provide Actionable Feedback
* Generate Scores
* Generate Recommendations

## AI Features

Resume Analysis

Interview Evaluation

Roadmap Generation (Future)

Career Mentor (Future)

---

# Deployment Standards

## Development

Frontend:
React

Backend:
Spring Boot

Database:
MySQL

## Production

Frontend:
Vercel

Backend:
AWS EC2

Database:
MySQL

Containerization:
Docker

---

# Current Sprint

Sprint Number:
1

Sprint Name:
Authentication Module

## Deliverables

* User Entity
* User Repository
* Auth DTOs
* Auth Service
* JWT Utility
* JWT Filter
* Security Configuration
* Auth Controller
* Register API
* Login API
* Global Exception Handler

Status:
Not Started

---

# Future Roadmap

## Phase 2

* AI Career Mentor
* Job Matching
* RAG Knowledge Base
* Placement Readiness Analytics

## Phase 3

* College Admin Dashboard
* Placement Coordinator Dashboard
* Recruiter Portal
* Student Analytics

## Phase 4

* Mobile Application
* AI Voice Interviewer
* Real-Time Mock Interviews

---

# Source of Truth

All development decisions must follow:

1. PRD.md
2. Database_Schema.md
3. API_Specification.md
4. UI_Wireframes.md
5. PLACEMENT_PLATFORM_MASTER_CONTEXT.md

These documents are the official project documentation and source of truth for the platform.

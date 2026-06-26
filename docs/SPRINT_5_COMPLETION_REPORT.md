# Sprint 5 Completion Report

## Project

AI Placement Platform

## Sprint

Sprint 5 – Company Hub & Placement Eligibility Engine

## Status

COMPLETED

## Completion Date

June 2026

---

# Objective

The objective of Sprint 5 was to implement company management and the Placement Eligibility Engine that evaluates whether a student is eligible for a company based on academic and skill criteria.

---

# Features Implemented

## Company Management

Implemented:

* Create Company
* View All Companies
* View Company Details

Endpoints:

* POST /api/companies
* GET /api/companies
* GET /api/company/{id}

---

## Placement Eligibility Engine

Implemented a dedicated EligibilityService responsible for evaluating:

* Minimum CGPA
* Eligible Branches
* Required Skills
* Missing Skills Detection

The engine returns detailed eligibility information including:

* eligible
* cgpaMatched
* branchMatched
* skillsMatched
* missingSkills

---

# Database Changes

Extended TargetCompany with:

* description
* minCgpa

Created supporting tables:

* company_eligible_branches
* company_required_skills

Relationships:

* Company ↔ Required Skills (Many-to-Many)
* Company ↔ Eligible Branches (Collection Table)

---

# Security

All endpoints are JWT protected.

Authenticated users are resolved from the Security Context.

No userId is accepted from frontend requests.

---

# Testing Results

## Backend

* BUILD SUCCESS
* Unit Tests: 10 Passed

## Functional Testing

### Authentication

PASSED

### Company Creation

PASSED

### Company Listing

PASSED

### Company Details

PASSED

### Eligibility Engine

Positive Scenario

* CGPA matched
* Branch matched
* Skills matched

Result:

eligible = true

PASSED

Negative Scenario

Required Skill Missing

Missing Skill:

Azure

Result:

eligible = false

missingSkills = ["Azure"]

PASSED

---

# Architecture

Controller

↓

CompanyService

↓

EligibilityService

↓

Repositories

Business rules remain isolated inside EligibilityService.

---

# Sprint Outcome

Sprint 5 successfully delivers the core Placement Eligibility Engine, enabling the platform to evaluate student eligibility against company-specific academic and skill requirements.

This module establishes the foundation for future AI-powered recommendations and placement analytics.

Status: COMPLETED

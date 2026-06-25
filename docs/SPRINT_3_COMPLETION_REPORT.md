# Sprint 3 Completion Report

## Project

AI Placement Platform

## Sprint

Sprint 3 – Skills & Target Companies Module

## Status

COMPLETED

## Completion Date

25-06-2026

---

# Sprint Objective

Implement student skill management and target company management functionality with JWT-protected access.

The sprint focused on:

* Add Skill
* View Skills
* Delete Skill
* Add Target Company
* View Target Companies
* Delete Target Company
* Duplicate Prevention
* Database Persistence
* JWT Authentication Integration

---

# Features Implemented

## Skills Module

### Add Skill

Endpoint:

POST /api/profile/skills

### Get Skills

Endpoint:

GET /api/profile/skills

### Delete Skill

Endpoint:

DELETE /api/profile/skills/{skillId}

---

## Target Companies Module

### Add Company

Endpoint:

POST /api/profile/companies

### Get Companies

Endpoint:

GET /api/profile/companies

### Delete Company

Endpoint:

DELETE /api/profile/companies/{companyId}

---

# Security

Implemented JWT-protected access for all Sprint 3 endpoints.

Authentication Flow:

JWT Token
→ Security Filter
→ Security Context
→ Authenticated User
→ Skill/Company Services

No userId is accepted from the frontend.

User identity is derived exclusively from the JWT token.

---

# Database Tables Implemented

## skills

Stores master skill records.

Columns:

* id
* name
* created_at

---

## user_skills

Maps users to skills.

Columns:

* id
* user_id
* skill_id
* created_at

Constraint:

Unique (user_id, skill_id)

---

## target_companies

Stores master company records.

Columns:

* id
* name
* created_at

---

## user_target_companies

Maps users to target companies.

Columns:

* id
* user_id
* company_id
* created_at

Constraint:

Unique (user_id, company_id)

---

# Backend Components Added

## Controllers

* SkillController
* CompanyController

## DTOs

* SkillDto
* AddSkillRequestDto
* CompanyDto
* AddCompanyRequestDto

## Services

* SkillService
* SkillServiceImpl
* CompanyService
* CompanyServiceImpl

## Repositories

* SkillRepository
* UserSkillRepository
* TargetCompanyRepository
* UserTargetCompanyRepository

## Mappers

* SkillMapper
* TargetCompanyMapper

## Entities

* Skill
* UserSkill
* TargetCompany
* UserTargetCompany

## Exceptions

* SkillAlreadyExistsException
* CompanyAlreadyExistsException
* ResourceNotFoundException

---

# Validation Implemented

## Skills

* Name required
* Name length validation

## Companies

* Name required
* Name length validation

---

# Duplicate Prevention

Implemented at two levels:

## Service Layer

Prevents duplicate mappings before database insertion.

## Database Layer

Unique constraints:

* (user_id, skill_id)
* (user_id, company_id)

---

# Testing Results

## Skills

### Add Skill

Status: PASSED

### Get Skills

Status: PASSED

### Duplicate Skill Detection

Status: PASSED

### Delete Skill

Status: PASSED

---

## Target Companies

### Add Company

Status: PASSED

### Get Companies

Status: PASSED

### Duplicate Company Detection

Status: PASSED

### Delete Company

Status: PASSED

---

## Security

JWT Protected Access

Status: PASSED

---

## Database Persistence

Status: PASSED

Records successfully persisted and retrieved from MySQL.

---

# Build Verification

Command:

mvn clean compile

Result:

BUILD SUCCESS

Compiled Source Files:

49 Java source files

---

# Sprint Outcome

Sprint 3 successfully delivered:

* Skill Management
* Target Company Management
* Duplicate Prevention
* Database Persistence
* JWT Protected Access

All acceptance criteria defined in the roadmap were satisfied.

---

# Git Information

Commit:

Sprint 3 Skills and Target Companies Module Completed

Tag:

sprint-3-completed

---

# Next Sprint

Sprint 4 – Resume Management Module

Planned Scope:

* Resume Upload
* Resume Download
* Resume Replacement
* Resume Metadata Storage
* File Validation
* Secure User Access

---

# Sprint Status

COMPLETED

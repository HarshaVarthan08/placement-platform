# Sprint 2 Completion Report

## Project

AI Placement Platform

## Sprint

Sprint 2 – Student Profile Module

## Status

COMPLETED ✅

Completion Date:
24 June 2026

---

# Sprint Objective

Allow authenticated users to:

* View their profile
* Update their profile
* Persist profile changes in database

All profile operations must be secured using JWT authentication.

---

# Scope Implemented

## Profile Retrieval

Implemented:

GET /api/profile/me

Capabilities:

* Retrieve authenticated user's profile
* Resolve user from JWT token
* Return profile information

---

## Profile Update

Implemented:

PUT /api/profile/me

Capabilities:

* Update profile information
* Persist changes to database
* Validate incoming data
* Return updated profile

---

# Editable Fields

The following fields can be updated:

* name
* college
* degree
* branch
* cgpa
* graduationYear
* targetRole

---

# Read Only Fields

The following fields are protected:

* id
* email
* role
* isActive

Users cannot modify these fields through Sprint 2 APIs.

---

# Security Implementation

Authentication:

JWT Token

Authorization Strategy:

Authenticated user is resolved through Spring Security Context.

Implementation Flow:

JWT Token
↓
JwtAuthenticationFilter
↓
SecurityContext
↓
Authenticated Email
↓
UserRepository.findByEmail()

No userId is accepted from client requests.

This prevents profile access to other users.

---

# Architecture Implemented

Controller
↓
Service
↓
Mapper
↓
Repository

Implemented Components:

## Controller

ProfileController

Endpoints:

GET /api/profile/me

PUT /api/profile/me

---

## Service

ProfileService

ProfileServiceImpl

Responsibilities:

* Fetch authenticated user
* Update profile fields
* Persist data
* Return DTOs

---

## Mapper

ProfileMapper

Responsibilities:

User Entity
↓
ProfileResponseDto

---

## DTOs

ProfileResponseDto

UpdateProfileRequestDto

---

## Exception Handling

UserNotFoundException

GlobalExceptionHandler updated to support:

404 Not Found

for missing users.

---

# Validation Rules

Name

@NotBlank
@Size(max = 100)

College

@Size(max = 150)

Degree

@Size(max = 100)

Branch

@Size(max = 100)

CGPA

@DecimalMin("0.0")
@DecimalMax("10.0")

Graduation Year

@Min(2000)
@Max(2100)

Target Role

@Size(max = 100)

---

# API Validation Results

## Test 1

GET /api/profile/me

Result:

PASSED ✅

Response returned authenticated user profile.

---

## Test 2

PUT /api/profile/me

Result:

PASSED ✅

Profile updated successfully.

---

## Test 3

Database Persistence

Result:

PASSED ✅

Changes correctly stored in MySQL users table.

---

## Test 4

JWT Authentication

Result:

PASSED ✅

Only authenticated users can access profile endpoints.

---

# Database Verification

Verified Fields:

* name
* college
* degree
* branch
* cgpa
* graduation_year
* target_role

All values successfully persisted.

---

# Files Added

ProfileController.java

ProfileResponseDto.java

UpdateProfileRequestDto.java

ProfileMapper.java

ProfileService.java

ProfileServiceImpl.java

UserNotFoundException.java

---

# Files Modified

GlobalExceptionHandler.java

---

# Git Milestone

Commit:

f8956c7

Message:

Sprint 2 Student Profile Module Completed

Tag:

sprint-2-completed

---

# Sprint Outcome

Sprint 2 successfully delivers:

* Student Profile Management
* JWT-Protected Profile Access
* Profile Update Functionality
* Data Validation
* Clean Layered Architecture

The platform now supports:

Authentication
+
Profile Management

and is ready for Sprint 3.

---

# Next Sprint

Sprint 3 – Skills & Target Companies Module

Planned Features:

* Add Skill
* Remove Skill
* View Skills
* Add Target Company
* Remove Target Company
* View Target Companies

Dependencies:

Sprint 1 Authentication ✅

Sprint 2 Profile Management ✅

Status:

READY TO START

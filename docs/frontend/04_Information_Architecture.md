# AI Placement Platform

# Information Architecture

Document Version: 1.0

Status: Frozen

Sprint: Frontend Sprint F1

---

# Purpose

This document defines how information, pages, and features are organized throughout the AI Placement Platform.

It ensures users can navigate the platform intuitively and developers maintain a consistent structure.

---

# Application Structure

Landing

↓

Authentication

↓

Dashboard

↓

Career Orbit™

↓

Resume

↓

Interview

↓

Jobs

↓

AI Coach

↓

Career Intelligence

↓

Premium

↓

Profile

↓

Settings

↓

Notifications

↓

Admin (Admin Role Only)

---

# Navigation Hierarchy

## Public Navigation

- Landing
- Features
- Career Orbit™
- Pricing
- About
- Login
- Register

---

## Authenticated Navigation

Dashboard

Career Orbit™

Resume

Interview

Jobs

AI Coach

Career Intelligence

Premium

Profile

Settings

Notifications

---

## Admin Navigation

Dashboard

Users

Jobs

Analytics

Reports

Premium

System Health

---

# Information Hierarchy

Every page follows the same hierarchy.

1. Header
2. Primary Content
3. Supporting Information
4. Insights
5. Recommended Action

---

# Dashboard Hierarchy

Dashboard

├── Career Orbit™

├── Today's Mission

├── Statistics

├── AI Recommendations

├── Recent Activity

├── Upcoming Tasks

└── Job Recommendations

---

# Resume Module

Resume

├── Upload Resume

├── Resume Score

├── Analysis

├── Weak Sections

├── AI Suggestions

└── Download

---

# Interview Module

Interview

├── Readiness Score

├── Practice

├── Mock Interview

├── Feedback

└── Progress

---

# Job Module

Jobs

├── Recommended Jobs

├── Search

├── Filters

├── Saved Jobs

├── Applied Jobs

└── Application Tracker

---

# AI Coach Module

AI Coach

├── Chat

├── Suggested Questions

├── Learning Plan

├── Resources

└── Recommendations

---

# Career Intelligence Module

Career Intelligence

├── Readiness

├── Career Confidence

├── Skill Gap

├── Market Insights

└── Improvement Plan

---

# Premium Module

Premium

├── Benefits

├── Comparison

├── Features

├── Pricing

└── Waitlist

---

# Profile Module

Profile

├── Personal Details

├── Education

├── Skills

├── Projects

├── Certifications

├── Preferences

└── Account

---

# Settings Module

Settings

├── General

├── Security

├── Notifications

├── Privacy

├── Appearance

└── Accessibility

---

# Notification Module

Notifications

├── Jobs

├── Resume

├── Interview

├── Career Orbit™

├── AI Coach

└── System

---

# User Journey Flow

Landing

↓

Register

↓

Onboarding

↓

Dashboard

↓

Today's Mission

↓

Career Orbit™

↓

Improve Resume

↓

Practice Interview

↓

Apply Jobs

↓

Placement

---

# Screen Relationships

Landing

→ Register

→ Login

Login

→ Dashboard

Dashboard

→ All Modules

Career Orbit™

→ Resume

→ Interview

→ Jobs

→ AI Coach

→ Career Intelligence

---

# Search Scope

Search supports:

- Jobs
- Skills
- Companies
- Pages
- Help Articles

---

# Permissions

Guest

- Landing
- Login
- Register

Student

- All student modules

Admin

- Student modules
- Admin Dashboard
- Reports
- User Management

---

# Design Rules

- Maximum two clicks to reach any primary feature.
- Consistent sidebar navigation.
- Consistent page headers.
- Every page includes one primary CTA.
- Every module links back to Dashboard.

---

# Future Expansion

Supports:

- Recruiter Portal
- University Portal
- Mentor Portal
- Mobile App
- Progressive Web App

without changing the existing hierarchy.

---

# References

- Product Principles
- Frontend Master Context
- Design Philosophy
- User Journey
- Design System
- Component Library
- Screen Blueprints
- Frontend Architecture

---

# Change Log

Version 1.0

- Initial Information Architecture.
- Approved during Frontend Sprint F1.

---

Status

Frozen

Approved for Frontend Development
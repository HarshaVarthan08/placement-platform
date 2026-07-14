# AI Placement Platform

# Screen Blueprints

Document Version: 1.0

Status: Frozen

Sprint: Frontend Sprint F1

---

# Purpose

This document defines the structure, purpose, and content of every screen in the AI Placement Platform.

It serves as the UI implementation blueprint for frontend development.

---

# Design Rules

Every screen must:

- Follow the Design System
- Use reusable components
- Be responsive
- Support accessibility
- Include loading states
- Include empty states
- Include error states
- Have one primary CTA
- Display clear navigation

---

# Global Layout

Authenticated Layout

Top Navigation

↓

Sidebar

↓

Page Header

↓

Main Content

↓

Footer

---

# Public Screens

## Landing Page

Purpose

Convert visitors into registered users.

Sections

- Hero
- Features
- Career Orbit™ Preview
- How It Works
- Success Stories
- Premium Preview
- FAQ
- Footer

Primary CTA

Start Your Career Journey

---

## Login

Components

- Email
- Password
- Remember Me
- Forgot Password
- Login Button

Secondary CTA

Create Account

---

## Registration

Steps

- Personal Information
- Education
- Career Interests
- Confirmation

Progress Indicator Required

---

## Forgot Password

Flow

Email

↓

OTP

↓

New Password

↓

Success

---

## Email Verification

Displays

Verification Status

Resend Email

Continue Button

---

## Onboarding

Steps

- Welcome
- Career Orbit™
- Dashboard Overview
- Resume Upload
- Ready to Begin

---

# Authenticated Screens

## Dashboard

Purpose

Daily workspace.

Sections

- Welcome Header
- Career Orbit™
- Today's Mission
- Statistics
- AI Recommendations
- Recent Activity
- Upcoming Tasks
- Job Recommendations

Primary CTA

Continue Improving

---

## Career Orbit™

Purpose

Visual representation of career readiness.

Displays

- User Avatar
- Overall Readiness
- Resume
- Skills
- Interview
- Jobs
- AI Coach
- Career Intelligence

Interactions

Hover

Click

Tooltip

Progress Animation

---

## Resume

Sections

- Resume Score
- Upload
- Analysis
- Weak Sections
- Suggestions
- Download

Primary CTA

Improve Resume

---

## Interview

Sections

- Readiness Score
- Practice
- Mock Interview
- Feedback
- Progress

Primary CTA

Start Practice

---

## Jobs

Sections

- Recommended Jobs
- Filters
- Saved Jobs
- Applied Jobs
- Tracker

Primary CTA

Apply Now

---

## AI Coach

Sections

- Chat
- Suggested Questions
- Learning Plan
- Recommendations
- Resources

Primary CTA

Ask AI

---

## Career Intelligence

Displays

- Career Confidence
- Readiness
- Skill Gap
- Timeline
- AI Predictions

Primary CTA

Improve Readiness

---

## Premium

Sections

- Benefits
- Comparison
- Features
- Pricing
- Waitlist

Primary CTA

Join Premium

---

## Profile

Sections

- Personal Information
- Education
- Skills
- Projects
- Certifications
- Preferences

Primary CTA

Save Changes

---

## Notifications

Categories

- Jobs
- Resume
- Interview
- Career Orbit™
- AI Coach
- System

---

## Settings

Sections

- General
- Security
- Notifications
- Privacy
- Appearance
- Accessibility

---

# Admin Screens

Admin Dashboard

Users

Jobs

Reports

Analytics

Premium

System Health

---

# Empty States

Every screen should display

- Illustration
- Friendly Message
- Primary Action

Never leave blank pages.

---

# Loading States

Support

- Skeleton Loader
- Progress Indicator
- Smooth Fade

---

# Error States

Display

- Error Message
- Explanation
- Retry Button
- Support Link

---

# Success States

Celebrate

- Resume Improvement
- Career Orbit™ Increase
- Interview Completion
- Job Application
- Profile Completion

---

# Navigation Rules

Maximum two clicks to reach any primary feature.

Sidebar remains consistent.

Breadcrumbs on workspace pages.

Dashboard is always accessible.

---

# Responsive Behaviour

Desktop

Sidebar

Multiple Columns

Tablet

Collapsible Sidebar

Mobile

Drawer Navigation

Single Column

Career Orbit™ adapts to every screen size.

---

# Accessibility

Support

- Keyboard Navigation
- Screen Readers
- Visible Focus
- Reduced Motion

Target

WCAG AA

---

# Performance

- Lazy Loading
- Optimized Images
- Route Splitting
- Efficient Rendering

---

# Future Expansion

Supports

- Dark Mode
- Recruiter Portal
- University Portal
- Mentor Portal
- Mobile Application

without redesign.

---

# References

- Product Principles
- Design System
- Component Library
- Frontend Architecture

---

# Change Log

Version 1.0

- Initial Screen Blueprints approved.

---

Status

Frozen

Approved for Frontend Development
# AI Placement Platform

# Frontend Architecture

Document Version: 1.0

Status: Frozen

Sprint: Frontend Sprint F1

---

# Purpose

Defines the React architecture for the frontend.

---

# Technology Stack

Framework

React

Language

TypeScript

Build Tool

Vite

UI

Material UI

Icons

Lucide React

Routing

React Router

HTTP

Axios

Forms

React Hook Form

Validation

Zod

Animation

Framer Motion

Charts

Chart.js

---

# Folder Structure

src/

assets/

components/

pages/

layouts/

hooks/

services/

api/

context/

routes/

theme/

types/

utils/

config/

constants/

styles/

tests/

---

# Layer Architecture

Presentation

↓

Business Logic

↓

API Services

↓

Backend

---

# Routing

Public

- Landing
- Login
- Register
- Forgot Password

Protected

- Dashboard
- Career Orbit™
- Resume
- Interview
- Jobs
- AI Coach
- Career Intelligence
- Premium
- Profile

Admin

- Dashboard
- Users
- Reports
- Analytics

---

# State Management

Global

- Authentication
- User
- Notifications
- Theme

Local

- Forms
- Dialogs
- Filters

Server

- Jobs
- Resume
- AI
- Career Intelligence

---

# API Layer

Centralized Axios instance.

JWT interceptor.

Global error handling.

---

# Theme

Central Material UI Theme.

Uses Design Tokens.

No hardcoded colors.

---

# Performance

- Lazy Loading
- Route Splitting
- Image Optimization
- Memoization
- Tree Shaking

---

# Security

JWT

Protected Routes

Role Guards

Input Validation

HTTPS

---

# Accessibility

WCAG AA

Keyboard Navigation

Reduced Motion

Focus Indicators

---

# Testing

Unit

Integration

Component

E2E (Future)

---

# Coding Standards

Reusable Components

Single Responsibility

Strict TypeScript

ESLint

Prettier

---

# References

Design System

Component Library

Screen Blueprints

---

# Change Log

Version 1.0

---

Status

Frozen
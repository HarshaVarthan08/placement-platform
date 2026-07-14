# AI Placement Platform

# Component Library

Document Version: 1.0

Status: Frozen

Sprint: Frontend Sprint F1

---

# Purpose

This document defines the reusable UI components used throughout the AI Placement Platform.

Every page should be built using these components to ensure consistency, maintainability, and scalability.

---

# Component Principles

Every component should be

- Reusable
- Responsive
- Accessible
- Modular
- Token Driven
- Easy to Maintain

---

# Component Categories

- Foundation
- Forms
- Navigation
- Data Display
- Feedback
- Business Components

---

# Foundation Components

## Button

Variants

- Primary
- Secondary
- Outline
- Text
- Success
- Danger

States

- Default
- Hover
- Focus
- Active
- Disabled
- Loading

---

## Icon Button

Supports

- Tooltip
- Loading
- Disabled

---

## Typography

Variants

- Heading
- Subheading
- Body
- Caption
- Label

---

## Avatar

Supports

- User Image
- Initials
- Placeholder

Sizes

- Small
- Medium
- Large

---

## Badge

Examples

- Premium
- Recommended
- New
- Applied
- Completed

---

## Chip

Used for

- Skills
- Technologies
- Companies
- Roles

---

## Divider

Horizontal

Vertical

---

## Tooltip

Displayed on hover or focus.

---

## Progress Indicator

Types

- Linear
- Circular
- Percentage

---

## Skeleton Loader

Used while loading

- Cards
- Tables
- Dashboard
- Career Orbit™

---

# Form Components

- Text Field
- Password Field
- Search Bar
- Dropdown
- Select
- Checkbox
- Radio Button
- Switch
- Slider
- Date Picker
- OTP Input
- File Upload
- Text Area
- Tag Input

All form components support

- Label
- Placeholder
- Validation
- Error Message
- Helper Text
- Disabled State

---

# Navigation Components

- Top Navigation
- Sidebar
- Drawer
- Breadcrumb
- Tabs
- Pagination
- Step Indicator

---

# Data Display Components

## Generic Card

Variants

- Standard
- Elevated
- Interactive
- Compact

---

## Statistic Card

Displays

- Title
- Value
- Trend
- Icon

---

## Progress Card

Displays

- Progress
- Percentage
- Status

---

## Recommendation Card

Displays

- Recommendation
- Reason
- Expected Benefit
- Action

---

## Job Card

Displays

- Company
- Role
- Package
- Location
- Match Score
- Apply Button

---

## Resume Card

Displays

- Resume Score
- Upload Status
- Improve Button

---

## Skill Card

Displays

- Skill Name
- Current Level
- Target Level
- Learning Action

---

## Timeline

Used for

- Learning Progress
- Application Status
- Activity History

---

## Charts

Supported

- Bar
- Line
- Pie
- Radar
- Area

---

## Tables

Support

- Search
- Sort
- Pagination
- Responsive Layout

---

# Feedback Components

- Alert
- Toast
- Snackbar
- Confirmation Dialog
- Loading Screen
- Empty State
- Error State
- Offline Banner
- Success Banner

---

# Business Components

## Career Orbit™

Displays

- User Avatar
- Career Readiness
- Orbit Nodes
- Progress
- AI Insights

Nodes

- Resume
- Skills
- Interview
- Jobs
- AI Coach
- Career Intelligence

---

## Today's Mission Card

Displays

- Mission
- Estimated Time
- Expected Benefit
- Start Button

---

## Career Intelligence Card

Displays

- Career Confidence
- Readiness
- Timeline
- Suggestions

---

## AI Insight Card

Displays

- Recommendation
- Reason
- Action

---

## Skill Gap Card

Displays

- Missing Skills
- Priority
- Learning Suggestion

---

## Resume Analyzer

Displays

- Score
- Weak Sections
- Suggestions

---

## Interview Readiness Card

Displays

- Confidence
- Weak Topics
- Practice Button

---

## Premium Banner

Displays

- Benefits
- Upgrade CTA

---

## Premium Lock

Used for premium-only features.

Should explain value rather than restrict users aggressively.

---

# Component States

Every component should support

- Default
- Hover
- Focus
- Active
- Disabled
- Loading
- Error

---

# Accessibility

All interactive components must support

- Keyboard Navigation
- Screen Readers
- Visible Focus
- ARIA Labels

---

# Responsive Behaviour

Every component defines

Desktop

Tablet

Mobile

Behavior.

---

# Performance

Components should

- Minimize re-renders
- Lazy load where appropriate
- Reuse logic
- Follow React best practices

---

# Folder Structure

components/

foundation/

forms/

navigation/

data-display/

feedback/

career/

premium/

shared/

---

# Naming Convention

Use PascalCase.

Examples

Button

CareerOrbit

JobCard

ResumeCard

PremiumBanner

---

# Future Expansion

The component library should support

- Dark Mode
- Recruiter Portal
- University Portal
- Mobile App
- Premium Features

without redesign.

---

# References

- Design System
- Screen Blueprints
- Frontend Architecture

---

# Change Log

Version 1.0

- Initial Component Library approved.

---

Status

Frozen

Approved for Frontend Development
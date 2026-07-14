\# AI Placement Platform



\# Frontend Master Context



\*\*Document Version:\*\* 1.0



\*\*Status:\*\* Frozen



\*\*Sprint:\*\* Frontend Sprint F1 – Documentation Freeze



\*\*Last Updated:\*\* July 2026



\---



\# 1. Purpose



This document provides the complete context for the frontend of the AI Placement Platform.



It serves as the primary reference for developers, AI coding assistants, and future contributors before implementing any frontend feature.



This document summarizes the product vision, architecture, technology stack, design philosophy, engineering standards, and project status.



\---



\# 2. Project Overview



\## Project Name



AI Placement Platform



\## Project Type



Full Stack SaaS Platform



\## Target Users



\- College Students

\- Fresh Graduates

\- Placement Coordinators (Future)

\- Recruiters (Future)

\- Mentors (Future)



\---



\# 3. Product Goal



Build an AI-powered career preparation platform that helps students:



\- Build strong resumes

\- Prepare for interviews

\- Track placement readiness

\- Discover job opportunities

\- Improve skills

\- Receive AI-driven career guidance



The platform should become a student's daily career companion rather than a traditional placement portal.



\---



\# 4. Current Project Status



\## Backend



Status



✅ Backend Beta v1.0 Frozen



Completed



\- Authentication

\- Authorization

\- JWT Security

\- Resume Module

\- Career Intelligence APIs

\- Job Recommendation APIs

\- Premium Infrastructure

\- Logging

\- Health Monitoring

\- Swagger Documentation

\- Docker Support

\- Environment Profiles

\- Production Configuration



Backend is considered production-ready for frontend integration.



\---



\## Frontend



Status



Frontend Sprint F1 Completed



Design foundation approved.



Implementation has not yet started.



The frontend architecture is frozen and ready for development.



\---



\# 5. Frontend Vision



The frontend should provide a premium user experience comparable to modern SaaS products.



Key characteristics:



\- Clean

\- Professional

\- Fast

\- Responsive

\- Accessible

\- Highly interactive



The application should feel like a career operating system rather than a student portal.



\---



\# 6. Core Product Experience



The user journey is centered around continuous improvement.



Visitor



↓



Registration



↓



Onboarding



↓



Dashboard



↓



Career Orbit™



↓



Daily Mission



↓



Resume Improvement



↓



Interview Preparation



↓



Job Applications



↓



Placement



↓



Career Growth



Every screen should naturally guide users toward the next meaningful action.



\---



\# 7. Signature Experience



Career Orbit™



Career Orbit™ is the visual identity of the platform.



It represents overall placement readiness through an interactive orbit containing major career dimensions such as:



\- Resume

\- Skills

\- Interview

\- Jobs

\- AI Coach

\- Career Intelligence



Career Orbit™ should appear consistently throughout the platform and remain the primary visualization of progress.



\---



\# 8. Design Philosophy



The frontend follows these principles:



\- Clarity

\- Simplicity

\- Consistency

\- Guidance

\- Motivation

\- Accessibility



The interface should encourage progress without overwhelming users.



\---



\# 9. Technology Stack



Framework



React



Language



TypeScript



Build Tool



Vite



UI Library



Material UI



Icons



Lucide React



Routing



React Router



HTTP Client



Axios



Forms



React Hook Form



Validation



Zod



Animation



Framer Motion



Charts



Chart.js



\---



\# 10. Frontend Architecture



The application follows a layered architecture.



Presentation Layer



↓



Business Layer



↓



API Layer



↓



Backend Services



Pages should never communicate directly with backend APIs.



Reusable services and hooks should encapsulate application logic.



\---



\# 11. Folder Structure



Frontend source code should follow this structure.



src/



assets/



components/



pages/



layouts/



hooks/



services/



api/



context/



theme/



routes/



utils/



types/



config/



constants/



styles/



tests/



\---



\# 12. Component Philosophy



All UI should be built using reusable components.



Component categories include:



\- Foundation

\- Forms

\- Navigation

\- Feedback

\- Data Display

\- Business Components



Every component should be responsive, accessible, and token-driven.



\---



\# 13. Design System



The design system governs:



\- Colors

\- Typography

\- Spacing

\- Icons

\- Shadows

\- Borders

\- Motion

\- Components



Hardcoded design values should be avoided.



\---



\# 14. Responsive Strategy



The application is mobile-first.



Supported breakpoints:



\- Mobile

\- Tablet

\- Laptop

\- Desktop

\- Wide Desktop



Functionality should remain identical across devices while layouts adapt appropriately.



\---



\# 15. Accessibility



The project targets WCAG AA compliance.



Key requirements include:



\- Keyboard navigation

\- Screen reader support

\- Visible focus states

\- High contrast

\- Reduced motion support



Accessibility is considered mandatory.



\---



\# 16. Performance Goals



Target objectives:



\- Fast initial load

\- Lazy-loaded routes

\- Optimized assets

\- Smooth animations

\- Minimal layout shifts



Performance should remain a priority throughout development.



\---



\# 17. Engineering Standards



Frontend code should emphasize:



\- Reusability

\- Modularity

\- Maintainability

\- Testability

\- Readability

\- Scalability



Duplicate implementations should be avoided.



\---



\# 18. Planned Modules



The platform currently includes the following modules:



\- Landing Page

\- Authentication

\- Dashboard

\- Career Orbit™

\- Resume Workspace

\- Interview Workspace

\- Job Recommendations

\- AI Coach

\- Career Intelligence

\- Premium

\- Profile

\- Notifications

\- Settings

\- Admin Dashboard



Future modules should follow the same architectural principles.



\---



\# 19. Documentation Structure



Frontend documentation consists of:



\- Product Principles

\- Frontend Master Context

\- Design Philosophy

\- User Journey

\- Information Architecture

\- Design System

\- Component Library

\- Screen Blueprints

\- Animation Guidelines

\- Frontend Architecture

\- Development Roadmap



These documents collectively define the frontend implementation.



\---



\# 20. Implementation Rules



During frontend development:



\- Follow the Design System.

\- Use reusable components.

\- Respect design tokens.

\- Follow screen blueprints.

\- Implement responsive layouts.

\- Validate accessibility.

\- Maintain consistent motion.

\- Use centralized API services.



Any architectural deviation should be documented and reviewed.



\---



\# 21. Future Expansion



The architecture has been designed to support:



\- Premium Subscription

\- Dark Mode

\- Mobile Application

\- Progressive Web App

\- Recruiter Portal

\- University Portal

\- Mentor Portal

\- Enterprise Features



without requiring significant redesign.



\---



\# 22. Definition of Ready



A frontend feature is ready for implementation when:



\- UX is approved.

\- Screen blueprint exists.

\- Components are defined.

\- API contract is available.

\- Responsive behavior is specified.



\---



\# 23. Definition of Done



A feature is complete only when it is:



\- Responsive

\- Accessible

\- Tested

\- Integrated with backend APIs

\- Documented

\- Visually consistent

\- Performance optimized



\---



\# 24. References



This document should be read before:



\- Starting a new frontend feature

\- Planning a sprint

\- Implementing UI

\- Refactoring frontend architecture



Related documents:



\- 00\_Product\_Principles.md

\- 02\_Design\_Philosophy.md

\- 03\_User\_Journey.md

\- 04\_Information\_Architecture.md

\- 05\_Design\_System.md

\- 06\_Component\_Library.md

\- 07\_Screen\_Blueprints.md

\- 08\_Animation\_Guidelines.md

\- 09\_Frontend\_Architecture.md

\- 10\_Development\_Roadmap.md



\---



\# 25. Change Log



\## Version 1.0



\- Initial frontend master context created.

\- Backend Beta v1.0 marked as frozen.

\- Frontend Sprint F1 architecture summarized.

\- Approved for implementation planning.



\---



\# Approval Status



\*\*Status:\*\* Frozen



\*\*Approved For:\*\* Frontend Development



\*\*Sprint:\*\* Frontend Sprint F1 – Documentation Freeze



\*\*Version:\*\* 1.0


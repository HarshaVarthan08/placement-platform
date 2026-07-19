# Git Release Notes: AI Placement Platform Landing Page Release v1.0.0-beta

This release marks the completion of the premium, data-driven, and fully accessible landing page interface for the AI Placement Platform.

---

## 1. Feature Additions
* **Landing Page Sections**:
  - `Hero`: Added landing title, description, call-to-actions, statistics, and floating illustration layers.
  - `CareerOrbit`: Added rotating outer ring orbits, capability hubs, and floating metrics.
  - `Features`: Added tabbed deck showcasing mockup frames of the platform's core preparation tools.
  - `HowItWorks`: Added timeline journey path.
  - `Pricing`: Added responsive pricing matrix.
  - `About`: Added problem challenge cards and mission sections.
  - `CTA`: Added conversion container with trust indicators.
  - `Footer`: Added semantic footer with branding, links columns, and social buttons.
* **Global Navigation**: Created sticky header navigation with responsive mobile menu overlay drawer.
* **Layout Isolation**: Implemented route layout check preventing duplicate footers on the Landing page.

---

## 2. Bug Fixes & Refactoring
* **Duplicate Footer Bug**: Fixed layout nesting inside `PublicLayout.tsx` to conditionally hide the global shared footer when routing path is `ROUTES.LANDING` (`/`).
* **Visual Cleanup**: Removed visible `(Placeholder)` italic text labels from the footer links to clean up lists.
* **TypeScript Refactoring**: Fixed strict type checking inside Framer Motion and MUI Box components by using `motion.create(Box)` instead of component overrides.
* **Icon Ambiguity**: Mapped missing `lucide-react` brand logos (GitHub/LinkedIn) to standard `@mui/icons-material` imports.

---

## 3. Package Dependencies
* **Core Runtime**: `react: ^19.2.7`, `react-dom: ^19.2.7`
* **Routing**: `react-router-dom: ^7.18.1`
* **Styling Theme**: `@mui/material: ^9.2.0`, `@emotion/react: ^11.14.0`, `@emotion/styled: ^11.14.1`
* **Animations**: `framer-motion: ^12.42.2`
* **Icons**: `lucide-react: ^1.24.0`, `@mui/icons-material: ^9.2.0`
* **Compilation**: `vite: ^8.1.1`

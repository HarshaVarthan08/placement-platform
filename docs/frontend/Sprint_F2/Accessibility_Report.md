# Accessibility Report: AI Placement Platform Landing Page

This report details how the landing page fulfills the accessibility (A11y) standards, aiming for **WCAG 2.1 AA** compliance.

---

## 1. Semantic HTML Structure
To support assistive technologies (such as screen readers) and ensure logical document structure, semantic HTML5 elements are utilized across all sections:

* **Page Layout**:
  - Main header area wraps the navigation in `<header>` and `<nav>`.
  - Main content is enclosed in the `<main>` tag.
  - Page footer uses the semantic `<footer>` element.
* **Component Sections**:
  - Each individual page block (Features, Pricing, About, CTA, and Footer) is declared using `<Box component="section">` with proper `id` bindings.
  - Section title tags are configured as headings (e.g. `<h2>` or `<h3>`) and bound via `aria-labelledby` or `aria-describedby` (e.g. `aria-labelledby="cta-title"`).

---

## 2. Keyboard Navigation & Focus-Visible States
Interactive elements (buttons, links, tab items) are fully keyboard-navigable:

1. **Tab Order**: All interactive nodes fall sequentially in logical reading flow order.
2. **Visual Focus Indicators**: Standard outlines are replaced with custom focus rings that contrast clearly with section backgrounds:
   - **CTA Primary & Secondary**: Focus triggers a `3px outline` (e.g. `outline: '3px solid rgba(255, 255, 255, 0.8)'`).
   - **Footer Socials & Links**: Circular buttons trigger `boxShadow` highlights and border color switches (e.g. `borderColor: theme.palette.primary.light`).
3. **Card Focus**: Non-interactive preview cards (like Problem cards and trust cards) include `tabIndex={0}` and `role="article"` to allow screen readers and keyboard users to tab directly to them and read their details sequentially.

---

## 3. Touch Targets & Interactivity Sizes
To prevent accidental mis-clicks on mobile devices, touch targets comply with mobile-friendly design guidelines:
* **Interactive Elements**: All main buttons (CTA, Pricing, Auth links) maintain heights of at least `48px` (accomplished via padding `py: 1.75` and `minHeight` controls).
* **Social Icon Buttons**: Circular buttons in the footer have dimensions of `44px x 44px` (satisfying the iOS HIG minimum guideline of 44px).
* **Navigation Links**: Footer list links use `minHeight: 24` padding overlays to expand their active hover boundary box.

---

## 4. Screen Reader Support & Contrast Compliance
* **ARIA Attributes**:
  - Social buttons utilize descriptive, non-generic `aria-label` values (e.g. `aria-label="Visit our GitHub organization"`, `aria-label="Connect with us on LinkedIn"`).
  - Decorative elements (such as backdrop blur grids, background glows, and SVG separators) are explicitly hidden using `aria-hidden="true"` so they are bypassed by screen readers.
* **Contrast Compliance**:
  - Text colors on the dark CTA card and footer background (`rgba(255, 255, 255, 0.85)` for body, `rgba(255, 255, 255, 0.7)` for subtext) maintain contrast ratios above 4.5:1 against the dark background.
  - Interactive states (like `primary.light` links) remain highly visible under dark viewport settings.

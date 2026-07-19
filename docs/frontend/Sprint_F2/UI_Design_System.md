# UI Design System: AI Placement Platform Landing Page

This document outlines the core tokens, style values, typography guidelines, and layout spacing established in the landing page design.

---

## 1. Color Palette
Colors are configured inside `frontend/src/theme/colors.ts` and map to the following hexadecimal tokens:

### Brand Primary & Secondary
* **Primary (Vibrant Tech Blue)**:
  - `primary.main`: `#0066FF`
  - `primary.light`: `#4D94FF`
  - `primary.dark`: `#0044B3`
  - `primary.contrastText`: `#FFFFFF`
* **Secondary (Aesthetic Deep Purple)**:
  - `secondary.main`: `#7C3AED`
  - `secondary.light`: `#A78BFA`
  - `secondary.dark`: `#5B21B6`
  - `secondary.contrastText`: `#FFFFFF`

### Core Layout Backgrounds
* **Default Light Canvas**: `background.default` = `#F9FAFB` (used in Pricing, Features, and main backgrounds)
* **Paper Card Canvas**: `background.paper` = `#FFFFFF` (used in About, Header, and specific content wrappers)
* **Premium Dark Theme**: `#0B0F19` (used for the Landing Footer background to contrast with preceding modules)
* **Premium Dark Card Gradient**: `linear-gradient(135deg, #0F172A 0%, #1E1B4B 50%, #312E81 100%)` (used in the conversion CTA container to make the block stand out)

### Semantic Colors
* **Success**: `main` = `#10B981` (used for pricing tags, ready markers)
* **Warning**: `main` = `#F59E0B`
* **Error**: `main` = `#EF4444`
* **Borders/Dividers**: `divider` = `#E5E7EB` (light mode default border color)

---

## 2. Typography
Typography styling is defined inside `frontend/src/theme/typography.ts` with the primary font family being **'Inter'**.

* **Font Weights**:
  - Light: `300`
  - Regular: `400`
  - Medium: `500`
  - SemiBold: `600`
  - Bold: `700`
* **Responsive Sizes**:
  - `h1` (Hero Titles): Scaled from `2.5rem` on mobile to `4.5rem` on desktop.
  - `h2` (Section Titles): Scaled from `1.75rem` on mobile to `2.75rem` on desktop.
  - `body1` (Core text): `1rem` on mobile, scaling up to `1.125rem` where appropriate.
  - `body2` (Subtext & details): `0.875rem` / `0.9rem`.

---

## 3. Spacing & Borders
Spacing configurations follow the scale in `frontend/src/theme/spacing.ts`:

### Border Radii
* **`small`**: `8px` (used for buttons, small icons)
* **`medium`**: `12px` (used for cards, trust chips, and problem highlights)
* **`large`**: `16px` (used for dialogs, larger segment groupings)
* **`extraLarge`**: `24px` (used for the premium CTA container card to give it soft rounded edges)
* **`pill`**: `999px` (used for circular elements)

### Elevation Shadows
Shadow configs are detailed in `frontend/src/theme/shadows.ts`:
* **`level1`**: `0px 2px 4px rgba(31, 41, 55, 0.06), 0px 4px 6px rgba(31, 41, 55, 0.1)` (standard card shadow)
* **`level2`**: `0px 10px 15px -3px rgba(31, 41, 55, 0.1), 0px 4px 6px -2px rgba(31, 41, 55, 0.05)` (active/hover card shadow)
* **`level3`**: `0px 20px 25px -5px rgba(31, 41, 55, 0.1), 0px 10px 10px -5px rgba(31, 41, 55, 0.04)` (modal levels)
* **`premiumDark`**: `0px 24px 48px rgba(0, 0, 0, 0.25)` (CTA card drop shadow)

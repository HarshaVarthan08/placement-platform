# Sprint F2 Handover Guide: AI Placement Platform Landing Page

Welcome to the Handover Guide for the landing page of the **AI Placement Platform** developed during Frontend Sprint F2. This document acts as an onboarding guide for incoming developers, detailing how the codebase is structured, key design decisions, and how to maintain or extend the landing page.

---

## 1. What Sprint F2 Delivered
Sprint F2 delivered a premium, highly responsive, interactive, and accessible public-facing landing page. It acts as the acquisition funnel for engineering students.
* **Navigation Header**: Dynamic sticky `<Navbar />` header with responsive scroll opacity adjustments and a mobile sidebar menu drawer.
* **Interactive Components**: Dynamic rotating elements in the `<CareerOrbit />` section, interactive tabs inside the `<Features />` deck, step cards in the `<HowItWorks />` timeline, and responsive layouts inside `<Pricing />` and `<About />`.
* **Conversion Components**: High-contrast premium gradient `<CTA />` container with value-indicator chips, and a semantic dark `<LandingFooter />` that wraps up the page cleanly.

---

## 2. Layout Structure & Code Organization
The code is designed to be highly modular and split logically into directory folders:

* **Entry Point**: The landing page is situated at [Landing.tsx](file:///d:/placement-platform/frontend/src/pages/public/Landing/Landing.tsx). It imports and appends the components sequentially.
* **Component Directory**: All modules reside in [components/landing/](file:///d:/placement-platform/frontend/src/components/landing/) and are exported via `components/landing/index.ts`.
* **Layout Isolation**: The global shared layout shell is located in [PublicLayout.tsx](file:///d:/placement-platform/frontend/src/layouts/PublicLayout.tsx). It holds the `<Navbar />` and conditionally disables the global light footer on the landing page, preventing duplication with the landing page's custom dark footer.

---

## 3. Key Design Decisions

### A. Data-Driven UI Structure
No text, labels, or paths are hardcoded inside rendering files. Each section contains a `*Data.ts` or constants file (e.g. `ctaData.ts`, `footerData.ts`, `plans.ts`).
- **Benefit**: Changing textual copy or link paths does not require modifying component files. This architecture also supports easy migration to content management systems (CMS) or internationalization tools (i18n).

### B. GPU-Accelerated Animations
Framer Motion animations only use GPU-optimized transitions (`opacity`, `transform`) and execute only when sections enter the viewport (`whileInView`). Easing transitions are cast as constants (e.g. `ease: 'easeOut' as const`) to prevent compiler warnings. Easing variables are checked under `prefers-reduced-motion` profiles to disable motion frames dynamically when specified by system settings.

### C. Contrast and Focus Outlines
Because the CTA and Footer components use a dark styling theme (`#0F172A` / `#0B0F19`) in contrast with the rest of the light theme, focus indicators use white high-contrast rings (e.g. `outline: '3px solid rgba(255, 255, 255, 0.8)'`) rather than theme-default colors.

---

## 4. How to Extend & Modify Components

Future developers can easily modify or scale features by targeting specific extension points:

### Replacing Placeholder Links with Real Routes
Currently, links for pages like FAQ, Documentation, Contact, and Privacy Policy use hash anchors (e.g. `#privacy`).
* **Step 1**: Define the actual path inside `src/constants/routes.ts` (e.g. `PRIVACY: '/privacy'`).
* **Step 2**: Open [footerData.ts](file:///d:/placement-platform/frontend/src/components/landing/Footer/footerData.ts) and modify the respective link definitions:
  ```typescript
  // Before:
  { label: 'Privacy Policy', href: '#privacy', isPlaceholder: true }
  // After:
  { label: 'Privacy Policy', href: '/privacy' }
  ```
* **Step 3**: Re-run build tools to verify compiling checks pass successfully.

### Adding New Legal Pages
* **Step 1**: Create a page folder inside `src/pages/public/` (e.g. `PrivacyPolicy/PrivacyPolicy.tsx`).
* **Step 2**: Add the new page to the `PublicLayout` route definitions in [AppRoutes.tsx](file:///d:/placement-platform/frontend/src/routes/AppRoutes.tsx):
  ```typescript
  <Route path={ROUTES.PRIVACY} element={<PrivacyPolicy />} />
  ```
* **Step 3**: Since the global shared layout footer is conditionally hidden *only* on the landing page, these new pages will automatically render the standard global light footer at the bottom.

### Enabling Social Media Redirects
To add or change social redirects, edit the `socialLinks` array inside [footerData.ts](file:///d:/placement-platform/frontend/src/components/landing/Footer/footerData.ts). Social icons are loaded using dynamic element types:
```typescript
{
  name: 'GitHub',
  href: 'YOUR_ACTUAL_URL_HERE',
  icon: GitHubIcon, // imported from @mui/icons-material
  ariaLabel: 'Visit our GitHub organization',
}
```

---

## 5. Verification Commands

When committing modifications, verify code standards using:
```bash
# Verify TypeScript compiles without warnings
$ npm run build

# Verify ESLint code standards pass cleanly
$ npm run lint

# Format code automatically using Prettier configurations
$ npm run format
```

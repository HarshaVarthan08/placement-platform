# Responsive Design Guidelines

## Document Metadata
- **Version:** 1.0.0
- **Status:** Frozen (Approved for Frontend Implementation)
- **Scope:** Frontend Sprint F3: Spacing, grid systems, scaling, orientation, and access guidelines
- **References:**
  - [Sprint_F3_Project_Plan.md](file:///d:/placement-platform/docs/frontend/Sprint_F3/Sprint_F3_Project_Plan.md)
  - [Dashboard_Architecture.md](file:///d:/placement-platform/docs/frontend/Sprint_F3/Dashboard_Architecture.md)
  - [Dashboard_Wireframes.md](file:///d:/placement-platform/docs/frontend/Sprint_F3/Dashboard_Wireframes.md)

---

## 2. Responsive Design Philosophy

The AI Placement Platform dashboard uses a mobile-first design strategy. Layouts are designed for smaller viewports first and progressively enhanced for larger screens. The layout is built around four key guidelines:

- **Mobile-First Progressive Enhancement:** Base styles are designed for mobile screens. Desktop layouts are applied progressively via CSS media queries as screen size increases, keeping code clean and performant.
- **Fluid Layout Grids:** Spacing, widths, and columns are defined using fluid percentages and CSS flexbox/grid configurations rather than fixed pixel dimensions, allowing components to scale smoothly between breakpoints.
- **Consistent Spacing System:** All margins, paddings, gaps, and heights are based on an 8px grid system, ensuring visual alignment and design consistency across all screens.
- **Touch & Accessibility Compliance:** Touch targets, font contrast ratios, and layout elements are optimized for accessibility, meeting WCAG 2.1 AA requirements across all device classes.

---

## 3. Supported Devices & Breakpoints

The design system supports five distinct screen sizes:

| Device Class | Viewport Range | Code Prefix | Sidebar Navigation Layout | Content Margin |
| :--- | :--- | :--- | :--- | :--- |
| **Mobile** | `< 600px` | `xs` | Hidden (temporary Drawer) | `12px` |
| **Tablet** | `600px - 899px` | `sm` | Hidden (temporary Drawer) | `16px` |
| **Laptop** | `900px - 1199px` | `md` | Collapsible Rail (collapsed default) | `24px` |
| **Desktop**| `1200px - 1535px`| `lg` | Docked Panel (260px fixed) | `24px` |
| **Wide** | `>= 1536px` | `xl` | Docked Panel (260px fixed) | `32px` |

---

## 4. Spacing System

All layout dimensions are based on an 8px grid system to ensure visual consistency:

```
Spacing Scale:
4px  (0.5x)  -->  Subtle paddings, checkbox labels, line-height adjustments
8px  (1.0x)  -->  Small gaps, button padding, list item separation
12px (1.5x)  -->  Mobile outer gutters, tag labels padding
16px (2.0x)  -->  Tablet outer gutters, standard card content padding
24px (3.0x)  -->  Desktop outer gutters, card vertical separation
32px (4.0x)  -->  Wide screen outer gutters, section margins
40px (5.0x)  -->  Large hero separation, modal header margins
48px (6.0x)  -->  Root banner heights, visual gap boundaries
```

---

## 5. Grid System & Container Widths

The main content area utilizes a 12-column grid layout built with Material UI (`Grid`). The layout adjusts dynamically based on the active viewport:

- **12-Column Grid:** All widget components are assigned column spans (1 to 12) across breakpoints.
- **Gutters:** Card spacing is managed via grid gaps, which adapt dynamically:
  - Mobile (`xs`): `12px`
  - Tablet (`sm`): `16px`
  - Desktop (`md`/`lg`): `24px`
- **Nesting:** Sub-grids nested inside widget cards must use matching spacing parameters to keep components aligned.
- **Container Max-Widths:** The main page content container has a maximum width of `1600px` on wide screens to prevent layouts from stretching excessively on large monitors.

---

## 6. Structural Components Behavior

### 6.1. Sidebar Navigation
- **Desktop (>= 1200px):** The sidebar is docked on the left with a width of `260px`. Toggling the collapse state reduces the width to `72px` and collapses text labels into icon tooltips, shifting adjacent page content margins.
- **Laptop (900px - 1199px):** Renders in collapsed mode (`72px`) by default to maximize content space.
- **Mobile/Tablet (< 900px):** The sidebar is hidden from the main grid. Tapping the Topbar hamburger button opens the sidebar as a temporary overlay drawer.
- **Transitions:** Width adjustments and toggles use smooth transitions: `transition: width 225ms cubic-bezier(0.4, 0, 0.2, 1)`.

### 6.2. Mobile Drawer
- **Backdrop:** Toggling the drawer slides it out from the left and overlays a backdrop (`opacity: 0.5`) that covers the rest of the screen.
- **Dismissal:** Tapping the backdrop or the close button (`[X]`), or swiping left, closes the drawer.
- **Focus Trap:** Tabbing through navigation links is trapped within the active drawer to ensure keyboard accessibility.

### 6.3. Topbar Header
- **Positioning:** The Topbar is sticky (`position: sticky; top: 0; z-index: 1100`) and has a height of `64px`.
- **Breadcrumbs:** Breadcrumbs are displayed on screens >= 900px, but hidden on smaller viewports (< 900px) to make room for page titles.
- **User dropdown:** Profile avatar dropdown menus open downward on the right side of the screen.

### 6.4. Widget Reflow
- **Layout Stacking:** Widgets reflow from multi-column rows on desktop to stacked single-column layouts on mobile.
- **Text Truncating:** Long text strings (e.g. long task names) use CSS text-truncation (`text-overflow: ellipsis; overflow: hidden; white-space: nowrap`) to prevent overflow.
- **Overflow Scroll:** Cards must not scroll horizontally. Scrollable content is restricted to vertical scrolling (`overflow-y: auto`) within bounded sections.

---

## 7. Scaling Standards

To maintain a consistent visual hierarchy across different screen sizes, interface components scale dynamically based on the active viewport:

### 7.1. Typography Scaling
Heading and body text sizes adjust dynamically to match different device viewports:

| Typographical Style | Mobile (< 600px) | Tablet (600px - 899px) | Desktop (>= 900px) | Line Height |
| :--- | :--- | :--- | :--- | :--- |
| **H1** (Page Header) | `28px` | `32px` | `40px` | `1.2` |
| **H2** (Section Header) | `22px` | `26px` | `32px` | `1.25` |
| **H3** (Card Title) | `18px` | `20px` | `24px` | `1.3` |
| **H4** (Sub Header) | `16px` | `18px` | `20px` | `1.4` |
| **Body** (Core Texts) | `14px` | `15px` | `16px` | `1.5` |
| **Caption** (Meta Tags) | `12px` | `13px` | `14px` | `1.43` |
| **Small** (Sub-Labels) | `11px` | `12px` | `12px` | `1.33` |

### 7.2. Icon Scaling (Lucide Icons)
- **Sidebar Links:** `20px` (standard sizing).
- **Topbar Actions (IconButton):** `24px`.
- **Widget Metric Highlights:** `24px` or `32px` (wrapped in circular colored badges).
- **Button Icons:** `16px` (centered or aligned next to text).

### 7.3. Button Scaling
- **Desktop/Tablet:** Buttons use fixed or content-based widths with `16px` horizontal padding.
- **Mobile:** Primary and secondary actions stretch to full width (`width: 100%`) for easier tap target acquisition.

---

## 8. Touch Targets & Interaction Rules

- **Minimum Dimensions:** All interactive targets (buttons, check boxes, tags, links) must meet a minimum size of `48px x 48px` to ensure they are easily tapable.
- **Gaps:** Interactive elements must have a minimum separation gap of `8px` to prevent accidental taps.
- **Active States:** Tap targets include active states (`:active`) that provide immediate visual feedback (e.g. shifting background color or scaling down slightly) when pressed.

---

## 9. Environmental Conditions & Performance

### 9.1. Landscape Mode Handling
On mobile devices in landscape mode:
- Card layout heights are adjusted to prevent vertical clipping.
- The Topbar can be collapsed or scrolled off-screen dynamically to maximize vertical reading area.
- Scrollable overlays are styled with vertical scroll fallback options: `overflow-y: auto`.

### 9.2. Accessibility & Color Contrast
- All background-to-text configurations must meet a minimum contrast ratio of `4.5:1` (`3:1` for large text elements) to comply with WCAG 2.1 AA targets.
- Interactive elements must have distinct `:focus-visible` styles, rendering a high-contrast focus ring around focused items.

### 9.3. Performance Optimization
- Transition rules use hardware-accelerated properties (`transform`, `opacity`) instead of modifying geometry properties (e.g., `width`, `height`, `margin`) to prevent layout updates.
- Images and heavy components use lazy loading to speed up page rendering.

---

## 10. Responsive Verification Matrix

| Target Screen Width | Target Device | Sidebar Expected | Topbar Expected | Grid Columns | Gutters | Verification Checklist |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **375px** | iPhone SE | Hidden (Drawer) | Hamburger | 1 Column | `12px` | Assert buttons are full width; verify touch targets are >= 48px. |
| **768px** | iPad Portrait | Hidden (Drawer) | Hamburger | 2 Columns | `16px` | Verify widgets wrap correctly; verify drawer toggles. |
| **1024px**| iPad Landscape | Collapsed Rail | Collapsed Rail | 3 Columns | `24px` | Confirm tooltips display correctly over icon rails. |
| **1440px**| Laptop Display | Docked (260px) | Full Title Bar | 3/4 Columns | `24px` | Verify grid alignments and spacing parameters. |
| **1920px**| Wide Monitor | Docked (260px) | Full Title Bar | 4 Columns | `32px` | Confirm maximum container width restricts content stretching. |

---

## 11. Validation Checklist
- [x] Spacing scale is based on an 8px base unit system.
- [x] Breakpoints match the Material UI layout specifications.
- [x] Interactive touch targets meet the minimum `48px x 48px` sizing requirement.
- [x] Font sizes scale dynamically across mobile, tablet, and desktop viewports.
- [x] Landscape overflow fallback vertical scrolling configurations are defined.
- [x] WCAG 2.1 AA contrast requirements are met across all states.

## Future Extension Notes
When adding complex features (such as mock interview audio/video setups in Sprint F5), developers must define responsive layouts for landscape mobile screens, optimize layout grids to prevent cropping, and verify touch targets before implementation.

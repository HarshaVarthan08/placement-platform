# Responsive Design Report: AI Placement Platform Landing Page

This report details how the landing page adapts across mobile, tablet, and desktop viewports to ensure a seamless layout experience.

---

## 1. Breakpoint Grid Sizing
Layout responsive behaviors align with the standard MUI breakpoints:
* **Mobile (xs)**: `< 600px`
* **Tablet (sm)**: `600px - 899px`
* **Medium Screen (md)**: `900px - 1199px`
* **Desktop (lg/xl)**: `>= 1200px`

---

## 2. Component Layout & Wrapping Behavior

The components adjust their column and padding properties dynamically based on the current viewport width:

### Hero & CTA Headers
* **Desktop**: Wide spacing with side margins, text alignment is centered with large fonts (e.g. `fontSize: 3.25rem` / `h1` titles).
* **Mobile**: Spacing is condensed, and text sizes are scaled down (`fontSize: 2rem`) to prevent long words from wrapping awkwardly.

### CTA Action Buttons
* **Desktop/Tablet**: Primary and secondary buttons align horizontally in a single row (`flexDirection: 'row'`).
* **Mobile**: Buttons stack vertically (`flexDirection: 'column'`) and span `100%` width to provide clear touch targets.

### Trust Indicators & Problem Cards
* **Desktop**: Arranged in a 4-column layout (`repeat(4, 1fr)`) to utilize wide viewports efficiently.
* **Tablet**: Wraps dynamically to a 2-column grid (`repeat(2, 1fr)`) in a 2x2 matrix.
* **Mobile**: Stacks into a single column (`1fr`) with standard card sizes. Equal heights are maintained natively through CSS grid properties.

### Navigation Header (Navbar)
* **Desktop**: Shows standard horizontal links with hover borders.
* **Mobile/Tablet**: Nav links are hidden, and a hamburger icon displays. When clicked, it opens a smooth slide-out Drawer overlay containing the menu links with touch-friendly spacing.

### Landing Footer
* **Desktop**: 4-column layout (`2.25fr 1fr 1fr 1fr`). Brand info and social icons sit in the first column, followed by Platform, Resources, and Legal links. The bottom bar is rendered as a single row.
* **Tablet**: Grid wraps to a 2x2 matrix (Brand + Platform in row 1, Resources + Legal in row 2).
* **Mobile**: Stacks everything in a single column. The bottom bar copyright, built-for note, and version label stack vertically with center alignment.

---

## 3. Responsive Styling Guidelines
* **Fluid Padding**: Component paddings dynamically scale down on small screens to preserve screen space (e.g. `py: { xs: 6, sm: 8, md: 10 }`).
* **Container Limits**: All sections wrap their content inside `<Container maxWidth="lg">` with horizontal padding offsets (`px: { xs: 2, sm: 3, md: 4 }`) to prevent content from touching the screen edges.
* **No Horizontal Scrolling**: Checked layout boundaries to ensure `overflow-x: hidden` is applied where appropriate, avoiding layout breaks on small device widths.

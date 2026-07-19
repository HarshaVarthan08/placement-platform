# Known Limitations: AI Placement Platform Landing Page

This document captures details about current placeholders, unbuilt routes, and dependency limitations identified at the conclusion of Frontend Sprint F2.

---

## 1. Unbuilt Pages & Router Placeholders
Several links in the header, pricing, and footer modules direct to router hash placeholders rather than finished sub-pages:
* **Resources Column**: FAQ (`#faq`), Documentation (`#docs`), and Support (`#support`) direct to hashes because their respective pages do not yet exist.
* **Legal & Company Columns**: About (`#about`) navigates to the about anchor section, but Contact (`#contact`), Privacy Policy (`#privacy`), and Terms of Service (`#terms`) link to placeholders.
* **Roadmap Link**: The Roadmap link under Resources is marked as "Coming Soon" and is disabled to reflect its pending status.

---

## 2. Icon Library Variations
* **Issue**: The project's package version of `lucide-react` (`^1.24.0`) is a custom build that does not include popular brand logos (such as GitHub, LinkedIn, Facebook, and Twitter).
* **Resolution**: To prevent build failures, we integrated brand icons from `@mui/icons-material` (which is already a dependency in the project):
  - GitHub is rendered using `@mui/icons-material/GitHub`.
  - LinkedIn is rendered using `@mui/icons-material/LinkedIn`.
  - Email continues to use `Mail` from `lucide-react`.
* **Impact**: Type definitions in `footerData.ts` use `React.ComponentType<any>` with ESLint rule overrides (`// eslint-disable-next-line @typescript-eslint/no-explicit-any`) to accommodate components from both libraries.

---

## 3. Dynamic Orbit Animation Limits
* **Behavior**: The Career Orbit nodes and rotating rings utilize CSS transition rules and simple Framer Motion properties.
* **Performance Consideration**: On low-performance mobile devices, multiple CSS shadows and radial blur filters on orbit rings may trigger minor graphics lag. If performance drops, consider reducing the count of ambient background radial glows or using static illustration assets for mobile screen sizes.

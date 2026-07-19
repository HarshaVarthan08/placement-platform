# Roadmap Update: Post Sprint F2 Landing Page Completion

Following the successful completion of the landing page UI layout (Sprint F2), this roadmap details the planned milestones for upcoming development sprints.

---

## 1. Upcoming Sprints & Milestones

### Sprint F3: Core Auth & Student Dashboard
* **Milestone 1: Authentication API Integration**
  - Implement actual login, registration, and email verification endpoints using JWT tokens.
  - Bind client input validations (Zod schema validations in Login/Register forms) to backend services.
* **Milestone 2: Student Dashboard Shell**
  - Create the core layout navigation (sidebar, top user menu) for authenticated users.
  - Setup routing guards preventing non-authenticated user traffic.
* **Milestone 3: Profile & Resume Upload View**
  - Implement CV file drop zone (PDF/Docx support).
  - Setup background jobs processing resume analysis mock scores.

### Sprint F4: AI Coach & Interview Arena
* **Milestone 4: Interactive AI Interviewer**
  - Setup streaming WebSocket channels connecting client speech/chat to the AI Coach API.
  - Build real-time mic input analyzer and response widgets.
* **Milestone 5: Company Practice Modules**
  - Create question repositories for top technology employers (Google, Microsoft, Meta, etc.).

---

## 2. Technical Enhancements & Future Optimization
1. **Dynamic Content Updates (CMS)**:
   - Move static pricing plans and about cards details from `*Data.ts` files to a Headless Content Management System (CMS) or config database, allowing marketing copy adjustments without codebase updates.
2. **SEO Optimization**:
   - Add Server-Side Rendering (SSR) support via Next.js or Vite SSG to optimize Google indexing, title metadata, and meta tags.
3. **Analytics Integration**:
   - Integrate cookie banners and analytics hooks (e.g. Google Analytics or Plausible) to monitor visitor traffic conversion rates on the landing page.

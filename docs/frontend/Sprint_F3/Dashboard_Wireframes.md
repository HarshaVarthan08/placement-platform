# Dashboard Wireframe Specifications: Authenticated Frontend

## 1. Document Metadata
- **Document Version:** 1.0.0
- **Status:** Frozen (Approved for Frontend Implementation)
- **Scope:** Frontend Sprint F3: Authenticated Dashboard UI Layout & Wireframes
- **Target Audience:** Frontend Engineers, UI/UX Developers, QA Engineers
- **Last Updated:** July 19, 2026
- **Reference Document:** [Dashboard_Architecture.md](file:///d:/placement-platform/docs/frontend/Sprint_F3/Dashboard_Architecture.md)

---

## 2. Dashboard Design Philosophy
The placement platform dashboard is designed to act as a focused "Daily Flight Deck" for student placement preparation. The UX philosophy rests on four foundational tenets:

1. **Cognitive Load Reduction:** Information is structured progressively. Complex graphs and tables are avoided in favor of clean metrics, status indicators, and clear linear steps.
2. **Visual Hierarchy:** Immediate status information (Welcome & Progress) is placed at the top, primary action items and metrics occupy the center, and secondary logging (Activity & Tasks) sits at the base.
3. **Decoupled Stateless Presentation:** All dashboard card components are "dumb" widgets. They receive structured properties (`props`) and delegate interactive transitions (e.g. routing, drawer triggers) to parent layout frameworks.
4. **Adaptive Fluidity:** Using a mobile-first design, layout panels change their spatial relationships smoothly across viewport breakpoints rather than relying on heavy layout resets.

---

## 3. Desktop Dashboard Wireframe

### Grid Layout (Width: 1280px+)
The desktop layout uses a dual-panel arrangement consisting of a docked Sidebar (260px wide) and a fluid Main Content area containing the Topbar and responsive grid.

```
+-------------------------------------------------------------------------------------------------------------------------+
|                                                      1280px+ Viewport                                                   |
+-------------------------------------------------------------------------------------------------------------------------+
| [260px Sidebar]  | [Topbar] (Height: 64px, Sticky, Fluid Width)                                                         |
|                  | Breadcrumbs: Home > Dashboard                            [Theme Toggle] [Profile Dropdown: User Avatar]  |
|                  +------------------------------------------------------------------------------------------------------+
|                  | [Main Content Scroll Container] (Padding: 24px)                                                      |
|                  |                                                                                                      |
|                  | +--------------------------------------------------------------------------------------------------+ |
|                  | | WelcomeCard (Row 1: Full-Width 12-Columns, Height: 140px)                                         | |
|                  | +--------------------------------------------------------------------------------------------------+ |
|                  |                                                                                                      |
|                  | +--------------------------+  +--------------------------+  +--------------------------+             | |
|                  | | ResumeScoreCard          |  | InterviewReadinessCard   |  | PlacementProgressCard    |             | |
|                  | | (Row 2, 4-Cols)          |  | (Row 2, 4-Cols)          |  | (Row 2, 4-Cols)          |             | |
|                  | | Height: 240px            |  | Height: 240px            |  | Height: 240px            |             | |
|                  | +--------------------------+  +--------------------------+  +--------------------------+             | |
|                  |                                                                                                      |
|                  | +-------------------------------------------------------+  +---------------------------------------+ |
|                  | | ApplicationsCard                                      |  | QuickActionsCard                      | |
|                  | | (Row 3, 7-Cols)                                       |  | (Row 3, 5-Cols)                       | |
|                  | | Height: 300px                                         |  | Height: 300px                         | |
|                  | +-------------------------------------------------------+  +---------------------------------------+ |
|                  |                                                                                                      |
|                  | +-------------------------------------------------------+  +---------------------------------------+ |
|                  | | UpcomingTasksCard                                     |  | RecentActivityCard                    | |
|                  | | (Row 4, 6-Cols)                                       |  | (Row 4, 6-Cols)                       | |
|                  | | Height: 320px                                         |  | Height: 320px                         | |
|                  | +-------------------------------------------------------+  +---------------------------------------+ |
+-------------------------------------------------------------------------------------------------------------------------+
```

### Layout Descriptions & Component Placement
- **Outer Shell:** The `DashboardLayout` manages the viewport structure. The Sidebar uses a fixed width, while the Main Content area expands to fill all remaining horizontal screen space.
- **Scroll Behavior:** The Topbar is sticky (`position: sticky; top: 0; z-index: 1100`). The Main Content container utilizes vertical scrolling (`overflow-y: auto`), preventing the header and navigation controls from moving out of view.
- **Card Spacing:** Horizontal and vertical gutters between widgets are locked to `24px` on desktop viewports.

---

## 4. Tablet Dashboard Wireframe

### Grid Layout (Width: 600px - 899px)
On tablet viewports, the Sidebar is hidden out of the viewport. A Topbar Hamburger button is revealed. The grid reflows into a 2-column format.

```
+---------------------------------------------------------------------------------------------------+
|                                      Tablet Viewport (600px - 899px)                              |
+---------------------------------------------------------------------------------------------------+
| [=] (Hamburger Menu) | Home > Dashboard                           [Theme] [Profile Dropdown Menu] |
+---------------------------------------------------------------------------------------------------+
| [Scroll Viewport Container] (Padding: 16px)                                                       |
|                                                                                                   |
| +-----------------------------------------------------------------------------------------------+ |
| | WelcomeCard (Row 1: 12-Cols, Full-Width)                                                      | |
| +-----------------------------------------------------------------------------------------------+ |
|                                                                                                   |
| +-----------------------------------------------+  +--------------------------------------------+ |
| | ResumeScoreCard                               |  | InterviewReadinessCard                     | |
| | (Row 2, Col 1 - 6-Cols)                       |  | (Row 2, Col 2 - 6-Cols)                    | |
| +-----------------------------------------------+  +--------------------------------------------+ |
|                                                                                                   |
| +-----------------------------------------------+  +--------------------------------------------+ |
| | PlacementProgressCard                         |  | QuickActionsCard                           | |
| | (Row 3, Col 1 - 6-Cols)                       |  | (Row 3, Col 2 - 6-Cols)                    | |
| +-----------------------------------------------+  +--------------------------------------------+ |
|                                                                                                   |
| +-----------------------------------------------------------------------------------------------+ |
| | ApplicationsCard (Row 4: 12-Cols, Full-Width)                                                 | |
| +-----------------------------------------------------------------------------------------------+ |
|                                                                                                   |
| +-----------------------------------------------------------------------------------------------+ |
| | UpcomingTasksCard (Row 5: 12-Cols, Full-Width)                                                | |
| +-----------------------------------------------------------------------------------------------+ |
|                                                                                                   |
| +-----------------------------------------------------------------------------------------------+ |
| | RecentActivityCard (Row 6: 12-Cols, Full-Width)                                               | |
| +-----------------------------------------------------------------------------------------------+ |
+---------------------------------------------------------------------------------------------------+
```

### Layout Descriptions
- **Sidebar State:** `sidebarCollapsed` is set to `true`, and the sidebar is visually hidden off-screen (`transform: translateX(-100%)`).
- **Trigger Actions:** Tapping the Hamburger `[=]` updates the `UIContext` state parameter `mobileDrawerOpen` to `true`, sliding out the mobile navigation drawer.
- **Card Spacing:** Horizontal and vertical card gutters are set to `16px` to conserve display space.

---

## 5. Mobile Dashboard Wireframe

### Grid Layout (Width: < 600px)
On mobile screens, all margins shrink, and all layout components are stacked vertically in a single column (12-columns grid mapping, 100% width).

```
+-----------------------------------------------------------------------+
|                       Mobile Viewport (< 600px)                       |
+-----------------------------------------------------------------------+
| [=] | Dashboard                                       [Theme] [User]  |
+-----------------------------------------------------------------------+
| [Scroll Viewport Container] (Padding: 12px)                           |
|                                                                       |
| +-------------------------------------------------------------------+ |
| | WelcomeCard (12-Cols, Stacked Content)                            | |
| +-------------------------------------------------------------------+ |
|                                                                       |
| +-------------------------------------------------------------------+ |
| | ResumeScoreCard (12-Cols)                                         | |
| +-------------------------------------------------------------------+ |
|                                                                       |
| +-------------------------------------------------------------------+ |
| | InterviewReadinessCard (12-Cols)                                  | |
| +-------------------------------------------------------------------+ |
|                                                                       |
| +-------------------------------------------------------------------+ |
| | PlacementProgressCard (12-Cols)                                   | |
| +-------------------------------------------------------------------+ |
|                                                                       |
| +-------------------------------------------------------------------+ |
| | QuickActionsCard (12-Cols)                                        | |
| +-------------------------------------------------------------------+ |
|                                                                       |
| +-------------------------------------------------------------------+ |
| | ApplicationsCard (12-Cols)                                        | |
| +-------------------------------------------------------------------+ |
|                                                                       |
| +-------------------------------------------------------------------+ |
| | UpcomingTasksCard (12-Cols)                                       | |
| +-------------------------------------------------------------------+ |
|                                                                       |
| +-------------------------------------------------------------------+ |
| | RecentActivityCard (12-Cols)                                      | |
| +-------------------------------------------------------------------+ |
+-----------------------------------------------------------------------+
```

### Layout Descriptions
- **Compact Spacing:** Paddings around the main dashboard container are reduced to `12px`, and card gaps are locked at `12px` to prevent overflow and clipping.
- **Breadcrumbs:** Hidden on mobile header to save vertical layout space. Page titles are shortened to the active view title (e.g., "Dashboard") centered or aligned next to the hamburger icon.

---

## 6. Sidebar Wireframes

The sidebar is the primary navigation component. It supports three layout variations based on screen sizes and user selection.

### 6.1. Expanded Sidebar (Width: 260px - Desktop Default)
```
+----------------------------------+
| (A) AI PLACEMENT PLATFORM  [<]   | <-- Brand Logo + Toggle Button
+----------------------------------+
|                                  |
|  (Nav Group: Preparation)        |
|  [D] Dashboard               (*) | <-- Active Item Indicator
|  [R] Resume Analyzer             |
|  [I] Interview Prep              |
|  [C] Company Prep                |
|                                  |
|  (Nav Group: Tracking)           |
|  [A] Applications                |
|  [P] Progress                    |
|                                  |
|  (Nav Group: Account)            |
|  [U] Profile                     |
|  [S] Settings                    |
|                                  |
+----------------------------------+
|  [L] Logout                      |
+----------------------------------+
| (v1.0.0) Copyright (c) 2026      | <-- Sidebar Footer
+----------------------------------+
```

### 6.2. Collapsed Sidebar (Width: 72px - Desktop Secondary)
```
+------+
| (A)  | <-- Icon-only Logo
+------+
|      |
| [D]* | <-- Active Indicator (Highlight Dot / Tooltip on Hover)
| [R]  |
| [I]  |
| [C]  |
|      |
| [A]  |
| [P]  |
|      |
| [U]  |
| [S]  |
|      |
+------+
| [L]  |
+------+
| [>]  | <-- Toggle Button (Expands Sidebar)
+------+
```

### 6.3. Mobile Drawer Sidebar (Slide-out Overlay)
```
+---------------------------+-----------------------------------------------+
| (A) PLACEMENT PLATFORM [X]|                                               |
+---------------------------+                                               |
|                           |                                               |
|  [D] Dashboard        (*) |                                               |
|  [R] Resume Analyzer      |                                               |
|  [I] Interview Prep       |                                               |
|  [C] Company Prep         |              Backdrop Overlay                 |
|  [A] Applications         |              (Opacity: 50%)                   |
|  [P] Progress             |              (Clicking closes drawer)         |
|  [U] Profile              |                                               |
|  [S] Settings             |                                               |
|                           |                                               |
|  [L] Logout               |                                               |
+---------------------------+                                               |
| (v1.0) Copyright (c) 2026 |                                               |
+---------------------------+-----------------------------------------------+
```

---

## 7. Topbar Wireframes

### 7.1. Desktop Topbar (Sticky Header)
```
+-------------------------------------------------------------------------------------------------------+
|  Home > Dashboard                                                     [Theme Toggle]  [Avatar - User] |
+-------------------------------------------------------------------------------------------------------+
                                                                                        | Profile       |
                                                                                        | Settings      |
                                                                                        |---------------+
                                                                                        | Logout        |
                                                                                        +---------------+
```
*Note: Clicking the Avatar opens the absolute-positioned User Menu Dropdown.*

### 7.2. Mobile Topbar (Compact Header)
```
+-------------------------------------------------------------------------------------------------------+
| [=] Dashboard                                                         [Theme Toggle]  [Avatar - User] |
+-------------------------------------------------------------------------------------------------------+
```

---

## 8. Dashboard Home Wireframe (Main Grid Setup)
This diagram illustrates the alignment margins, spacing units, and layout columns defining the `DashboardHome` screen.

```
|<------------------------------------------ Fluid Content Width ------------------------------------------>|
|                                                                                                           |
| 24px Padding (Left)                                                                  24px Padding (Right) |
|-->+---------------------------------------------------------------------------------------------------+<--|
|   | WelcomeCard (Row 1)                                                                               |   |
|   | Span: 12 Columns                                                                                  |   |
|   +---------------------------------------------------------------------------------------------------+   |
|   | 24px Vertical Spacing                                                                             |   |
|   +------------------------+ 24px Gap +------------------------+ 24px Gap +------------------------+   |
|   | ResumeScoreCard        |--------->| InterviewReadinessCard |--------->| PlacementProgressCard  |   |
|   | Span: 4 Columns        |          | Span: 4 Columns        |          | Span: 4 Columns        |   |
|   +------------------------+          +------------------------+          +------------------------+   |
|   | 24px Vertical Spacing                                                                             |   |
|   +--------------------------------------------------+ 24px Gap +-------------------------------------+   |
|   | ApplicationsCard                                 |--------->| QuickActionsCard                    |   |
|   | Span: 7 Columns                                  |          | Span: 5 Columns                     |   |
|   +--------------------------------------------------+          +-------------------------------------+   |
|   | 24px Vertical Spacing                                                                             |   |
|   +---------------------------------------+ 24px Gap +------------------------------------------------+   |
|   | UpcomingTasksCard                     |--------->| RecentActivityCard                             |   |
|   | Span: 6 Columns                       |          | Span: 6 Columns                                |   |
|   +---------------------------------------+          +------------------------------------------------+   |
|   |                                                                                                   |   |
```

---

## 9. Widget Specifications

### 9.1. WelcomeCard
```
+-------------------------------------------------------------------------------------------------------+
| (Avatar)  Welcome back, Alex!                                         Overall Readiness: [ 68% ]      |
|           Targeting: Full Stack Software Engineer                     [===============>--------]      |
+-------------------------------------------------------------------------------------------------------+
```
- **Purpose:** Provide an encouraging user greeting, display their configured primary target role, and display their overall placement readiness rating at a glance.
- **Contents:**
  - Standard user profile image placeholder (Avatar - Circle).
  - Heading: "Welcome back, {User First Name}!" (Inter, 24px, Bold).
  - Subtext: "Targeting: {Target Role}" (Inter, 16px, Regular, Grey).
  - Linear Progress Indicator: Metric badge (e.g. `68%`) and a horizontal progress bar (MUI Line Progress).
- **User Actions:**
  - Clicking the target role badge or text redirects the user to `/profile` (via `useNavigate`).
  - Progress bar hover states reveal a tooltip reading: "Based on Resume (85%), Interview (74%), and Profile (90%)".
- **Responsive Behavior:**
  - Desktop/Tablet: Horizontal layout (flex row).
  - Mobile: Layout wraps into vertical stack. The progress bar shifts below the text to preserve horizontal clearance.
- **Future API Mapping:**
  - Mock Payload Target: `mockDashboardData.welcome`
  - REST Endpoint: GET `/api/v1/dashboard/welcome`
  - JSON Data Contract:
    ```json
    {
      "firstName": "Alex",
      "targetRole": "Full Stack Software Engineer",
      "overallReadinessScore": 68
    }
    ```

### 9.2. ResumeScoreCard
```
+-----------------------------------------------------------------------+
| RESUME SCORE                                                          |
|                                                                       |
|    /---\      85 / 100                                                |
|   |  *  |     Good Status                                             |
|    \---/      Last Analyzed: 2026-07-15                               |
|                                                                       |
|   [ ] Fix 3 pending checklist items                                   |
|                                                                       |
|                                                     [Analyze Resume]  |
+-----------------------------------------------------------------------+
```
- **Purpose:** Summarize the student's latest ATS Resume Score and pending action items.
- **Contents:**
  - Heading: "RESUME SCORE" (Inter, 14px, Semibold, Grey).
  - Circular Progress Indicator: ATS score count (e.g., `85`) embedded in a radial progress ring.
  - Text badge reflecting status rating (e.g. "Good Match" or "Needs Improvement").
  - Date description label: "Last Analyzed: {YYYY-MM-DD}".
  - Small actionable warning checklist indicator: "Fix 3 pending checklist items".
  - Action Button: "Analyze Resume" (MUI Button, Contained Primary, right-aligned).
- **User Actions:**
  - Tapping "Analyze Resume" routes the user to `/resume-analyzer`.
  - Tapping the checklist warning routes directly to `/resume-analyzer#checklist`.
- **Responsive Behavior:**
  - Desktop: Spans 4 Columns.
  - Tablet: Spans 6 Columns.
  - Mobile: Spans 12 Columns.
- **Future API Mapping:**
  - Mock Payload Target: `mockDashboardData.resumeScore`
  - REST Endpoint: GET `/api/v1/resume/latest-score`
  - JSON Data Contract:
    ```json
    {
      "score": 85,
      "status": "GOOD",
      "pendingChecklistCount": 3,
      "lastAnalyzed": "2026-07-15T12:00:00Z"
    }
    ```

### 9.3. ApplicationsCard
```
+-----------------------------------------------------------------------+
| JOB APPLICATIONS                                                      |
| Active: 8   |   Interviewing: 2   |   Offers: 1                       |
|-----------------------------------------------------------------------|
|  (G) Google - Frontend Engineer                                       |
|      Status: Technical Interview (Scheduled: 2026-07-22)              |
|                                                                       |
|  (M) Meta - Software Engineer                                         |
|      Status: Applied (Date: 2026-07-18)                               |
|                                                                       |
|                                                   [View Applications] |
+-----------------------------------------------------------------------+
```
- **Purpose:** Track active job application counts and highlight upcoming application milestones.
- **Contents:**
  - Metric Strip: Summary items detailing active counts (Active, Interviewing, Offers).
  - Application Item List: Vertical list presenting the 2 most recent active applications.
  - Organization brand placeholder circle logo, role title, active status badge, and date.
  - Action Link: "View Applications" (MUI Button, Text, bottom-right).
- **User Actions:**
  - Clicking "View Applications" routes the user to `/applications`.
  - Clicking on a single company row routes the user to the corresponding application detail view (future).
- **Responsive Behavior:**
  - Desktop: Spans 7 Columns.
  - Tablet: Spans 12 Columns.
  - Mobile: Spans 12 Columns. Text labels shrink, status badges wrap below company titles.
- **Future API Mapping:**
  - Mock Payload Target: `mockDashboardData.applications`
  - REST Endpoint: GET `/api/v1/applications/summary`
  - JSON Data Contract:
    ```json
    {
      "metrics": {
        "activeCount": 8,
        "interviewingCount": 2,
        "offersCount": 1
      },
      "latestApplications": [
        {
          "companyName": "Google",
          "roleTitle": "Frontend Engineer",
          "status": "TECHNICAL_INTERVIEW",
          "eventDate": "2026-07-22T09:00:00Z"
        },
        {
          "companyName": "Meta",
          "roleTitle": "Software Engineer",
          "status": "APPLIED",
          "eventDate": "2026-07-18T14:30:00Z"
        }
      ]
    }
    ```

### 9.4. InterviewReadinessCard
```
+-----------------------------------------------------------------------+
| INTERVIEW READINESS                                                   |
|                                                                       |
|    Mocks Completed: 12 / 20                  Readiness Rating: 74%    |
|                                              [===========>----]       |
|                                                                       |
|    Next Recommended Session:                                          |
|    -> System Design Mock Interview (Focused on Microservices)         |
|                                                                       |
|                                                       [Practice Now]  |
+-----------------------------------------------------------------------+
```
- **Purpose:** Provide visibility into student performance during mock interview preparation.
- **Contents:**
  - Quantitative status: Mocks completed vs. scheduled (e.g. "12/20 completed").
  - Readiness metric score (e.g., `74%`) displayed via a linear meter.
  - Prompt Box: "Next Recommended Session" display detailing the topic, complexity, and focus areas.
  - Action Button: "Practice Now" (MUI Button, Contained Primary, right-aligned).
- **User Actions:**
  - Tapping "Practice Now" routes the user to `/interview-prep`.
- **Responsive Behavior:**
  - Desktop: Spans 4 Columns.
  - Tablet: Spans 6 Columns.
  - Mobile: Spans 12 Columns. Layout wraps vertically; margins compress.
- **Future API Mapping:**
  - Mock Payload Target: `mockDashboardData.interviewReadiness`
  - REST Endpoint: GET `/api/v1/interview/readiness-summary`
  - JSON Data Contract:
    ```json
    {
      "completedMocks": 12,
      "totalMocksSuggested": 20,
      "readinessPercentage": 74,
      "nextRecommendedSession": "System Design Mock Interview (Focused on Microservices)"
    }
    ```

### 9.5. PlacementProgressCard
```
+-----------------------------------------------------------------------+
| PREPARATION BREAKDOWN                                                 |
|                                                                       |
|    Resume Prep:        85%    [==================>-----]              |
|    Interview Prep:     74%    [===============>--------]              |
|    Profile Completion: 90%    [====================>---]              |
|                                                                       |
|                                                  [Detailed Progress]  |
+-----------------------------------------------------------------------+
```
- **Purpose:** Present a progress breakdown across the three preparation segments: Resume, Interview, Profile.
- **Contents:**
  - Title: "PREPARATION BREAKDOWN".
  - Linear Progress meters matching key readiness aspects:
    - Resume Prep (e.g. 85%)
    - Interview Prep (e.g. 74%)
    - Profile Completion (e.g. 90%)
  - Action Link: "Detailed Progress" (MUI Button, Text, bottom-right).
- **User Actions:**
  - Clicking "Detailed Progress" routes the user to `/progress`.
- **Responsive Behavior:**
  - Desktop: Spans 4 Columns.
  - Tablet: Spans 6 Columns.
  - Mobile: Spans 12 Columns.
- **Future API Mapping:**
  - Mock Payload Target: `mockDashboardData.progressBreakdown`
  - REST Endpoint: GET `/api/v1/dashboard/progress-breakdown`
  - JSON Data Contract:
    ```json
    {
      "resumeProgress": 85,
      "interviewProgress": 74,
      "profileProgress": 90
    }
    ```

### 9.6. UpcomingTasksCard
```
+-----------------------------------------------------------------------+
| UPCOMING TASKS (3)                                                    |
|                                                                       |
|   [ ] Complete Resume Revision                   Due: Tomorrow        |
|   [ ] Practice System Design Patterns            Due: 2026-07-21      |
|   [ ] Submit applications to 3 Companies         Due: 2026-07-23      |
|                                                                       |
|                                                       [Manage Tasks]  |
+-----------------------------------------------------------------------+
```
- **Purpose:** Display a task tracker containing immediate preparation items.
- **Contents:**
  - Title: "UPCOMING TASKS" showing count indicator.
  - Task Rows: Vertical list displaying up to 3 upcoming tasks with active checkboxes and date labels.
  - Action Link: "Manage Tasks" (MUI Button, Text, bottom-right).
- **User Actions:**
  - Checking a task box triggers local React state update, crossing out the item, and displays a temporary toast message.
  - Clicking "Manage Tasks" routes the user to `/settings` or `/dashboard` tasks modal.
- **Responsive Behavior:**
  - Desktop: Spans 6 Columns.
  - Tablet: Spans 12 Columns.
  - Mobile: Spans 12 Columns. Due dates move below titles on small viewports.
- **Future API Mapping:**
  - Mock Payload Target: `mockDashboardData.tasks`
  - REST Endpoint: GET `/api/v1/tasks/upcoming`
  - JSON Data Contract:
    ```json
    [
      { "id": 1001, "taskDescription": "Complete Resume Revision", "dueDate": "2026-07-20", "completed": false },
      { "id": 1002, "taskDescription": "Practice System Design Patterns", "dueDate": "2026-07-21", "completed": false },
      { "id": 1003, "taskDescription": "Submit applications to 3 Companies", "dueDate": "2026-07-23", "completed": false }
    ]
    ```

### 9.7. RecentActivityCard
```
+-----------------------------------------------------------------------+
| RECENT ACTIVITY                                                       |
|                                                                       |
|   (*) Uploaded resume_v2.pdf                          2 hours ago     |
|   (V) Completed Mock Interview Prep #3                1 day ago       |
|   (A) Applied to Stripe (Frontend Engineer)           2 days ago      |
|                                                                       |
|                                                                       |
+-----------------------------------------------------------------------+
```
- **Purpose:** Log a chronological feed of recent events and system actions for verification.
- **Contents:**
  - Title: "RECENT ACTIVITY".
  - Timeline Stream: Vertically stacked log lines detailing recent events.
  - Event metadata: Symbol status indicator (Upload, Test, Applied), event descriptions, and timestamp labels (relative format).
- **User Actions:**
  - Hovering over a row highlights the activity card segment with a subtle background shift.
- **Responsive Behavior:**
  - Desktop: Spans 6 Columns.
  - Tablet: Spans 12 Columns.
  - Mobile: Spans 12 Columns. Timeline line lines wrap to prevent overflow.
- **Future API Mapping:**
  - Mock Payload Target: `mockDashboardData.recentActivity`
  - REST Endpoint: GET `/api/v1/activity/recent`
  - JSON Data Contract:
    ```json
    [
      { "id": 501, "description": "Uploaded resume_v2.pdf", "type": "UPLOAD", "timestamp": "2026-07-19T14:00:00Z" },
      { "id": 502, "description": "Completed Mock Interview Prep #3", "type": "MOCK_INTERVIEW", "timestamp": "2026-07-18T18:30:00Z" },
      { "id": 503, "description": "Applied to Stripe (Frontend Engineer)", "type": "APPLICATION", "timestamp": "2026-07-17T11:15:00Z" }
    ]
    ```

### 9.8. QuickActionsCard
```
+-----------------------------------------------------------------------+
| QUICK ACTIONS                                                         |
|                                                                       |
|   [ Upload New Resume ]          --> Routes to Resume Analyzer        |
|                                                                       |
|   [ Start Mock Interview ]       --> Routes to Interview Prep         |
|                                                                       |
|   [ Explore Companies ]          --> Routes to Company Prep           |
|                                                                       |
+-----------------------------------------------------------------------+
```
- **Purpose:** Serve as a launcher hub, directing users to core workflows.
- **Contents:**
  - Large button blocks styled with left-aligned Lucide icons and right-aligned navigation arrows.
    - Button 1: "Upload New Resume" (Lucide: `FileUp`).
    - Button 2: "Start Mock Interview" (Lucide: `Video`).
    - Button 3: "Explore Companies" (Lucide: `Building`).
- **User Actions:**
  - Tapping Button 1 routes to `/resume-analyzer`.
  - Tapping Button 2 routes to `/interview-prep`.
  - Tapping Button 3 routes to `/company-prep`.
- **Responsive Behavior:**
  - Desktop: Spans 5 Columns.
  - Tablet: Spans 6 Columns.
  - Mobile: Spans 12 Columns. Buttons expand to full width.
- **Future API Mapping:**
  - Static navigation wrapper. Requires no dynamic data mapping. Routes correspond directly to layout paths configured inside `routes.ts`.

---

## 10. Dashboard Grid System

The authenticated layout utilizes a 12-column grid layout built with Material UI (`Grid`). The layout adjusts dynamically based on the active viewport.

### 10.1. Breakpoints & Columns
| Breakpoint Key | Min Viewport Width | Sidebar Behavior | Content Margins | Card Columns (Out of 12) |
| :--- | :--- | :--- | :--- | :--- |
| **xs** (Mobile) | `< 600px` | Hidden (Drawer) | `12px` | 12 (Stacked) |
| **sm** (Tablet) | `600px - 899px` | Hidden (Drawer) | `16px` | 6 or 12 |
| **md** (Laptop) | `900px - 1199px` | Docked (Expanded) | `24px` | 4, 6, or 12 |
| **lg** (Desktop)| `1200px - 1535px`| Docked (Expanded) | `24px` | 4, 5, 6, 7, or 12 |
| **xl** (Wide)   | `>= 1536px` | Docked (Expanded) | `32px` | 3, 4, 6, or 12 |

### 10.2. Gutters & Card Spacing
- **Outer Spacing:** Padding inside the outer viewport frame:
  - Mobile (`xs`): `12px`
  - Tablet (`sm`): `16px`
  - Desktop (`md`/`lg`): `24px`
  - Wide (`xl`): `32px`
- **Internal Spacing:** Space between cards (gutters):
  - Mobile: `12px` grid column/row spacing.
  - Tablet: `16px` grid column/row spacing.
  - Desktop: `24px` grid column/row spacing.
- **Card Padding:** Spacing inside card components is configured to `20px` uniformly to ensure clear text readable margins.

---

## 11. Empty State Designs

When a widget card does not contain dynamic data (e.g. no application tracking records exist), it displays an empty state template instead of rendering blank space.

### Empty State Visual Layout (Inside Widget Card)
```
+-----------------------------------------------------------------------+
| WIDGET TITLE                                                          |
|                                                                       |
|                              ( Icon )                                 |
|                         No Data Available                             |
|             You have not added any records to this list yet.           |
|                                                                       |
|                              [ Add New ]                              |
|                                                                       |
+-----------------------------------------------------------------------+
```

### Empty State Specifications
- **Icon:** A neutral-toned Lucide outline icon representing the card type (e.g., `FileQuestion` for empty resume checklist, `Briefcase` for empty applications).
- **Heading:** "No {Data Type} Available" (Inter, 16px, Medium, Neutral Dark).
- **Description:** A short, friendly explanation instructing the user on how to resolve the state (Inter, 14px, Regular, Neutral Grey).
- **CTA Button:** Small outline button (e.g., "Add New" or "Get Started") that redirects the user to the appropriate input screen.

---

## 12. Loading State Designs

To prevent layout shifts during page transitions and mock data retrieval, widget cards use skeleton loaders.

### Loading Skeleton Visual Layout (Inside Widget Card)
```
+-----------------------------------------------------------------------+
| [====== Skeleton Header ======]                                       |
|                                                                       |
|    (###)  [======= Skeleton Title =======]                            |
|           [=========== Skeleton Subtitle ===========]                 |
|                                                                       |
|    [================== Skeleton Progress Line ==================]      |
|                                                                       |
|                                                   [ Skeleton Button ] |
+-----------------------------------------------------------------------+
```

### Loading State Specifications
- **MUI Skeleton:** Utilize Material UI's `<Skeleton variant="rectangular" />` component.
- **Animation:** Pulse animation enabled (`animation="pulse"`).
- **Matching Geometry:** The skeleton components match the height, margins, and border radius (`12px`) of the final card elements. This keeps the layout stable while data loads.
- **Background Color:** Light grey (`#f0f2f5`) in light mode, and dark grey (`#1e1e24`) in dark mode.

---

## 13. Accessibility & UX Guidelines

To comply with the WCAG 2.1 AA targets defined in the architecture goals, frontend developers must adhere to the following rules during implementation:

1. **Semantic HTML:**
   - The shell must use standard landmark elements: `<aside>` for the Sidebar, `<header>` for the Topbar, and `<main>` for the dashboard viewport.
   - Cards must wrap their headers in hierarchy headings (e.g. `<h3>` or `<h4>`).
2. **Keyboard Navigation:**
   - All interactive items (buttons, links, active icons, checkboxes) must be focusable using keyboard navigation (`tabIndex={0}`).
   - An active focus ring outline (`2px solid primary.main` with a `4px` outline offset) must appear during keyboard tab actions.
   - Users must be able to activate widgets and buttons using the `Spacebar` or `Enter` keys.
3. **Screen Readers (ARIA):**
   - Provide explicit descriptive titles: `<button aria-label="Toggle Sidebar Left" ...>` or `<svg aria-label="ATS Score circular progress gauge" role="img" ...>`.
   - Update state changes dynamically using screen-reader live region flags (`aria-live="polite"`).
4. **Motion Safeguards:**
   - All transition animations (e.g. sidebar sliding, menu expansions, list updates) must respect user system preferences:
     ```css
     @media (prefers-reduced-motion: reduce) {
       * {
         animation-duration: 0s !important;
         transition-duration: 0s !important;
       }
     }
     ```

---

## 14. Responsive Behavior Matrix

This table summarizes how key shell and grid properties adapt across breakpoints:

| Viewport | Screen Width | Sidebar State | Topbar Layout | Grid Columns | Gutter | Widget Layouts |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **Mobile** | `< 600px` | Hidden (Drawer) | Hamburger + Title + User | 1 Column | 12px | Vertical stack; text wraps. |
| **Tablet** | `600px - 899px` | Hidden (Drawer) | Hamburger + Breadcrumb + User| 2 Columns | 16px | Two cards per row; progress vertical. |
| **Laptop** | `900px - 1199px` | Collapsible Rail | Collapsed Rail + Breadcrumb | 3 Columns | 24px | Dynamic grid reflow; actions row stacks. |
| **Desktop**| `1200px - 1535px`| Fixed Docked (260px) | Breadcrumb + Theme + Profile | 3/4 Columns | 24px | Full details; inline actions; charts display.|
| **Wide** | `>= 1536px` | Fixed Docked (260px) | Breadcrumb + Theme + Profile | 4 Columns | 32px | Side-by-side components; large card paddings. |

---

## 15. Future UI Extension Strategy

To ensure feature additions (e.g., Sprint F4 Resume Analyzer details, Sprint F5 Mock Interview records) do not break the dashboard layout, follow this extension protocol:

1. **Card Size Limits:** New cards must be designed to fit the 12-column grid. Column sizes must be specified for all breakpoints (`xs`, `sm`, `md`, `lg`).
2. **Prop-Driven Design:** Do not hardcode internal state logic inside card widgets. All data must be passed in via props, and all action buttons must use callback handlers (e.g., `onActionClick: () => void`).
3. **API Contracts First:** Before implementing a new card, developers must define a TypeScript interface for its data schema inside `types/index.ts`. This interface must match the structure of the mock data and future backend endpoints.
4. **Mock Fallback:** When adding a new dashboard card, developers must first add mock data to `mockDashboardData.ts` to support offline testing and review before connecting the card to live APIs.

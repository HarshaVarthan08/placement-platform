import type {
  GreetingData,
  StatCardData,
  QuickActionItem,
  PlacementJourneyStage,
  ResumeInsightsData,
  LearningProgressItem,
  ActivityItem,
  UpcomingEventItem,
} from '../../types/dashboard';
import {
  statisticsMockData,
  activitiesMockData,
  eventsMockData,
  learningMockData,
  placementMockData,
  resumeMockData,
  quickActionsMockData,
  getGreetingMockData,
} from '../../data/dashboard';

/**
 * Dashboard Service
 *
 * This layer is the sole source of truth for dashboard-related transactions.
 * Currently, during SPRINT F3 PHASE B, it is powered by localized mock data structures.
 *
 * FUTURE IMPLEMENTATION (PHASE D+):
 * These synchronous getters can be replaced with asynchronous REST API integrations
 * (e.g. using axios calls to backend routes like GET /api/v1/dashboard/metrics).
 * The public methods contract and returned types must remain stable to prevent downstream changes
 * in the React hook and page components.
 */
export const dashboardService = {
  /**
   * Retrieves user greeting details, profile readiness gauges, and motivational quotes.
   */
  getDashboardGreeting(userName: string): GreetingData {
    // Phase B: Returns structured greeting statistics populated client-side.
    // Phase D+: Fetch from GET /api/v1/dashboard/welcome or /api/v1/profile.
    return getGreetingMockData(userName);
  },

  /**
   * Retrieves summary statistics cards (Resume Score, Application Counts, Mock counts).
   */
  getDashboardStatistics(): StatCardData[] {
    // Phase B: Return local statistics mock data.
    // Phase D+: Fetch from GET /api/v1/dashboard/statistics.
    return statisticsMockData;
  },

  /**
   * Retrieves lists of quick actions available for the student dashboard.
   */
  getDashboardQuickActions(): QuickActionItem[] {
    // Phase B: Return quick actions navigation settings.
    // Phase D+: Fetch dynamically based on features toggles or role properties.
    return quickActionsMockData;
  },

  /**
   * Retrieves overall milestone journey stages.
   */
  getDashboardPlacementJourney(): PlacementJourneyStage[] {
    // Phase B: Return mock stages of the student's preparation.
    // Phase D+: Fetch from GET /api/v1/dashboard/journey.
    return placementMockData;
  },

  /**
   * Retrieves detailed resume analysis, ATS highlights, strengths, and weaknesses.
   */
  getDashboardResumeInsights(): ResumeInsightsData {
    // Phase B: Return detailed ATS resume mock insights.
    // Phase D+: Fetch from GET /api/v1/resume/latest-score.
    return resumeMockData;
  },

  /**
   * Retrieves mastery levels in DSA, programming, quantitative, and communication skills.
   */
  getDashboardLearningProgress(): LearningProgressItem[] {
    // Phase B: Return mock progress indicators.
    // Phase D+: Fetch from GET /api/v1/dashboard/learning-progress.
    return learningMockData;
  },

  /**
   * Retrieves recent user activity timeline actions.
   */
  getDashboardActivities(): ActivityItem[] {
    // Phase B: Return recent chronological logs.
    // Phase D+: Fetch from GET /api/v1/activity/recent.
    return activitiesMockData;
  },

  /**
   * Retrieves upcoming company drives, deadlines, mock sessions, and contests.
   */
  getDashboardEvents(): UpcomingEventItem[] {
    // Phase B: Return scheduled upcoming calendars.
    // Phase D+: Fetch from GET /api/v1/events/upcoming.
    return eventsMockData;
  },
};
export default dashboardService;

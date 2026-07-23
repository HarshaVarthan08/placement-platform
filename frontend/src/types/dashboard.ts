export type ActivityType = 'SUCCESS' | 'WARNING' | 'INFO' | 'EVENT';
export type PlacementStageStatus = 'NOT_STARTED' | 'IN_PROGRESS' | 'COMPLETED';
export type LearningLevel = 'BEGINNER' | 'INTERMEDIATE' | 'ADVANCED';
export type QuickActionType =
  | 'RESUME_UPLOAD'
  | 'RESUME_ANALYZE'
  | 'MOCK_INTERVIEW'
  | 'TRACK_APPLICATIONS'
  | 'CONTINUE_LEARNING'
  | 'EDIT_PROFILE';

export interface GreetingData {
  greeting: string;
  currentDate: string;
  userName: string;
  motivationalMessage: string;
  focusArea: string;
  currentGoal: string;
  profileCompletion: number;
  placementReadiness: number;
  currentStreak: number;
}

export interface StatCardData {
  id: string;
  title: string;
  value: string | number;
  subtitle: string;
  trend: 'up' | 'down' | 'neutral';
  trendValue: string;
  iconName: string;
}

export interface QuickActionItem {
  type: QuickActionType;
  label: string;
  description: string;
  iconName: string;
  path: string;
  enabled: boolean;
  comingSoon: boolean;
}

export interface PlacementJourneyStage {
  id: string;
  name: string;
  status: PlacementStageStatus;
  progress: number;
  current: boolean;
}

export interface ResumeInsightsData {
  overallScore: number;
  atsScore: number;
  grammarScore: number;
  formattingScore: number;
  keywordScore: number;
  projectsScore: number;
  skillsScore: number;
  educationScore: number;
  strengths: string[];
  weaknesses: string[];
  missingKeywords: string[];
  suggestions: string[];
}

export interface LearningProgressItem {
  id: string;
  category: string;
  progress: number;
  level: LearningLevel;
}

export interface ActivityItem {
  id: string;
  description: string;
  type: ActivityType;
  timestamp: string;
  iconName: string;
}

export interface UpcomingEventItem {
  id: string;
  title: string;
  type: string;
  date: string;
  time: string;
  daysRemaining: number;
  priority: 'high' | 'medium' | 'low';
  status: string;
}

export interface DashboardPayload {
  greeting: GreetingData;
  statistics: StatCardData[];
  quickActions: QuickActionItem[];
  placementJourney: PlacementJourneyStage[];
  resumeInsights: ResumeInsightsData;
  learningProgress: LearningProgressItem[];
  activities: ActivityItem[];
  upcomingEvents: UpcomingEventItem[];
}

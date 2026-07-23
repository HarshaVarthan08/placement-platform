import type { StatCardData } from '../../types/dashboard';

export const statisticsMockData: StatCardData[] = [
  {
    id: 'resume-score',
    title: 'Resume Score',
    value: 82,
    subtitle: 'ATS optimized status',
    trend: 'up',
    trendValue: '+5% since last scan',
    iconName: 'FileText',
  },
  {
    id: 'applications',
    title: 'Applications',
    value: 14,
    subtitle: 'Submitted jobs tracking',
    trend: 'up',
    trendValue: '+3 this week',
    iconName: 'ClipboardList',
  },
  {
    id: 'mock-interviews',
    title: 'Mock Interviews',
    value: 8,
    subtitle: 'Practice drills completed',
    trend: 'neutral',
    trendValue: 'Same as last week',
    iconName: 'Video',
  },
  {
    id: 'placement-progress',
    title: 'Placement Readiness',
    value: '85%',
    subtitle: 'Overall prep completion',
    trend: 'up',
    trendValue: '+12% in 30 days',
    iconName: 'TrendingUp',
  },
];

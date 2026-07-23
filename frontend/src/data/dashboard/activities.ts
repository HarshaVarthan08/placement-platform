import type { ActivityItem } from '../../types/dashboard';

export const activitiesMockData: ActivityItem[] = [
  {
    id: 'act-1',
    description: 'Resume analyzed successfully: Score increased to 82',
    type: 'SUCCESS',
    timestamp: 'Today, 10:30 AM',
    iconName: 'CheckCircle',
  },
  {
    id: 'act-2',
    description: 'Applied to Software Engineer role at Infosys',
    type: 'INFO',
    timestamp: 'Yesterday, 4:15 PM',
    iconName: 'Send',
  },
  {
    id: 'act-3',
    description: 'Completed Mock Interview 4: System Design Core',
    type: 'SUCCESS',
    timestamp: 'Yesterday, 11:00 AM',
    iconName: 'Video',
  },
  {
    id: 'act-4',
    description: 'Action Required: Update profile with graduation GPA',
    type: 'WARNING',
    timestamp: '2 Days Ago',
    iconName: 'AlertTriangle',
  },
  {
    id: 'act-5',
    description: 'Completed Practice Aptitude Test: Logical Reasoning',
    type: 'EVENT',
    timestamp: '3 Days Ago',
    iconName: 'Award',
  },
];

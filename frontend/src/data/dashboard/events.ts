import type { UpcomingEventItem } from '../../types/dashboard';

export const eventsMockData: UpcomingEventItem[] = [
  {
    id: 'evt-1',
    title: 'Microsoft Off-Campus Coding Drive',
    type: 'Company Drive',
    date: 'Jul 28, 2026',
    time: '09:00 AM - 12:00 PM',
    daysRemaining: 5,
    priority: 'high',
    status: 'Registered',
  },
  {
    id: 'evt-2',
    title: 'Behavioral Mock Interview with Mentor',
    type: 'Mock Interview',
    date: 'Jul 25, 2026',
    time: '03:00 PM - 03:45 PM',
    daysRemaining: 2,
    priority: 'medium',
    status: 'Scheduled',
  },
  {
    id: 'evt-3',
    title: 'Weekly DSA Sprint Contest',
    type: 'Coding Contest',
    date: 'Jul 26, 2026',
    time: '06:00 PM - 08:00 PM',
    daysRemaining: 3,
    priority: 'low',
    status: 'Open',
  },
  {
    id: 'evt-4',
    title: 'Resume Review: Final Feedback Submission',
    type: 'Resume Review',
    date: 'Jul 24, 2026',
    time: '11:59 PM (Deadline)',
    daysRemaining: 1,
    priority: 'high',
    status: 'Pending Submission',
  },
];

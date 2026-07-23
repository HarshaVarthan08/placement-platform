import type { QuickActionItem } from '../../types/dashboard';
import { ROUTES } from '../../constants';

export const quickActionsMockData: QuickActionItem[] = [
  {
    type: 'RESUME_UPLOAD',
    label: 'Upload Resume',
    description: 'Upload your latest CV in PDF format to start analysis.',
    iconName: 'UploadCloud',
    path: ROUTES.PRIVATE.RESUME,
    enabled: true,
    comingSoon: false,
  },
  {
    type: 'RESUME_ANALYZE',
    label: 'Analyze Resume',
    description: 'Scan your resume against target JDs for ATS scoring.',
    iconName: 'FileSearch',
    path: ROUTES.PRIVATE.RESUME,
    enabled: true,
    comingSoon: false,
  },
  {
    type: 'MOCK_INTERVIEW',
    label: 'Start Mock Interview',
    description: 'Practice simulated AI technical and behavioral drills.',
    iconName: 'Video',
    path: ROUTES.PRIVATE.INTERVIEW,
    enabled: true,
    comingSoon: false,
  },
  {
    type: 'TRACK_APPLICATIONS',
    label: 'Track Applications',
    description: 'Manage and update your active company hiring pipelines.',
    iconName: 'ClipboardList',
    path: ROUTES.PRIVATE.APPLICATIONS,
    enabled: true,
    comingSoon: false,
  },
  {
    type: 'CONTINUE_LEARNING',
    label: 'Continue Learning',
    description: 'Solve DSA challenges and quantitative quizzes.',
    iconName: 'BookOpen',
    path: ROUTES.PRIVATE.PROGRESS,
    enabled: true,
    comingSoon: false,
  },
  {
    type: 'EDIT_PROFILE',
    label: 'Edit Profile Info',
    description: 'Update academic records and job preferences.',
    iconName: 'UserCog',
    path: ROUTES.PRIVATE.PROFILE,
    enabled: true,
    comingSoon: false,
  },
];

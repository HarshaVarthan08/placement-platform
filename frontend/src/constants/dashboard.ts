export const DASHBOARD_CONSTANTS = {
  SECTIONS: {
    WELCOME: {
      TITLE: 'Welcome Back',
      DESCRIPTION: 'Track your readiness and prepare for placements.',
    },
    STATISTICS: {
      TITLE: 'Key Performance Indicators',
      DESCRIPTION: 'Overview of your preparation progress.',
    },
    QUICK_ACTIONS: {
      TITLE: 'Quick Actions',
      DESCRIPTION: 'Fast access to placement preparation tools.',
    },
    PLACEMENT_PROGRESS: {
      TITLE: 'Placement Readiness Journey',
      DESCRIPTION: 'Track your path from resume creation to final offer.',
    },
    RESUME_INSIGHTS: {
      TITLE: 'Resume & ATS Insights',
      DESCRIPTION: 'Optimize your resume using AI feedback.',
    },
    LEARNING_PROGRESS: {
      TITLE: 'Learning & Mastery Progress',
      DESCRIPTION: 'Monitor your subject-level performance.',
    },
    RECENT_ACTIVITY: {
      TITLE: 'Recent Activity Feed',
      DESCRIPTION: 'Your latest placement prep operations.',
      ACTION_LABEL: 'View All',
    },
    UPCOMING_EVENTS: {
      TITLE: 'Upcoming Events & Deadlines',
      DESCRIPTION: 'Stay updated with upcoming sessions and coding drives.',
      ACTION_LABEL: 'View Calendar',
    },
  },

  COLORS: {
    SUCCESS: 'success.main',
    WARNING: 'warning.main',
    INFO: 'primary.main',
    ERROR: 'error.main',
    MUTED: 'text.secondary',
  },

  PRIORITY_COLORS: {
    high: 'error',
    medium: 'warning',
    low: 'info',
  } as const,

  STATUS_LABELS: {
    COMPLETED: 'Completed',
    IN_PROGRESS: 'In Progress',
    NOT_STARTED: 'Not Started',
  },

  LIMITS: {
    RECENT_ACTIVITIES_MAX: 5,
    UPCOMING_EVENTS_MAX: 4,
  },

  DEFAULTS: {
    STREAK_SYMBOL: '🔥',
    READINESS_TARGET: 100,
  },
} as const;

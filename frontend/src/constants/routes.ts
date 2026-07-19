export const ROUTES = {
  // Flat properties for backward compatibility
  LANDING: '/',
  LOGIN: '/login',
  REGISTER: '/register',
  FORGOT_PASSWORD: '/forgot-password',
  VERIFY_EMAIL: '/verify-email',
  NOT_FOUND: '/404',

  // Nested categories according to Dashboard Routing Specification
  PUBLIC: {
    LANDING: '/',
    NOT_FOUND: '/404',
  },
  GUEST: {
    LOGIN: '/login',
    REGISTER: '/register',
    FORGOT_PASSWORD: '/forgot-password',
    VERIFY_EMAIL: '/verify-email',
  },
  PRIVATE: {
    DASHBOARD: '/dashboard',
    RESUME: '/resume-analyzer',
    INTERVIEW: '/interview-prep',
    COMPANY: '/company-prep',
    APPLICATIONS: '/applications',
    PROGRESS: '/progress',
    PROFILE: '/profile',
    SETTINGS: '/settings',
  },
} as const;

export type RouteKeys = keyof typeof ROUTES;
export type RouteValues = (typeof ROUTES)[RouteKeys];

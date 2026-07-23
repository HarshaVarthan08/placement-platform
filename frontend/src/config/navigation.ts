import {
  LayoutDashboard,
  FileText,
  Video,
  Briefcase,
  ClipboardList,
  TrendingUp,
  User,
  Settings,
  LogOut,
} from 'lucide-react';
import type { LucideIcon } from 'lucide-react';
import { ROUTES } from '../constants';

export interface NavigationItem {
  label: string;
  icon: LucideIcon;
  route: string;
  permission?: string; // placeholder for role-based authorization
  badgeKey?: string; // placeholder for dynamic notification badge counts
}

export interface NavigationGroup {
  groupLabel: string;
  items: NavigationItem[];
}

export const NAVIGATION_GROUPS: NavigationGroup[] = [
  {
    groupLabel: 'Preparation',
    items: [
      {
        label: 'Dashboard',
        icon: LayoutDashboard,
        route: ROUTES.PRIVATE.DASHBOARD,
      },
      {
        label: 'Resume Analyzer',
        icon: FileText,
        route: ROUTES.PRIVATE.RESUME,
      },
      {
        label: 'Interview Preparation',
        icon: Video,
        route: ROUTES.PRIVATE.INTERVIEW,
      },
      {
        label: 'Job Tracker',
        icon: Briefcase,
        route: ROUTES.PRIVATE.COMPANY,
      },
    ],
  },
  {
    groupLabel: 'Tracking',
    items: [
      {
        label: 'Applications',
        icon: ClipboardList,
        route: ROUTES.PRIVATE.APPLICATIONS,
      },
      {
        label: 'Mock Interviews',
        icon: Video,
        route: ROUTES.PRIVATE.INTERVIEW, // Maps to existing interview prep placeholder
      },
      {
        label: 'Analytics',
        icon: TrendingUp,
        route: ROUTES.PRIVATE.PROGRESS, // Maps to existing progress placeholder
      },
    ],
  },
  {
    groupLabel: 'Account',
    items: [
      {
        label: 'Profile',
        icon: User,
        route: ROUTES.PRIVATE.PROFILE,
      },
      {
        label: 'Settings',
        icon: Settings,
        route: ROUTES.PRIVATE.SETTINGS,
      },
    ],
  },
];

export const LOGOUT_ITEM: NavigationItem = {
  label: 'Logout',
  icon: LogOut,
  route: '/logout',
};

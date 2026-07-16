import { FileText, BrainCircuit, Code2, MessageSquare, Map, BarChart3 } from 'lucide-react';
import type { LucideIcon } from 'lucide-react';

export const FEATURE_CARD_DIMENSIONS = {
  CARD_HEIGHT: 356,
  HEADER_HEIGHT: 56,
  DESCRIPTION_HEIGHT: 40,
  CANVAS_HEIGHT: 180,
} as const;

export const FEATURE_CANVAS_HEIGHT = FEATURE_CARD_DIMENSIONS.CANVAS_HEIGHT;

export interface FeatureConfig {
  id: string;
  title: string;
  description: string;
  icon: LucideIcon;
  futureRoute: string;
  disabled: boolean;
  animationDelay: number;
}

export const FEATURES_LIST: FeatureConfig[] = [
  {
    id: 'resume-analyzer',
    title: 'Resume Analyzer',
    description: 'Get instant feedback and ATS scoring optimization for your resume.',
    icon: FileText,
    futureRoute: '/resume',
    disabled: true,
    animationDelay: 0.1,
  },
  {
    id: 'ai-coach',
    title: 'AI Career Coach',
    description: 'Interact with a personalized mentor for placement advice and strategies.',
    icon: BrainCircuit,
    futureRoute: '/coach',
    disabled: true,
    animationDelay: 0.15,
  },
  {
    id: 'coding-practice',
    title: 'Coding Practice',
    description: 'Solve curated problems and track your coding progress.',
    icon: Code2,
    futureRoute: '/coding',
    disabled: true,
    animationDelay: 0.2,
  },
  {
    id: 'mock-interview',
    title: 'Mock Interview',
    description: 'Simulate company-specific interviews with real-time AI feedback.',
    icon: MessageSquare,
    futureRoute: '/interview',
    disabled: true,
    animationDelay: 0.25,
  },
  {
    id: 'career-roadmap',
    title: 'Career Roadmap',
    description: 'Follow a structured learning path tailored to your graduation timeline.',
    icon: Map,
    futureRoute: '/roadmap',
    disabled: true,
    animationDelay: 0.3,
  },
  {
    id: 'analytics-dashboard',
    title: 'Analytics Dashboard',
    description: 'Monitor your placement readiness scores and skill proficiencies.',
    icon: BarChart3,
    futureRoute: '/analytics',
    disabled: true,
    animationDelay: 0.35,
  },
];

import {
  FileText,
  Building2,
  Route,
  MessageSquare,
  Activity,
  Sparkles,
  Compass,
  GraduationCap,
  XCircle,
  AlertTriangle,
  HelpCircle,
  TrendingUp,
} from 'lucide-react';
import type { LucideIcon } from 'lucide-react';

export interface ProblemCardData {
  id: string;
  title: string;
  description: string;
  icon: LucideIcon;
}

export interface ValuePillarData {
  id: string;
  title: string;
  description: string;
  icon: LucideIcon;
}

export const ABOUT_HEADER = {
  title: 'Why AI Placement Platform?',
  subtitle:
    'AI-powered placement preparation designed to help engineering students prepare smarter, improve continuously, and achieve their career goals with confidence.',
};

export const PROBLEM_CARDS: ProblemCardData[] = [
  {
    id: 'resume-rejections',
    title: 'Resume Rejections',
    description:
      'Many students struggle to understand why their resumes are rejected before reaching recruiters.',
    icon: XCircle,
  },
  {
    id: 'company-prep',
    title: 'Company-Specific Preparation',
    description:
      'Every company follows a different hiring process, making preparation overwhelming without personalized guidance.',
    icon: Building2,
  },
  {
    id: 'interview-confidence',
    title: 'Interview Confidence',
    description:
      'Students often lack realistic interview practice and detailed feedback before attending actual interviews.',
    icon: AlertTriangle,
  },
  {
    id: 'career-direction',
    title: 'Career Direction',
    description:
      'Knowing what to learn, what skills are missing, and how to prepare for your dream role is often unclear.',
    icon: HelpCircle,
  },
];

export const HOW_WE_SOLVE = {
  title: 'How We Solve It',
  description:
    'AI Placement Platform guides students through every stage of placement preparation—from resume optimization and company-specific interview preparation to skill gap identification, placement readiness tracking, and intelligent job recommendations—all within one personalized AI-powered experience. By bringing structure, feedback, and personalization to your preparation, we replace placement anxiety with a repeatable, data-driven path to landing your target engineering role.',
};

export const VALUE_PILLARS: ValuePillarData[] = [
  {
    id: 'resume-intel',
    title: 'AI Resume Intelligence',
    description:
      'Instant, recruiter-grade ATS evaluation to optimize your profile and eliminate early screen rejections.',
    icon: FileText,
  },
  {
    id: 'company-prep',
    title: 'Company-Specific Preparation',
    description:
      'Curated practice paths matching the exact coding styles and question patterns of target employers.',
    icon: TrendingUp,
  },
  {
    id: 'career-roadmaps',
    title: 'Personalized Career Roadmaps',
    description:
      'Dynamic learning schedules that adapt to your core strengths and fill specific computer science gaps.',
    icon: Route,
  },
  {
    id: 'mock-interviews',
    title: 'AI Mock Interview Experience',
    description:
      'Simulated real-time technical and behavioral interviews with micro-feedback on pacing and content.',
    icon: MessageSquare,
  },
  {
    id: 'readiness-tracking',
    title: 'Placement Readiness Tracking',
    description:
      'A live readiness index that measures your prep level so you know exactly when you are ready to apply.',
    icon: Activity,
  },
  {
    id: 'job-matching',
    title: 'Intelligent Job Matching',
    description:
      'Direct opportunities automatically suggested based on your skill levels and verified coding profiles.',
    icon: Sparkles,
  },
  {
    id: 'career-insights',
    title: 'Career Intelligence Insights',
    description:
      'Data on current hiring trends, demand changes, and specific requirements for emerging roles.',
    icon: Compass,
  },
  {
    id: 'skill-gap-analysis',
    title: 'Skill Gap Analysis',
    description:
      'Diagnostic tools highlighting the exact programming languages or topics holding back your score.',
    icon: GraduationCap,
  },
];

export const MISSION_SECTION = {
  title: 'Our Mission',
  text: 'Empower every engineering student with personalized AI guidance that transforms placement preparation into a structured, measurable, and confidence-driven journey.',
  supporting:
    'We believe your dream role should be the result of predictable, focused preparation, not random chance.',
};

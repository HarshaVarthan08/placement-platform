import { UserPlus, BrainCircuit, Code2, LineChart, Briefcase } from 'lucide-react';
import type { LucideIcon } from 'lucide-react';

export interface ProcessStepData {
  id: string;
  step: number;
  title: string;
  description: string;
  points: string[];
  icon: LucideIcon;
}

export const PROCESS_STEPS: ProcessStepData[] = [
  {
    id: 'create-profile',
    step: 1,
    title: 'Create Profile',
    description: 'Establish your candidate profile and set your career parameters.',
    points: [
      'Complete academic/skills profile',
      'Upload your current resume',
      'Select target roles and preferences',
    ],
    icon: UserPlus,
  },
  {
    id: 'ai-analysis',
    step: 2,
    title: 'AI Analysis',
    description: 'Get deep insights into your market readiness and resume strength.',
    points: [
      'Get instant ATS resume score',
      'Detect critical skills and course gaps',
      'Receive personalized skill roadmaps',
    ],
    icon: BrainCircuit,
  },
  {
    id: 'practice',
    step: 3,
    title: 'Practice',
    description: 'Train with interactive modules designed for technical interviews.',
    points: [
      'Solve curated coding challenges',
      'Take aptitude and reasoning tests',
      'Conduct simulated AI mock interviews',
    ],
    icon: Code2,
  },
  {
    id: 'track-progress',
    step: 4,
    title: 'Track Progress',
    description: 'Monitor your placement readiness and preparation metrics.',
    points: [
      'View placement readiness score',
      'Track learning progress milestones',
      'Follow your dynamic roadmap updates',
    ],
    icon: LineChart,
  },
  {
    id: 'get-placed',
    step: 5,
    title: 'Get Placed',
    description: 'Transition from preparation to active application and hiring.',
    points: [
      'Get AI company recommendations',
      'Unlock exclusive job postings',
      'Succeed in live interviews',
    ],
    icon: Briefcase,
  },
];

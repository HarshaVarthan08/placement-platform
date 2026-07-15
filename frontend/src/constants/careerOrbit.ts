export interface OrbitNodeConfig {
  id: string;
  title: string;
  iconName: string;
  colorType: 'primary' | 'secondary' | 'success';
}

export interface OrbitCenterConfig {
  title: string;
  subtitle: string;
  iconName: string;
}

export interface FloatingCardConfig {
  id: string;
  title: string;
  value: string;
  iconName: string;
  colorType: 'primary' | 'secondary' | 'success';
  initialX: number;
  initialY: number;
  floatDuration: number;
}

export const ORBIT_CENTER: OrbitCenterConfig = {
  title: 'AI Career Coach',
  subtitle: 'Assistant',
  iconName: 'Sparkles',
};

export const ORBIT_NODES: OrbitNodeConfig[] = [
  {
    id: 'resume-review',
    title: 'Resume Review',
    iconName: 'FileText',
    colorType: 'primary',
  },
  {
    id: 'ats-score',
    title: 'ATS Score',
    iconName: 'Target',
    colorType: 'success',
  },
  {
    id: 'coding-practice',
    title: 'Coding Practice',
    iconName: 'Code',
    colorType: 'secondary',
  },
  {
    id: 'mock-interview',
    title: 'Mock Interview',
    iconName: 'MessageSquare',
    colorType: 'primary',
  },
  {
    id: 'skill-gap',
    title: 'Skill Gap Analysis',
    iconName: 'BrainCircuit',
    colorType: 'secondary',
  },
  {
    id: 'career-roadmap',
    title: 'Career Roadmap',
    iconName: 'Map',
    colorType: 'success',
  },
];

export const DECORATIVE_CARDS: FloatingCardConfig[] = [
  {
    id: 'decor-ats',
    title: 'ATS Match',
    value: '+18%',
    iconName: 'TrendingUp',
    colorType: 'success',
    initialX: 12,
    initialY: 15,
    floatDuration: 5,
  },
  {
    id: 'decor-resume',
    title: 'Resume Score',
    value: '88/100',
    iconName: 'Award',
    colorType: 'primary',
    initialX: 75,
    initialY: 25,
    floatDuration: 6,
  },
  {
    id: 'decor-interview',
    title: 'Readiness',
    value: 'Ready',
    iconName: 'CheckCircle2',
    colorType: 'secondary',
    initialX: 30,
    initialY: 80,
    floatDuration: 7,
  },
];

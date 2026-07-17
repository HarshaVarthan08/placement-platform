export interface PricingPlan {
  id: string;
  name: string;
  priceLabel: string;
  pricePeriod?: string;
  description: string;
  features: string[];
  buttonText: string;
  buttonVariant: 'outlined' | 'contained';
  isPopular: boolean;
  buttonAction: 'get-started' | 'coming-soon';
}

export const PRICING_PLANS: PricingPlan[] = [
  {
    id: 'free',
    name: 'Free',
    priceLabel: 'Free',
    description: 'Build your foundation and start tracking your placement readiness.',
    features: [
      'AI Resume Analysis with ATS Score',
      'Resume Strength & Weakness Analysis',
      'Resume Improvement Suggestions',
      'Placement Readiness Dashboard',
      'Company Eligibility Checker',
      'Target Company Preparation Hub',
      'AI Mock Interview Practice',
      'Personalized Interview Feedback',
      'Track Your Placement Journey',
      'Career Progress Insights',
    ],
    buttonText: 'Get Started',
    buttonVariant: 'contained',
    isPopular: false,
    buttonAction: 'get-started',
  },
  {
    id: 'pro',
    name: 'Pro',
    priceLabel: 'Coming Soon',
    description: 'Unlock advanced AI intelligence, targeted company prep, and matching engines.',
    features: [
      'Everything included in Free',
      'Company-Specific Resume Analysis',
      'Job Description ATS Optimization',
      'Preferred Company Mock Interviews',
      'AI Learning Interviews',
      'Personalized Career Roadmaps',
      'Missing Skill Analysis',
      'AI-Powered Skill Improvement Roadmap',
      'Personalized Interview Preparation Plans',
      'Intelligent Job Matching Engine',
      'Preferred Company Job Opportunities',
      'Preferred Role Job Recommendations',
      'Career Intelligence Reports',
      'Resume Optimization for Target Companies',
      'AI Career Coach (Coming Soon)',
    ],
    buttonText: 'Coming Soon',
    buttonVariant: 'outlined',
    isPopular: true,
    buttonAction: 'coming-soon',
  },
];

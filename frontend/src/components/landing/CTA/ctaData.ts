import { Sparkles, Building2, Compass, Gift, ArrowRight } from 'lucide-react';
import type { LucideIcon } from 'lucide-react';

export interface TrustIndicator {
  icon: LucideIcon;
  title: string;
  description: string;
  ariaLabel: string;
}

export interface CTAButtonConfig {
  label: string;
  action: string;
  ariaLabel: string;
}

export interface CTAData {
  header: {
    title: string;
    subtitle: string;
  };
  primaryButton: CTAButtonConfig & { icon: LucideIcon };
  secondaryButton: CTAButtonConfig;
  reassuranceText: string;
  trustIndicators: TrustIndicator[];
}

export const ctaData: CTAData = {
  header: {
    title: 'Your Dream Placement Starts Here.',
    subtitle:
      'Start your placement journey with AI-powered resume analysis, company-specific preparation, mock interviews, career intelligence, and personalized guidance—all in one platform designed for engineering students.',
  },
  primaryButton: {
    label: 'Start Free Today',
    action: '/signup',
    ariaLabel: 'Start your free placement preparation journey',
    icon: ArrowRight,
  },
  secondaryButton: {
    label: 'Explore Features',
    action: '#features',
    ariaLabel: 'Explore the key features of the platform',
  },
  reassuranceText: "Start free. Upgrade whenever you're ready.",
  trustIndicators: [
    {
      icon: Sparkles,
      title: 'AI Resume Analysis',
      description: 'Instant feedback and ATS optimization tailored for tech roles.',
      ariaLabel: 'Feature: AI Resume Analysis. Instant feedback and ATS optimization.',
    },
    {
      icon: Building2,
      title: 'Company-Specific Preparation',
      description: 'Practice questions and guides for top engineering companies.',
      ariaLabel: 'Feature: Company-Specific Preparation. Practice questions and guides.',
    },
    {
      icon: Compass,
      title: 'Personalized Career Guidance',
      description: 'Get AI-driven insights and step-by-step career path mapping.',
      ariaLabel: 'Feature: Personalized Career Guidance. Step-by-step career mapping.',
    },
    {
      icon: Gift,
      title: 'Free Forever Starter Plan',
      description: 'Access essential preparation tools with no credit card required.',
      ariaLabel: 'Feature: Free Forever Starter Plan. Access tools with no credit card.',
    },
  ],
};

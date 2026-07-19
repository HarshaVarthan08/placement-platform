import { Mail } from 'lucide-react';
import GitHubIcon from '@mui/icons-material/GitHub';
import LinkedInIcon from '@mui/icons-material/LinkedIn';
import type { ComponentType } from 'react';

export interface FooterLink {
  label: string;
  href: string;
  isPlaceholder?: boolean;
  isComingSoon?: boolean;
}

export interface FooterSection {
  title: string;
  links: FooterLink[];
}

export interface FooterSocialLink {
  name: string;
  href: string;
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  icon: ComponentType<any>;
  ariaLabel: string;
}

export interface FooterData {
  brand: {
    title: string;
    description: string;
  };
  socialLinks: FooterSocialLink[];
  sections: FooterSection[];
  bottomBar: {
    copyright: string;
    builtFor: string;
    version: string;
    links: FooterLink[];
  };
  futurePlaceholders: FooterLink[];
}

export const footerData: FooterData = {
  brand: {
    title: 'AI Placement Platform',
    description:
      'Helping engineering students prepare smarter with AI-powered resume analysis, interview practice, career guidance, and placement readiness tools.',
  },
  socialLinks: [
    {
      name: 'GitHub',
      href: 'https://github.com/HarshaVarthan08/placement-platform',
      icon: GitHubIcon,
      ariaLabel: 'Visit our GitHub organization',
    },
    {
      name: 'LinkedIn',
      href: 'https://linkedin.com',
      icon: LinkedInIcon,
      ariaLabel: 'Connect with us on LinkedIn',
    },
    {
      name: 'Email',
      href: 'mailto:support@placementplatform.ai',
      icon: Mail,
      ariaLabel: 'Send us an email at support@placementplatform.ai',
    },
  ],
  sections: [
    {
      title: 'Platform',
      links: [
        { label: 'Features', href: '#features' },
        { label: 'How It Works', href: '#how-it-works' },
        { label: 'Pricing', href: '#pricing' },
        { label: 'Why AI Placement Platform', href: '#about' },
      ],
    },
    {
      title: 'Resources',
      links: [
        { label: 'FAQ', href: '#faq', isPlaceholder: true },
        { label: 'Documentation', href: '#docs', isPlaceholder: true },
        { label: 'Support', href: '#support', isPlaceholder: true },
        { label: 'Roadmap', href: '#roadmap', isComingSoon: true },
      ],
    },
    {
      title: 'Legal',
      links: [
        { label: 'About', href: '#about' },
        { label: 'Contact', href: '#contact', isPlaceholder: true },
        { label: 'Privacy Policy', href: '#privacy', isPlaceholder: true },
        { label: 'Terms of Service', href: '#terms', isPlaceholder: true },
      ],
    },
  ],
  bottomBar: {
    copyright: '© 2026 AI Placement Platform',
    builtFor: 'Built for Engineering Students',
    version: 'Version 1.0.0 Beta',
    links: [
      { label: 'Privacy Policy', href: '#privacy', isPlaceholder: true },
      { label: 'Terms of Service', href: '#terms', isPlaceholder: true },
    ],
  },
  futurePlaceholders: [
    { label: 'Careers', href: '#careers' },
    { label: 'API Documentation', href: '#api-docs' },
    { label: 'Community', href: '#community' },
    { label: 'Changelog', href: '#changelog' },
  ],
};

export interface FooterLink {
  label: string;
  to: string;
  isPlaceholder?: boolean;
}

export interface FooterSection {
  title: string;
  links: FooterLink[];
}

export const FOOTER_SECTIONS: FooterSection[] = [
  {
    title: 'Company',
    links: [
      { label: 'Our Story', to: '/#about' },
      { label: 'Careers', to: '/#careers', isPlaceholder: true },
      { label: 'Contact', to: '/#contact', isPlaceholder: true },
    ],
  },
  {
    title: 'Product',
    links: [
      { label: 'Features', to: '/#features' },
      { label: 'Pricing', to: '/#pricing' },
      { label: 'Roadmap', to: '/#roadmap', isPlaceholder: true },
    ],
  },
  {
    title: 'Resources',
    links: [
      { label: 'Help Center', to: '/#help-center', isPlaceholder: true },
      { label: 'Documentation', to: '/#docs', isPlaceholder: true },
      { label: 'FAQ', to: '/#faq', isPlaceholder: true },
    ],
  },
  {
    title: 'Legal',
    links: [
      { label: 'Privacy Policy', to: '/#privacy', isPlaceholder: true },
      { label: 'Terms of Service', to: '/#terms', isPlaceholder: true },
    ],
  },
];

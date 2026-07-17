import { ROUTES } from './routes';

export interface NavigationItemType {
  label: string;
  to: string;
  isHash?: boolean;
}

export const PUBLIC_NAVIGATION_ITEMS: NavigationItemType[] = [
  { label: 'Features', to: '/#features', isHash: true },
  { label: 'How It Works', to: '/#how-it-works', isHash: true },
  { label: 'Pricing', to: '/#pricing', isHash: true },
  { label: 'Our Story', to: '/#about', isHash: true },
];

export const AUTH_NAVIGATION_ITEMS = {
  LOGIN: { label: 'Login', to: ROUTES.LOGIN },
  REGISTER: { label: 'Get Started', to: ROUTES.REGISTER },
} as const;

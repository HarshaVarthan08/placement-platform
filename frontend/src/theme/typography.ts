import { THEME_CONSTANTS } from '../constants';

export const typography = {
  fontFamily: THEME_CONSTANTS.FONT_FAMILY,
  fontWeightLight: 300,
  fontWeightRegular: 400,
  fontWeightMedium: 500,
  fontWeightSemiBold: 600,
  fontWeightBold: 700,
  h1: {
    fontSize: '2.5rem', // 40px
    fontWeight: 700,
    lineHeight: 1.2,
  },
  h2: {
    fontSize: '2rem', // 32px
    fontWeight: 600,
    lineHeight: 1.25,
  },
  h3: {
    fontSize: '1.5rem', // 24px
    fontWeight: 600,
    lineHeight: 1.3,
  },
  h4: {
    fontSize: '1.25rem', // 20px
    fontWeight: 500,
    lineHeight: 1.35,
  },
  body1: {
    fontSize: '1rem', // 16px
    fontWeight: 400,
    lineHeight: 1.5,
  },
  body2: {
    fontSize: '0.875rem', // 14px (Caption / helper text size in design system)
    fontWeight: 400,
    lineHeight: 1.43,
  },
  caption: {
    fontSize: '0.75rem', // 12px (Small in design system)
    fontWeight: 400,
    lineHeight: 1.33,
  },
  button: {
    textTransform: 'none' as const,
    fontWeight: 500,
  },
} as const;

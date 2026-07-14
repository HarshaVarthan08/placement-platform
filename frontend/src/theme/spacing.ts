import { THEME_CONSTANTS } from '../constants';

export const spacing = {
  base: THEME_CONSTANTS.BASE_SPACING,

  // Design System Border Radii
  borderRadius: {
    small: 8, // 8px
    medium: 12, // 12px
    large: 16, // 16px
    extraLarge: 24, // 24px
    pill: 999, // 999px
  },
} as const;

import { createTheme } from '@mui/material/styles';
import { colors } from './colors';
import { typography } from './typography';
import { spacing } from './spacing';
import { breakpoints } from './breakpoints';
import { customShadows } from './shadows';

// Extend MUI theme types to include design tokens
declare module '@mui/material/styles' {
  interface Theme {
    customShadows: typeof customShadows;
    customSpacing: typeof spacing;
  }
  interface ThemeOptions {
    customShadows?: typeof customShadows;
    customSpacing?: typeof spacing;
  }
}

export const theme = createTheme({
  palette: {
    mode: 'light',
    primary: colors.primary,
    secondary: colors.secondary,
    success: colors.success,
    warning: colors.warning,
    error: colors.error,
    background: colors.background,
    text: colors.text,
    divider: colors.border.default,
  },
  typography: typography,
  breakpoints: breakpoints,
  shape: {
    borderRadius: spacing.borderRadius.medium, // Default border radius
  },
  customShadows: customShadows,
  customSpacing: spacing,
  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          borderRadius: spacing.borderRadius.small,
          padding: '8px 16px',
        },
      },
    },
    MuiCard: {
      styleOverrides: {
        root: {
          borderRadius: spacing.borderRadius.medium,
          boxShadow: customShadows.level1,
        },
      },
    },
    MuiDialog: {
      styleOverrides: {
        paper: {
          borderRadius: spacing.borderRadius.large,
          boxShadow: customShadows.level3,
        },
      },
    },
  },
});

export default theme;

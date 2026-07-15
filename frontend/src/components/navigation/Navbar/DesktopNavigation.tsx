import { Box, Button, Link } from '@mui/material';
import { Link as RouterLink, useLocation } from 'react-router-dom';
import { PUBLIC_NAVIGATION_ITEMS, AUTH_NAVIGATION_ITEMS } from '../../../constants';
import NavigationItem from './NavigationItem';

export const DesktopNavigation = () => {
  const location = useLocation();
  const isLoginActive = location.pathname === AUTH_NAVIGATION_ITEMS.LOGIN.to;
  const isRegisterActive = location.pathname === AUTH_NAVIGATION_ITEMS.REGISTER.to;

  return (
    <Box
      component="nav"
      sx={{
        display: { xs: 'none', md: 'flex' },
        alignItems: 'center',
        gap: 3, // Clean spacing between links and actions
      }}
      aria-label="Desktop site navigation"
    >
      {/* Navigation Links */}
      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
        {PUBLIC_NAVIGATION_ITEMS.map((item) => (
          <NavigationItem key={item.label} label={item.label} to={item.to} isHash={item.isHash} />
        ))}
      </Box>

      {/* Divider line between nav links and auth */}
      <Box
        sx={{
          height: '20px',
          width: '1px',
          backgroundColor: 'divider',
        }}
      />

      {/* Auth Actions */}
      <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
        <Link
          component={RouterLink}
          to={AUTH_NAVIGATION_ITEMS.LOGIN.to}
          sx={{
            color: isLoginActive ? 'primary.main' : 'text.secondary',
            fontSize: '0.875rem', // body2
            fontWeight: 'fontWeightSemiBold',
            textDecoration: 'none',
            transition: (theme) =>
              theme.transitions.create(['color'], {
                easing: theme.transitions.easing.easeInOut,
                duration: theme.transitions.duration.shorter,
              }),
            '&:hover': {
              color: 'primary.main',
            },
            '&:focus-visible': {
              outline: '2px solid',
              outlineColor: 'primary.main',
              outlineOffset: '4px',
              borderRadius: '4px',
            },
            padding: '8px 16px',
          }}
        >
          {AUTH_NAVIGATION_ITEMS.LOGIN.label}
        </Link>
        <Button
          component={RouterLink}
          to={AUTH_NAVIGATION_ITEMS.REGISTER.to}
          variant="contained"
          sx={{
            boxShadow: 'none',
            textTransform: 'none',
            fontSize: '0.875rem',
            fontWeight: 'fontWeightSemiBold',
            padding: '8px 20px',
            borderRadius: (theme) => `${theme.customSpacing.borderRadius.pill}px`, // Using design tokens
            backgroundColor: isRegisterActive ? 'primary.dark' : 'primary.main',
            color: 'primary.contrastText',
            '&:hover': {
              boxShadow: 'none',
              backgroundColor: 'primary.dark',
            },
            '&:focus-visible': {
              outline: '2px solid',
              outlineColor: 'primary.main',
              outlineOffset: '4px',
            },
          }}
        >
          {AUTH_NAVIGATION_ITEMS.REGISTER.label}
        </Button>
      </Box>
    </Box>
  );
};

export default DesktopNavigation;

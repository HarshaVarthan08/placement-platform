import { Box, Typography, Link } from '@mui/material';
import { Orbit } from 'lucide-react';
import { Link as RouterLink } from 'react-router-dom';
import { ROUTES } from '../../../constants';

export const NavbarLogo = () => {
  // Placeholder icon - easily replaceable in the future
  const LogoIcon = Orbit;

  return (
    <Link
      component={RouterLink}
      to={ROUTES.LANDING}
      sx={{
        display: 'flex',
        alignItems: 'center',
        gap: 1.5,
        textDecoration: 'none',
        color: 'text.primary',
        '&:focus-visible': {
          outline: '2px solid',
          outlineColor: 'primary.main',
          outlineOffset: '4px',
          borderRadius: '4px',
        },
      }}
      aria-label="AI Placement Platform Home"
    >
      <Box
        sx={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          color: 'primary.main',
        }}
      >
        <LogoIcon size={28} strokeWidth={2.5} />
      </Box>
      <Typography
        variant="h4"
        component="span"
        sx={{
          fontSize: { xs: '1.1rem', sm: '1.25rem' }, // Scaled down on extra small screens for responsiveness
          fontWeight: 'fontWeightBold',
          color: 'text.primary',
          letterSpacing: '-0.02em',
          userSelect: 'none',
          whiteSpace: 'nowrap',
        }}
      >
        AI Placement Platform
      </Typography>
    </Link>
  );
};

export default NavbarLogo;

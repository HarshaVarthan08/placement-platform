import React from 'react';
import { Box, Typography, Link, useTheme } from '@mui/material';
import { Orbit } from 'lucide-react';
import { Link as RouterLink } from 'react-router-dom';
import { footerData } from './footerData';
import FooterSocial from './FooterSocial';

export const FooterBrand: React.FC = () => {
  const theme = useTheme();
  const LogoIcon = Orbit;

  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'flex-start',
        gap: 3,
      }}
    >
      {/* Brand Logo Link */}
      <Link
        component={RouterLink}
        to="/"
        sx={{
          display: 'flex',
          alignItems: 'center',
          gap: 1.5,
          textDecoration: 'none',
          outline: 'none',
          '&:focus-visible': {
            outline: `2px solid ${theme.palette.primary.light}`,
            outlineOffset: '4px',
            borderRadius: '4px',
          },
        }}
        aria-label={`${footerData.brand.title} Home`}
      >
        <Box
          sx={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            color: 'primary.light',
          }}
        >
          <LogoIcon size={28} strokeWidth={2.5} />
        </Box>
        <Typography
          variant="h4"
          component="span"
          sx={{
            fontSize: '1.25rem',
            fontWeight: 'fontWeightBold',
            color: 'common.white',
            letterSpacing: '-0.02em',
            userSelect: 'none',
            whiteSpace: 'nowrap',
          }}
        >
          {footerData.brand.title}
        </Typography>
      </Link>

      {/* Description */}
      <Typography
        variant="body2"
        sx={{
          color: 'rgba(255, 255, 255, 0.7)',
          lineHeight: 1.6,
          fontSize: '0.9rem',
          maxWidth: 320,
        }}
      >
        {footerData.brand.description}
      </Typography>

      {/* Social Icons */}
      <FooterSocial />
    </Box>
  );
};

export default FooterBrand;

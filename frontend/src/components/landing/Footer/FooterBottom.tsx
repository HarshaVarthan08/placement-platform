import React from 'react';
import { Box, Typography, Link, useTheme } from '@mui/material';
import { footerData } from './footerData';

export const FooterBottom: React.FC = () => {
  const theme = useTheme();

  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: { xs: 'column', md: 'row' },
        justifyContent: 'space-between',
        alignItems: { xs: 'flex-start', md: 'center' },
        gap: 3,
        pt: 4,
        pb: { xs: 4, md: 5 },
      }}
    >
      {/* Copyright & Info Stack */}
      <Box
        sx={{
          display: 'flex',
          flexDirection: { xs: 'column', sm: 'row' },
          alignItems: { xs: 'flex-start', sm: 'center' },
          gap: { xs: 1, sm: 2.5 },
          flexWrap: 'wrap',
        }}
      >
        <Typography
          variant="body2"
          sx={{
            color: 'rgba(255, 255, 255, 0.45)',
            fontSize: '0.85rem',
          }}
        >
          {footerData.bottomBar.copyright}
        </Typography>

        <Box
          sx={{
            display: { xs: 'none', sm: 'block' },
            width: 4,
            height: 4,
            borderRadius: '50%',
            backgroundColor: 'rgba(255, 255, 255, 0.2)',
          }}
        />

        <Typography
          variant="body2"
          sx={{
            color: 'rgba(255, 255, 255, 0.45)',
            fontSize: '0.85rem',
          }}
        >
          {footerData.bottomBar.builtFor}
        </Typography>

        <Box
          sx={{
            display: { xs: 'none', sm: 'block' },
            width: 4,
            height: 4,
            borderRadius: '50%',
            backgroundColor: 'rgba(255, 255, 255, 0.2)',
          }}
        />

        <Typography
          variant="body2"
          sx={{
            color: 'rgba(255, 255, 255, 0.35)',
            fontSize: '0.85rem',
            backgroundColor: 'rgba(255, 255, 255, 0.04)',
            px: 1,
            py: 0.25,
            borderRadius: '4px',
            border: '1px solid rgba(255, 255, 255, 0.06)',
          }}
        >
          {footerData.bottomBar.version}
        </Typography>
      </Box>

      {/* Policy Links Stack */}
      <Box
        sx={{
          display: 'flex',
          alignItems: 'center',
          gap: 3,
          flexWrap: 'wrap',
        }}
      >
        {footerData.bottomBar.links.map((link) => (
          <Link
            key={link.label}
            href={link.href}
            variant="body2"
            underline="none"
            sx={{
              color: 'rgba(255, 255, 255, 0.45)',
              fontSize: '0.85rem',
              transition: 'color 0.2s ease-in-out',
              outline: 'none',
              minHeight: 24, // Touch target assistant
              '&:hover': {
                color: 'primary.light',
              },
              '&:focus-visible': {
                color: 'primary.light',
                outline: `1px solid ${theme.palette.primary.light}`,
                outlineOffset: '4px',
                borderRadius: '2px',
              },
            }}
          >
            {link.label}
          </Link>
        ))}
      </Box>
    </Box>
  );
};

export default FooterBottom;

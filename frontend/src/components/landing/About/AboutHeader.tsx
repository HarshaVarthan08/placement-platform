import React from 'react';
import { Box, Typography, useTheme } from '@mui/material';
import { ABOUT_HEADER } from './aboutData';

export const AboutHeader: React.FC = () => {
  const theme = useTheme();

  return (
    <Box
      sx={{
        textAlign: 'center',
        maxWidth: 768,
        mx: 'auto',
        mb: { xs: 6, sm: 8, md: 10 },
        px: 2,
      }}
    >
      <Typography
        variant="h2"
        component="h2"
        id="about-title"
        sx={{
          fontWeight: theme.typography.fontWeightBold,
          color: 'text.primary',
          fontSize: { xs: '1.75rem', sm: '2.25rem', md: '2.75rem' },
          lineHeight: 1.2,
          letterSpacing: '-0.02em',
          mb: 2,
        }}
      >
        {ABOUT_HEADER.title}
      </Typography>
      <Typography
        variant="body1"
        sx={{
          color: 'text.secondary',
          fontSize: { xs: '1rem', sm: '1.125rem' },
          lineHeight: 1.6,
        }}
      >
        {ABOUT_HEADER.subtitle}
      </Typography>
    </Box>
  );
};

export default AboutHeader;

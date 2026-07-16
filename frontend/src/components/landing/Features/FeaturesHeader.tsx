import React from 'react';
import { Box, Typography, useTheme } from '@mui/material';

export const FeaturesHeader: React.FC = () => {
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
        id="features-title"
        sx={{
          fontWeight: theme.typography.fontWeightBold,
          color: 'text.primary',
          fontSize: { xs: '1.75rem', sm: '2.25rem', md: '2.75rem' },
          lineHeight: 1.2,
          letterSpacing: '-0.02em',
          mb: 2,
        }}
      >
        Prepare Smarter, Get Placed Faster
      </Typography>
      <Typography
        variant="body1"
        sx={{
          color: 'text.secondary',
          fontSize: { xs: '1rem', sm: '1.125rem' },
          lineHeight: 1.6,
        }}
      >
        Everything you need to master your placement preparation. Run simulated interviews, optimize
        your resume for ATS scanners, and follow personalized learning roadmaps.
      </Typography>
    </Box>
  );
};

export default FeaturesHeader;

import React from 'react';
import { Box, Container, useTheme } from '@mui/material';
import { alpha } from '@mui/material/styles';
import FeaturesHeader from './FeaturesHeader';
import FeatureGrid from './FeatureGrid';

export const Features: React.FC = () => {
  const theme = useTheme();

  return (
    <Box
      component="section"
      id="features"
      aria-labelledby="features-title"
      sx={{
        position: 'relative',
        width: '100%',
        py: { xs: 8, sm: 10, md: 12 },
        backgroundColor: 'background.default',
        overflow: 'hidden', // Contain ambient glows
      }}
    >
      {/* Soft Ambient Background Glow */}
      <Box
        aria-hidden="true"
        sx={{
          position: 'absolute',
          top: '30%',
          left: '50%',
          transform: 'translateX(-50%)',
          width: { xs: 350, md: 600 },
          height: { xs: 350, md: 600 },
          borderRadius: '50%',
          background: `radial-gradient(circle, ${alpha(theme.palette.primary.main, 0.04)} 0%, ${alpha(theme.palette.secondary.main, 0.03)} 50%, transparent 70%)`,
          filter: 'blur(50px)',
          zIndex: 0,
          pointerEvents: 'none',
        }}
      />

      <Container
        maxWidth="lg"
        sx={{
          position: 'relative',
          zIndex: 1,
          px: { xs: 2, sm: 3, md: 4 },
        }}
      >
        <FeaturesHeader />
        <FeatureGrid />
      </Container>
    </Box>
  );
};

export default Features;

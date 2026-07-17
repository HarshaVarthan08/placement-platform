import React from 'react';
import { Box, Container, useTheme } from '@mui/material';
import { alpha } from '@mui/material/styles';
import HowItWorksHeader from './HowItWorksHeader';
import ProcessTimeline from './ProcessTimeline';

export const HowItWorks: React.FC = () => {
  const theme = useTheme();

  return (
    <Box
      component="section"
      id="how-it-works"
      aria-labelledby="how-it-works-title"
      sx={{
        position: 'relative',
        width: '100%',
        py: { xs: 8, sm: 10, md: 12 },
        backgroundColor: 'background.default',
        overflow: 'hidden', // Contain ambient background glows
        borderTop: `1px solid ${theme.palette.divider}`,
      }}
    >
      {/* Soft Ambient Radial Background Glow (positioned opposite of the Features section to balance the page visual flow) */}
      <Box
        aria-hidden="true"
        sx={{
          position: 'absolute',
          bottom: '10%',
          right: '5%',
          width: { xs: 350, md: 600 },
          height: { xs: 350, md: 600 },
          borderRadius: '50%',
          background: `radial-gradient(circle, ${alpha(theme.palette.secondary.main, 0.03)} 0%, ${alpha(theme.palette.primary.main, 0.02)} 50%, transparent 70%)`,
          filter: 'blur(60px)',
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
        <HowItWorksHeader />
        <ProcessTimeline />
      </Container>
    </Box>
  );
};

export default HowItWorks;

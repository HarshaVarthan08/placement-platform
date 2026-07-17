import React from 'react';
import { Box, Container, useTheme } from '@mui/material';
import { alpha } from '@mui/material/styles';
import AboutHeader from './AboutHeader';
import ProblemCards from './ProblemCards';
import AboutStory from './AboutStory';
import MissionSection from './MissionSection';

export const About: React.FC = () => {
  const theme = useTheme();

  return (
    <Box
      component="section"
      id="about"
      aria-labelledby="about-title"
      sx={{
        position: 'relative',
        width: '100%',
        py: { xs: 8, sm: 10, md: 12 },
        backgroundColor: 'background.paper', // Visually separates it from Pricing which uses background.default
        overflow: 'hidden', // Contains background ambient glows
        borderTop: `1px solid ${theme.palette.divider}`,
      }}
    >
      {/* Soft Ambient Radial Background Glow */}
      <Box
        aria-hidden="true"
        sx={{
          position: 'absolute',
          bottom: '20%',
          left: '10%',
          width: { xs: 350, md: 600 },
          height: { xs: 350, md: 600 },
          borderRadius: '50%',
          background: `radial-gradient(circle, ${alpha(theme.palette.primary.main, 0.03)} 0%, ${alpha(theme.palette.secondary.main, 0.02)} 50%, transparent 70%)`,
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
        {/* Section Header */}
        <AboutHeader />

        {/* Four Problem Cards */}
        <ProblemCards />

        {/* Our Solution / Story + Different Grid */}
        <AboutStory />

        {/* Mission Statement Card */}
        <MissionSection />
      </Container>
    </Box>
  );
};

export default About;

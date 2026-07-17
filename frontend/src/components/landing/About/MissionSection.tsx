import React from 'react';
import { Card, Box, Typography, useTheme } from '@mui/material';
import { alpha } from '@mui/material/styles';
import { motion, useReducedMotion } from 'framer-motion';
import { MISSION_SECTION } from './aboutData';

export const MissionSection: React.FC = () => {
  const theme = useTheme();
  const prefersReducedMotion = useReducedMotion();

  return (
    <Box
      component={motion.div}
      initial={{ opacity: 0 }}
      whileInView={{ opacity: 1 }}
      viewport={{ once: true, margin: '-50px' }}
      transition={{ duration: prefersReducedMotion ? 0 : 0.6 }}
      sx={{
        maxWidth: 800,
        mx: 'auto',
        textAlign: 'center',
      }}
    >
      <Card
        tabIndex={0}
        role="region"
        aria-label="Our Mission Statement"
        sx={{
          p: { xs: 4, sm: 6 },
          backgroundColor: alpha(theme.palette.primary.main, 0.02),
          borderColor: alpha(theme.palette.primary.main, 0.15),
          borderWidth: 1,
          borderStyle: 'solid',
          borderRadius: `${theme.customSpacing.borderRadius.extraLarge}px`,
          boxShadow: `0 8px 32px ${alpha(theme.palette.primary.main, 0.04)}, ${theme.customShadows.level2}`,
          position: 'relative',
          overflow: 'hidden',
          outline: 'none',
          '&:focus-visible': {
            borderColor: theme.palette.primary.main,
            boxShadow: `0 0 0 3px ${alpha(theme.palette.primary.main, 0.25)}, ${theme.customShadows.level2}`,
          },
        }}
      >
        {/* Soft internal gradient background for the mission card */}
        <Box
          aria-hidden="true"
          sx={{
            position: 'absolute',
            top: '-50%',
            left: '-50%',
            width: '200%',
            height: '200%',
            background: `radial-gradient(circle, ${alpha(theme.palette.primary.main, 0.04)} 0%, ${alpha(theme.palette.secondary.main, 0.02)} 60%, transparent 80%)`,
            zIndex: 0,
            pointerEvents: 'none',
          }}
        />

        <Box sx={{ position: 'relative', zIndex: 1 }}>
          <Typography
            variant="h4"
            sx={{
              fontSize: '0.9rem',
              fontWeight: 700,
              textTransform: 'uppercase',
              letterSpacing: '0.15em',
              color: 'primary.main',
              mb: 2.5,
            }}
          >
            {MISSION_SECTION.title}
          </Typography>
          <Typography
            variant="h3"
            sx={{
              fontSize: { xs: '1.25rem', sm: '1.5rem', md: '1.75rem' },
              fontWeight: 600,
              lineHeight: 1.5,
              color: 'text.primary',
              mb: 2.5,
              letterSpacing: '-0.01em',
            }}
          >
            “{MISSION_SECTION.text}”
          </Typography>
          <Typography
            variant="body1"
            sx={{
              color: 'text.secondary',
              fontSize: { xs: '0.95rem', sm: '1.05rem' },
              fontStyle: 'italic',
              fontWeight: 400,
              lineHeight: 1.6,
            }}
          >
            {MISSION_SECTION.supporting}
          </Typography>
        </Box>
      </Card>
    </Box>
  );
};

export default MissionSection;

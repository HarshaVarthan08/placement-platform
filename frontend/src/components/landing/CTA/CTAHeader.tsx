import React from 'react';
import { Box, Typography, useTheme } from '@mui/material';
import { motion, useReducedMotion } from 'framer-motion';
import { ctaData } from './ctaData';

const MotionBox = motion.create(Box);

export const CTAHeader: React.FC = () => {
  const theme = useTheme();
  const prefersReducedMotion = useReducedMotion();

  const animationProps = prefersReducedMotion
    ? {}
    : {
        initial: { opacity: 0 },
        whileInView: { opacity: 1 },
        viewport: { once: true, margin: '-50px' },
        transition: { duration: 0.8, ease: 'easeOut' as const },
      };

  return (
    <MotionBox
      {...animationProps}
      sx={{
        textAlign: 'center',
        maxWidth: 800,
        mx: 'auto',
        mb: { xs: 4, sm: 5, md: 6 },
        px: { xs: 2, sm: 3 },
      }}
    >
      <Typography
        variant="h2"
        component="h2"
        id="cta-title"
        sx={{
          fontWeight: theme.typography.fontWeightBold,
          color: 'common.white',
          fontSize: { xs: '2rem', sm: '2.5rem', md: '3.25rem' },
          lineHeight: 1.15,
          letterSpacing: '-0.02em',
          mb: 2.5,
          textShadow: '0 2px 10px rgba(0, 0, 0, 0.15)',
        }}
      >
        {ctaData.header.title}
      </Typography>
      <Typography
        variant="body1"
        sx={{
          color: 'rgba(255, 255, 255, 0.85)',
          fontSize: { xs: '1rem', sm: '1.125rem', md: '1.25rem' },
          lineHeight: 1.6,
          fontWeight: 400,
          maxWidth: 720,
          mx: 'auto',
        }}
      >
        {ctaData.header.subtitle}
      </Typography>
    </MotionBox>
  );
};

export default CTAHeader;

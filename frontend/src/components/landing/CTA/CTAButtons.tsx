import React from 'react';
import { Box, Button, Typography, useTheme } from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';
import { motion, useReducedMotion } from 'framer-motion';
import { ctaData } from './ctaData';

const MotionBox = motion.create(Box);

export const CTAButtons: React.FC = () => {
  const theme = useTheme();
  const prefersReducedMotion = useReducedMotion();

  const ArrowIcon = ctaData.primaryButton.icon;

  const animationProps = prefersReducedMotion
    ? {}
    : {
        initial: { opacity: 0 },
        whileInView: { opacity: 1 },
        viewport: { once: true, margin: '-50px' },
        transition: { duration: 0.8, delay: 0.2, ease: 'easeOut' as const },
      };

  return (
    <MotionBox
      {...animationProps}
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        gap: 2.5,
        mb: { xs: 6, sm: 8, md: 10 },
      }}
    >
      <Box
        sx={{
          display: 'flex',
          flexDirection: { xs: 'column', sm: 'row' },
          justifyContent: 'center',
          alignItems: 'center',
          gap: { xs: 2, sm: 3 },
          width: '100%',
        }}
      >
        {/* Primary Action Button */}
        <Button
          component={RouterLink}
          to={ctaData.primaryButton.action}
          variant="contained"
          color="primary"
          size="large"
          endIcon={<ArrowIcon size={20} />}
          sx={{
            fontWeight: 600,
            fontSize: { xs: '1.05rem', sm: '1.125rem' },
            px: { xs: 4, sm: 5 },
            py: { xs: 1.75, sm: 2.25 }, // Largest button
            borderRadius: `${theme.customSpacing.borderRadius.medium}px`,
            minHeight: 48,
            minWidth: { xs: '100%', sm: 220 },
            boxShadow: `0 4px 18px rgba(0, 102, 255, 0.4)`,
            transition: 'all 0.25s ease-in-out',
            outline: 'none',
            '&:hover': {
              transform: prefersReducedMotion ? 'none' : 'translateY(-2px)',
              boxShadow: `0 6px 24px rgba(0, 102, 255, 0.6)`,
            },
            '&:active': {
              transform: 'none',
            },
            '&:focus-visible': {
              outline: `3px solid rgba(255, 255, 255, 0.8)`,
              outlineOffset: '2px',
            },
          }}
          aria-label={ctaData.primaryButton.ariaLabel}
        >
          {ctaData.primaryButton.label}
        </Button>

        {/* Secondary Action Button */}
        <Button
          component="a"
          href={ctaData.secondaryButton.action}
          variant="outlined"
          size="large"
          sx={{
            fontWeight: 600,
            fontSize: { xs: '1.05rem', sm: '1.125rem' },
            px: { xs: 4, sm: 5 },
            py: { xs: 1.75, sm: 2.25 }, // Match size/padding of primary
            borderRadius: `${theme.customSpacing.borderRadius.medium}px`,
            borderColor: 'rgba(255, 255, 255, 0.4)',
            color: 'common.white',
            minHeight: 48,
            minWidth: { xs: '100%', sm: 220 },
            transition: 'all 0.25s ease-in-out',
            outline: 'none',
            '&:hover': {
              borderColor: 'common.white',
              backgroundColor: 'rgba(255, 255, 255, 0.08)',
              transform: prefersReducedMotion ? 'none' : 'translateY(-2px)',
            },
            '&:active': {
              transform: 'none',
            },
            '&:focus-visible': {
              outline: `3px solid rgba(255, 255, 255, 0.8)`,
              outlineOffset: '2px',
            },
          }}
          aria-label={ctaData.secondaryButton.ariaLabel}
        >
          {ctaData.secondaryButton.label}
        </Button>
      </Box>

      {/* Reassurance text */}
      <Typography
        variant="body2"
        sx={{
          color: 'rgba(255, 255, 255, 0.65)',
          textAlign: 'center',
          fontSize: '0.875rem',
          letterSpacing: '0.02em',
        }}
      >
        {ctaData.reassuranceText}
      </Typography>
    </MotionBox>
  );
};

export default CTAButtons;

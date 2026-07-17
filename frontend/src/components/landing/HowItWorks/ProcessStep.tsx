import React from 'react';
import { Box, Card, Typography, useTheme } from '@mui/material';
import { alpha } from '@mui/material/styles';
import { motion, useReducedMotion } from 'framer-motion';
import { Check } from 'lucide-react';
import type { ProcessStepData } from './process';

interface ProcessStepProps {
  data: ProcessStepData;
  index: number;
}

const MotionCard = motion.create(Card);

export const ProcessStep: React.FC<ProcessStepProps> = ({ data, index }) => {
  const theme = useTheme();
  const prefersReducedMotion = useReducedMotion();
  const { step, title, description, points } = data;

  const cardVariants = {
    hidden: {
      opacity: 0,
      y: prefersReducedMotion ? 0 : 24,
    },
    visible: {
      opacity: 1,
      y: 0,
      transition: {
        duration: prefersReducedMotion ? 0 : 0.5,
        delay: index * 0.12,
        ease: 'easeOut' as const,
      },
    },
  };

  return (
    <MotionCard
      initial="hidden"
      whileInView="visible"
      viewport={{ once: true, margin: '-60px' }}
      variants={cardVariants}
      tabIndex={0}
      role="article"
      aria-label={`Step ${step}: ${title}. ${description}`}
      sx={{
        display: 'flex',
        flexDirection: 'column',
        height: '100%',
        width: '100%',
        p: { xs: 3, sm: 4 },
        boxSizing: 'border-box',
        backgroundColor: 'background.paper',
        borderRadius: `${theme.customSpacing.borderRadius.medium}px`,
        boxShadow: theme.customShadows.level1,
        border: `1px solid ${theme.palette.divider}`,
        position: 'relative',
        transition: prefersReducedMotion
          ? 'none'
          : 'box-shadow 0.3s ease, border-color 0.3s ease, transform 0.3s ease',
        outline: 'none',
        '&:focus-visible': {
          borderColor: theme.palette.primary.main,
          boxShadow: `0 0 0 3px ${alpha(theme.palette.primary.main, 0.25)}, ${theme.customShadows.level1}`,
        },
        '&:hover': {
          borderColor: theme.palette.primary.main,
          boxShadow: `0 0 16px ${alpha(theme.palette.primary.main, 0.15)}, ${theme.customShadows.level2}`,
          transform: prefersReducedMotion ? 'none' : 'translateY(-4px)',
        },
      }}
    >
      {/* Absolute Step Number in top-right corner */}
      <Typography
        variant="h4"
        sx={{
          position: 'absolute',
          top: { xs: 16, sm: 24 },
          right: { xs: 16, sm: 24 },
          fontSize: '1.25rem',
          fontWeight: theme.typography.fontWeightBold,
          color: alpha(theme.palette.text.disabled, 0.4),
          lineHeight: 1,
          userSelect: 'none',
        }}
      >
        {String(step).padStart(2, '0')}
      </Typography>

      {/* Step Title */}
      <Typography
        variant="h3"
        component="h3"
        sx={{
          fontSize: '1.25rem',
          fontWeight: 700,
          color: 'text.primary',
          lineHeight: 1.3,
          mb: 1.5,
          pr: 4, // Prevent text overlap with step number
          minHeight: { md: 52 }, // Align descriptions in the same horizontal line on desktop
        }}
      >
        {title}
      </Typography>

      {/* Step Short Description */}
      <Typography
        variant="body2"
        sx={{
          color: 'text.secondary',
          lineHeight: 1.5,
          mb: 3,
        }}
      >
        {description}
      </Typography>

      {/* Step Bullet Points */}
      <Box
        component="ul"
        sx={{
          listStyle: 'none',
          p: 0,
          m: 0,
          display: 'flex',
          flexDirection: 'column',
          gap: 1.5,
          mt: 'auto', // Pushes bullet points to the bottom of the card for alignment
        }}
      >
        {points.map((point, idx) => (
          <Box
            component="li"
            key={idx}
            sx={{
              display: 'flex',
              alignItems: 'flex-start',
              gap: 1.25,
            }}
          >
            <Box
              aria-hidden="true"
              sx={{
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                width: 18,
                height: 18,
                borderRadius: '50%',
                backgroundColor: alpha(theme.palette.success.main, 0.1),
                color: 'success.main',
                flexShrink: 0,
                mt: 0.25,
              }}
            >
              <Check size={12} strokeWidth={3} />
            </Box>
            <Typography
              variant="body2"
              sx={{
                color: 'text.primary',
                fontSize: '0.875rem',
                lineHeight: 1.4,
              }}
            >
              {point}
            </Typography>
          </Box>
        ))}
      </Box>
    </MotionCard>
  );
};

export default ProcessStep;

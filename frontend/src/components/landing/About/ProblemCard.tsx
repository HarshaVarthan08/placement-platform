import React from 'react';
import { Card, Box, Typography, useTheme } from '@mui/material';
import { alpha } from '@mui/material/styles';
import { motion, useReducedMotion } from 'framer-motion';
import type { ProblemCardData } from './aboutData';

interface ProblemCardProps {
  data: ProblemCardData;
  index: number;
}

const MotionCard = motion.create(Card);

export const ProblemCard: React.FC<ProblemCardProps> = ({ data, index }) => {
  const theme = useTheme();
  const prefersReducedMotion = useReducedMotion();
  const { title, description, icon: Icon } = data;

  const cardVariants = {
    hidden: {
      opacity: 0,
    },
    visible: {
      opacity: 1,
      transition: {
        duration: prefersReducedMotion ? 0 : 0.6,
        delay: index * 0.12,
        ease: 'easeOut' as const,
      },
    },
  };

  return (
    <MotionCard
      initial="hidden"
      whileInView="visible"
      viewport={{ once: true, margin: '-50px' }}
      variants={cardVariants}
      tabIndex={0}
      role="article"
      aria-label={`Student Challenge: ${title}. ${description}`}
      sx={{
        display: 'flex',
        flexDirection: 'column',
        height: '100%',
        p: { xs: 3.5, sm: 4 },
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
          borderColor: theme.palette.error.main, // Challenging issues, red accent
          boxShadow: `0 0 0 3px ${alpha(theme.palette.error.main, 0.25)}, ${theme.customShadows.level1}`,
        },
        '&:hover': {
          borderColor: alpha(theme.palette.error.main, 0.4),
          boxShadow: `0 8px 24px ${alpha(theme.palette.error.main, 0.08)}, ${theme.customShadows.level2}`,
          transform: prefersReducedMotion ? 'none' : 'translateY(-4px)',
        },
      }}
    >
      {/* Icon Area */}
      <Box
        aria-hidden="true"
        sx={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          width: 44,
          height: 44,
          borderRadius: `${theme.customSpacing.borderRadius.small}px`,
          backgroundColor: alpha(theme.palette.error.main, 0.08),
          color: 'error.main',
          mb: 3,
          flexShrink: 0,
        }}
      >
        <Icon size={22} strokeWidth={2} />
      </Box>

      {/* Challenge Title */}
      <Typography
        variant="h3"
        component="h3"
        sx={{
          fontSize: '1.25rem',
          fontWeight: 700,
          color: 'text.primary',
          lineHeight: 1.3,
          mb: 1.5,
        }}
      >
        {title}
      </Typography>

      {/* Challenge Description */}
      <Typography
        variant="body2"
        sx={{
          color: 'text.secondary',
          lineHeight: 1.6,
          fontSize: '0.925rem',
        }}
      >
        {description}
      </Typography>
    </MotionCard>
  );
};

export default ProblemCard;

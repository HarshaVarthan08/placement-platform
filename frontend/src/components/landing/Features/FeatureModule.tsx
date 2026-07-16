import React from 'react';
import { Box, Card, Typography, useTheme } from '@mui/material';
import { alpha } from '@mui/material/styles';
import { motion, useReducedMotion } from 'framer-motion';
import type { LucideIcon } from 'lucide-react';
import { FEATURE_CARD_DIMENSIONS } from '../../../constants/features';

export interface FeatureModuleProps {
  title: string;
  description: string;
  icon: LucideIcon;
  futureRoute: string;
  disabled?: boolean;
  onClick?: () => void;
  children?: React.ReactNode;
  animationDelay?: number;
  id?: string;
}

const MotionCard = motion.create(Card);
const MotionBox = motion.create(Box);

export const FeatureModule: React.FC<FeatureModuleProps> = ({
  title,
  description,
  icon: Icon,
  futureRoute,
  disabled = true,
  onClick,
  children,
  animationDelay = 0.1,
  id,
}) => {
  const theme = useTheme();
  const prefersReducedMotion = useReducedMotion();

  // Handle clicking, checking future usability
  const handleClick = (e: React.MouseEvent) => {
    if (disabled) {
      e.preventDefault();
      return;
    }
    if (onClick) {
      onClick();
    } else if (futureRoute) {
      // Future navigation logic: e.g. navigate(futureRoute)
    }
  };

  // Stagger/entrance animations for the card
  const cardVariants = {
    hidden: {
      opacity: 0,
      y: prefersReducedMotion ? 0 : 20,
    },
    visible: {
      opacity: 1,
      y: 0,
      transition: {
        duration: prefersReducedMotion ? 0 : 0.5,
        delay: animationDelay,
        ease: 'easeOut' as const,
      },
    },
  };

  // Entrance animations for the internal visualization
  const visualizationVariants = {
    hidden: {
      opacity: 0,
      scale: prefersReducedMotion ? 1 : 0.95,
    },
    visible: {
      opacity: 1,
      scale: 1,
      transition: {
        duration: prefersReducedMotion ? 0 : 0.4,
        delay: animationDelay + (prefersReducedMotion ? 0 : 0.25),
        ease: 'easeOut' as const,
      },
    },
  };

  return (
    <MotionCard
      id={id}
      initial="hidden"
      whileInView="visible"
      viewport={{ once: true, margin: '-50px' }}
      variants={cardVariants}
      onClick={handleClick}
      tabIndex={0}
      role="button"
      aria-disabled={disabled}
      aria-label={`${title}: ${description}`}
      sx={{
        display: 'flex',
        flexDirection: 'column',
        height: FEATURE_CARD_DIMENSIONS.CARD_HEIGHT,
        width: '100%',
        p: 3,
        boxSizing: 'border-box',
        backgroundColor: 'background.paper',
        borderRadius: `${theme.customSpacing.borderRadius.medium}px`,
        boxShadow: theme.customShadows.level1,
        border: `1px solid ${theme.palette.divider}`,
        transition: prefersReducedMotion
          ? 'none'
          : 'box-shadow 0.3s ease, border-color 0.3s ease, transform 0.2s ease',
        outline: 'none',
        cursor: disabled ? 'default' : 'pointer',
        '&:focus-visible': {
          borderColor: theme.palette.primary.main,
          boxShadow: `0 0 0 3px ${alpha(theme.palette.primary.main, 0.25)}, ${theme.customShadows.level1}`,
        },
        '&:hover': disabled
          ? {}
          : {
              borderColor: theme.palette.primary.main,
              boxShadow: `0 0 0 1px ${alpha(theme.palette.primary.main, 0.1)}, ${theme.customShadows.level2}`,
              transform: prefersReducedMotion ? 'none' : 'translateY(-2px)',
            },
      }}
    >
      {/* Header Region (icon + title) */}
      <Box
        sx={{
          display: 'flex',
          alignItems: 'center',
          gap: 1.5,
          mb: 1.5,
          height: FEATURE_CARD_DIMENSIONS.HEADER_HEIGHT,
          overflow: 'hidden',
        }}
      >
        <Box
          aria-hidden="true"
          sx={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            width: 32,
            height: 32,
            borderRadius: `${theme.customSpacing.borderRadius.small - 2}px`,
            backgroundColor: alpha(theme.palette.primary.main, 0.08),
            color: 'primary.main',
            flexShrink: 0,
          }}
        >
          <Icon size={16} strokeWidth={2} />
        </Box>
        <Typography
          variant="h3"
          component="h3"
          sx={{
            fontSize: '1.25rem',
            fontWeight: 600,
            color: 'text.primary',
            lineHeight: 1.3,
          }}
        >
          {title}
        </Typography>
      </Box>

      {/* Description Region */}
      <Typography
        variant="body2"
        sx={{
          color: 'text.secondary',
          lineHeight: 1.4,
          mb: 2.5,
          height: FEATURE_CARD_DIMENSIONS.DESCRIPTION_HEIGHT,
          overflow: 'hidden',
          textOverflow: 'ellipsis',
          display: '-webkit-box',
          WebkitLineClamp: 2,
          WebkitBoxOrient: 'vertical',
        }}
      >
        {description}
      </Typography>

      {/* Preview Area Region */}
      <MotionBox
        variants={visualizationVariants}
        aria-hidden="true"
        sx={{
          width: '100%',
          height: FEATURE_CARD_DIMENSIONS.CANVAS_HEIGHT,
          overflow: 'hidden',
          display: 'block',
          '& > *': {
            width: '100%',
            height: '100%',
            maxHeight: FEATURE_CARD_DIMENSIONS.CANVAS_HEIGHT,
            boxSizing: 'border-box',
          },
        }}
      >
        {children}
      </MotionBox>
    </MotionCard>
  );
};

export default FeatureModule;


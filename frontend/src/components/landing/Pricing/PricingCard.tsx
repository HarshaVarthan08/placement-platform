import React from 'react';
import { Card, Box, Typography, Button, useTheme } from '@mui/material';
import { alpha } from '@mui/material/styles';
import { motion, useReducedMotion } from 'framer-motion';
import { useNavigate } from 'react-router-dom';
import type { PricingPlan } from './plans';
import PricingFeatures from './PricingFeatures';

interface PricingCardProps {
  plan: PricingPlan;
  index: number;
}

const MotionCard = motion.create(Card);

export const PricingCard: React.FC<PricingCardProps> = ({ plan, index }) => {
  const theme = useTheme();
  const navigate = useNavigate();
  const prefersReducedMotion = useReducedMotion();
  const { name, priceLabel, description, features, buttonText, buttonVariant, isPopular, buttonAction } = plan;

  const isComingSoon = buttonAction === 'coming-soon';
  const isLongLabel = priceLabel.length > 8;

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
      aria-label={`${name} Plan: ${priceLabel}. ${description}`}
      sx={{
        display: 'flex',
        flexDirection: 'column',
        height: '100%',
        width: '100%',
        p: { xs: 3, sm: 4 },
        boxSizing: 'border-box',
        borderRadius: `${theme.customSpacing.borderRadius.medium}px`,
        position: 'relative',
        outline: 'none',
        transition: prefersReducedMotion
          ? 'none'
          : 'box-shadow 0.3s ease, border-color 0.3s ease, transform 0.3s ease, background-color 0.3s ease',
        
        // Emphasize the Pro card subtly using a purple border, stronger shadow, and very soft gradient background
        border: isPopular
          ? `2px solid ${theme.palette.secondary.main}`
          : `1px solid ${theme.palette.divider}`,
        boxShadow: isPopular
          ? `0 10px 25px ${alpha(theme.palette.secondary.main, 0.08)}, ${theme.customShadows.level2}`
          : theme.customShadows.level1,
        background: isPopular
          ? `linear-gradient(135deg, ${theme.palette.background.paper} 0%, ${alpha(theme.palette.secondary.main, 0.015)} 100%)`
          : theme.palette.background.paper,

        '&:focus-visible': {
          borderColor: isPopular ? theme.palette.secondary.main : theme.palette.primary.main,
          boxShadow: isPopular
            ? `0 0 0 3px ${alpha(theme.palette.secondary.main, 0.25)}, ${theme.customShadows.level2}`
            : `0 0 0 3px ${alpha(theme.palette.primary.main, 0.25)}, ${theme.customShadows.level2}`,
        },
        '&:hover': {
          borderColor: isPopular ? theme.palette.secondary.main : theme.palette.primary.main,
          boxShadow: isPopular
            ? `0 16px 32px ${alpha(theme.palette.secondary.main, 0.12)}, ${theme.customShadows.level3}`
            : `0 12px 24px ${alpha(theme.palette.primary.main, 0.12)}, ${theme.customShadows.level2}`,
          transform: prefersReducedMotion ? 'none' : 'translateY(-6px)',
        },
      }}
    >
      {/* Card Header */}
      <Box sx={{ mb: 2 }}>
        <Typography
          variant="h3"
          component="h3"
          sx={{
            fontSize: '1.5rem',
            fontWeight: theme.typography.fontWeightBold,
            color: 'text.primary',
            mb: 1.5,
          }}
        >
          {name}
        </Typography>
        
        <Typography
          variant="body2"
          sx={{
            color: 'text.secondary',
            lineHeight: 1.5,
            minHeight: { xs: 'auto', md: 48 }, // Align height across cards on desktop
          }}
        >
          {description}
        </Typography>
      </Box>

      {/* Card Pricing Display (No actual subscription prices, clean and dynamic text sizing) */}
      <Box sx={{ mb: 3, minHeight: 60, display: 'flex', alignItems: 'center' }}>
        <Typography
          variant="h2"
          component="p"
          sx={{
            fontWeight: theme.typography.fontWeightBold,
            color: isPopular ? 'secondary.main' : 'text.primary',
            fontSize: isLongLabel ? { xs: '1.75rem', sm: '2rem' } : { xs: '2.25rem', sm: '2.5rem' },
            lineHeight: 1.1,
            letterSpacing: '-0.02em',
          }}
        >
          {priceLabel}
        </Typography>
      </Box>

      {/* Divider */}
      <Box
        sx={{
          height: '1px',
          width: '100%',
          backgroundColor: theme.palette.divider,
          mb: 3,
        }}
      />

      {/* Features list container with flexGrow: 1 to push CTA button to the bottom */}
      <Box sx={{ flexGrow: 1, mb: 4 }}>
        <PricingFeatures features={features} />
      </Box>

      {/* CTA Button */}
      <Box sx={{ mt: 'auto' }}>
        <Button
          fullWidth
          variant={buttonVariant}
          disabled={isComingSoon}
          onClick={() => {
            if (buttonAction === 'get-started') {
              navigate('/register');
            }
          }}
          sx={{
            py: 1.25,
            fontWeight: theme.typography.fontWeightBold,
            textTransform: 'none',
            fontSize: '0.95rem',
            borderRadius: `${theme.customSpacing.borderRadius.small}px`,
            transition: 'all 0.2s ease',
            ...(buttonVariant === 'contained' && {
              backgroundColor: 'primary.main',
              color: 'primary.contrastText',
              '&:hover': {
                backgroundColor: 'primary.dark',
              },
            }),
            ...(isComingSoon && {
              // Custom styles for "Coming Soon" secondary outlined disabled state to make it look premium
              '&.Mui-disabled': {
                borderColor: alpha(theme.palette.secondary.main, 0.3),
                color: theme.palette.secondary.main,
                borderStyle: 'dashed',
                backgroundColor: alpha(theme.palette.secondary.main, 0.02),
              },
            }),
          }}
        >
          {buttonText}
        </Button>
      </Box>
    </MotionCard>
  );
};

export default PricingCard;

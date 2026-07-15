import { Box, Button, useTheme } from '@mui/material';
import { motion, useReducedMotion } from 'framer-motion';
import { Link as RouterLink } from 'react-router-dom';
import { HERO_CONTENT } from '../../../constants/hero';
import { ArrowRight } from 'lucide-react';

const MotionBox = motion.create(Box);

export const HeroActions = () => {
  const theme = useTheme();
  const prefersReducedMotion = useReducedMotion();

  return (
    <MotionBox
      initial={prefersReducedMotion ? { opacity: 1, y: 0 } : { opacity: 0, y: 15 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.6, delay: 0.4, ease: 'easeOut' as const }}
      sx={{
        display: 'flex',
        flexWrap: 'wrap',
        gap: 2,
        mb: 6,
      }}
    >
      {/* Primary CTA */}
      <Button
        component={RouterLink}
        to={HERO_CONTENT.ctas.primary.path}
        variant="contained"
        color="primary"
        size="large"
        endIcon={<ArrowRight size={18} />}
        sx={{
          fontWeight: 600,
          px: 3.5,
          py: 1.5,
          borderRadius: `${theme.customSpacing.borderRadius.medium}px`,
          boxShadow: `0 4px 14px ${theme.palette.primary.main}33`,
          transition: 'all 0.2s ease-in-out',
          '&:hover': {
            transform: prefersReducedMotion ? 'none' : 'translateY(-2px)',
            boxShadow: `0 6px 20px ${theme.palette.primary.main}4D`,
          },
          '&:active': {
            transform: 'none',
          },
        }}
        aria-label={HERO_CONTENT.ctas.primary.label}
      >
        {HERO_CONTENT.ctas.primary.label}
      </Button>

      {/* Secondary CTA */}
      <Button
        component="a"
        href={HERO_CONTENT.ctas.secondary.path}
        variant="outlined"
        color="primary"
        size="large"
        sx={{
          fontWeight: 600,
          px: 3.5,
          py: 1.5,
          borderRadius: `${theme.customSpacing.borderRadius.medium}px`,
          borderColor: 'divider',
          color: 'text.primary',
          transition: 'all 0.2s ease-in-out',
          '&:hover': {
            borderColor: 'text.primary',
            backgroundColor: 'transparent',
            transform: prefersReducedMotion ? 'none' : 'translateY(-2px)',
          },
          '&:active': {
            transform: 'none',
          },
        }}
        aria-label={HERO_CONTENT.ctas.secondary.label}
      >
        {HERO_CONTENT.ctas.secondary.label}
      </Button>
    </MotionBox>
  );
};

export default HeroActions;

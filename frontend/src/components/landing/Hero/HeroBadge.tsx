import { Box, useTheme } from '@mui/material';
import { motion, useReducedMotion } from 'framer-motion';
import { alpha } from '@mui/material/styles';
import { HERO_CONTENT } from '../../../constants/hero';

const MotionBox = motion.create(Box);

export const HeroBadge = () => {
  const theme = useTheme();
  const prefersReducedMotion = useReducedMotion();

  return (
    <MotionBox
      initial={prefersReducedMotion ? { opacity: 1, y: 0 } : { opacity: 0, y: -15 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.5, ease: 'easeOut' as const }}
      sx={{
        display: 'inline-flex',
        mb: 2,
      }}
    >
      <Box
        sx={{
          display: 'flex',
          alignItems: 'center',
          gap: 1,
          px: 2,
          py: 0.75,
          borderRadius: `${theme.customSpacing.borderRadius.pill}px`,
          backgroundColor: alpha(theme.palette.secondary.main, 0.08),
          border: `1px solid ${alpha(theme.palette.secondary.main, 0.2)}`,
          color: 'secondary.main',
          fontSize: '0.85rem',
          fontWeight: 600,
          letterSpacing: '0.02em',
          boxShadow: `0 2px 8px ${alpha(theme.palette.secondary.main, 0.04)}`,
        }}
      >
        {HERO_CONTENT.badge}
      </Box>
    </MotionBox>
  );
};

export default HeroBadge;

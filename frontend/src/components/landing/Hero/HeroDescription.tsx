import { Typography, useTheme } from '@mui/material';
import { motion, useReducedMotion } from 'framer-motion';
import { HERO_CONTENT } from '../../../constants/hero';

const MotionTypography = motion.create(Typography);

export const HeroDescription = () => {
  const theme = useTheme();
  const prefersReducedMotion = useReducedMotion();

  return (
    <MotionTypography
      variant="body1"
      initial={prefersReducedMotion ? { opacity: 1, y: 0 } : { opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.6, delay: 0.3, ease: 'easeOut' as const }}
      sx={{
        fontSize: { xs: '1rem', md: '1.125rem' },
        fontWeight: theme.typography.fontWeightRegular,
        color: 'text.secondary',
        mb: 4,
        maxWidth: 600,
        lineHeight: 1.6,
      }}
    >
      {HERO_CONTENT.description}
    </MotionTypography>
  );
};

export default HeroDescription;

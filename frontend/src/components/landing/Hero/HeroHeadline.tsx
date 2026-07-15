import { Typography, useTheme } from '@mui/material';
import { motion, useReducedMotion } from 'framer-motion';
import { HERO_CONTENT } from '../../../constants/hero';

const MotionTypography = motion.create(Typography);

export const HeroHeadline = () => {
  const theme = useTheme();
  const prefersReducedMotion = useReducedMotion();

  return (
    <MotionTypography
      variant="h1"
      initial={prefersReducedMotion ? { opacity: 1, y: 0 } : { opacity: 0, y: 25 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.6, delay: 0.15, ease: 'easeOut' as const }}
      sx={{
        fontSize: { xs: '2.25rem', sm: '2.75rem', md: '3.25rem', lg: '3.75rem' }, // Responsive styling leveraging theme guidelines
        fontWeight: theme.typography.fontWeightBold,
        color: 'text.primary',
        mb: 2,
        lineHeight: { xs: 1.2, sm: 1.15 },
        letterSpacing: '-0.02em',
        '& span': {
          display: 'block',
        },
      }}
    >
      <span>{HERO_CONTENT.headline.line1}</span>
      <span style={{ color: theme.palette.primary.main }}>{HERO_CONTENT.headline.line2}</span>
      <span>{HERO_CONTENT.headline.line3}</span>
    </MotionTypography>
  );
};

export default HeroHeadline;

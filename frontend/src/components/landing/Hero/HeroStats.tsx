import { Box, Paper, Typography, useTheme } from '@mui/material';
import { motion, useReducedMotion } from 'framer-motion';
import { HERO_CONTENT } from '../../../constants/hero';
import { alpha } from '@mui/material/styles';

const MotionContainer = motion.create(Box);
const MotionPaper = motion.create(Paper);

export const HeroStats = () => {
  const theme = useTheme();
  const prefersReducedMotion = useReducedMotion();

  // Framer Motion variants for stagger entry
  const containerVariants = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: {
        staggerChildren: 0.08,
        delayChildren: 0.5,
      },
    },
  };

  const cardVariants = {
    hidden: prefersReducedMotion ? { opacity: 1, y: 0 } : { opacity: 0, y: 15 },
    visible: {
      opacity: 1,
      y: 0,
      transition: {
        duration: 0.4,
        ease: 'easeOut' as const,
      },
    },
  };

  return (
    <Box
      component="dl"
      sx={{
        margin: 0,
        width: '100%',
        maxWidth: 600,
      }}
    >
      <MotionContainer
        variants={containerVariants}
        initial="hidden"
        animate="visible"
        sx={{
          display: 'grid',
          gridTemplateColumns: {
            xs: 'repeat(2, 1fr)', // 2x2 on mobile
            sm: 'repeat(4, 1fr)', // 1x4 on tablet
            md: 'repeat(2, 1fr)', // 2x2 on desktop (inside left column)
          },
          gap: 2,
          width: '100%',
        }}
      >
        {HERO_CONTENT.stats.map((stat, idx) => (
          <MotionPaper
            key={idx}
            variants={cardVariants}
            whileHover={
              prefersReducedMotion
                ? {}
                : {
                    scale: 1.025,
                    y: -2,
                    transition: { duration: 0.2 },
                  }
            }
            sx={{
              p: 2,
              borderRadius: `${theme.customSpacing.borderRadius.medium}px`,
              backgroundColor: alpha(theme.palette.background.paper, 0.6),
              backdropFilter: 'blur(4px)',
              border: `1px solid ${theme.palette.divider}`,
              boxShadow: theme.customShadows.level1,
              cursor: 'default',
              display: 'flex',
              flexDirection: 'column',
              justifyContent: 'center',
              transition: theme.transitions.create(['border-color', 'box-shadow'], {
                duration: theme.transitions.duration.short,
              }),
              '&:hover': {
                borderColor: alpha(theme.palette.primary.main, 0.4),
                boxShadow: `0 4px 12px ${alpha(theme.palette.primary.main, 0.05)}, ${theme.customShadows.level2}`,
              },
            }}
          >
            {/* dd for description details (the stat value) */}
            <Typography
              variant="h4"
              component="dd"
              sx={{
                fontWeight: theme.typography.fontWeightBold,
                color: 'primary.main',
                fontSize: { xs: '1.25rem', sm: '1.5rem' },
                lineHeight: 1.2,
                mb: 0.5,
                marginInlineStart: 0, // Reset default dd padding/margin
              }}
            >
              {stat.value}
            </Typography>
            {/* dt for description term (the stat label) */}
            <Typography
              variant="caption"
              component="dt"
              sx={{
                fontWeight: theme.typography.fontWeightMedium,
                color: 'text.secondary',
                fontSize: '0.75rem',
                textTransform: 'uppercase',
                letterSpacing: '0.05em',
              }}
            >
              {stat.label}
            </Typography>
          </MotionPaper>
        ))}
      </MotionContainer>
    </Box>
  );
};

export default HeroStats;

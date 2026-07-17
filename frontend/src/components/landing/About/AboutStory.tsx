import React from 'react';
import { Box, Card, Typography, useTheme } from '@mui/material';
import { alpha } from '@mui/material/styles';
import { motion, useReducedMotion } from 'framer-motion';
import { HOW_WE_SOLVE, VALUE_PILLARS } from './aboutData';

const MotionCard = motion.create(Card);
const MotionBox = motion.create(Box);

export const AboutStory: React.FC = () => {
  const theme = useTheme();
  const prefersReducedMotion = useReducedMotion();

  const containerVariants = {
    hidden: {},
    visible: {
      transition: {
        staggerChildren: 0.08,
      },
    },
  };

  const itemVariants = {
    hidden: {
      opacity: 0,
    },
    visible: {
      opacity: 1,
      transition: {
        duration: prefersReducedMotion ? 0 : 0.5,
        ease: 'easeOut' as const,
      },
    },
  };

  return (
    <Box
      sx={{
        display: 'grid',
        gridTemplateColumns: {
          xs: '1fr',
          md: '1fr 1.5fr',
        },
        gap: { xs: 6, md: 8 },
        alignItems: 'start',
        mb: { xs: 8, sm: 10, md: 12 },
      }}
    >
      {/* Left Column - Outcomes / How We Solve It */}
      <Box
        component={motion.div}
        initial={{ opacity: 0 }}
        whileInView={{ opacity: 1 }}
        viewport={{ once: true, margin: '-50px' }}
        transition={{ duration: prefersReducedMotion ? 0 : 0.6 }}
        sx={{
          position: { md: 'sticky' },
          top: { md: 100 }, // Keep it in view while scrolling the list on desktop
        }}
      >
        <Typography
          variant="h3"
          component="h3"
          sx={{
            fontWeight: theme.typography.fontWeightBold,
            color: 'text.primary',
            fontSize: { xs: '1.5rem', sm: '1.75rem', md: '2rem' },
            lineHeight: 1.25,
            letterSpacing: '-0.02em',
            mb: 3,
          }}
        >
          {HOW_WE_SOLVE.title}
        </Typography>
        <Typography
          variant="body1"
          sx={{
            color: 'text.secondary',
            fontSize: '1.05rem',
            lineHeight: 1.7,
            mb: 4,
          }}
        >
          {HOW_WE_SOLVE.description}
        </Typography>
      </Box>

      {/* Right Column - What Makes Us Different / Premium Pillars */}
      <Box>
        <Typography
          variant="h3"
          component="h3"
          sx={{
            fontWeight: theme.typography.fontWeightBold,
            color: 'text.primary',
            fontSize: { xs: '1.5rem', sm: '1.75rem', md: '2rem' },
            lineHeight: 1.25,
            letterSpacing: '-0.02em',
            mb: 3,
            display: { xs: 'block', md: 'block' },
          }}
        >
          What Makes Us Different
        </Typography>

        <MotionBox
          variants={containerVariants}
          initial="hidden"
          whileInView="visible"
          viewport={{ once: true, margin: '-50px' }}
          sx={{
            display: 'grid',
            gridTemplateColumns: {
              xs: '1fr',
              sm: 'repeat(2, 1fr)',
              md: 'repeat(2, 1fr)', // Desktop: 2 columns × 4 rows (8 cards total)
            },
            gap: 3,
          }}
        >
          {VALUE_PILLARS.map((pillar) => {
            const PillarIcon = pillar.icon;
            return (
              <MotionCard
                key={pillar.id}
                variants={itemVariants}
                tabIndex={0}
                role="article"
                aria-label={`${pillar.title}: ${pillar.description}`}
                sx={{
                  display: 'flex',
                  flexDirection: 'column',
                  height: '100%',
                  p: 3,
                  backgroundColor: 'background.paper',
                  borderRadius: `${theme.customSpacing.borderRadius.medium}px`,
                  boxShadow: theme.customShadows.level1,
                  border: `1px solid ${theme.palette.divider}`,
                  transition: prefersReducedMotion
                    ? 'none'
                    : 'box-shadow 0.3s ease, border-color 0.3s ease, transform 0.2s ease',
                  outline: 'none',
                  '&:focus-visible': {
                    borderColor: theme.palette.primary.main,
                    boxShadow: `0 0 0 3px ${alpha(theme.palette.primary.main, 0.25)}, ${theme.customShadows.level1}`,
                  },
                  '&:hover': {
                    borderColor: theme.palette.primary.main,
                    boxShadow: `0 8px 24px ${alpha(theme.palette.primary.main, 0.08)}, ${theme.customShadows.level2}`,
                    transform: prefersReducedMotion ? 'none' : 'translateY(-2px)',
                  },
                }}
              >
                {/* Icon box */}
                <Box
                  aria-hidden="true"
                  sx={{
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    width: 38,
                    height: 38,
                    borderRadius: `${theme.customSpacing.borderRadius.small - 2}px`,
                    backgroundColor: alpha(theme.palette.primary.main, 0.08),
                    color: 'primary.main',
                    mb: 2,
                    flexShrink: 0,
                  }}
                >
                  <PillarIcon size={18} strokeWidth={2.2} />
                </Box>

                {/* Pillar Title */}
                <Typography
                  variant="h4"
                  component="h4"
                  sx={{
                    fontSize: '1.05rem',
                    fontWeight: 700,
                    color: 'text.primary',
                    lineHeight: 1.3,
                    mb: 1,
                  }}
                >
                  {pillar.title}
                </Typography>

                {/* Pillar Description */}
                <Typography
                  variant="body2"
                  sx={{
                    color: 'text.secondary',
                    lineHeight: 1.5,
                    fontSize: '0.875rem',
                  }}
                >
                  {pillar.description}
                </Typography>
              </MotionCard>
            );
          })}
        </MotionBox>
      </Box>
    </Box>
  );
};

export default AboutStory;

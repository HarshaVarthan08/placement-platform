import React from 'react';
import { Box, useTheme, useMediaQuery } from '@mui/material';
import { motion, useReducedMotion } from 'framer-motion';
import { PROCESS_STEPS } from './process';
import ProcessStep from './ProcessStep';
import TimelineConnector from './TimelineConnector';

const MotionBox = motion.create(Box);

export const ProcessTimeline: React.FC = () => {
  const theme = useTheme();
  const prefersReducedMotion = useReducedMotion();
  const isMobile = useMediaQuery(theme.breakpoints.down('md'));

  // Define spring animation for the timeline circles
  const circleVariants = (index: number) => ({
    hidden: {
      scale: prefersReducedMotion ? 1 : 0,
      opacity: 0,
    },
    visible: {
      scale: 1,
      opacity: 1,
      transition: {
        type: prefersReducedMotion ? ('tween' as const) : ('spring' as const),
        stiffness: 260,
        damping: 20,
        duration: prefersReducedMotion ? 0 : 0.4,
        delay: index * 0.12,
      },
    },
  });

  if (isMobile) {
    // Mobile/Tablet: Left-aligned vertical timeline layout
    return (
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          width: '100%',
          maxWidth: 550,
          mx: 'auto',
          gap: 3, // gap of 24px
        }}
      >
        {PROCESS_STEPS.map((step, index) => {
          const Icon = step.icon;
          return (
            <Box
              key={step.id}
              sx={{
                display: 'flex',
                position: 'relative',
                width: '100%',
                gap: 3,
              }}
            >
              {/* Left Column: Icon Circle & Connector line */}
              <Box
                sx={{
                  display: 'flex',
                  flexDirection: 'column',
                  alignItems: 'center',
                  flexShrink: 0,
                  position: 'relative',
                  width: 48,
                }}
              >
                {/* Connector Line behind the circle */}
                {index < PROCESS_STEPS.length - 1 && (
                  <TimelineConnector orientation="vertical" index={index} />
                )}

                {/* Circle Bubble wrapper */}
                <MotionBox
                  initial="hidden"
                  whileInView="visible"
                  viewport={{ once: true, margin: '-60px' }}
                  variants={circleVariants(index)}
                  sx={{
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    width: 48,
                    height: 48,
                    borderRadius: '50%',
                    backgroundColor: 'background.paper',
                    border: `2px solid ${theme.palette.primary.main}`,
                    color: 'primary.main',
                    boxShadow: theme.customShadows.level1,
                    zIndex: 1,
                    position: 'relative',
                  }}
                >
                  <Icon size={20} strokeWidth={2} />
                </MotionBox>
              </Box>

              {/* Right Column: Card */}
              <Box sx={{ flexGrow: 1 }}>
                <ProcessStep data={step} index={index} />
              </Box>
            </Box>
          );
        })}
      </Box>
    );
  }

  // Desktop: Horizontal timeline layout
  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        width: '100%',
        gap: 4, // Space between timeline circles and cards
      }}
    >
      {/* 1. Timeline Indicator Bar (Circles + Horizontal Connector Line) */}
      <Box
        sx={{
          position: 'relative',
          display: 'flex',
          flexDirection: 'row',
          alignItems: 'center',
          width: '100%',
          gap: {
            md: theme.spacing(3),
            lg: theme.spacing(4),
          },
        }}
      >
        {/* Continuous horizontal connector line behind circles */}
        <TimelineConnector orientation="horizontal" />

        {PROCESS_STEPS.map((step, index) => {
          const Icon = step.icon;
          return (
            <Box
              key={step.id}
              sx={{
                flex: 1,
                display: 'flex',
                justifyContent: 'center',
                position: 'relative',
              }}
            >
              <MotionBox
                initial="hidden"
                whileInView="visible"
                viewport={{ once: true, margin: '-80px' }}
                variants={circleVariants(index)}
                sx={{
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  width: 48,
                  height: 48,
                  borderRadius: '50%',
                  backgroundColor: 'background.paper',
                  border: `2px solid ${theme.palette.primary.main}`,
                  color: 'primary.main',
                  boxShadow: theme.customShadows.level1,
                  zIndex: 1,
                  position: 'relative',
                  transition: prefersReducedMotion
                    ? 'none'
                    : 'transform 0.2s ease, border-color 0.2s ease',
                  '&:hover': {
                    transform: prefersReducedMotion ? 'none' : 'scale(1.1)',
                    borderColor: theme.palette.secondary.main,
                    color: 'secondary.main',
                  },
                }}
              >
                <Icon size={20} strokeWidth={2} />
              </MotionBox>
            </Box>
          );
        })}
      </Box>

      {/* 2. Cards Grid (Evenly spaced, equal height) */}
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'row',
          alignItems: 'stretch',
          width: '100%',
          gap: {
            md: theme.spacing(3),
            lg: theme.spacing(4),
          },
        }}
      >
        {PROCESS_STEPS.map((step, index) => (
          <Box
            key={step.id}
            sx={{
              flex: 1,
              display: 'flex',
            }}
          >
            <ProcessStep data={step} index={index} />
          </Box>
        ))}
      </Box>
    </Box>
  );
};

export default ProcessTimeline;

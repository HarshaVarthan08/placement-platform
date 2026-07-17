import React from 'react';
import { Box, useTheme } from '@mui/material';
import { motion, useReducedMotion } from 'framer-motion';

interface TimelineConnectorProps {
  orientation: 'horizontal' | 'vertical';
  index?: number;
}

const MotionBox = motion.create(Box);

export const TimelineConnector: React.FC<TimelineConnectorProps> = ({ orientation, index = 0 }) => {
  const theme = useTheme();
  const prefersReducedMotion = useReducedMotion();
  const isHorizontal = orientation === 'horizontal';

  const lineVariants = {
    hidden: {
      scaleX: isHorizontal ? 0 : 1,
      scaleY: isHorizontal ? 1 : 0,
      opacity: 0,
    },
    visible: {
      scaleX: 1,
      scaleY: 1,
      opacity: 1,
      transition: {
        duration: prefersReducedMotion ? 0 : 0.8,
        delay: index * 0.1,
        ease: 'easeInOut' as const,
      },
    },
  };

  return (
    <Box
      aria-hidden="true"
      sx={{
        position: 'absolute',
        zIndex: 0,
        pointerEvents: 'none',
        ...(isHorizontal
          ? {
              // Horizontal connector line behind circles on desktop
              left: '10%',
              right: '10%',
              top: '24px', // Align with vertical center of 48px circle
              height: '2px',
              transform: 'translateY(-50%)',
            }
          : {
              // Vertical connector line behind circles on mobile
              left: '24px', // Align with horizontal center of 48px circle
              top: '24px', // Start from center of circle
              bottom: '-24px', // Extend to top of the next card wrapper (accounting for gap)
              width: '2px',
              transform: 'translateX(-50%)',
            }),
      }}
    >
      <MotionBox
        initial="hidden"
        whileInView="visible"
        viewport={{ once: true, margin: '-50px' }}
        variants={lineVariants}
        sx={{
          width: '100%',
          height: '100%',
          backgroundColor: theme.palette.primary.main,
          opacity: 0.3,
          transformOrigin: isHorizontal ? 'left center' : 'top center',
        }}
      />
    </Box>
  );
};

export default TimelineConnector;

import { Box } from '@mui/material';
import { motion, useReducedMotion } from 'framer-motion';
import CareerOrbit from '../CareerOrbit';

const MotionBox = motion.create(Box);

export const HeroIllustration = () => {
  const prefersReducedMotion = useReducedMotion();

  return (
    <MotionBox
      initial={prefersReducedMotion ? { opacity: 1, scale: 1 } : { opacity: 0, scale: 0.92 }}
      animate={{ opacity: 1, scale: 1 }}
      transition={{ duration: 0.8, delay: 0.25, ease: 'easeOut' as const }}
      sx={{
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        width: '100%',
        height: '100%',
        zIndex: 1,
      }}
    >
      <CareerOrbit />
    </MotionBox>
  );
};

export default HeroIllustration;

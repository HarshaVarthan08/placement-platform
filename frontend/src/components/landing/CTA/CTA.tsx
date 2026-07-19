import React from 'react';
import { Box, Container, useTheme } from '@mui/material';
import { alpha } from '@mui/material/styles';
import { motion, useReducedMotion } from 'framer-motion';
import CTAHeader from './CTAHeader';
import CTAButtons from './CTAButtons';
import TrustIndicators from './TrustIndicators';

const MotionBox = motion.create(Box);

export const CTA: React.FC = () => {
  const theme = useTheme();
  const prefersReducedMotion = useReducedMotion();

  const animationProps = prefersReducedMotion
    ? {}
    : {
        initial: { opacity: 0 },
        whileInView: { opacity: 1 },
        viewport: { once: true, margin: '-100px' },
        transition: { duration: 0.8, ease: 'easeOut' as const },
      };

  return (
    <Box
      component="section"
      id="cta-section"
      aria-labelledby="cta-title"
      sx={{
        width: '100%',
        py: { xs: 8, sm: 10, md: 12 },
        backgroundColor: 'background.default',
        position: 'relative',
        overflow: 'hidden',
        borderTop: `1px solid ${theme.palette.divider}`,
      }}
    >
      <Container
        maxWidth="lg"
        sx={{
          px: { xs: 2, sm: 3, md: 4 },
        }}
      >
        <MotionBox
          {...animationProps}
          sx={{
            position: 'relative',
            borderRadius: `${theme.customSpacing.borderRadius.extraLarge || 24}px`,
            background: `linear-gradient(135deg, #0F172A 0%, #1E1B4B 50%, #312E81 100%)`, // Premium dark gradient
            boxShadow: '0 24px 48px rgba(0, 0, 0, 0.25)',
            py: { xs: 6, sm: 8, md: 9 },
            px: { xs: 3, sm: 6, md: 8 },
            overflow: 'hidden',
            border: '1px solid rgba(255, 255, 255, 0.08)',
          }}
        >
          {/* Ambient Glows */}
          <Box
            aria-hidden="true"
            sx={{
              position: 'absolute',
              top: '-30%',
              right: '-10%',
              width: { xs: 250, md: 350 },
              height: { xs: 250, md: 350 },
              borderRadius: '50%',
              background: `radial-gradient(circle, ${alpha(theme.palette.primary.main, 0.2)} 0%, transparent 70%)`,
              filter: 'blur(50px)',
              pointerEvents: 'none',
              zIndex: 0,
            }}
          />
          <Box
            aria-hidden="true"
            sx={{
              position: 'absolute',
              bottom: '-30%',
              left: '-10%',
              width: { xs: 250, md: 350 },
              height: { xs: 250, md: 350 },
              borderRadius: '50%',
              background: `radial-gradient(circle, ${alpha(theme.palette.secondary.main, 0.15)} 0%, transparent 70%)`,
              filter: 'blur(50px)',
              pointerEvents: 'none',
              zIndex: 0,
            }}
          />

          <Box sx={{ position: 'relative', zIndex: 1 }}>
            <CTAHeader />
            <CTAButtons />
            <TrustIndicators />
          </Box>
        </MotionBox>
      </Container>
    </Box>
  );
};

export default CTA;

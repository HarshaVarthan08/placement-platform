import React from 'react';
import { Box, Container } from '@mui/material';
import { motion, useReducedMotion } from 'framer-motion';
import FooterBrand from './FooterBrand';
import FooterLinks from './FooterLinks';
import FooterBottom from './FooterBottom';

const MotionBox = motion.create(Box);

export const Footer: React.FC = () => {
  const prefersReducedMotion = useReducedMotion();

  const animationProps = prefersReducedMotion
    ? {}
    : {
        initial: { opacity: 0 },
        whileInView: { opacity: 1 },
        viewport: { once: true },
        transition: { duration: 0.8, ease: 'easeOut' as const },
      };

  return (
    <Box
      component="footer"
      id="landing-footer"
      sx={{
        width: '100%',
        backgroundColor: '#0B0F19', // Premium dark background
        borderTop: '1px solid rgba(255, 255, 255, 0.08)', // Divider between CTA and Footer
        pt: { xs: 8, sm: 10 },
        position: 'relative',
        overflow: 'hidden',
      }}
    >
      <MotionBox {...animationProps}>
        <Container
          maxWidth="lg"
          sx={{
            px: { xs: 2, sm: 3, md: 4 },
          }}
        >
          {/* Main Footer Content Grid */}
          <Box
            sx={{
              display: 'grid',
              gridTemplateColumns: {
                xs: '1fr',
                sm: 'repeat(2, 1fr)',
                md: '2.25fr 1fr 1fr 1fr', // Brand gets more room
              },
              gap: { xs: 5, sm: 6, md: 4 },
              pb: { xs: 6, md: 8 },
            }}
          >
            {/* Brand & Socials Column */}
            <FooterBrand />

            {/* Nav Link Columns (Platform, Resources, Legal) */}
            <FooterLinks />
          </Box>

          {/* Thin Divider before Bottom Bar */}
          <Box
            sx={{
              borderTop: '1px solid rgba(255, 255, 255, 0.08)',
              width: '100%',
            }}
          />

          {/* Footer Bottom Bar */}
          <FooterBottom />
        </Container>
      </MotionBox>
    </Box>
  );
};

export default Footer;

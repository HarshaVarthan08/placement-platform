import React from 'react';
import { Box, Card, Typography, useTheme } from '@mui/material';
import { alpha } from '@mui/material/styles';
import { motion, useReducedMotion } from 'framer-motion';
import { ctaData } from './ctaData';

const MotionBox = motion.create(Box);

export const TrustIndicators: React.FC = () => {
  const theme = useTheme();
  const prefersReducedMotion = useReducedMotion();

  return (
    <Box
      sx={{
        display: 'grid',
        gridTemplateColumns: {
          xs: '1fr',
          sm: 'repeat(2, 1fr)',
          md: 'repeat(4, 1fr)',
        },
        gap: { xs: 3, md: 4 },
        width: '100%',
      }}
    >
      {ctaData.trustIndicators.map((indicator, index) => {
        const Icon = indicator.icon;

        const cardVariants = {
          hidden: { opacity: 0 },
          visible: {
            opacity: 1,
            transition: {
              duration: prefersReducedMotion ? 0 : 0.6,
              delay: index * 0.1,
              ease: 'easeOut' as const,
            },
          },
        };

        return (
          <MotionBox
            key={indicator.title}
            initial="hidden"
            whileInView="visible"
            viewport={{ once: true, margin: '-50px' }}
            variants={cardVariants}
            style={{ height: '100%' }}
          >
            <Card
              tabIndex={0}
              role="article"
              aria-label={indicator.ariaLabel}
              sx={{
                display: 'flex',
                flexDirection: 'column',
                height: '100%',
                p: { xs: 3, sm: 3.5 },
                backgroundColor: 'rgba(255, 255, 255, 0.03)',
                backdropFilter: 'blur(12px)',
                borderRadius: `${theme.customSpacing.borderRadius.medium}px`,
                border: '1px solid rgba(255, 255, 255, 0.08)',
                boxShadow: 'none',
                transition: 'all 0.3s ease-in-out',
                outline: 'none',
                '&:hover': {
                  borderColor: 'rgba(255, 255, 255, 0.25)',
                  backgroundColor: 'rgba(255, 255, 255, 0.06)',
                  transform: prefersReducedMotion ? 'none' : 'translateY(-4px)',
                },
                '&:focus-visible': {
                  borderColor: theme.palette.primary.light,
                  boxShadow: `0 0 0 3px ${alpha(theme.palette.primary.light, 0.35)}`,
                  backgroundColor: 'rgba(255, 255, 255, 0.08)',
                },
              }}
            >
              {/* Icon Container */}
              <Box
                aria-hidden="true"
                sx={{
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  width: 46,
                  height: 46,
                  borderRadius: `${theme.customSpacing.borderRadius.small}px`,
                  backgroundColor: alpha(theme.palette.primary.light, 0.1),
                  color: theme.palette.primary.light,
                  mb: 2.5,
                  flexShrink: 0,
                }}
              >
                <Icon size={24} strokeWidth={2} />
              </Box>

              {/* Title */}
              <Typography
                variant="h3"
                component="h3"
                sx={{
                  fontSize: '1.125rem',
                  fontWeight: 700,
                  color: 'common.white',
                  lineHeight: 1.3,
                  mb: 1.5,
                }}
              >
                {indicator.title}
              </Typography>

              {/* Supporting Description */}
              <Typography
                variant="body2"
                sx={{
                  color: 'rgba(255, 255, 255, 0.7)',
                  lineHeight: 1.5,
                  fontSize: '0.875rem',
                }}
              >
                {indicator.description}
              </Typography>
            </Card>
          </MotionBox>
        );
      })}
    </Box>
  );
};

export default TrustIndicators;

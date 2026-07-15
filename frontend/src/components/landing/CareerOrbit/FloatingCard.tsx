import type { ComponentType } from 'react';
import { Box, Typography, useTheme } from '@mui/material';
import { motion, useReducedMotion } from 'framer-motion';
import * as Icons from 'lucide-react';
import { alpha } from '@mui/material/styles';
import type { FloatingCardConfig } from '../../../constants/careerOrbit';

const IconMap: Record<string, ComponentType<{ size?: number; className?: string }>> = {
  TrendingUp: Icons.TrendingUp,
  Award: Icons.Award,
  CheckCircle2: Icons.CheckCircle, // Using CheckCircle or CheckCircle2
};

interface FloatingCardProps {
  card: FloatingCardConfig;
}

const MotionBox = motion.create(Box);

export const FloatingCard = ({ card }: FloatingCardProps) => {
  const theme = useTheme();
  const prefersReducedMotion = useReducedMotion();

  const IconComponent = IconMap[card.iconName] || Icons.HelpCircle;

  const getColor = () => {
    switch (card.colorType) {
      case 'secondary':
        return theme.palette.secondary.main;
      case 'success':
        return theme.palette.success.main;
      case 'primary':
      default:
        return theme.palette.primary.main;
    }
  };

  const accentColor = getColor();

  return (
    <MotionBox
      aria-hidden="true"
      style={{
        position: 'absolute',
        top: `${card.initialY}%`,
        left: `${card.initialX}%`,
        zIndex: 2,
      }}
      animate={
        prefersReducedMotion
          ? {}
          : {
              y: [0, -12, 0],
            }
      }
      transition={{
        duration: card.floatDuration,
        ease: 'easeInOut' as const,
        repeat: Infinity,
      }}
    >
      <Box
        sx={{
          display: 'flex',
          alignItems: 'center',
          gap: 1,
          py: 0.75,
          px: 1.5,
          backgroundColor: alpha(theme.palette.background.paper, 0.75),
          backdropFilter: 'blur(6px)',
          borderRadius: `${theme.customSpacing.borderRadius.small}px`,
          border: `1px solid ${alpha(theme.palette.divider, 0.55)}`,
          boxShadow: theme.customShadows.level1,
          pointerEvents: 'none',
          whiteSpace: 'nowrap',
        }}
      >
        <Box
          sx={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            color: accentColor,
            backgroundColor: alpha(accentColor, 0.08),
            borderRadius: '4px',
            p: 0.5,
          }}
        >
          <IconComponent size={14} />
        </Box>
        <Box sx={{ display: 'flex', flexDirection: 'column' }}>
          <Typography
            variant="caption"
            sx={{
              color: 'text.secondary',
              fontSize: '0.65rem',
              lineHeight: 1.1,
              fontWeight: theme.typography.fontWeightRegular,
            }}
          >
            {card.title}
          </Typography>
          <Typography
            variant="caption"
            sx={{
              color: 'text.primary',
              fontSize: '0.75rem',
              lineHeight: 1.1,
              fontWeight: 600,
            }}
          >
            {card.value}
          </Typography>
        </Box>
      </Box>
    </MotionBox>
  );
};

export default FloatingCard;

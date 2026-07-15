import type { ComponentType } from 'react';
import { Box, Typography, useTheme } from '@mui/material';
import { motion, useReducedMotion } from 'framer-motion';
import * as Icons from 'lucide-react';
import { alpha } from '@mui/material/styles';
import type { OrbitNodeConfig } from '../../../constants/careerOrbit';

// Map icon strings to Lucide components
const IconMap: Record<string, ComponentType<{ size?: number; className?: string }>> = {
  FileText: Icons.FileText,
  Target: Icons.Target,
  Code: Icons.Code,
  MessageSquare: Icons.MessageSquare,
  BrainCircuit: Icons.BrainCircuit,
  Map: Icons.Map,
};

interface OrbitNodeProps {
  node: OrbitNodeConfig;
  x: number; // calculated x coordinate
  y: number; // calculated y coordinate
  index: number;
}

const MotionBox = motion.create(Box);

export const OrbitNode = ({ node, x, y, index }: OrbitNodeProps) => {
  const theme = useTheme();
  const prefersReducedMotion = useReducedMotion();

  // Look up icon
  const IconComponent = IconMap[node.iconName] || Icons.HelpCircle;

  // Resolve palette colors based on node config
  const getColor = () => {
    switch (node.colorType) {
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

  // Floating keyframe animation for individual vertical movement
  const floatY = prefersReducedMotion ? [0, 0, 0] : [0, -6 - (index % 3) * 2, 0];

  return (
    <MotionBox
      style={{
        position: 'absolute',
        top: `calc(50% + ${y}px)`,
        left: `calc(50% + ${x}px)`,
        x: '-50%',
        y: '-50%',
        zIndex: 5,
      }}
      animate={
        prefersReducedMotion
          ? {}
          : {
              y: floatY,
            }
      }
      transition={{
        duration: 4 + (index % 3),
        ease: 'easeInOut' as const,
        repeat: Infinity,
      }}
    >
      {/* Counter-rotation block to keep the text upright */}
      <MotionBox
        animate={
          prefersReducedMotion
            ? { rotate: 0 }
            : {
                rotate: -360,
              }
        }
        transition={{
          duration: 120, // Must match parent orbit rotation speed
          ease: 'linear' as const,
          repeat: Infinity,
        }}
        whileHover={{
          scale: 1.04,
          transition: { duration: 0.2 },
        }}
        sx={{
          display: 'flex',
          alignItems: 'center',
          gap: 1.25,
          py: 0.85,
          px: 1.75,
          backgroundColor: alpha(theme.palette.background.paper, 0.92),
          backdropFilter: 'blur(10px)',
          borderRadius: `${theme.customSpacing.borderRadius.medium}px`,
          border: `1.5px solid ${alpha(accentColor, 0.25)}`,
          boxShadow: `0 2px 10px ${alpha(accentColor, 0.04)}, ${theme.customShadows.level1}`,
          cursor: 'default',
          whiteSpace: 'nowrap',
          transition: 'all 0.3s cubic-bezier(0.4, 0, 0.2, 1)',
          '&:hover': {
            borderColor: accentColor,
            boxShadow: `0 4px 16px ${alpha(accentColor, 0.2)}, ${theme.customShadows.level2}`,
            backgroundColor: alpha(theme.palette.background.paper, 0.98),
          },
        }}
      >
        {/* Node Icon */}
        <Box
          sx={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            color: accentColor,
            backgroundColor: alpha(accentColor, 0.08),
            borderRadius: '50%',
            p: 0.75,
          }}
        >
          <IconComponent size={16} aria-hidden="true" />
        </Box>

        {/* Node Title & Pulse dot */}
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
          <Typography
            variant="body2"
            sx={{
              fontWeight: theme.typography.fontWeightMedium,
              color: 'text.primary',
              userSelect: 'none',
              fontSize: '0.825rem',
            }}
          >
            {node.title}
          </Typography>

          {/* Glowing Status Dot (Ecosystem Feel) */}
          <Box
            sx={{
              width: 6,
              height: 6,
              borderRadius: '50%',
              backgroundColor: accentColor,
              boxShadow: `0 0 8px ${accentColor}`,
              display: { xs: 'none', sm: 'block' },
            }}
          />
        </Box>
      </MotionBox>
    </MotionBox>
  );
};

export default OrbitNode;

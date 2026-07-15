import { Box, Typography, useTheme } from '@mui/material';
import { motion, useReducedMotion } from 'framer-motion';
import { Bot } from 'lucide-react';
import { alpha } from '@mui/material/styles';

const MotionBox = motion.create(Box);

export const OrbitCenter = () => {
  const theme = useTheme();
  const prefersReducedMotion = useReducedMotion();

  return (
    <Box
      sx={{
        position: 'absolute',
        top: '50%',
        left: '50%',
        transform: 'translate(-50%, -50%)',
        zIndex: 10,
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
      }}
    >
      {/* 1. Purple Outer Pulsing Glow Ring */}
      <MotionBox
        animate={
          prefersReducedMotion
            ? { scale: 1, opacity: 0.4 }
            : {
                scale: [1, 1.15, 1],
                opacity: [0.25, 0.55, 0.25],
              }
        }
        transition={{
          duration: 4,
          ease: 'easeInOut' as const,
          repeat: Infinity,
        }}
        sx={{
          position: 'absolute',
          width: 170,
          height: 170,
          borderRadius: '50%',
          border: `2px solid ${alpha(theme.palette.secondary.main, 0.3)}`,
          background: `radial-gradient(circle, ${alpha(
            theme.palette.secondary.main,
            0.18,
          )} 0%, transparent 70%)`,
          filter: 'blur(6px)',
          pointerEvents: 'none',
        }}
      />

      {/* 2. Blue Inner Pulsing Glow Ring */}
      <MotionBox
        animate={
          prefersReducedMotion
            ? { scale: 1, opacity: 0.4 }
            : {
                scale: [0.95, 1.08, 0.95],
                opacity: [0.3, 0.6, 0.3],
              }
        }
        transition={{
          duration: 3,
          ease: 'easeInOut' as const,
          repeat: Infinity,
          delay: 1, // Offset phase
        }}
        sx={{
          position: 'absolute',
          width: 140,
          height: 140,
          borderRadius: '50%',
          border: `1.5px solid ${alpha(theme.palette.primary.main, 0.35)}`,
          background: `radial-gradient(circle, ${alpha(
            theme.palette.primary.main,
            0.15,
          )} 0%, transparent 60%)`,
          filter: 'blur(4px)',
          pointerEvents: 'none',
        }}
      />

      {/* 3. Deep Core Ambient Shadow Glow */}
      <Box
        sx={{
          position: 'absolute',
          width: 110,
          height: 110,
          borderRadius: '50%',
          boxShadow: `0 0 35px 10px ${alpha(theme.palette.secondary.main, 0.25)}`,
          zIndex: 0,
          pointerEvents: 'none',
        }}
      />

      {/* 4. Main Center Capsule */}
      <Box
        sx={{
          position: 'relative',
          zIndex: 1,
          width: 125,
          height: 125,
          borderRadius: '50%',
          backgroundColor: alpha(theme.palette.background.paper, 0.88),
          backdropFilter: 'blur(16px)',
          border: `2px solid ${theme.palette.secondary.main}`,
          boxShadow: `0 0 25px ${alpha(theme.palette.secondary.main, 0.35)}, ${theme.customShadows.level2}`,
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          justifyContent: 'center',
          textAlign: 'center',
          p: 1.5,
          cursor: 'default',
          transition: 'all 0.3s cubic-bezier(0.4, 0, 0.2, 1)',
          '&:hover': {
            boxShadow: `0 0 40px ${alpha(theme.palette.secondary.main, 0.65)}, ${theme.customShadows.level3}`,
            borderColor: theme.palette.secondary.light,
            transform: 'scale(1.04)',
            backgroundColor: alpha(theme.palette.background.paper, 0.95),
          },
        }}
      >
        <Box
          sx={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            backgroundColor: alpha(theme.palette.secondary.main, 0.1),
            borderRadius: '50%',
            p: 1.25,
            mb: 0.75,
            color: 'secondary.main',
            boxShadow: `inset 0 0 8px ${alpha(theme.palette.secondary.main, 0.15)}`,
          }}
        >
          <Bot size={24} />
        </Box>
        <Typography
          variant="caption"
          sx={{
            fontWeight: 600,
            color: 'text.primary',
            fontSize: '0.78rem',
            lineHeight: 1.1,
            letterSpacing: '0.02em',
          }}
        >
          AI Coach
        </Typography>
        <Typography
          variant="caption"
          sx={{
            color: 'text.secondary',
            fontSize: '0.65rem',
            lineHeight: 1.1,
            mt: 0.25,
          }}
        >
          Assistant
        </Typography>
      </Box>
    </Box>
  );
};

export default OrbitCenter;

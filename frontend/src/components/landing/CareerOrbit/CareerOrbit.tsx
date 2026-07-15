import { Box, useTheme, useMediaQuery } from '@mui/material';
import { alpha } from '@mui/material/styles';
import { motion, useReducedMotion } from 'framer-motion';
import OrbitCenter from './OrbitCenter';
import OrbitRing from './OrbitRing';
import OrbitNode from './OrbitNode';
import FloatingCard from './FloatingCard';
import { ORBIT_NODES, DECORATIVE_CARDS } from '../../../constants/careerOrbit';

const MotionBox = motion.create(Box);

export const CareerOrbit = () => {
  const theme = useTheme();
  const prefersReducedMotion = useReducedMotion();

  // Responsive radius calculation
  const isXs = useMediaQuery(theme.breakpoints.down('sm'));
  const isSm = useMediaQuery(theme.breakpoints.down('md'));
  const isMd = useMediaQuery(theme.breakpoints.down('lg'));

  // Increased radius slightly on desktop to create depth and prevent node crowding
  const radius = isXs ? 115 : isSm ? 135 : isMd ? 170 : 205;
  const orbitSize = radius * 2;

  // Number of nodes
  const totalNodes = ORBIT_NODES.length;

  return (
    <Box
      sx={{
        position: 'relative',
        width: { xs: 320, sm: 380, md: 440, lg: 500 },
        height: { xs: 320, sm: 380, md: 440, lg: 500 },
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        margin: '0 auto',
        // Hide horizontal overflow on very small devices
        overflow: 'visible',
      }}
    >
      {/* Soft Ambient Constellation Glow inside the Orbit */}
      <MotionBox
        animate={
          prefersReducedMotion
            ? { opacity: 0.35 }
            : {
                opacity: [0.25, 0.45, 0.25],
              }
        }
        transition={{
          duration: 6,
          ease: 'easeInOut' as const,
          repeat: Infinity,
        }}
        sx={{
          position: 'absolute',
          width: '80%',
          height: '80%',
          borderRadius: '50%',
          background: `radial-gradient(circle, ${alpha(theme.palette.primary.main, 0.04)} 0%, ${alpha(theme.palette.secondary.main, 0.04)} 50%, transparent 100%)`,
          filter: 'blur(30px)',
          zIndex: 0,
          pointerEvents: 'none',
        }}
      />

      {/* 1. Orbit Tracks (Visual Rings) */}
      <OrbitRing size={orbitSize} dashed={true} colorType="primary" />
      <OrbitRing size={orbitSize * 0.65} dashed={true} colorType="secondary" />

      {/* 2. Floating Decorative Cards */}
      {DECORATIVE_CARDS.map((card) => (
        <FloatingCard key={card.id} card={card} />
      ))}

      {/* 3. Central AI Assistant Core */}
      <OrbitCenter />

      {/* 4. Rotating Orbit Nodes Group */}
      <MotionBox
        animate={
          prefersReducedMotion
            ? { rotate: 0 }
            : {
                rotate: 360,
              }
        }
        transition={{
          duration: 120, // Very slow, ambient rotation
          ease: 'linear' as const,
          repeat: Infinity,
        }}
        sx={{
          position: 'absolute',
          width: '100%',
          height: '100%',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          pointerEvents: 'none', // Allow clicking elements through the container box
          willChange: prefersReducedMotion ? 'auto' : 'transform', // Optimize rendering layer
          '& > *': {
            pointerEvents: 'auto', // Enable pointer events for actual node cards
          },
        }}
      >
        {ORBIT_NODES.map((node, index) => {
          // Dynamic Trigonometric Layout for equal angular spacing
          const angle = index * ((2 * Math.PI) / totalNodes);
          const x = Math.cos(angle) * radius;
          const y = Math.sin(angle) * radius;

          return <OrbitNode key={node.id} node={node} x={x} y={y} index={index} />;
        })}
      </MotionBox>
    </Box>
  );
};

export default CareerOrbit;

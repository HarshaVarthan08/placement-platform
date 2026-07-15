import { Box, useTheme } from '@mui/material';
import { alpha } from '@mui/material/styles';

interface OrbitRingProps {
  size: number; // diameter in pixels
  dashed?: boolean;
  colorType?: 'primary' | 'secondary';
}

export const OrbitRing = ({ size, dashed = true, colorType = 'primary' }: OrbitRingProps) => {
  const theme = useTheme();

  // Resolve themed track color
  const trackColor =
    colorType === 'secondary' ? theme.palette.secondary.main : theme.palette.primary.main;

  return (
    <Box
      aria-hidden="true"
      sx={{
        position: 'absolute',
        width: size,
        height: size,
        borderRadius: '50%',
        border: `1.5px ${dashed ? 'dashed' : 'solid'}`,
        borderColor: alpha(trackColor, 0.25),
        top: '50%',
        left: '50%',
        transform: 'translate(-50%, -50%)',
        pointerEvents: 'none',
        zIndex: 1,
        // Soft glowing filter effect
        filter: `drop-shadow(0 0 2px ${alpha(trackColor, 0.2)})`,
        // Subtle radial backdrop mask to emphasize orbit bands
        background: `radial-gradient(circle, transparent 60%, ${alpha(trackColor, 0.005)} 100%)`,
        boxShadow: `inset 0 0 12px ${alpha(trackColor, 0.02)}`,
        transition: 'all 0.4s ease',
      }}
    />
  );
};

export default OrbitRing;

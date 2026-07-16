import React from 'react';
import { Box, useTheme } from '@mui/material';
import { alpha } from '@mui/material/styles';
import { FEATURE_CARD_DIMENSIONS } from '../../../../constants/features';

export interface FeaturePreviewCanvasProps {
  children: React.ReactNode;
  loading?: boolean;
}

export const FeaturePreviewCanvas: React.FC<FeaturePreviewCanvasProps> = ({
  children,
  loading = false,
}) => {
  const theme = useTheme();

  return (
    <Box
      sx={{
        width: '100%',
        height: FEATURE_CARD_DIMENSIONS.CANVAS_HEIGHT,
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'space-between',
        p: 2, // Standard design system padding token
        backgroundColor: alpha(theme.palette.background.default, 0.5),
        borderRadius: `${theme.customSpacing.borderRadius.small}px`,
        border: `1px solid ${theme.palette.divider}`,
        opacity: loading ? 0.6 : 1,
        overflow: 'hidden',
        boxSizing: 'border-box',
      }}
    >
      {children}
    </Box>
  );
};

export default FeaturePreviewCanvas;

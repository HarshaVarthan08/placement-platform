import React from 'react';
import { Box, Typography, useTheme } from '@mui/material';
import { alpha } from '@mui/material/styles';
import { ChevronRight } from 'lucide-react';
import FeaturePreviewCanvas from './FeaturePreviewCanvas';

export interface RoadmapPreviewProps {
  currentStep?: string;
  steps?: string[];
  loading?: boolean;
}

export const RoadmapPreview: React.FC<RoadmapPreviewProps> = ({
  currentStep = 'Semester',
  steps = ['Semester', 'Resume', 'Coding', 'Interview'],
  loading = false,
}) => {
  const theme = useTheme();

  return (
    <FeaturePreviewCanvas loading={loading}>
      {/* Outer row wrapping nodes */}
      <Box
        sx={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          width: '100%',
          position: 'relative',
          my: 'auto',
        }}
      >
        {steps.map((step, index) => {
          const isActive = step === currentStep;

          return (
            <React.Fragment key={step}>
              {/* Node Card */}
              <Box
                sx={{
                  display: 'flex',
                  flexDirection: 'column',
                  alignItems: 'center',
                  flex: 1,
                  zIndex: 2,
                }}
              >
                <Box
                  sx={{
                    width: 32,
                    height: 32,
                    borderRadius: '50%',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    backgroundColor: isActive
                      ? alpha(theme.palette.primary.main, 0.1)
                      : theme.palette.background.paper,
                    border: `2px solid ${
                      isActive ? theme.palette.primary.main : theme.palette.divider
                    }`,
                    boxShadow: isActive
                      ? `0 0 10px ${alpha(theme.palette.primary.main, 0.25)}`
                      : 'none',
                    mb: 1,
                  }}
                >
                  <Typography
                    variant="caption"
                    sx={{
                      fontWeight: theme.typography.fontWeightBold,
                      color: isActive ? 'primary.main' : 'text.disabled',
                      fontSize: '0.7rem',
                    }}
                  >
                    0{index + 1}
                  </Typography>
                </Box>
                <Typography
                  variant="caption"
                  sx={{
                    fontWeight: isActive
                      ? theme.typography.fontWeightBold
                      : theme.typography.fontWeightMedium,
                    color: isActive ? 'text.primary' : 'text.disabled',
                    fontSize: '0.65rem',
                    textAlign: 'center',
                  }}
                >
                  {step}
                </Typography>
              </Box>

              {/* Connecting line / Chevron */}
              {index < steps.length - 1 && (
                <Box
                  sx={{
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    zIndex: 1,
                    color: theme.palette.text.disabled,
                    opacity: 0.5,
                  }}
                >
                  <ChevronRight size={14} />
                </Box>
              )}
            </React.Fragment>
          );
        })}
      </Box>
    </FeaturePreviewCanvas>
  );
};

export default RoadmapPreview;

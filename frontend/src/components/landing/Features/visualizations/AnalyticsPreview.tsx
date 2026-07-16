import React from 'react';
import { Box, Typography, useTheme } from '@mui/material';
import { alpha } from '@mui/material/styles';
import FeaturePreviewCanvas from './FeaturePreviewCanvas';

export interface AnalyticsPreviewProps {
  skills?: string[];
  skillValues?: Record<string, number | string>;
  loading?: boolean;
}

export const AnalyticsPreview: React.FC<AnalyticsPreviewProps> = ({
  skills = ['Python', 'React', 'SQL', 'DSA'],
  skillValues = {},
  loading = false,
}) => {
  const theme = useTheme();

  return (
    <FeaturePreviewCanvas loading={loading}>
      {/* Title */}
      <Typography
        variant="caption"
        sx={{
          fontWeight: theme.typography.fontWeightBold,
          color: 'text.primary',
          mb: 1.5,
          display: 'block',
        }}
      >
        Placement Readiness
      </Typography>

      {/* Skill List */}
      <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1, flexGrow: 1 }}>
        {skills.map((skill) => {
          const val = skillValues[skill];
          const hasVal = typeof val === 'number';

          return (
            <Box
              key={skill}
              sx={{
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'space-between',
                width: '100%',
              }}
            >
              <Typography
                variant="caption"
                sx={{
                  fontSize: '0.7rem',
                  color: 'text.secondary',
                  fontWeight: theme.typography.fontWeightMedium,
                }}
              >
                {skill}
              </Typography>

              {/* Progress bar or neutral placeholder text */}
              {hasVal ? (
                <Box
                  sx={{
                    width: 60,
                    height: 5,
                    borderRadius: 2.5,
                    backgroundColor: alpha(theme.palette.primary.main, 0.1),
                    position: 'relative',
                    overflow: 'hidden',
                  }}
                >
                  <Box
                    sx={{
                      width: `${val}%`,
                      height: '100%',
                      backgroundColor: theme.palette.primary.main,
                      borderRadius: 2.5,
                    }}
                  />
                </Box>
              ) : (
                <Typography
                  variant="caption"
                  sx={{
                    fontSize: '0.6rem',
                    color: 'text.disabled',
                    fontWeight: theme.typography.fontWeightRegular,
                  }}
                >
                  Personalized after onboarding
                </Typography>
              )}
            </Box>
          );
        })}
      </Box>
    </FeaturePreviewCanvas>
  );
};

export default AnalyticsPreview;

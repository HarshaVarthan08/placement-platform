import React from 'react';
import { Box, Typography, LinearProgress, useTheme } from '@mui/material';
import { alpha } from '@mui/material/styles';
import FeaturePreviewCanvas from './FeaturePreviewCanvas';

export interface CodingPreviewProps {
  easySolved?: number;
  easyTotal?: number;
  mediumSolved?: number;
  mediumTotal?: number;
  hardSolved?: number;
  hardTotal?: number;
  loading?: boolean;
}

export const CodingPreview: React.FC<CodingPreviewProps> = ({
  easySolved,
  easyTotal,
  mediumSolved,
  mediumTotal,
  hardSolved,
  hardTotal,
  loading = false,
}) => {
  const theme = useTheme();

  // Helper to check if values are provided
  const hasData = (solved?: number, total?: number) =>
    typeof solved === 'number' && typeof total === 'number';

  const formatSolved = (solved?: number, total?: number) => {
    if (hasData(solved, total)) {
      return `${solved}/${total}`;
    }
    return '--';
  };

  const getProgress = (solved?: number, total?: number) => {
    if (hasData(solved, total) && total! > 0) {
      return (solved! / total!) * 100;
    }
    return 0;
  };

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
        Problems Solved
      </Typography>

      {/* Progress Bars Container */}
      <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1.5, flexGrow: 1 }}>
        {/* Easy */}
        <Box>
          <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 0.5 }}>
            <Typography
              variant="caption"
              sx={{ fontSize: '0.7rem', color: 'success.main', fontWeight: 600 }}
            >
              Easy
            </Typography>
            <Typography
              variant="caption"
              sx={{ fontSize: '0.7rem', color: 'text.secondary', fontWeight: 500 }}
            >
              {formatSolved(easySolved, easyTotal)}
            </Typography>
          </Box>
          <LinearProgress
            variant="determinate"
            value={getProgress(easySolved, easyTotal)}
            sx={{
              height: 4,
              borderRadius: 2,
              backgroundColor: alpha(theme.palette.success.main, 0.1),
              '& .MuiLinearProgress-bar': {
                backgroundColor: theme.palette.success.main,
                borderRadius: 2,
              },
            }}
          />
        </Box>

        {/* Medium */}
        <Box>
          <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 0.5 }}>
            <Typography
              variant="caption"
              sx={{ fontSize: '0.7rem', color: 'warning.main', fontWeight: 600 }}
            >
              Medium
            </Typography>
            <Typography
              variant="caption"
              sx={{ fontSize: '0.7rem', color: 'text.secondary', fontWeight: 500 }}
            >
              {formatSolved(mediumSolved, mediumTotal)}
            </Typography>
          </Box>
          <LinearProgress
            variant="determinate"
            value={getProgress(mediumSolved, mediumTotal)}
            sx={{
              height: 4,
              borderRadius: 2,
              backgroundColor: alpha(theme.palette.warning.main, 0.1),
              '& .MuiLinearProgress-bar': {
                backgroundColor: theme.palette.warning.main,
                borderRadius: 2,
              },
            }}
          />
        </Box>

        {/* Hard */}
        <Box>
          <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 0.5 }}>
            <Typography
              variant="caption"
              sx={{ fontSize: '0.7rem', color: 'error.main', fontWeight: 600 }}
            >
              Hard
            </Typography>
            <Typography
              variant="caption"
              sx={{ fontSize: '0.7rem', color: 'text.secondary', fontWeight: 500 }}
            >
              {formatSolved(hardSolved, hardTotal)}
            </Typography>
          </Box>
          <LinearProgress
            variant="determinate"
            value={getProgress(hardSolved, hardTotal)}
            sx={{
              height: 4,
              borderRadius: 2,
              backgroundColor: alpha(theme.palette.error.main, 0.1),
              '& .MuiLinearProgress-bar': {
                backgroundColor: theme.palette.error.main,
                borderRadius: 2,
              },
            }}
          />
        </Box>
      </Box>

      {/* Helper Footer */}
      {!hasData(easySolved, easyTotal) && (
        <Typography
          variant="caption"
          sx={{
            fontSize: '0.65rem',
            color: 'text.disabled',
            mt: 1,
            textAlign: 'center',
            display: 'block',
          }}
        >
          Waiting for profile
        </Typography>
      )}
    </FeaturePreviewCanvas>
  );
};

export default CodingPreview;

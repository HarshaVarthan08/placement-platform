import React from 'react';
import { Box, Typography, useTheme } from '@mui/material';
import { alpha } from '@mui/material/styles';
import FeaturePreviewCanvas from './FeaturePreviewCanvas';

export interface ResumePreviewProps {
  resumeScore?: number | string;
  atsScore?: number | string;
  loading?: boolean;
  resumeName?: string;
}

export const ResumePreview: React.FC<ResumePreviewProps> = ({
  resumeScore = 'Awaiting upload',
  atsScore = 'Awaiting upload',
  loading = false,
  resumeName = 'Draft_Resume.pdf',
}) => {
  const theme = useTheme();

  return (
    <FeaturePreviewCanvas loading={loading}>
      {/* Mini Doc Header */}
      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 1.5 }}>
        <Box
          sx={{
            width: 8,
            height: 8,
            borderRadius: '50%',
            backgroundColor: theme.palette.text.disabled,
          }}
        />
        <Typography
          variant="caption"
          sx={{
            fontWeight: theme.typography.fontWeightMedium,
            color: 'text.secondary',
            textOverflow: 'ellipsis',
            overflow: 'hidden',
            whiteSpace: 'nowrap',
          }}
        >
          {resumeName}
        </Typography>
      </Box>

      {/* Mini Mock Document Skeleton Lines */}
      <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1, flexGrow: 1 }}>
        <Box
          sx={{
            width: '80%',
            height: 6,
            borderRadius: 3,
            backgroundColor: alpha(theme.palette.text.disabled, 0.25),
          }}
        />
        <Box
          sx={{
            width: '95%',
            height: 6,
            borderRadius: 3,
            backgroundColor: alpha(theme.palette.text.disabled, 0.15),
          }}
        />
        <Box
          sx={{
            width: '60%',
            height: 6,
            borderRadius: 3,
            backgroundColor: alpha(theme.palette.text.disabled, 0.15),
          }}
        />
      </Box>

      {/* Metric Breakdown */}
      <Box
        sx={{
          display: 'flex',
          gap: 2,
          mt: 2,
          pt: 1.5,
          borderTop: `1px dashed ${theme.palette.divider}`,
        }}
      >
        <Box sx={{ flex: 1 }}>
          <Typography
            variant="caption"
            sx={{
              display: 'block',
              fontSize: '0.65rem',
              textTransform: 'uppercase',
              color: 'text.secondary',
              fontWeight: theme.typography.fontWeightBold,
              letterSpacing: '0.05em',
            }}
          >
            Resume Score
          </Typography>
          <Typography
            variant="body2"
            sx={{
              fontWeight: theme.typography.fontWeightBold,
              color: typeof resumeScore === 'number' ? 'primary.main' : 'text.disabled',
              fontSize: typeof resumeScore === 'number' ? '0.9rem' : '0.75rem',
            }}
          >
            {resumeScore}
          </Typography>
        </Box>
        <Box sx={{ flex: 1 }}>
          <Typography
            variant="caption"
            sx={{
              display: 'block',
              fontSize: '0.65rem',
              textTransform: 'uppercase',
              color: 'text.secondary',
              fontWeight: theme.typography.fontWeightBold,
              letterSpacing: '0.05em',
            }}
          >
            ATS Match
          </Typography>
          <Typography
            variant="body2"
            sx={{
              fontWeight: theme.typography.fontWeightBold,
              color: typeof atsScore === 'number' ? 'success.main' : 'text.disabled',
              fontSize: typeof atsScore === 'number' ? '0.9rem' : '0.75rem',
            }}
          >
            {atsScore}
          </Typography>
        </Box>
      </Box>
    </FeaturePreviewCanvas>
  );
};

export default ResumePreview;

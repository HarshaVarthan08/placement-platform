import React from 'react';
import { Box, Typography, useTheme } from '@mui/material';
import FeaturePreviewCanvas from './FeaturePreviewCanvas';

export interface InterviewPreviewProps {
  question?: string;
  answer?: string;
  communicationScore?: number | string;
  technicalScore?: number | string;
  loading?: boolean;
}

export const InterviewPreview: React.FC<InterviewPreviewProps> = ({
  question = 'Explain the difference between SQL and NoSQL databases.',
  answer = 'SQL databases are relational and table-based, whereas NoSQL databases are non-relational and document-based...',
  communicationScore = 'Available after assessment',
  technicalScore = 'Available after assessment',
  loading = false,
}) => {
  const theme = useTheme();

  return (
    <FeaturePreviewCanvas loading={loading}>
      {/* Question */}
      <Box sx={{ mb: 1 }}>
        <Typography
          variant="caption"
          sx={{
            display: 'block',
            fontWeight: theme.typography.fontWeightBold,
            color: 'primary.main',
            fontSize: '0.65rem',
            textTransform: 'uppercase',
            mb: 0.25,
          }}
        >
          Question
        </Typography>
        <Typography
          variant="body2"
          sx={{
            fontWeight: theme.typography.fontWeightMedium,
            color: 'text.primary',
            fontSize: '0.75rem',
            lineHeight: 1.25,
            display: '-webkit-box',
            WebkitLineClamp: 1,
            WebkitBoxOrient: 'vertical',
            overflow: 'hidden',
          }}
        >
          {question}
        </Typography>
      </Box>

      {/* Answer */}
      <Box sx={{ mb: 1.5, flexGrow: 1 }}>
        <Typography
          variant="caption"
          sx={{
            display: 'block',
            fontWeight: theme.typography.fontWeightBold,
            color: 'secondary.main',
            fontSize: '0.65rem',
            textTransform: 'uppercase',
            mb: 0.25,
          }}
        >
          Response
        </Typography>
        <Typography
          variant="body2"
          sx={{
            color: 'text.secondary',
            fontSize: '0.7rem',
            lineHeight: 1.3,
            display: '-webkit-box',
            WebkitLineClamp: 2,
            WebkitBoxOrient: 'vertical',
            overflow: 'hidden',
          }}
        >
          {answer}
        </Typography>
      </Box>

      {/* Feedback metrics */}
      <Box
        sx={{
          display: 'flex',
          gap: 1.5,
          pt: 1,
          borderTop: `1px dashed ${theme.palette.divider}`,
        }}
      >
        <Box sx={{ flex: 1 }}>
          <Typography
            variant="caption"
            sx={{
              display: 'block',
              fontSize: '0.6rem',
              color: 'text.secondary',
              fontWeight: theme.typography.fontWeightBold,
              textTransform: 'uppercase',
            }}
          >
            Communication
          </Typography>
          <Typography
            variant="body2"
            sx={{
              fontWeight: theme.typography.fontWeightBold,
              color: typeof communicationScore === 'number' ? 'success.main' : 'text.disabled',
              fontSize: typeof communicationScore === 'number' ? '0.85rem' : '0.65rem',
              whiteSpace: 'nowrap',
              overflow: 'hidden',
              textOverflow: 'ellipsis',
            }}
          >
            {communicationScore}
          </Typography>
        </Box>
        <Box sx={{ flex: 1 }}>
          <Typography
            variant="caption"
            sx={{
              display: 'block',
              fontSize: '0.6rem',
              color: 'text.secondary',
              fontWeight: theme.typography.fontWeightBold,
              textTransform: 'uppercase',
            }}
          >
            Technical
          </Typography>
          <Typography
            variant="body2"
            sx={{
              fontWeight: theme.typography.fontWeightBold,
              color: typeof technicalScore === 'number' ? 'success.main' : 'text.disabled',
              fontSize: typeof technicalScore === 'number' ? '0.85rem' : '0.65rem',
              whiteSpace: 'nowrap',
              overflow: 'hidden',
              textOverflow: 'ellipsis',
            }}
          >
            {technicalScore}
          </Typography>
        </Box>
      </Box>
    </FeaturePreviewCanvas>
  );
};

export default InterviewPreview;

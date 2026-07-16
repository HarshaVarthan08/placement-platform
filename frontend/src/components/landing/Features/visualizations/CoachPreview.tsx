import React from 'react';
import { Box, Typography, useTheme } from '@mui/material';
import { alpha } from '@mui/material/styles';
import FeaturePreviewCanvas from './FeaturePreviewCanvas';

export interface CoachMessage {
  sender: 'ai' | 'user';
  text: string;
}

export interface CoachPreviewProps {
  messages?: CoachMessage[];
  status?: string;
  loading?: boolean;
}

export const CoachPreview: React.FC<CoachPreviewProps> = ({
  messages = [{ sender: 'ai', text: "Hello! Let's improve your resume." }],
  status = 'Online',
  loading = false,
}) => {
  const theme = useTheme();

  return (
    <FeaturePreviewCanvas loading={loading}>
      {/* Header */}
      <Box
        sx={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          pb: 1,
          mb: 1.5,
          borderBottom: `1px solid ${theme.palette.divider}`,
        }}
      >
        <Typography
          variant="caption"
          sx={{
            fontWeight: theme.typography.fontWeightBold,
            color: 'text.primary',
          }}
        >
          AI Coach
        </Typography>
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5 }}>
          <Box
            sx={{
              width: 6,
              height: 6,
              borderRadius: '50%',
              backgroundColor: theme.palette.success.main,
            }}
          />
          <Typography
            variant="caption"
            sx={{
              fontSize: '0.65rem',
              color: 'text.secondary',
            }}
          >
            {status}
          </Typography>
        </Box>
      </Box>

      {/* Chat Messages */}
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          gap: 1,
          flexGrow: 1,
          overflowY: 'auto',
          mb: 1.5,
        }}
      >
        {messages.map((msg, i) => (
          <Box
            key={i}
            sx={{
              alignSelf: msg.sender === 'ai' ? 'flex-start' : 'flex-end',
              maxWidth: '85%',
              backgroundColor:
                msg.sender === 'ai'
                  ? alpha(theme.palette.primary.main, 0.08)
                  : alpha(theme.palette.secondary.main, 0.1),
              borderRadius: `${theme.customSpacing.borderRadius.small}px`,
              p: 1,
              border: `1px solid ${
                msg.sender === 'ai'
                  ? alpha(theme.palette.primary.main, 0.15)
                  : alpha(theme.palette.secondary.main, 0.15)
              }`,
            }}
          >
            <Typography
              variant="caption"
              sx={{
                color: 'text.primary',
                fontSize: '0.75rem',
                lineHeight: 1.3,
                display: 'block',
              }}
            >
              {msg.text}
            </Typography>
          </Box>
        ))}
      </Box>

      {/* Mock Text Field */}
      <Box
        sx={{
          backgroundColor: theme.palette.background.paper,
          borderRadius: `${theme.customSpacing.borderRadius.small - 2}px`,
          border: `1px solid ${theme.palette.divider}`,
          px: 1.5,
          py: 0.75,
          display: 'flex',
          alignItems: 'center',
        }}
      >
        <Typography
          variant="caption"
          sx={{
            color: 'text.disabled',
            fontSize: '0.7rem',
          }}
        >
          Ask the coach a question...
        </Typography>
      </Box>
    </FeaturePreviewCanvas>
  );
};

export default CoachPreview;

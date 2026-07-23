import React from 'react';
import { Box, Grid, Typography, LinearProgress, Paper } from '@mui/material';
import { Award, Zap } from 'lucide-react';
import type { GreetingData } from '../../../../types/dashboard';
import { WidgetCard } from '../common';

interface WelcomeBannerProps {
  data?: GreetingData;
  loading?: boolean;
  error?: Error | null;
}

export const WelcomeBanner: React.FC<WelcomeBannerProps> = ({
  data,
  loading = false,
  error = null,
}) => {
  return (
    <WidgetCard loading={loading} loadingVariant="banner" error={error} isEmpty={!data}>
      {data && (
        <Grid container spacing={3} sx={{ alignItems: 'center' }}>
          {/* Greeting Text Block */}
          <Grid size={{ xs: 12, md: 7 }}>
            <Box>
              <Typography variant="caption" color="text.secondary" sx={{ fontWeight: 500 }}>
                {data.currentDate}
              </Typography>
              <Typography
                variant="h3"
                color="text.primary"
                gutterBottom
                sx={{
                  fontWeight: 800,
                  mt: 0.5,
                  letterSpacing: '-0.02em',
                  background: (theme) =>
                    `linear-gradient(45deg, ${theme.palette.primary.main} 30%, ${theme.palette.secondary.main} 90%)`,
                  WebkitBackgroundClip: 'text',
                  WebkitTextFillColor: 'transparent',
                }}
              >
                {data.greeting}, {data.userName}
              </Typography>
              <Typography variant="body1" color="text.primary" sx={{ fontWeight: 500, mb: 1 }}>
                {data.motivationalMessage}
              </Typography>
              <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                <strong>{"Today's Focus:"}</strong> {data.focusArea}
              </Typography>

              {/* Goal indicator */}
              <Paper
                variant="outlined"
                sx={{
                  display: 'inline-flex',
                  alignItems: 'center',
                  gap: 1.5,
                  p: '8px 16px',
                  borderColor: 'primary.light',
                  backgroundColor: 'primary.lighter',
                  borderRadius: 2,
                  mt: 0.5,
                }}
              >
                <Box sx={{ color: 'primary.main', display: 'flex' }}>
                  <Award size={18} />
                </Box>
                <Typography variant="body2" color="primary.dark" sx={{ fontWeight: 600 }}>
                  {data.currentGoal}
                </Typography>
              </Paper>
            </Box>
          </Grid>

          {/* Progress Indicators Block */}
          <Grid size={{ xs: 12, md: 5 }}>
            <Box
              sx={{
                p: 2.5,
                borderRadius: 3,
                backgroundColor: 'background.default',
                border: '1px solid',
                borderColor: 'divider',
              }}
            >
              {/* Streak info */}
              <Box
                sx={{
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'space-between',
                  mb: 2.5,
                }}
              >
                <Typography variant="subtitle2" color="text.secondary" sx={{ fontWeight: 600 }}>
                  Preparation Streak
                </Typography>
                <Box
                  sx={{
                    display: 'flex',
                    alignItems: 'center',
                    gap: 0.5,
                    backgroundColor: 'warning.lighter',
                    color: 'warning.dark',
                    px: 1.5,
                    py: 0.5,
                    borderRadius: 99,
                    fontWeight: 700,
                    fontSize: '0.875rem',
                  }}
                >
                  <Zap size={16} fill="currentColor" />
                  {data.currentStreak} Days
                </Box>
              </Box>

              {/* Profile Completion Indicator */}
              <Box sx={{ mb: 2 }}>
                <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 0.75 }}>
                  <Typography variant="body2" color="text.secondary" sx={{ fontWeight: 500 }}>
                    Profile Completion
                  </Typography>
                  <Typography variant="body2" color="text.primary" sx={{ fontWeight: 700 }}>
                    {data.profileCompletion}%
                  </Typography>
                </Box>
                <LinearProgress
                  variant="determinate"
                  value={data.profileCompletion}
                  color="secondary"
                  sx={{
                    height: 8,
                    borderRadius: 4,
                    backgroundColor: 'grey.200',
                  }}
                />
              </Box>

              {/* Placement Readiness Indicator */}
              <Box>
                <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 0.75 }}>
                  <Typography variant="body2" color="text.secondary" sx={{ fontWeight: 500 }}>
                    Placement Readiness
                  </Typography>
                  <Typography variant="body2" color="primary.main" sx={{ fontWeight: 700 }}>
                    {data.placementReadiness}%
                  </Typography>
                </Box>
                <LinearProgress
                  variant="determinate"
                  value={data.placementReadiness}
                  color="primary"
                  sx={{
                    height: 8,
                    borderRadius: 4,
                    backgroundColor: 'grey.200',
                  }}
                />
              </Box>
            </Box>
          </Grid>
        </Grid>
      )}
    </WidgetCard>
  );
};

export default WelcomeBanner;

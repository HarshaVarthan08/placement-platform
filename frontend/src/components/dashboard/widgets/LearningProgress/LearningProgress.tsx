import React from 'react';
import { Box, Typography, LinearProgress, Chip } from '@mui/material';
import { BookOpen } from 'lucide-react';
import type { LearningProgressItem, LearningLevel } from '../../../../types/dashboard';
import { WidgetCard } from '../common';
import { DASHBOARD_CONSTANTS } from '../../../../constants/dashboard';

interface LearningProgressProps {
  data?: LearningProgressItem[];
  loading?: boolean;
  error?: Error | null;
}

export const LearningProgress: React.FC<LearningProgressProps> = ({
  data = [],
  loading = false,
  error = null,
}) => {
  // Resolve chip color and label from LearningLevel
  const getLevelDetails = (level: LearningLevel) => {
    switch (level) {
      case 'ADVANCED':
        return { color: 'success' as const, label: 'Advanced', colorToken: 'success.main' };
      case 'INTERMEDIATE':
        return { color: 'primary' as const, label: 'Intermediate', colorToken: 'primary.main' };
      case 'BEGINNER':
      default:
        return { color: 'secondary' as const, label: 'Beginner', colorToken: 'secondary.main' };
    }
  };

  return (
    <WidgetCard
      loading={loading}
      loadingVariant="progress"
      error={error}
      isEmpty={data.length === 0}
    >
      <Box sx={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
        <Box sx={{ mb: 3 }}>
          <Typography variant="h6" sx={{ fontWeight: 600 }}>
            {DASHBOARD_CONSTANTS.SECTIONS.LEARNING_PROGRESS.TITLE}
          </Typography>
          <Typography variant="body2" color="text.secondary">
            {DASHBOARD_CONSTANTS.SECTIONS.LEARNING_PROGRESS.DESCRIPTION}
          </Typography>
        </Box>

        <Box
          sx={{
            display: 'flex',
            flexDirection: 'column',
            gap: 2.25,
            flexGrow: 1,
            overflowY: 'auto',
          }}
        >
          {data.map((item) => {
            const levelInfo = getLevelDetails(item.level);

            return (
              <Box key={item.id} sx={{ width: '100%' }}>
                <Box
                  sx={{
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'space-between',
                    mb: 0.75,
                  }}
                >
                  <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                    <Box sx={{ color: 'text.secondary', display: 'flex' }}>
                      <BookOpen size={16} />
                    </Box>
                    <Typography variant="body2" color="text.primary" sx={{ fontWeight: 600 }}>
                      {item.category}
                    </Typography>
                  </Box>

                  <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5 }}>
                    <Typography variant="body2" color="text.primary" sx={{ fontWeight: 700 }}>
                      {item.progress}%
                    </Typography>
                    <Chip
                      label={levelInfo.label}
                      color={levelInfo.color}
                      size="small"
                      variant="outlined"
                      sx={{
                        fontSize: '0.6875rem',
                        fontWeight: 700,
                        height: 20,
                      }}
                    />
                  </Box>
                </Box>

                <LinearProgress
                  variant="determinate"
                  value={item.progress}
                  color={levelInfo.color}
                  sx={{
                    height: 8,
                    borderRadius: 4,
                    backgroundColor: 'grey.200',
                  }}
                />
              </Box>
            );
          })}
        </Box>
      </Box>
    </WidgetCard>
  );
};

export default LearningProgress;

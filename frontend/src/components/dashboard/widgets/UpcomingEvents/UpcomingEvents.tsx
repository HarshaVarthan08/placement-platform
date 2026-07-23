import React from 'react';
import { Box, Typography, Chip, Grid, Paper } from '@mui/material';
import { Calendar, Clock } from 'lucide-react';
import type { UpcomingEventItem } from '../../../../types/dashboard';
import { WidgetCard, SectionHeader } from '../common';
import { DASHBOARD_CONSTANTS } from '../../../../constants/dashboard';

interface UpcomingEventsProps {
  data?: UpcomingEventItem[];
  loading?: boolean;
  error?: Error | null;
  onCalendarClick?: () => void;
}

export const UpcomingEvents: React.FC<UpcomingEventsProps> = ({
  data = [],
  loading = false,
  error = null,
  onCalendarClick = () => {},
}) => {
  // Resolve chip styling from priority
  const getPriorityDetails = (priority: 'high' | 'medium' | 'low') => {
    switch (priority) {
      case 'high':
        return { label: 'High Priority', color: 'error' as const };
      case 'medium':
        return { label: 'Medium Priority', color: 'warning' as const };
      case 'low':
      default:
        return { label: 'Low Priority', color: 'info' as const };
    }
  };

  return (
    <WidgetCard loading={loading} loadingVariant="list" error={error} isEmpty={data.length === 0}>
      <Box sx={{ display: 'flex', flexDirection: 'column', height: '100%', width: '100%' }}>
        <SectionHeader
          title={DASHBOARD_CONSTANTS.SECTIONS.UPCOMING_EVENTS.TITLE}
          description={DASHBOARD_CONSTANTS.SECTIONS.UPCOMING_EVENTS.DESCRIPTION}
          actionLabel={DASHBOARD_CONSTANTS.SECTIONS.UPCOMING_EVENTS.ACTION_LABEL}
          onActionClick={onCalendarClick}
        />

        <Box
          sx={{ display: 'flex', flexDirection: 'column', gap: 2, flexGrow: 1, overflowY: 'auto' }}
        >
          {data.map((event) => {
            const priorityInfo = getPriorityDetails(event.priority);

            return (
              <Paper
                key={event.id}
                variant="outlined"
                sx={{
                  p: 2,
                  borderRadius: 2,
                  backgroundColor: 'background.default',
                  borderColor: 'divider',
                  transition: 'border-color 0.2s',
                  '&:hover': {
                    borderColor: 'primary.light',
                  },
                }}
              >
                <Grid container spacing={1.5} sx={{ alignItems: 'center' }}>
                  <Grid size={{ xs: 12, sm: 8 }}>
                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 0.5 }}>
                      <Chip
                        label={event.type}
                        size="small"
                        color="primary"
                        variant="outlined"
                        sx={{ fontSize: '0.625rem', fontWeight: 700, height: 20 }}
                      />
                      <Chip
                        label={priorityInfo.label}
                        size="small"
                        color={priorityInfo.color}
                        sx={{ fontSize: '0.625rem', fontWeight: 700, height: 20 }}
                      />
                    </Box>

                    <Typography
                      variant="body2"
                      color="text.primary"
                      sx={{ fontWeight: 700, mb: 1 }}
                    >
                      {event.title}
                    </Typography>

                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, flexWrap: 'wrap' }}>
                      <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5 }}>
                        <Box sx={{ color: 'text.disabled', display: 'flex' }}>
                          <Calendar size={14} />
                        </Box>
                        <Typography
                          variant="caption"
                          color="text.secondary"
                          sx={{ fontWeight: 500 }}
                        >
                          {event.date}
                        </Typography>
                      </Box>

                      <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5 }}>
                        <Box sx={{ color: 'text.disabled', display: 'flex' }}>
                          <Clock size={14} />
                        </Box>
                        <Typography
                          variant="caption"
                          color="text.secondary"
                          sx={{ fontWeight: 500 }}
                        >
                          {event.time}
                        </Typography>
                      </Box>
                    </Box>
                  </Grid>

                  <Grid
                    size={{ xs: 12, sm: 4 }}
                    sx={{
                      display: 'flex',
                      flexDirection: 'column',
                      alignItems: { xs: 'flex-start', sm: 'flex-end' },
                      justifyContent: 'center',
                    }}
                  >
                    <Box
                      sx={{
                        backgroundColor:
                          event.daysRemaining <= 1
                            ? 'error.lighter'
                            : event.daysRemaining <= 3
                              ? 'warning.lighter'
                              : 'action.hover',
                        color:
                          event.daysRemaining <= 1
                            ? 'error.dark'
                            : event.daysRemaining <= 3
                              ? 'warning.dark'
                              : 'text.secondary',
                        px: 1.5,
                        py: 0.5,
                        borderRadius: 1,
                        fontSize: '0.75rem',
                        fontWeight: 700,
                        mb: 0.5,
                      }}
                    >
                      {event.daysRemaining === 1 ? 'In 1 Day' : `In ${event.daysRemaining} Days`}
                    </Box>
                    <Typography
                      variant="caption"
                      color="text.disabled"
                      sx={{ fontWeight: 600, fontSize: '0.6875rem' }}
                    >
                      Status: {event.status}
                    </Typography>
                  </Grid>
                </Grid>
              </Paper>
            );
          })}
        </Box>
      </Box>
    </WidgetCard>
  );
};

export default UpcomingEvents;

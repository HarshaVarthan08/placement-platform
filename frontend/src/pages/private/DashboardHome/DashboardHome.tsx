import React from 'react';
import { Box, Grid } from '@mui/material';
import { useDashboard } from '../../../hooks/useDashboard';
import {
  WelcomeBanner,
  StatisticsSection,
  QuickActions,
  PlacementProgress,
  ResumeInsights,
  LearningProgress,
  RecentActivity,
  UpcomingEvents,
} from '../../../components/dashboard/widgets';

export const DashboardHome: React.FC = () => {
  const {
    loading,
    error,
    greeting,
    statistics,
    quickActions,
    placementJourney,
    resumeInsights,
    learningProgress,
    activities,
    upcomingEvents,
  } = useDashboard();

  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        gap: 3,
        width: '100%',
        pb: 4,
      }}
    >
      {/* Banner greeting block */}
      <WelcomeBanner data={greeting} loading={loading} error={error} />

      {/* Primary key metrics stats */}
      <StatisticsSection data={statistics} loading={loading} error={error} />

      {/* Direct stepper progress journey */}
      <PlacementProgress data={placementJourney} loading={loading} error={error} />

      {/* Main core widgets grid */}
      <Grid container spacing={3}>
        {/* Resume insights (ATS score / checklist) */}
        <Grid size={{ xs: 12, md: 6 }}>
          <ResumeInsights data={resumeInsights} loading={loading} error={error} />
        </Grid>

        {/* Subjects learning progress ratings */}
        <Grid size={{ xs: 12, md: 6 }}>
          <LearningProgress data={learningProgress} loading={loading} error={error} />
        </Grid>

        {/* Timeline chronological feeds */}
        <Grid size={{ xs: 12, md: 6 }}>
          <RecentActivity data={activities} loading={loading} error={error} />
        </Grid>

        {/* Calendar scheduled drives and contests */}
        <Grid size={{ xs: 12, md: 6 }}>
          <UpcomingEvents data={upcomingEvents} loading={loading} error={error} />
        </Grid>
      </Grid>

      {/* Quick launch actions hub */}
      <QuickActions data={quickActions} loading={loading} error={error} />
    </Box>
  );
};

export default DashboardHome;

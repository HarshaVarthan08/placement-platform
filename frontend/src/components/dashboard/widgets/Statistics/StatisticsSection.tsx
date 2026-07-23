import React from 'react';
import { Grid } from '@mui/material';
import type { StatCardData } from '../../../../types/dashboard';
import StatCard from './StatCard';

interface StatisticsSectionProps {
  data?: StatCardData[];
  loading?: boolean;
  error?: Error | null;
}

export const StatisticsSection: React.FC<StatisticsSectionProps> = ({
  data = [],
  loading = false,
  error = null,
}) => {
  // If loading, render four loading cards. If error, pass error.
  const cardsCount = 4;
  const skeletonArray = Array.from({ length: cardsCount });

  return (
    <Grid container spacing={3}>
      {loading
        ? skeletonArray.map((_, index) => (
            <Grid size={{ xs: 12, sm: 6, md: 3 }} key={`skeleton-${index}`}>
              <StatCard loading={true} />
            </Grid>
          ))
        : error
          ? skeletonArray.map((_, index) => (
              <Grid size={{ xs: 12, sm: 6, md: 3 }} key={`error-${index}`}>
                <StatCard error={error} />
              </Grid>
            ))
          : data.map((stat) => (
              <Grid size={{ xs: 12, sm: 6, md: 3 }} key={stat.id}>
                <StatCard data={stat} />
              </Grid>
            ))}
    </Grid>
  );
};

export default StatisticsSection;

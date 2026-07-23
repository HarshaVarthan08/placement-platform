import React from 'react';
import { Box, Typography } from '@mui/material';
import * as LucideIcons from 'lucide-react';
import type { StatCardData } from '../../../../types/dashboard';
import { WidgetCard } from '../common';

interface StatCardProps {
  data?: StatCardData;
  loading?: boolean;
  error?: Error | null;
  disabled?: boolean;
}

export const StatCard: React.FC<StatCardProps> = ({
  data,
  loading = false,
  error = null,
  disabled = false,
}) => {
  const lucide = LucideIcons as unknown as Record<
    string,
    React.ComponentType<{ size?: number; className?: string }>
  >;

  // Dynamically resolve icon from name
  const iconName = data?.iconName || 'TrendingUp';
  const IconComponent = iconName in lucide ? lucide[iconName] : lucide.TrendingUp;

  // Resolve color based on trend
  const getTrendColor = () => {
    if (disabled) return 'text.disabled';
    if (data?.trend === 'up') return 'success.main';
    if (data?.trend === 'down') return 'error.main';
    return 'text.secondary';
  };

  const getTrendBgColor = () => {
    if (data?.trend === 'up') return 'success.lighter';
    if (data?.trend === 'down') return 'error.lighter';
    return 'action.hover';
  };

  return (
    <WidgetCard
      loading={loading}
      loadingVariant="metric"
      error={error}
      isEmpty={!data}
      sx={{
        opacity: disabled ? 0.6 : 1,
        pointerEvents: disabled ? 'none' : 'auto',
      }}
    >
      {data && (
        <Box sx={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
          {/* Header row with Title and Icon */}
          <Box
            sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', mb: 2 }}
          >
            <Typography variant="subtitle2" color="text.secondary" sx={{ fontWeight: 600 }}>
              {data.title}
            </Typography>
            <Box
              sx={{
                p: 1,
                borderRadius: 2,
                backgroundColor: disabled ? 'action.disabledBackground' : 'primary.lighter',
                color: disabled ? 'text.disabled' : 'primary.main',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
              }}
            >
              <IconComponent size={20} />
            </Box>
          </Box>

          {/* Primary metric value */}
          <Typography
            variant="h3"
            color="text.primary"
            sx={{ fontWeight: 800, mb: 1, letterSpacing: '-0.02em', lineHeight: 1 }}
          >
            {data.value}
          </Typography>

          {/* Subtitle / Helper description */}
          <Typography variant="caption" color="text.secondary" sx={{ mb: 1.5, display: 'block' }}>
            {data.subtitle}
          </Typography>

          {/* Trend Indicator */}
          <Box sx={{ display: 'flex', alignItems: 'center', flexWrap: 'wrap', gap: 1, mt: 'auto' }}>
            <Box
              sx={{
                display: 'inline-flex',
                alignItems: 'center',
                px: 1,
                py: 0.25,
                borderRadius: 1,
                backgroundColor: disabled ? 'action.disabledBackground' : getTrendBgColor(),
                color: getTrendColor(),
                fontWeight: 700,
                fontSize: '0.75rem',
              }}
            >
              {data.trendValue.split(' ')[0]}
            </Box>
            <Typography variant="caption" color="text.disabled" sx={{ fontWeight: 500 }}>
              {data.trendValue.slice(data.trendValue.indexOf(' ') + 1)}
            </Typography>
          </Box>
        </Box>
      )}
    </WidgetCard>
  );
};

export default StatCard;

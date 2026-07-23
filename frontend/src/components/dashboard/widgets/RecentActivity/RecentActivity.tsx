import React from 'react';
import { Box, Typography } from '@mui/material';
import * as LucideIcons from 'lucide-react';
import type { ActivityItem, ActivityType } from '../../../../types/dashboard';
import { WidgetCard, SectionHeader } from '../common';
import { DASHBOARD_CONSTANTS } from '../../../../constants/dashboard';

interface RecentActivityProps {
  data?: ActivityItem[];
  loading?: boolean;
  error?: Error | null;
  onViewAllClick?: () => void;
}

export const RecentActivity: React.FC<RecentActivityProps> = ({
  data = [],
  loading = false,
  error = null,
  onViewAllClick = () => {},
}) => {
  // Resolve indicator colors based on ActivityType
  const getActivityColor = (type: ActivityType) => {
    switch (type) {
      case 'SUCCESS':
        return { color: 'success.main', bgColor: 'success.lighter' };
      case 'WARNING':
        return { color: 'warning.main', bgColor: 'warning.lighter' };
      case 'EVENT':
        return { color: 'secondary.main', bgColor: 'secondary.lighter' };
      case 'INFO':
      default:
        return { color: 'primary.main', bgColor: 'primary.lighter' };
    }
  };

  const lucide = LucideIcons as unknown as Record<
    string,
    React.ComponentType<{ size?: number; className?: string }>
  >;

  return (
    <WidgetCard loading={loading} loadingVariant="list" error={error} isEmpty={data.length === 0}>
      <Box sx={{ display: 'flex', flexDirection: 'column', height: '100%', width: '100%' }}>
        <SectionHeader
          title={DASHBOARD_CONSTANTS.SECTIONS.RECENT_ACTIVITY.TITLE}
          description={DASHBOARD_CONSTANTS.SECTIONS.RECENT_ACTIVITY.DESCRIPTION}
          actionLabel={DASHBOARD_CONSTANTS.SECTIONS.RECENT_ACTIVITY.ACTION_LABEL}
          onActionClick={onViewAllClick}
        />

        <Box sx={{ position: 'relative', pl: 2, mt: 1, flexGrow: 1 }}>
          {/* Vertical connection line */}
          {data.length > 1 && (
            <Box
              sx={{
                position: 'absolute',
                top: 8,
                bottom: 8,
                left: 17,
                width: 2,
                backgroundColor: 'divider',
                zIndex: 0,
              }}
            />
          )}

          {/* Activities rows */}
          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2.5 }}>
            {data.map((item) => {
              const iconName = item.iconName || 'Info';
              const IconComponent = iconName in lucide ? lucide[iconName] : lucide.Info;
              const styleInfo = getActivityColor(item.type);

              return (
                <Box
                  key={item.id}
                  sx={{
                    display: 'flex',
                    alignItems: 'flex-start',
                    gap: 2,
                    position: 'relative',
                    zIndex: 1,
                  }}
                >
                  {/* Indicator Icon Circle */}
                  <Box
                    sx={{
                      width: 36,
                      height: 36,
                      borderRadius: '50%',
                      backgroundColor: styleInfo.bgColor,
                      color: styleInfo.color,
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      flexShrink: 0,
                      boxShadow: '0 0 0 4px #FFFFFF',
                    }}
                  >
                    <IconComponent size={18} />
                  </Box>

                  {/* Detail description */}
                  <Box sx={{ flexGrow: 1, pt: 0.5 }}>
                    <Typography
                      variant="body2"
                      color="text.primary"
                      sx={{ fontWeight: 500, lineHeight: 1.35 }}
                    >
                      {item.description}
                    </Typography>
                    <Typography
                      variant="caption"
                      color="text.disabled"
                      sx={{ mt: 0.25, display: 'block', fontWeight: 500 }}
                    >
                      {item.timestamp}
                    </Typography>
                  </Box>
                </Box>
              );
            })}
          </Box>
        </Box>
      </Box>
    </WidgetCard>
  );
};

export default RecentActivity;

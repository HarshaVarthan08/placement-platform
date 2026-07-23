import React from 'react';
import { Grid, Box, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import * as LucideIcons from 'lucide-react';
import type { QuickActionItem } from '../../../../types/dashboard';
import { WidgetCard, SectionHeader } from '../common';
import { DASHBOARD_CONSTANTS } from '../../../../constants/dashboard';

interface QuickActionsProps {
  data?: QuickActionItem[];
  loading?: boolean;
  error?: Error | null;
}

export const QuickActions: React.FC<QuickActionsProps> = ({
  data = [],
  loading = false,
  error = null,
}) => {
  const navigate = useNavigate();

  const handleActionClick = (action: QuickActionItem) => {
    if (!action.enabled || action.comingSoon) return;
    navigate(action.path);
  };

  const lucide = LucideIcons as unknown as Record<
    string,
    React.ComponentType<{ size?: number; className?: string }>
  >;

  return (
    <Box sx={{ width: '100%' }}>
      <SectionHeader
        title={DASHBOARD_CONSTANTS.SECTIONS.QUICK_ACTIONS.TITLE}
        description={DASHBOARD_CONSTANTS.SECTIONS.QUICK_ACTIONS.DESCRIPTION}
      />

      <Grid container spacing={3}>
        {loading
          ? Array.from({ length: 6 }).map((_, index) => (
              <Grid size={{ xs: 12, sm: 6, md: 4 }} key={`skeleton-${index}`}>
                <WidgetCard loading={true} />
              </Grid>
            ))
          : error
            ? Array.from({ length: 6 }).map((_, index) => (
                <Grid size={{ xs: 12, sm: 6, md: 4 }} key={`error-${index}`}>
                  <WidgetCard error={error} />
                </Grid>
              ))
            : data.map((action) => {
                const iconName = action.iconName || 'Settings';
                const IconComponent = iconName in lucide ? lucide[iconName] : lucide.Settings;
                const isClickable = action.enabled && !action.comingSoon;

                return (
                  <Grid size={{ xs: 12, sm: 6, md: 4 }} key={action.type}>
                    <Box
                      onClick={() => handleActionClick(action)}
                      sx={{
                        cursor: isClickable ? 'pointer' : 'default',
                        height: '100%',
                      }}
                    >
                      <WidgetCard
                        sx={{
                          backgroundColor: 'background.paper',
                          borderColor: isClickable ? 'divider' : 'action.disabledBackground',
                          opacity: isClickable ? 1 : 0.6,
                          height: '100%',
                        }}
                      >
                        <Box
                          sx={{
                            display: 'flex',
                            alignItems: 'center',
                            gap: 2,
                            height: '100%',
                            position: 'relative',
                          }}
                        >
                          {/* Action Icon */}
                          <Box
                            sx={{
                              p: 1.5,
                              borderRadius: 2,
                              backgroundColor: isClickable
                                ? 'primary.lighter'
                                : 'action.disabledBackground',
                              color: isClickable ? 'primary.main' : 'text.disabled',
                              display: 'flex',
                              alignItems: 'center',
                              justifyContent: 'center',
                            }}
                          >
                            <IconComponent size={24} />
                          </Box>

                          {/* Text details */}
                          <Box sx={{ flexGrow: 1, pr: 2 }}>
                            <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                              <Typography
                                variant="subtitle1"
                                color="text.primary"
                                sx={{ fontWeight: 700, lineHeight: 1.2 }}
                              >
                                {action.label}
                              </Typography>
                              {action.comingSoon && (
                                <Box
                                  sx={{
                                    fontSize: '0.625rem',
                                    fontWeight: 700,
                                    backgroundColor: 'grey.300',
                                    color: 'text.secondary',
                                    px: 1,
                                    py: 0.25,
                                    borderRadius: 1,
                                    textTransform: 'uppercase',
                                  }}
                                >
                                  Soon
                                </Box>
                              )}
                            </Box>
                            <Typography variant="body2" color="text.secondary" sx={{ mt: 0.5 }}>
                              {action.description}
                            </Typography>
                          </Box>

                          {/* Arrow indicator */}
                          {isClickable && (
                            <Box
                              sx={{
                                color: 'text.disabled',
                                display: 'flex',
                                alignItems: 'center',
                                '&:hover': { color: 'primary.main' },
                                ml: 'auto',
                              }}
                            >
                              <lucide.ChevronRight size={20} />
                            </Box>
                          )}
                        </Box>
                      </WidgetCard>
                    </Box>
                  </Grid>
                );
              })}
      </Grid>
    </Box>
  );
};

export default QuickActions;

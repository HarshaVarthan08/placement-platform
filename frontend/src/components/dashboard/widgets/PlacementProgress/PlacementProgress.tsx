import React from 'react';
import {
  Box,
  Typography,
  Stepper,
  Step,
  StepLabel,
  StepContent,
  useTheme,
  useMediaQuery,
} from '@mui/material';
import { Check, Dot } from 'lucide-react';
import type { PlacementJourneyStage, PlacementStageStatus } from '../../../../types/dashboard';
import { WidgetCard } from '../common';
import { DASHBOARD_CONSTANTS } from '../../../../constants/dashboard';

interface PlacementProgressProps {
  data?: PlacementJourneyStage[];
  loading?: boolean;
  error?: Error | null;
}

export const PlacementProgress: React.FC<PlacementProgressProps> = ({
  data = [],
  loading = false,
  error = null,
}) => {
  const theme = useTheme();
  // Switch to vertical stepper on mobile screens
  const isMobile = useMediaQuery(theme.breakpoints.down('sm'));

  // Custom step icon resolver
  const CustomStepIcon = (props: { status: PlacementStageStatus; current: boolean }) => {
    const { status, current } = props;

    let bgColor = 'action.disabledBackground';
    let textColor = 'text.disabled';
    let icon = <Dot size={24} />;

    if (status === 'COMPLETED') {
      bgColor = 'success.main';
      textColor = 'success.contrastText';
      icon = <Check size={16} strokeWidth={3} />;
    } else if (status === 'IN_PROGRESS' || current) {
      bgColor = 'primary.main';
      textColor = 'primary.contrastText';
      icon = (
        <Box sx={{ width: 8, height: 8, borderRadius: '50%', backgroundColor: 'currentColor' }} />
      );
    }

    return (
      <Box
        sx={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          width: 32,
          height: 32,
          borderRadius: '50%',
          backgroundColor: bgColor,
          color: textColor,
          fontWeight: 700,
          fontSize: '0.875rem',
          boxShadow: current ? `0 0 0 4px ${theme.palette.primary.light}` : 'none',
        }}
      >
        {icon}
      </Box>
    );
  };

  return (
    <WidgetCard loading={loading} loadingVariant="card" error={error} isEmpty={data.length === 0}>
      <Box sx={{ width: '100%' }}>
        <Box sx={{ mb: 3 }}>
          <Typography variant="h6" sx={{ fontWeight: 600 }}>
            {DASHBOARD_CONSTANTS.SECTIONS.PLACEMENT_PROGRESS.TITLE}
          </Typography>
          <Typography variant="body2" color="text.secondary">
            {DASHBOARD_CONSTANTS.SECTIONS.PLACEMENT_PROGRESS.DESCRIPTION}
          </Typography>
        </Box>

        {isMobile ? (
          // Mobile Layout: Vertical Stepper
          <Stepper orientation="vertical" activeStep={data.findIndex((s) => s.current)}>
            {data.map((stage) => (
              <Step key={stage.id} active={stage.current} completed={stage.status === 'COMPLETED'}>
                <StepLabel icon={<CustomStepIcon status={stage.status} current={stage.current} />}>
                  <Typography variant="subtitle2" sx={{ fontWeight: stage.current ? 700 : 500 }}>
                    {stage.name}
                  </Typography>
                  <Typography variant="caption" color="text.secondary">
                    {stage.status === 'COMPLETED'
                      ? 'Completed'
                      : stage.status === 'IN_PROGRESS'
                        ? 'In Progress'
                        : 'Not Started'}
                  </Typography>
                </StepLabel>
                <StepContent>
                  {stage.status === 'IN_PROGRESS' && (
                    <Typography variant="caption" color="primary.main" sx={{ fontWeight: 600 }}>
                      Current stage focus. Progress: {stage.progress}%
                    </Typography>
                  )}
                </StepContent>
              </Step>
            ))}
          </Stepper>
        ) : (
          // Desktop Layout: Horizontal Custom Journey Grid
          <Box
            sx={{
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'space-between',
              position: 'relative',
              mt: 2,
              px: 2,
            }}
          >
            {/* Background progress lines */}
            <Box
              sx={{
                position: 'absolute',
                top: 16,
                left: '6%',
                right: '6%',
                height: 3,
                backgroundColor: 'divider',
                zIndex: 0,
              }}
            />

            {data.map((stage) => {
              const isActive = stage.current;
              const isDone = stage.status === 'COMPLETED';

              return (
                <Box
                  key={stage.id}
                  sx={{
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    zIndex: 1,
                    position: 'relative',
                    width: `${100 / data.length}%`,
                    textAlign: 'center',
                  }}
                >
                  <CustomStepIcon status={stage.status} current={stage.current} />

                  <Typography
                    variant="body2"
                    sx={{
                      fontWeight: isActive ? 700 : 500,
                      color: isActive
                        ? 'text.primary'
                        : isDone
                          ? 'text.secondary'
                          : 'text.disabled',
                      mt: 1.5,
                      px: 1,
                    }}
                  >
                    {stage.name.split(' ')[0]}
                  </Typography>
                  <Typography
                    variant="caption"
                    color={isActive ? 'primary.main' : 'text.disabled'}
                    sx={{ fontWeight: isActive ? 600 : 400 }}
                  >
                    {stage.status === 'COMPLETED'
                      ? 'Done'
                      : stage.status === 'IN_PROGRESS'
                        ? 'In Progress'
                        : 'Pending'}
                  </Typography>
                </Box>
              );
            })}
          </Box>
        )}
      </Box>
    </WidgetCard>
  );
};

export default PlacementProgress;

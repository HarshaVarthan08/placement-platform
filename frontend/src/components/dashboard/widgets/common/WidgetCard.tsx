import React from 'react';
import { Card, CardContent, Box, Typography, Button } from '@mui/material';
import { AlertCircle } from 'lucide-react';
import LoadingSkeleton from './LoadingSkeleton';
import EmptyState from './EmptyState';

interface WidgetCardProps {
  children?: React.ReactNode;
  loading?: boolean;
  loadingVariant?: 'metric' | 'list' | 'progress' | 'banner' | 'card';
  loadingRows?: number;
  isEmpty?: boolean;
  emptyTitle?: string;
  emptyDescription?: string;
  emptyIconName?: string;
  emptyActionLabel?: string;
  onEmptyActionClick?: () => void;
  error?: Error | null;
  onRetry?: () => void;
  sx?: object;
  disablePadding?: boolean;
}

export const WidgetCard: React.FC<WidgetCardProps> = ({
  children,
  loading = false,
  loadingVariant = 'card',
  loadingRows = 3,
  isEmpty = false,
  emptyTitle = 'No Data Available',
  emptyDescription = 'There is currently no information to show in this section.',
  emptyIconName = 'FileQuestion',
  emptyActionLabel,
  onEmptyActionClick,
  error = null,
  onRetry,
  sx = {},
  disablePadding = false,
}) => {
  return (
    <Card
      sx={{
        height: '100%',
        display: 'flex',
        flexDirection: 'column',
        backgroundColor: 'background.paper',
        borderRadius: (theme) => `${theme.shape.borderRadius}px`,
        boxShadow: (theme) => theme.customShadows.level1,
        transition: 'transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out',
        border: '1px solid',
        borderColor: 'divider',
        '&:hover': {
          transform: 'translateY(-2px)',
          boxShadow: (theme) => theme.customShadows.level2,
        },
        ...sx,
      }}
    >
      <CardContent
        sx={{
          p: disablePadding ? 0 : 3,
          '&:last-child': {
            pb: disablePadding ? 0 : 3,
          },
          display: 'flex',
          flexDirection: 'column',
          flexGrow: 1,
          height: '100%',
        }}
      >
        {loading ? (
          <LoadingSkeleton variant={loadingVariant} rows={loadingRows} />
        ) : error ? (
          <Box
            sx={{
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
              justifyContent: 'center',
              textAlign: 'center',
              p: 3,
              minHeight: 180,
              color: 'error.main',
            }}
          >
            <AlertCircle size={40} style={{ marginBottom: 16 }} />
            <Typography variant="h6" color="text.primary" sx={{ fontWeight: 600, mb: 1 }}>
              Failed to Load Widget
            </Typography>
            <Typography variant="body2" color="text.secondary" sx={{ mb: 2, maxWidth: 280 }}>
              {error.message || 'An unexpected error occurred while loading this section.'}
            </Typography>
            {onRetry && (
              <Button
                variant="outlined"
                color="primary"
                onClick={onRetry}
                size="small"
                sx={{ fontWeight: 600 }}
              >
                Retry
              </Button>
            )}
          </Box>
        ) : isEmpty ? (
          <EmptyState
            title={emptyTitle}
            description={emptyDescription}
            iconName={emptyIconName}
            actionLabel={emptyActionLabel}
            onActionClick={onEmptyActionClick}
          />
        ) : (
          children
        )}
      </CardContent>
    </Card>
  );
};

export default WidgetCard;

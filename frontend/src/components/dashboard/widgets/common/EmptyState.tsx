import React from 'react';
import { Box, Typography, Button } from '@mui/material';
import * as LucideIcons from 'lucide-react';

interface EmptyStateProps {
  title: string;
  description: string;
  iconName?: string;
  actionLabel?: string;
  onActionClick?: () => void;
}

export const EmptyState: React.FC<EmptyStateProps> = ({
  title,
  description,
  iconName = 'FileQuestion',
  actionLabel,
  onActionClick,
}) => {
  const lucide = LucideIcons as unknown as Record<
    string,
    React.ComponentType<{ size?: number; className?: string }>
  >;
  // Dynamically resolve lucide icon
  const IconComponent = iconName in lucide ? lucide[iconName] : lucide.FileQuestion;

  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        textAlign: 'center',
        p: 4,
        minHeight: 200,
        height: '100%',
        width: '100%',
      }}
    >
      <Box
        sx={{
          color: 'text.disabled',
          mb: 2,
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
        }}
      >
        <IconComponent size={48} className="lucide-icon" />
      </Box>
      <Typography variant="h6" color="text.primary" gutterBottom sx={{ fontWeight: 600 }}>
        {title}
      </Typography>
      <Typography
        variant="body2"
        color="text.secondary"
        sx={{ maxWidth: 360, mb: actionLabel ? 3 : 0 }}
      >
        {description}
      </Typography>
      {actionLabel && onActionClick && (
        <Button
          variant="contained"
          color="primary"
          onClick={onActionClick}
          size="small"
          sx={{ fontWeight: 600 }}
        >
          {actionLabel}
        </Button>
      )}
    </Box>
  );
};

export default EmptyState;

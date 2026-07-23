import React from 'react';
import { Box, Typography, Button } from '@mui/material';

interface SectionHeaderProps {
  title: string;
  description?: string;
  actionLabel?: string;
  onActionClick?: () => void;
  titleVariant?: 'h5' | 'h6' | 'subtitle1';
  sx?: object;
}

export const SectionHeader: React.FC<SectionHeaderProps> = ({
  title,
  description,
  actionLabel,
  onActionClick,
  titleVariant = 'h6',
  sx = {},
}) => {
  return (
    <Box
      sx={{
        display: 'flex',
        alignItems: 'flex-start',
        justifyContent: 'space-between',
        width: '100%',
        mb: 2,
        ...sx,
      }}
    >
      <Box sx={{ flexGrow: 1 }}>
        <Typography
          variant={titleVariant}
          color="text.primary"
          sx={{ fontWeight: 600, letterSpacing: '-0.01em', lineHeight: 1.25 }}
        >
          {title}
        </Typography>
        {description && (
          <Typography variant="body2" color="text.secondary" sx={{ mt: 0.5, maxWidth: 640 }}>
            {description}
          </Typography>
        )}
      </Box>
      {actionLabel && onActionClick && (
        <Button
          variant="text"
          color="primary"
          onClick={onActionClick}
          size="small"
          sx={{
            fontWeight: 600,
            fontSize: '0.8125rem',
            whiteSpace: 'nowrap',
            ml: 2,
            mt: -0.5,
            p: '4px 8px',
            '&:hover': {
              backgroundColor: 'primary.lighter',
            },
          }}
        >
          {actionLabel}
        </Button>
      )}
    </Box>
  );
};

export default SectionHeader;

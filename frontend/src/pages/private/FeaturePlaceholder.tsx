import React from 'react';
import { Box, Button, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { ROUTES } from '../../constants';

interface FeaturePlaceholderProps {
  pageTitle: string;
  description: string;
}

export const FeaturePlaceholder: React.FC<FeaturePlaceholderProps> = ({
  pageTitle,
  description,
}) => {
  const navigate = useNavigate();

  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        minHeight: '60vh',
        textAlign: 'center',
        p: 3,
      }}
    >
      <Typography variant="h4" gutterBottom sx={{ fontWeight: 600, color: 'text.primary' }}>
        {pageTitle}
      </Typography>
      <Typography variant="body1" color="text.secondary" sx={{ mb: 3, maxWidth: 500 }}>
        {description}
      </Typography>
      <Typography variant="body2" color="text.disabled" sx={{ mb: 4 }}>
        This feature is scheduled for release in a future Sprint.
      </Typography>
      <Button
        variant="contained"
        color="primary"
        onClick={() => navigate(ROUTES.PRIVATE.DASHBOARD)}
        sx={{ textTransform: 'none', px: 4 }}
      >
        Return to Dashboard
      </Button>
    </Box>
  );
};

export default FeaturePlaceholder;

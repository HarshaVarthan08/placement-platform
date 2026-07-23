import React from 'react';
import { Box, Typography } from '@mui/material';

export const DashboardPlaceholder: React.FC = () => {
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
      <Typography variant="h4" color="primary" gutterBottom sx={{ fontWeight: 600 }}>
        Dashboard Layout Foundation
      </Typography>
      <Typography variant="body1" color="text.secondary" sx={{ maxWidth: 500 }}>
        The authenticated application shell foundation is active. Nested outlet routing, protected
        route guards, and UI state provider are successfully initialized.
      </Typography>
    </Box>
  );
};

export default DashboardPlaceholder;

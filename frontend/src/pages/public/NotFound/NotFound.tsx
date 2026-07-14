import { Box, Typography } from '@mui/material';

export const NotFound = () => {
  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        minHeight: '100vh',
        backgroundColor: 'background.default',
      }}
    >
      <Typography variant="h2" gutterBottom color="error">
        404 - Page Not Found
      </Typography>
      <Typography variant="h4" color="text.secondary">
        Coming Soon
      </Typography>
    </Box>
  );
};

export default NotFound;

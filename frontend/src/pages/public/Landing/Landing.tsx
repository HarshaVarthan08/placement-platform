import { Box, Typography } from '@mui/material';

export const Landing = () => {
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
      <Typography variant="h1" gutterBottom>
        Landing Page
      </Typography>
      <Typography variant="h4" color="text.secondary">
        Coming Soon
      </Typography>
    </Box>
  );
};

export default Landing;

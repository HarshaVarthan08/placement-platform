import { Box, Typography } from '@mui/material';

export const Register = () => {
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
      <Typography variant="h2" gutterBottom>
        Register Page
      </Typography>
      <Typography variant="h4" color="text.secondary">
        Coming Soon
      </Typography>
    </Box>
  );
};

export default Register;

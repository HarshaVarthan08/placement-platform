import { Box } from '@mui/material';
import { Hero, Features } from '../../../components';

export const Landing = () => {
  return (
    <Box
      sx={{
        width: '100%',
        backgroundColor: 'background.default',
      }}
    >
      {/* Landing Hero Section */}
      <Hero />

      {/* Landing Features Section */}
      <Features />
    </Box>
  );
};

export default Landing;

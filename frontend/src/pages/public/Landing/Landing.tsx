import { Box } from '@mui/material';
import { Hero, Features, HowItWorks } from '../../../components';

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

      {/* Landing How It Works Section */}
      <HowItWorks />
    </Box>
  );
};

export default Landing;

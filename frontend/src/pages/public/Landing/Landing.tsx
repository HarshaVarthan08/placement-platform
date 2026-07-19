import { Box } from '@mui/material';
import { Hero, Features, HowItWorks, Pricing, About, CTA } from '../../../components';

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

      {/* Landing Pricing Section */}
      <Pricing />

      {/* Landing About / Why AI Placement Platform Section */}
      <About />

      {/* Final Call to Action Section */}
      <CTA />
    </Box>
  );
};

export default Landing;

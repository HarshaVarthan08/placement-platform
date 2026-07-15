import { Box } from '@mui/material';
import { Hero } from '../../../components';

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
    </Box>
  );
};

export default Landing;

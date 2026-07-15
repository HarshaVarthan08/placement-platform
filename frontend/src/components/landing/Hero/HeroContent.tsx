import { Box } from '@mui/material';
import HeroHeadline from './HeroHeadline';
import HeroDescription from './HeroDescription';
import HeroActions from './HeroActions';
import HeroStats from './HeroStats';

export const HeroContent = () => {
  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: { xs: 'center', md: 'flex-start' },
        textAlign: { xs: 'center', md: 'left' },
        width: '100%',
        zIndex: 2,
      }}
    >
      <HeroHeadline />
      <HeroDescription />
      <HeroActions />
      <HeroStats />
    </Box>
  );
};

export default HeroContent;

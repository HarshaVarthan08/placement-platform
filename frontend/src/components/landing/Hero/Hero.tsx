import { Box, Container, Grid, useTheme } from '@mui/material';
import { alpha } from '@mui/material/styles';
import HeroContent from './HeroContent';
import HeroIllustration from './HeroIllustration';

export const Hero = () => {
  const theme = useTheme();

  return (
    <Box
      component="section"
      sx={{
        position: 'relative',
        minHeight: '90vh',
        width: '100%',
        display: 'flex',
        alignItems: 'center',
        py: { xs: 6, sm: 8, md: 10 },
        backgroundColor: 'background.default',
        overflow: 'hidden', // Contain ambient glows
      }}
    >
      {/* Soft Ambient Background Glows */}
      <Box
        aria-hidden="true"
        sx={{
          position: 'absolute',
          top: '-10%',
          right: '-5%',
          width: { xs: 300, md: 500 },
          height: { xs: 300, md: 500 },
          borderRadius: '50%',
          background: `radial-gradient(circle, ${alpha(theme.palette.secondary.main, 0.06)} 0%, transparent 70%)`,
          filter: 'blur(40px)',
          zIndex: 0,
          pointerEvents: 'none',
        }}
      />
      <Box
        aria-hidden="true"
        sx={{
          position: 'absolute',
          bottom: '10%',
          left: '-10%',
          width: { xs: 250, md: 450 },
          height: { xs: 250, md: 450 },
          borderRadius: '50%',
          background: `radial-gradient(circle, ${alpha(theme.palette.primary.main, 0.05)} 0%, transparent 70%)`,
          filter: 'blur(40px)',
          zIndex: 0,
          pointerEvents: 'none',
        }}
      />

      <Container
        maxWidth="lg"
        sx={{
          position: 'relative',
          zIndex: 1,
          px: { xs: 2, sm: 3, md: 4 },
        }}
      >
        <Grid
          container
          component="div"
          spacing={{ xs: 6, md: 4, lg: 8 }}
          sx={{
            alignItems: 'center',
            justifyContent: 'space-between',
          }}
        >
          {/* Left Column: Hero Content */}
          <Grid
            size={{ xs: 12, md: 7, lg: 6 }}
            sx={{
              display: 'flex',
              alignItems: 'center',
              justifyContent: { xs: 'center', md: 'flex-start' },
            }}
          >
            <HeroContent />
          </Grid>

          {/* Right Column: Career Orbit Illustration */}
          <Grid
            size={{ xs: 12, md: 5, lg: 5.5 }}
            sx={{
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              width: '100%',
            }}
          >
            <HeroIllustration />
          </Grid>
        </Grid>
      </Container>
    </Box>
  );
};

export default Hero;

import React from 'react';
import { Box, Card, Container, Typography, useTheme } from '@mui/material';
import { alpha } from '@mui/material/styles';
import { Orbit } from 'lucide-react';

interface AuthCardWrapperProps {
  children: React.ReactNode;
  title: string;
  subtitle: string;
}

export const AuthCardWrapper: React.FC<AuthCardWrapperProps> = ({ children, title, subtitle }) => {
  const theme = useTheme();

  return (
    <Box
      sx={{
        minHeight: 'calc(100vh - 64px)', // Deduct navigation navbar height
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        py: { xs: 4, sm: 6, md: 8 },
        px: 2,
        backgroundColor: 'background.default',
        position: 'relative',
        overflow: 'hidden',
      }}
    >
      {/* Background decorative styling mimicking Landing Hero */}
      <Box
        aria-hidden="true"
        sx={{
          position: 'absolute',
          top: '-10%',
          right: '-5%',
          width: { xs: 300, md: 500 },
          height: { xs: 300, md: 500 },
          borderRadius: '50%',
          background: `radial-gradient(circle, ${alpha(theme.palette.secondary.main, 0.05)} 0%, transparent 70%)`,
          filter: 'blur(40px)',
          zIndex: 0,
          pointerEvents: 'none',
        }}
      />
      <Box
        aria-hidden="true"
        sx={{
          position: 'absolute',
          bottom: '-10%',
          left: '-5%',
          width: { xs: 300, md: 500 },
          height: { xs: 300, md: 500 },
          borderRadius: '50%',
          background: `radial-gradient(circle, ${alpha(theme.palette.primary.main, 0.05)} 0%, transparent 70%)`,
          filter: 'blur(40px)',
          zIndex: 0,
          pointerEvents: 'none',
        }}
      />

      <Container maxWidth="lg" sx={{ position: 'relative', zIndex: 1 }}>
        <Card
          sx={{
            display: 'flex',
            flexDirection: { xs: 'column', md: 'row' },
            minHeight: { xs: 'auto', md: '550px' },
            boxShadow: theme.customShadows.level2,
            borderRadius: `${theme.shape.borderRadius}px`,
            overflow: 'hidden',
            backgroundColor: 'background.paper',
            border: '1px solid',
            borderColor: 'divider',
          }}
        >
          {/* Left Side: Branding Column */}
          <Box
            sx={{
              flex: { xs: 'none', md: 1 },
              background: `linear-gradient(135deg, ${theme.palette.primary.dark} 0%, ${theme.palette.secondary.dark} 100%)`,
              color: 'primary.contrastText',
              p: { xs: 4, sm: 6 },
              display: 'flex',
              flexDirection: 'column',
              justifyContent: 'center',
              alignItems: { xs: 'center', md: 'flex-start' },
              textAlign: { xs: 'center', md: 'left' },
            }}
          >
            <Box
              sx={{
                display: 'flex',
                alignItems: 'center',
                gap: 1.5,
                mb: 4,
              }}
            >
              <Orbit size={36} strokeWidth={2.5} />
              <Typography
                variant="h4"
                sx={{
                  color: 'primary.contrastText',
                  letterSpacing: '-0.02em',
                  fontSize: { xs: '1.25rem', sm: '1.5rem' },
                  fontWeight: 'fontWeightBold',
                }}
              >
                AI Placement Platform
              </Typography>
            </Box>

            <Typography
              variant="h2"
              gutterBottom
              sx={{
                color: 'primary.contrastText',
                mb: 2,
                fontSize: { xs: '1.75rem', sm: '2.25rem', md: '2.5rem' },
                lineHeight: 1.2,
                fontWeight: 'fontWeightBold',
              }}
            >
              {title}
            </Typography>

            <Typography
              variant="body1"
              sx={{
                color: 'primary.contrastText',
                opacity: 0.85,
                maxWidth: '400px',
                lineHeight: 1.6,
                fontSize: { xs: '0.9rem', sm: '1rem' },
              }}
            >
              {subtitle}
            </Typography>
          </Box>

          {/* Right Side: Inputs / Forms Container */}
          <Box
            sx={{
              flex: { xs: 'none', md: 1.2 },
              p: { xs: 4, sm: 6 },
              display: 'flex',
              flexDirection: 'column',
              justifyContent: 'center',
            }}
          >
            {children}
          </Box>
        </Card>
      </Container>
    </Box>
  );
};

export default AuthCardWrapper;

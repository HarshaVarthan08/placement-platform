import React from 'react';
import { Box, Typography, useTheme } from '@mui/material';
import { alpha } from '@mui/material/styles';
import { Check } from 'lucide-react';

interface PricingFeaturesProps {
  features: string[];
}

export const PricingFeatures: React.FC<PricingFeaturesProps> = ({ features }) => {
  const theme = useTheme();

  return (
    <Box
      component="ul"
      sx={{
        listStyle: 'none',
        p: 0,
        m: 0,
        display: 'flex',
        flexDirection: 'column',
        gap: 1.5,
        width: '100%',
      }}
    >
      {features.map((feature, index) => (
        <Box
          component="li"
          key={index}
          sx={{
            display: 'flex',
            alignItems: 'flex-start',
            gap: 1.5,
          }}
        >
          <Box
            aria-hidden="true"
            sx={{
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              width: 20,
              height: 20,
              borderRadius: '50%',
              backgroundColor: alpha(theme.palette.success.main, 0.1),
              color: 'success.main',
              flexShrink: 0,
              mt: 0.25,
            }}
          >
            <Check size={13} strokeWidth={3} />
          </Box>
          <Typography
            variant="body2"
            sx={{
              color: 'text.primary',
              fontSize: '0.875rem',
              lineHeight: 1.5,
            }}
          >
            {feature}
          </Typography>
        </Box>
      ))}
    </Box>
  );
};

export default PricingFeatures;

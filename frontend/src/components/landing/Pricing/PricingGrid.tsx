import React from 'react';
import { Box } from '@mui/material';
import { PRICING_PLANS } from './plans';
import PricingCard from './PricingCard';

export const PricingGrid: React.FC = () => {
  return (
    <Box
      sx={{
        display: 'grid',
        gridTemplateColumns: {
          xs: '1fr',
          md: 'repeat(2, 1fr)',
        },
        gap: { xs: 4, md: 4 },
        alignItems: 'stretch', // Stretches grid children to equal heights
        width: '100%',
        maxWidth: 840,
        mx: 'auto',
      }}
    >
      {PRICING_PLANS.map((plan, index) => (
        <PricingCard key={plan.id} plan={plan} index={index} />
      ))}
    </Box>
  );
};

export default PricingGrid;

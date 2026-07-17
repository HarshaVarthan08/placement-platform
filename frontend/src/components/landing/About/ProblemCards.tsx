import React from 'react';
import { Box } from '@mui/material';
import { PROBLEM_CARDS } from './aboutData';
import ProblemCard from './ProblemCard';

export const ProblemCards: React.FC = () => {
  return (
    <Box
      sx={{
        display: 'grid',
        gridTemplateColumns: {
          xs: '1fr',
          sm: 'repeat(2, 1fr)',
          md: 'repeat(4, 1fr)',
        },
        gap: { xs: 3, md: 4 },
        mb: { xs: 8, sm: 10, md: 12 },
      }}
    >
      {PROBLEM_CARDS.map((card, index) => (
        <ProblemCard key={card.id} data={card} index={index} />
      ))}
    </Box>
  );
};

export default ProblemCards;

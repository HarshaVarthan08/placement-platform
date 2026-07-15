import { Box, Typography } from '@mui/material';

export const FooterBottom = () => {
  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: { xs: 'column', sm: 'row' },
        justifyContent: 'space-between',
        alignItems: 'center',
        gap: 2,
        paddingTop: 3,
        borderTop: '1px solid',
        borderColor: 'divider',
        marginTop: 6,
      }}
    >
      <Typography variant="body2" color="text.secondary" sx={{ fontSize: '0.875rem' }}>
        &copy; 2026 AI Placement Platform. All rights reserved.
      </Typography>
      <Typography
        variant="body2"
        color="text.secondary"
        sx={{
          fontSize: '0.875rem',
          fontWeight: 'fontWeightMedium',
        }}
      >
        Built for Future Professionals.
      </Typography>
    </Box>
  );
};

export default FooterBottom;

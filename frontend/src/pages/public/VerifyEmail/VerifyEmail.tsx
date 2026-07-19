import { Link as RouterLink } from 'react-router-dom';
import { ROUTES } from '../../../constants';
import { AuthCardWrapper } from '../../../components/shared';
import { Box, Button, Typography } from '@mui/material';
import { MailCheck } from 'lucide-react';

export const VerifyEmail = () => {
  return (
    <AuthCardWrapper
      title="Verify Your Email"
      subtitle="Thank you for signing up! Please verify your email address to unlock all mock preparation engines."
    >
      <Box
        sx={{
          width: '100%',
          maxWidth: '400px',
          mx: 'auto',
          textAlign: 'center',
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          py: 2,
        }}
      >
        <Box
          sx={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            width: 72,
            height: 72,
            borderRadius: '50%',
            backgroundColor: 'rgba(16, 185, 129, 0.08)',
            color: 'success.main',
            mb: 3,
          }}
        >
          <MailCheck size={36} strokeWidth={2} />
        </Box>

        <Typography variant="h3" gutterBottom sx={{ mb: 1.5, fontWeight: 'fontWeightBold' }}>
          Coming Soon
        </Typography>

        <Typography variant="body2" color="text.secondary" sx={{ mb: 4, lineHeight: 1.6 }}>
          This feature will be available in a future sprint. Once connected, a verification link
          will be sent to confirm your email address dynamically.
        </Typography>

        <Button
          component={RouterLink}
          to={ROUTES.GUEST.LOGIN}
          variant="contained"
          color="primary"
          size="large"
          fullWidth
          sx={{
            py: 1.5,
            fontWeight: 'fontWeightBold',
            textTransform: 'none',
            minHeight: '48px',
          }}
        >
          Back to Sign In
        </Button>
      </Box>
    </AuthCardWrapper>
  );
};

export default VerifyEmail;
